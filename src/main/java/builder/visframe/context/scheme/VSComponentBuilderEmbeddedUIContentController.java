package builder.visframe.context.scheme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.VfNotes;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import context.scheme.VSComponent;
import core.builder.NodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.table.visframeUDT.multipleselection.VisframeUDTTypeMultiSelectorManager;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.group.ShapeCFG;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VSComponentBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VSComponent> {
	public static final String FXML_FILE_DIR = "/builder/visframe/context/scheme/VSComponentBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		// whenever the children list of the SelectedCoreShapeCFGEntrySetContainerVBox is changed, try to build the VSComponent to check if current value is valid
		
		this.notesTextArea.focusedProperty().addListener((o,ov,nv)->{
			this.update();
		});
		
//		this.precedenceIndexTextField.textProperty().addListener((o,ov,nv)->{
//			this.update();
//		});
		
		this.getOwnerNodeBuilder().updateSelectedCoreShapeCFGNumTextField();
		
//		this.update();
	}
	
	/**
	 * try to build the target object
	 */
	void update() {
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().updateNonNullValueFromContentController(false);
		
		//VSComponentBuilder's root UI is not used, however, every time after updating, the root node of the embedded UI will be added to the root UI (see {@link EmbeddedUIRootContainerNodeController#setUIToNonNullStatus()});
		//thus need to add it back at the corresponding location
		if(!this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().contains(this.getRootParentNode()))
			this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getEmbeddedUIContentController().getVSComponentPrecedenceListVBox().getChildren().add(
					this.getOwnerNodeBuilder().getPrecedenceIndex(),
					this.getRootParentNode()
					);
	}
	
	
	@Override
	public VSComponentBuilder getOwnerNodeBuilder() {
		return (VSComponentBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	
	/**
	 * must first check if owner {@link NodeBuilder}'s current status is valid value or not;
	 */
	@Override
	public VSComponent build() {
		
		VfNotes notes = new VfNotes(this.notesTextArea.getText());
		
		Set<CompositionFunctionGroupID> coreShapeCFGIDSet = new HashSet<>();
		
		coreShapeCFGIDSet.addAll(this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDEntryManagerMap().keySet());
		
		return new VSComponent(notes, coreShapeCFGIDSet);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		//do nothing, all the UI related resetting are controlled by others ??
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(VSComponent value) throws SQLException {
		this.getOwnerNodeBuilder().removeAllCoreShapeCFGs();
		for(CompositionFunctionGroupID id:value.getCoreShapeCFGIDSet()){
			ShapeCFG shapeCFG = 
					(ShapeCFG) this.getOwnerNodeBuilder().getTempVisScheme().getCompositionFunctionGroupLookup().lookup(id);
			
			this.getOwnerNodeBuilder().addCoreShapeCFG(shapeCFG);
		}
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	
	VBox getSelectedCoreShapeCFGEntrySetContainerVBox() {
		return this.selectedCoreShapeCFGEntrySetContainerVBox;
	}
	
	TitledPane getSelectedCoreShapeCFGSetTitledPane() {
		return this.selectedCoreShapeCFGSetTitledPane;
	}
	
	TextField getPrecedenceIndexTextField() {
		return this.precedenceIndexTextField;
	}
	
	TextField getSelectedCoreShapeCFGNumTextField() {
		return this.selectedCoreShapeCFGNumTextField;
	}
	
	//////////////////////////
	
	@FXML
	public void initialize() {
		
	}

	@FXML
	private VBox rootContainerVBox;
	@FXML
	private HBox mainOperationHBox;
	@FXML
	private Button deleteButton;
	@FXML
	private Button moveDownButton;
	@FXML
	private Button moveUpButton;
	@FXML
	private TextField precedenceIndexTextField;
	@FXML
	private TextField selectedCoreShapeCFGNumTextField;
	@FXML
	private TitledPane selectedCoreShapeCFGSetTitledPane;
	@FXML
	private Button addButton;
	@FXML
	private Button clearButton;
	@FXML
	private VBox selectedCoreShapeCFGEntrySetContainerVBox;
	@FXML
	private TextArea notesTextArea;

	// Event Listener on Button[#deleteButton].onAction
	@FXML
	public void deleteButtonOnAction(ActionEvent event) throws SQLException {
		this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().deleteVSComponentBuilder(this.getOwnerNodeBuilder().getPrecedenceIndex());
	}
	// Event Listener on Button[#moveDownButton].onAction
	@FXML
	public void moveDownButtonOnAction(ActionEvent event) throws SQLException {
		if(this.getOwnerNodeBuilder().getPrecedenceIndex()==this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getComponentBuilderList().size()-1) {
			//already the last one, cannot move down further
			
		}else {
			this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().exchangeComponentPosition(
					this.getOwnerNodeBuilder().getPrecedenceIndex(), this.getOwnerNodeBuilder().getPrecedenceIndex()+1);
			
			this.update();
		}
	}
	// Event Listener on Button[#moveUpButton].onAction
	@FXML
	public void moveUpButtonOnAction(ActionEvent event) throws SQLException {
		if(this.getOwnerNodeBuilder().getPrecedenceIndex()==0) {
			//already the first one, cannot move down further
		}else {
			this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().exchangeComponentPosition(
					this.getOwnerNodeBuilder().getPrecedenceIndex(), this.getOwnerNodeBuilder().getPrecedenceIndex()-1);
			
			this.update();
		}
	}
	
	// Event Listener on Button[#addButton].onAction
	@FXML
	public void addButtonOnAction(ActionEvent event) throws SQLException, IOException {
		//condition
		//1. must be of ShapeCFG type with all mandatory targets assigned to CompositionFunctions
		//2. cannot be any of the currently selected core ShapeCFGs
		Predicate<CompositionFunctionGroup> entityFilteringCondition = 
				e->{
					//already selected
					if(this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getSelectedShapeCFGIDSet().contains(e.getID()))
						return false;
					
					//ShapeCFG with all mandatory targets assigned to a CompositionFunction
					return this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getShapeCFGIDSetWithAllMandatoryTargetAssigned().contains(e.getID());
				};
				
		VisframeUDTTypeMultiSelectorManager<CompositionFunctionGroup, CompositionFunctionGroupID> manager = 
				new VisframeUDTTypeMultiSelectorManager<>(
						this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
						CompositionFunctionGroupBuilderFactory.singleton(this.getOwnerNodeBuilder().getOwnerVisSchemeBuilder().getHostVisProjectDBContext()),
						null,
						entityFilteringCondition
						);
		
		manager.showAndWait((Stage) this.getRootParentNode().getScene().getWindow());
		
		for(CompositionFunctionGroup cfg:manager.getSelectedEntitySet()){
			this.getOwnerNodeBuilder().addCoreShapeCFG((ShapeCFG) cfg);
		}
		
	}
	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) throws SQLException {
		this.getOwnerNodeBuilder().removeAllCoreShapeCFGs();
	}
}
