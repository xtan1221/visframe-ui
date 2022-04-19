package builder.visframe.function.component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.VfNotes;
import builder.visframe.function.component.ComponentFunctionBuilder.ComponentFunctionType;
import builder.visframe.function.component.utils.SimpleFunctionEvaluatorEntryBuilder;
import function.component.ComponentFunction;
import function.component.PiecewiseFunction;
import function.component.SimpleFunction;
import function.composition.CompositionFunctionID;
import function.evaluator.AbstractEvaluator;
import function.evaluator.Evaluator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import utils.FXUtils;

public class SimpleFunctionBuilderEmbeddedUIContentController extends ComponentFunctionBuilderEmbeddedUIContentController<SimpleFunction>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/component/SimpleFunctionBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		super.setupLogicToCheckEffectiveUIInput();
		//any specific initialization based on the owner builder
		
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	public SimpleFunctionBuilder getOwnerNodeBuilder() {
		return (SimpleFunctionBuilder) super.getOwnerNodeBuilder();
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.rootContainerVBox;
	}
	
	/**
	 * 1. check if the owner ComponentFunctionBuilder's currentVisframeExecptionSet is empty or not;
	 * 		if not, throw them;
	 * 2. build the SimpleFunction
	 * 		1. build the next function if not leaf;
	 * 		2. build the evaluator map
	 */
	@Override
	public SimpleFunction build() {
//		if(!this.getOwnerNodeBuilder().getCurrentVisframeExecptionSet().isEmpty()) {
//			throw this.getOwnerNodeBuilder().getCurrentVisframeExecptionSet().iterator().next();
//		}
		
		//////////////
		CompositionFunctionID hostCompositionFunctionID = this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getCompositionFunctionID();
		int indexID = this.getOwnerNodeBuilder().getIndexID();
		VfNotes notes = VfNotes.makeVisframeDefinedVfNotes();
		
		
		ComponentFunction nextFunction = null;
		if(this.getOwnerNodeBuilder().getNextComponentFunctionBuilder()!=null)
			nextFunction = this.getOwnerNodeBuilder().getNextComponentFunctionBuilder().getEmbeddedUIContentController().build();
		
		Map<Integer,Evaluator> evaluatorIndexIDMap = new LinkedHashMap<>();
		
		for(SimpleFunctionEvaluatorEntryBuilder entry:this.getOwnerNodeBuilder().getEvaluatorIndexIDEntryBuilderMap().values()) {
			AbstractEvaluator evaluator = entry.getSimpleFunctionEvaluatorBuilderDelegate().getEvaluatorBuilder().getCurrentValue();
			evaluatorIndexIDMap.put(evaluator.getIndexID(), evaluator);
		}
		
		return new SimpleFunction(hostCompositionFunctionID, indexID, notes, nextFunction, evaluatorIndexIDMap);
	}
	
	@Override
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.deleteThisSimpleFunctionButton, !modifiable);
		FXUtils.set2Disable(this.addEvaluatorButton, !modifiable);
		FXUtils.set2Disable(this.makeNextFunctionButton, !modifiable);
		FXUtils.set2Disable(this.deleteNextFunctionButton, !modifiable);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		//TODO
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * 1. set index id
	 * 
	 * 2. set evaluators
	 * 
	 * 3. set next function if not null
	 * 
	 * note that evaluators must be set before the next function!
	 * 		this is because downstream functions' input variables may depend on the output variables of evaluators of this SimpleFunction
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(SimpleFunction value) throws SQLException, IOException {
		//1. set index id
		this.getOwnerNodeBuilder().setIndexID(value.getIndexID());
		
		
		//2. set evaluators
		for(int id:value.getEvaluatorIndexIDMap().keySet()){
			SimpleFunctionEvaluatorEntryBuilder entryBuilder = new SimpleFunctionEvaluatorEntryBuilder(this.getOwnerNodeBuilder());
			
			this.evaluatorListVBox.getChildren().add(entryBuilder.getController().getRootContainerNode());
			//
			this.getOwnerNodeBuilder().getEvaluatorIndexIDEntryBuilderMap().put(id, entryBuilder);
			//
			entryBuilder.getSimpleFunctionEvaluatorBuilderDelegate().addStatusChangedAction(
					()->{
						try {
							this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().update();
						} catch (SQLException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}}
					);
			
			//!!!set the value after the status change event action is added;
			entryBuilder.getSimpleFunctionEvaluatorBuilderDelegate().setValue(value.getEvaluatorIndexIDMap().get(id), false);
		}
		
		
		//3. set next function if not null
		if(value.getNext()!=null) {
			ComponentFunctionBuilder<?,?> nextCFBuiler;
			if(value.getNext() instanceof SimpleFunction) {
				nextCFBuiler = new SimpleFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());
				
				//add next function before set the value!!!!!!!!!
				this.getOwnerNodeBuilder().setNextComponentFunctionBuilder(nextCFBuiler);
				
				//set value
				SimpleFunctionBuilderEmbeddedUIContentController controller = 
						(SimpleFunctionBuilderEmbeddedUIContentController)nextCFBuiler.getEmbeddedUIContentController();
				controller.setUIToNonNullValue((SimpleFunction)value.getNext());
			}else {
				nextCFBuiler = new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder());
				
				//add next function before set the value!!!!!!!!!
				this.getOwnerNodeBuilder().setNextComponentFunctionBuilder(nextCFBuiler);
				
				//set value
				PiecewiseFunctionBuilderEmbeddedUIContentController controller = 
						(PiecewiseFunctionBuilderEmbeddedUIContentController)nextCFBuiler.getEmbeddedUIContentController();
				controller.setUIToNonNullValue((PiecewiseFunction)value.getNext());
			}
			
		}
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	

	@Override
	public Pane getMainContentPane() {
		return this.mainVBox;
	}

	@Override
	public Circle getToPreviousCircle() {
		return this.goToPreviousFunctionCircle;
	}
	
	
	/////////////////////////////////////////////////////////
	
	void updateSummary() {
		this.targetNumAssignedByThisCFTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getCFGTargetNameSetCalculatedByThisBuilder().size()));
		this.targetNumUnAssignedTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getUncalculatedCFGTargetNameMap().size()));
	}
	
	
	void setAddEvaluatorButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.addEvaluatorButton, disable);
	}
	
	void setMakeNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.makeNextFunctionButton, disable);
	}
	
	void setDeleteNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.deleteNextFunctionButton, disable);
	}
	//////////////////////////
	
	@FXML
	public void initialize() {
		super.initialize();
		//TODO
//		this.makeNextFunctionButton.setDisable(false);
//		this.deleteNextFunctionButton.setDisable(true);
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private VBox mainVBox;
	@FXML
	private Button deleteThisSimpleFunctionButton;
	@FXML
	private Circle goToPreviousFunctionCircle;
	@FXML
	private Button addEvaluatorButton;
	@FXML
	VBox evaluatorListVBox;
	@FXML
	Button makeNextFunctionButton;
	@FXML
	Button deleteNextFunctionButton;
	@FXML
	public Circle goToNextFunctionCircle;
	@FXML
	private TitledPane inforTitledPane;
	@FXML
	private TextField targetNumAssignedByThisCFTextField;
	@FXML
	private TextField targetNumUnAssignedTextField;
	
	
	
	// Event Listener on Button[#deleteThisSimpleFunctionButton].onAction
	@FXML
	public void deleteThisSimpleFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder()==null) {
			this.getOwnerNodeBuilder().deleteAllNextFunctions();
			this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().setRootComponentFunctionBuilder(null);
		}else {
			//first delete all next functions of this
			this.getOwnerNodeBuilder().deleteAllNextFunctions();
			//then delete this function from previous function
			this.getOwnerNodeBuilder().getPreviousComponentFunctionBuilder().deleteNextFunction(this.getOwnerNodeBuilder());
		}
	
		//
		this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().update();
	}
	
	// Event Listener on Circle[#goToPreviousFunctionCircle].onMouseClicked
	@FXML
	public void goToPreviousCircleOnMouseClicked(MouseEvent event) {
		// TODO Autogenerated
	}
	
	// Event Listener on Button[#addEvaluatorButton].onAction
	@FXML
	public void addEvaluatorButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.evaluatorListVBox.getChildren().add(this.getOwnerNodeBuilder().addEvaluator().getController().getRootContainerNode());
		
		this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().update();
	}
	
	
	// Event Listener on Button[#nextFunctionButton].onAction
	@FXML
	public void makeNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		ComponentFunctionType selectedType = this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder().getComponentFunctionTypeSelectionPopup().showAndWait();
		
		if(selectedType!=null) {
			if(selectedType.equals(ComponentFunctionType.SIMPLE)) {
				this.getOwnerNodeBuilder().setNextComponentFunctionBuilder(new SimpleFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder()));
			}else if(selectedType.equals(ComponentFunctionType.PIECEWISE)) {
				this.getOwnerNodeBuilder().setNextComponentFunctionBuilder(new PiecewiseFunctionBuilder(this.getOwnerNodeBuilder().getHostCompositionFunctionBuilder(), this.getOwnerNodeBuilder()));
			}
			
			
		}else {
			//
		}
		
	}
	// Event Listener on Button[#deleteNextFunctionButton].onAction
	@FXML
	public void deleteNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerNodeBuilder().setNextComponentFunctionBuilder(null);
		
	}
	// Event Listener on Circle[#goToNextFunctionCircle].onMouseClicked
	@FXML
	public void goToNextFunctionCircleOnMouseClicked(MouseEvent event) {
		// TODO Autogenerated
	}
	
}
