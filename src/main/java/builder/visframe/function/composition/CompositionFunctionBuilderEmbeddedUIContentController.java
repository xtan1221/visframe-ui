package builder.visframe.function.composition;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.function.component.PiecewiseFunctionBuilder;
import builder.visframe.function.component.PiecewiseFunctionBuilderEmbeddedUIContentController;
import builder.visframe.function.component.SimpleFunctionBuilder;
import builder.visframe.function.component.SimpleFunctionBuilderEmbeddedUIContentController;
import builder.visframe.function.composition.originalIndieFIVTypeUtils.OriginalIndieFIVTypeEntryBuilder;
import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder.ComponentFunctionType;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.pipeline.cf.steps.BMakeCompositionFunctionStepManager;
import function.component.ComponentFunction;
import function.component.PiecewiseFunction;
import function.component.SimpleFunction;
import function.composition.CompositionFunction;
import function.composition.CompositionFunctionID;
import function.composition.CompositionFunctionImpl;
import function.target.CFGTarget;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import metadata.MetadataID;
import utils.AlertUtils;
import utils.FXUtils;

public class CompositionFunctionBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<CompositionFunction>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/composition/CompositionFunctionBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	/**
	 * set of TargetHBox each represent a target of the owner CFG of the CF to be built;
	 * 
	 * 1. for TargetHBoxes of those targets assigned to existing CFs of the same CFG, their CheckBoxes are disabled throughout the lifetime of the owner builder;
	 * 
	 * 2. for TargetHBoxes of those targets NOT assigned to any existing CFs of the same CFG, their CheckBoxes are not disabled; also the mouseClickEvent of the CheckBoxes are used to detect whether such target is selected or de-selected
	 */
	private Set<TargetHBox> targetHBoxSet;
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.ownerCFGNameTextField.setText(this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getName().getStringValue());
		this.recordDataNameTextField.setText(this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID().getName().getStringValue());
		this.indexIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getIndexID()));
		
		//initialize the targetHBoxSet and add to targetItemsVBox
		this.targetHBoxSet = new LinkedHashSet<>();
		for(SimpleName sn:this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getTargetNameMap().keySet()) {//TODO need to add the ones assigned to this one when in view mode
			
			CFGTarget<?> target = this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getTargetNameMap().get(sn);
			
			TargetHBox targetHBox = new TargetHBox(target, this.getOwnerNodeBuilder().getTargetNameSetAssignedToOtherCompositionFunctions().contains(sn));
			targetHBoxSet.add(targetHBox);
			
	        this.targetItemsVBox.getChildren().add(targetHBox.containerHBox);
		}
		
	
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public CompositionFunctionBuilder getOwnerNodeBuilder() {
		return (CompositionFunctionBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	/**
	 * build and return a CompositionFunction;
	 * 
	 * this method should only be invoked after the owner {@link CompositionFunctionBuilder#isFinishable()} is checked and returns true;
	 * 
	 * see {@link BMakeCompositionFunctionStepManager#finish()} from where this method will be invoked;
	 * 
	 * ================================
	 * note that normally, {@link LeafNodeBuilderEmbeddedUIContentController#build()} method will be invoked during construction of the target entity to check the status of the target, 
	 * but for CompositionFunctionBuilder, this feature is disabled;
	 * all the validation of the status of the target CompositionFunction is achieved by the {@link CompositionFunctionBuilder#update()} method which updates the {@link CompositionFunctionBuilder#isFinishable()};
	 * 
	 * thus, this method is only used to build and retrieve the current valid CompositionFunction object rather than perform validation;
	 */
	@Override
	public CompositionFunction build() {
		
		ComponentFunction root = this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().getEmbeddedUIContentController().build();
		//
		
		CompositionFunction cf = new CompositionFunctionImpl(
				this.getOwnerNodeBuilder().getIndexID(),//int indexID, 
				new VfNotes(this.compositionFunctionNotesTextArea.getText()),//VfNotes notes,
				this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getID(),//CompositionFunctionGroupID groupID, 
				new LinkedHashSet<>(this.getOwnerNodeBuilder().getAssignedTargetNameMap().keySet()),//Set<SimpleName> assignedTargetNameSet, //TODO debug, here if not using HashSet constructor, java.io.NotSerializableException will be thrown
//				this.getOwnerNodeBuilder().getAssignedTargetNameMap().keySet(),//Set<SimpleName> assignedTargetNameSet, //TODO debug, here if not using HashSet constructor, java.io.NotSerializableException will be thrown
				root//ComponentFunction rootFunction
				);
		
		return cf;
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//vfnotes
		this.compositionFunctionNotesTextArea.setEditable(modifiable);
		
		//targets
		this.targetItemsVBox.setMouseTransparent(!modifiable);
		
		//independentFreeInputVariableTypes
		FXUtils.set2Disable(this.addIndieFIVTypeButton, !modifiable);
		this.getOwnerNodeBuilder().getOriginalIndependentFreeInputVariableTypeNameEntryBuilderMap().values().forEach(e->{
			e.getController().setModifiable(modifiable);
		});
		
		//tree operation buttons
		this.treeOperationButtonHBox.setMouseTransparent(!modifiable);
		
		
		//component function tree
		if(this.getOwnerNodeBuilder().getRootComponentFunctionBuilder()!=null)
			this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().setModifiable(modifiable);
		
		
		//invoke the update() method
		//this is very important because some features should be always be disabled even if modifiable is true!
		//if not invoke the update() method, those features may be mistakenly enabled!
		//also this will make implementation of setModifiable() of all involved UIs simple and straight-forward without concerning of the above issue;
		if(modifiable)
			this.getOwnerNodeBuilder().update(); //only invoke this when modifiable is true!!
		
	}
	
	
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
		//
		this.compositionFunctionNotesTextArea.setText("");
		
		
		//delete the current tree which will trigger actions that clear all the tree related UI content
		//this must be invoked before clear selected targets and created original IndependentFreeInputVariableTypes
		if(this.getOwnerNodeBuilder().getRootComponentFunctionBuilder()!=null)
			this.deleteTreeButtonOnAction(null);
		
		//clear selected targets
		if(this.getOwnerNodeBuilder().getAssignedTargetNameMap().isEmpty()) { //already cleared
			//
		}else {
			/////
			this.targetHBoxSet.forEach(e->{
				if(e.assignedToPreExistingCFs) {
					//for those assigned to other composition functions, do nothing
				}else {
					e.checkBox.setSelected(false);
					this.getOwnerNodeBuilder().removeAssignedTarget(e.target.getName());
				}
				
			});
		}
		
		
		//clear all original IndependentFreeInputVariableTypes
		this.getOwnerNodeBuilder().deleteAllOriginalIndieFIVTypes();
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * for CompositionFunctionBuilder, owner CFG and index ID are given in constructor, thus cannot be set in this method;
	 * 
	 * only set the following
	 * 1. notes
	 * 2. selected CFGTargets;
	 * 3. created original IndependentFreeInputVariableTypes
	 * 4. ComponentFunction tree
	 * 
	 * note that ComponentFunction tree must be set after selected CFGTargets and created original IndependentFreeInputVariableTypes
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(CompositionFunction value) throws SQLException, IOException {
		//0 first set to default empty
		this.setUIToDefaultEmptyStatus();
		
		
		
		//1. notes
		this.compositionFunctionNotesTextArea.setText(value.getNotes().getNotesString());
		
		
		//2. selected CFGTargets;
//		Set<SimpleName> a = value.getAssignedTargetNameSet();
		this.targetHBoxSet.forEach(e->{
			if(e.assignedToPreExistingCFs) {
				//for those assigned to other composition functions, do nothing
			}else {
//				if(value.getAssignedTargetNameSet().contains(e.target.getName())) {
				e.checkBox.setSelected(value.getAssignedTargetNameSet().contains(e.target.getName()));
//				}
			}
		});
		
		//3. created original IndependentFreeInputVariableTypes
//		Set<IndependentFreeInputVariableType> b = value.getOriginalIndependentFreeInputVariableTypeSet();
		for(IndependentFreeInputVariableTypeID k:value.getOriginalIndependentFreeInputVariableTypeIDMap().keySet()){
			OriginalIndieFIVTypeEntryBuilder entryBuilder = new OriginalIndieFIVTypeEntryBuilder(this.getOwnerNodeBuilder());
			entryBuilder.getIndependentFreeInputVariableTypeBuilder().setValue(value.getOriginalIndependentFreeInputVariableTypeIDMap().get(k), false);
			this.getOwnerNodeBuilder().addOriginalIndieFIVTypeEntryBuilder(entryBuilder);
		}
		
		//4. ComponentFunction tree
		ComponentFunctionBuilder<? extends ComponentFunction,?> rootCFBuiler;
		if(value.getRootFunction() instanceof SimpleFunction) {
			rootCFBuiler = new SimpleFunctionBuilder(this.getOwnerNodeBuilder(), null);
			//set the root CF builder before set the value!!!!!!!!!!!!!!!!!!
			this.getOwnerNodeBuilder().setRootComponentFunctionBuilder(rootCFBuiler);
			
			//set value
			SimpleFunctionBuilderEmbeddedUIContentController controller = 
					(SimpleFunctionBuilderEmbeddedUIContentController)rootCFBuiler.getEmbeddedUIContentController();
			controller.setUIToNonNullValue((SimpleFunction)value.getRootFunction());
		}else {
			rootCFBuiler = new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder(), null);
			
			//set the root CF builder before set the value!!!!!!!!!!!!!!!!!!
			this.getOwnerNodeBuilder().setRootComponentFunctionBuilder(rootCFBuiler);
			
			//set value
			PiecewiseFunctionBuilderEmbeddedUIContentController controller = 
					(PiecewiseFunctionBuilderEmbeddedUIContentController)rootCFBuiler.getEmbeddedUIContentController();
			controller.setUIToNonNullValue((PiecewiseFunction)value.getRootFunction());
		}
		
		//this will reset layout the new tree on the canvass?
		this.layoutButtonOnAction(null);
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	
	
	
	//////////////////////////////
	/**
	 * add the embedded UI of the given ComponentFunctionBuilder to the componentFunctionTreeAnchorPane
	 * @param cfb
	 */
	public void addComponentFunctionBuilder(ComponentFunctionBuilder<?,?> cfb) {
		if(cfb!=null) {
			this.componentFunctionTreeAnchorPane.getChildren().add(cfb.getEmbeddedUIContentController().getRootParentNode());
			
			cfb.getEmbeddedUIContentController().buildCurveToPrevious();
			if(cfb.getEmbeddedUIContentController().getCurveToPrevious()!=null)
				this.componentFunctionTreeAnchorPane.getChildren().add(cfb.getEmbeddedUIContentController().getCurveToPrevious());
		}
	}
	
	/**
	 * remove the embedded UI of the given ComponentFunctionBuilder from the componentFunctionTreeAnchorPane
	 * @param cfb
	 */
	public void removeComponentFunctionBuilder(ComponentFunctionBuilder<?,?> cfb) {
		if(cfb!=null) {
			this.componentFunctionTreeAnchorPane.getChildren().remove(cfb.getEmbeddedUIContentController().getRootParentNode());
			if(cfb.getEmbeddedUIContentController().getCurveToPrevious()!=null)
				this.componentFunctionTreeAnchorPane.getChildren().remove(cfb.getEmbeddedUIContentController().getCurveToPrevious());
			//
			if(this.componentFunctionTreeAnchorPane.getChildren().isEmpty()) {
				FXUtils.set2Disable(this.makeRootButton, false);
				FXUtils.set2Disable(this.deleteTreeButton, true);
			}
		}
		
	}
	
	
	void updateSummary() {
//		this.independentFreeInputVariableTypeListView.getItems().clear();
//		this.independentFreeInputVariableTypeListView.getItems().addAll(this.getOwnerNodeBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().values());
		
		this.dependedRecordDataMetadataListView.getItems().clear();
		this.dependedRecordDataMetadataListView.getItems().addAll(this.getOwnerNodeBuilder().getDependedRecordDataMetadataIDSet());
		
		this.dependedCFListView.getItems().clear();
		this.dependedCFListView.getItems().addAll(this.getOwnerNodeBuilder().getDependedCompositionFunctionIDSet());
	}
	
	void setTargetSelectionDisable(boolean disable) {
		this.targetItemsVBox.setMouseTransparent(disable);
	}
	
	
	void setFinishableLabe() {
		this.finishableLabel.setText(this.getOwnerNodeBuilder().isFinishable()?"FINISHABLE":"UNFINISHABLE");
	}
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		FXUtils.set2Disable(this.makeRootButton, false);
		FXUtils.set2Disable(this.deleteTreeButton, true);
		
		
		this.dependedRecordDataMetadataListView.setCellFactory(new Callback<ListView<MetadataID>, ListCell<MetadataID>>() {

		    @Override
		    public ListCell<MetadataID> call(ListView<MetadataID> param) {
		        ListCell<MetadataID> cell = new ListCell<MetadataID>() {

		            @Override
		            protected void updateItem(MetadataID item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item != null) {
		                    setText(item.getName().getStringValue());
		                }
		            }
		        };
		        return cell;
		    }
		});
		
		this.dependedCFListView.setCellFactory(new Callback<ListView<CompositionFunctionID>, ListCell<CompositionFunctionID>>() {

		    @Override
		    public ListCell<CompositionFunctionID> call(ListView<CompositionFunctionID> param) {
		        ListCell<CompositionFunctionID> cell = new ListCell<CompositionFunctionID>() {
		        	
		            @Override
		            protected void updateItem(CompositionFunctionID item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item != null) {
		                    setText(item.getHostCompositionFunctionGroupID().getName().getStringValue().concat(";").concat(Integer.toString(item.getIndexID())));
		                }
		            }
		        };
		        return cell;
		    }
		});
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField ownerCFGNameTextField;
	@FXML
	private Button viewOwnerCFGDetailButton;
	@FXML
	private TextField recordDataNameTextField;
	@FXML
	private TextField indexIDTextField;
	@FXML
	private TextArea compositionFunctionNotesTextArea;
	@FXML
	private VBox targetItemsVBox;
	@FXML
	VBox independentFreeInputVariableTypeVBox;
	@FXML
	private Button addIndieFIVTypeButton;
	@FXML
	private ListView<MetadataID> dependedRecordDataMetadataListView;
	@FXML
	private ListView<CompositionFunctionID> dependedCFListView;
	@FXML
	private HBox treeOperationButtonHBox;
	@FXML
	private Button deleteTreeButton;
	@FXML
	public AnchorPane componentFunctionTreeAnchorPane;
	@FXML
	private Label finishableLabel;

	////////////////////
	private AbstractCompositionFunctionGroupBuilder<?> ownerCFGBuilder;
	private Scene ownerCFGBuilderScene;
	private Stage ownerCFGBuilderStage;
	@FXML
	public void viewOwnerCFGDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.ownerCFGBuilderStage == null) {
			this.ownerCFGBuilder = CompositionFunctionGroupBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext())
					.build(this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getClass());
			this.ownerCFGBuilder.setValue(this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup(), false);
			
			//set builder modifiable 
			ownerCFGBuilder.setModifiable(false);
			
			ownerCFGBuilderScene = new Scene(ownerCFGBuilder.getIntegrativeUIController().getRootNode());
			
			//////////////
			ownerCFGBuilderStage = new Stage();
			ownerCFGBuilderStage.setScene(ownerCFGBuilderScene);
			
			ownerCFGBuilderStage.setWidth(1200);
			ownerCFGBuilderStage.setHeight(800);
			ownerCFGBuilderStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getOwnerCompositionFunctionGroup().getID().toString();
			ownerCFGBuilderStage.setTitle(title);
		}
		
		ownerCFGBuilderStage.showAndWait();
	}
	
	
	
	// Event Listener on Button[#addIndieFIVTypeButton].onAction
	@FXML
	public void addIndieFIVTypeButtonOnAction(ActionEvent event) throws SQLException, IOException {
		// TODO Autogenerated
		OriginalIndieFIVTypeEntryBuilder entryBuilder = new OriginalIndieFIVTypeEntryBuilder(this.getOwnerNodeBuilder());
		
		entryBuilder.getIndependentFreeInputVariableTypeBuilder().getIntegrativeUIController().showAndWait(this.getRootParentNode().getScene().getWindow(), "Original IndependentFreeInputVariableType Builder");
		
		//add to the owner CompositionFunctionBuilder is a 
		if(entryBuilder.getIndependentFreeInputVariableTypeBuilder().getCurrentStatus().hasValidValue()) {
			IndependentFreeInputVariableType type = entryBuilder.getIndependentFreeInputVariableTypeBuilder().getCurrentValue();
			if(this.getOwnerNodeBuilder().getOriginalIndependentFIVTypeNameNumberOfEmplyoerFIVMap().keySet().contains(//name is already taken by an existing one;
					type.getName())) {
				//this constraint is included in the IndependentFreeInputVariableTypeBuilder#buildGenericChildrenNodeBuilderConstraintSet() methods
				//no need to check it again;
				//AlertUtils.popAlert("Error", "Failed! created IndependentFreeInputVariableType's name is duplicate with an existing one!");
			}else {
				this.getOwnerNodeBuilder().addOriginalIndieFIVTypeEntryBuilder(entryBuilder);
			}
		}else {//no valid value created
			//
			AlertUtils.popAlert("Warning", "Failed! no IndependentFreeInputVariableType is created!");
		}
		
	}
	
	
	//////////////////////////
	@FXML
	private Button makeRootButton;
	// Event Listener on Button[#makeRootButton].onAction
	@FXML
	public void makeRootButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.getOwnerNodeBuilder().getAssignedTargetNameMap().isEmpty()) {
			AlertUtils.popAlert("warning", "no target is selected, cannot start building CompositionFunction!");
			return;
		}
		
		ComponentFunctionType selectedType = this.getOwnerNodeBuilder().getComponentFunctionTypeSelectionPopup().showAndWait();
		
		if(selectedType!=null) {
			if(selectedType.equals(ComponentFunctionType.SIMPLE)) {
				this.getOwnerNodeBuilder().setRootComponentFunctionBuilder(
						new SimpleFunctionBuilder(this.getOwnerNodeBuilder(), null));
			}else if(selectedType.equals(ComponentFunctionType.PIECEWISE)) {
				this.getOwnerNodeBuilder().setRootComponentFunctionBuilder(
						new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder(), null));
			}
			
			this.getOwnerNodeBuilder().update();
			
			
			FXUtils.set2Disable(this.makeRootButton, true);
			FXUtils.set2Disable(this.deleteTreeButton, false);
		}else {
			//no type selected, do nothing
		}
	}
	
	// Event Listener on Button[#deleteTreeButton].onAction
	@FXML
	public void deleteTreeButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().deleteFromTree();
		//
		this.getOwnerNodeBuilder().update();
		
		FXUtils.set2Disable(this.makeRootButton, false);
		FXUtils.set2Disable(this.deleteTreeButton, true);
	}
	
	//////////////////
	@FXML
	private Button layoutButton;
	// Event Listener on Button[#layoutButton].onAction
	@FXML
	public void layoutButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerNodeBuilder().update();
	}
	
	
	/////////////////////////
	@FXML
	private Button debugButton;
	// Event Listener on Button[#debugButton].onAction
	@FXML
	public void debugButtonOnAction(ActionEvent event) throws SQLException, IOException {
		System.out.println("==============debug===================");
		
		this.getOwnerNodeBuilder();
		this.layoutButtonOnAction(null);
//		System.out.println(
//				this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().getCFGTargetNameSetCalculatedByThisBuilder());
//		this.getOwnerNodeBuilder().getOriginalIndependentFreeInputVariableTypeNameMap().forEach((k,v)->{
//			System.out.println(k.getStringValue());
//		});
		
		
		
		
//		System.out.println("********************************");
//		this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().printlnLayout();
		
//		Node root = this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().getEmbeddedUIContentController().getRootParentNode();
//		root.setLayoutX(root.getLayoutX()+20);
//		root.setLayoutY(root.getLayoutY()+20);
		
		
//		this.getOwnerNodeBuilder().getRootComponentFunctionBuilder().setLayout();
		
		System.out.println("==============debug===================");
	}
	
	
	//////////////////////
	private class TargetHBox{
		private final CFGTarget<?> target;
		/**
		 * whether this target is assigned to an existing CompositionFunciton of the same CFG or not;
		 * if true, this target should never be selectable for the owner CompositionFunctionBuilder
		 */
		private final boolean assignedToPreExistingCFs;
		/////////////////
		private HBox containerHBox;
		private CheckBox checkBox;
		private TextField textField;
		
		
		TargetHBox(CFGTarget<?> target, boolean assignedToPreExistingCFs){
			this.target = target;
			this.assignedToPreExistingCFs = assignedToPreExistingCFs;
			
			
			containerHBox = new HBox();
	        checkBox = new CheckBox();
	        textField = new TextField(this.target.getName().getStringValue().concat(this.target.isMandatory()?";mandatory":";optional").concat(";").concat(this.target.getSQLDataType().getSQLString()));
	        textField.setEditable(false);
	        textField.setPrefWidth(300);
	        if(this.target.isMandatory())
	        	textField.setStyle("-fx-background-color:red");
	        containerHBox.getChildren().add(checkBox);
	        containerHBox.getChildren().add(textField);
	        
	        //whenever a item is selected, invoke the addAssignedTarget() method 
			//when an item is de-selected, invoke the removeAssignedTarget() method
	        if(assignedToPreExistingCFs) {
	        	this.checkBox.setSelected(true);
	        	FXUtils.set2Disable(this.checkBox, true);
	        	this.containerHBox.setStyle("-fx-background-color:green");
	        }else {
	        	checkBox.setOnMouseClicked(e->{
	        		try {
			        	if(checkBox.isSelected()) {
			        		CompositionFunctionBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().addAssignedTarget(this.target);
			        		
								CompositionFunctionBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().update();
							
			        	}else {
			        		CompositionFunctionBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().removeAssignedTarget(this.target.getName());
			        		CompositionFunctionBuilderEmbeddedUIContentController.this.getOwnerNodeBuilder().update();
			        	}
	        		} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        });
	        }
	        
	        
		}
		
		
		
	}
}
