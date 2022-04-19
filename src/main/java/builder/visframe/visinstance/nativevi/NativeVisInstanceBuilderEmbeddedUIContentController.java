package builder.visframe.visinstance.nativevi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import builder.visframe.visinstance.VisInstanceBuilderEmbeddedUIContentControllerBase;
import builder.visframe.visinstance.nativevi.utils.CoreShapeCFManager;
import core.table.visframeUDT.multipleselection.VisframeUDTTypeMultiSelectorManager;
import exception.VisframeException;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.group.ShapeCFG;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FXUtils;
import visinstance.NativeVisInstance;

/**
 * 
 * @author tanxu
 *
 */
public class NativeVisInstanceBuilderEmbeddedUIContentController extends VisInstanceBuilderEmbeddedUIContentControllerBase<NativeVisInstance>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/nativevi/NativeVisInstanceBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() throws SQLException {
		//any specific initialization based on the owner builder
		this.visInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisInstanceUID()));
		
		//name textfield
		this.nameTextField.setStyle("-fx-border-color:red");//initialize
		this.nameTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.nameTextField.focusedProperty().get()) {//lose focus
				//check if valid name is given and update the border color
				try {
					@SuppressWarnings("unused")
					SimpleName sn = new SimpleName(this.nameTextField.getText());
					this.nameTextField.setStyle("-fx-border-color:green");
				}catch(Exception e) {
					this.nameTextField.setStyle("-fx-border-color:red");
				}
				//invoke checkFinishable
				this.getOwnerNodeBuilder().checkFinishable();
			}
		});
		
		this.nameTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.nameTextField.getParent().requestFocus();
			}
		});
		
		
		//
		this.finishableLabel.textProperty().addListener((obs,ov,nv)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public NativeVisInstanceBuilder getOwnerNodeBuilder() {
		return (NativeVisInstanceBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.rootContainerVBox;
	}
	
	/**
	 * build and return a NativeVisInstance;
	 * 
	 * exception will be thrown if constraints are violated;
	 */
	@Override
	public NativeVisInstance build() {
		Set<CompositionFunctionGroupID> coreShapeCFGIDSet = new LinkedHashSet<>();
		Map<CompositionFunctionGroupID, Set<CompositionFunctionID>> coreShapeCFGIDCFIDSetMap = new LinkedHashMap<>();
		
		if(this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().isEmpty())
			throw new VisframeException("no core ShapeCFG is selected!");
		
		
		for(CompositionFunctionGroupID cfgID:this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().keySet()){
			coreShapeCFGIDSet.add(cfgID);
			Set<CompositionFunctionID> cfidSet = new HashSet<>();
			
			for(int index:this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().get(cfgID).getCfIndexCoreShapeCFManagerMap().keySet()){
				CoreShapeCFManager cfManager = this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().get(cfgID).getCfIndexCoreShapeCFManagerMap().get(index);
				if(cfManager.isSelected())
					cfidSet.add(cfManager.getCompositionFunction().getID());
			}
			
			if(cfidSet.isEmpty())
				throw new VisframeException("no CompositionFunction is selected for a core ShapeCFG!");
			
			coreShapeCFGIDCFIDSetMap.put(cfgID, cfidSet);
		}
		
		
		NativeVisInstance ret = new NativeVisInstance(
				new SimpleName(this.nameTextField.getText()),
				new VfNotes(this.notesTextArea.getText()),
				this.getOwnerNodeBuilder().getVisInstanceUID(),
				coreShapeCFGIDSet,
				coreShapeCFGIDCFIDSetMap
				);
		
		return ret;
	}
	
	
	/**
	 * set modifiable of the UI;
	 * 
	 * note that the selected CoreShapeCFGManagers are set by {@link NativeVisInstanceBuilder#setModifiable(boolean)} method
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.nameTextField.setEditable(modifiable);
		this.notesTextArea.setEditable(modifiable);
		
		FXUtils.set2Disable(cfgSelectionControlHBox, !modifiable);
	}
	
	
	/**
	 * TODO
	 * @throws SQLException 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException {
		this.nameTextField.setText("");
		this.nameTextField.setStyle("-fx-border-color:red");
		this.notesTextArea.setText("");
		
		this.sortByCFGNameCheckBox.setSelected(false);
		this.sortByOwnerRecordDataNameCheckBox.setSelected(false);
		
		///
		this.getOwnerNodeBuilder().clearAllCoreShapeCFGs();
	}	
	
	/**
	 * set to the give value;
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(NativeVisInstance value) throws SQLException, IOException {
		this.setUIToDefaultEmptyStatus();
		
		//
		this.nameTextField.setText(value.getName().getStringValue());
		this.nameTextField.setStyle("-fx-border-color:green");
		this.notesTextArea.setText(value.getNotes().getNotesString());
		
		//
		for(CompositionFunctionGroupID coreShapeCFGID:value.getCoreShapeCFGIDSet()){
			ShapeCFG coreShapeCFG = (ShapeCFG) this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(coreShapeCFGID);
			
			this.getOwnerNodeBuilder().addCoreShapeCFG(coreShapeCFG, false);
			
			this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().getCompositionFunctionIDSetOfGroupID(coreShapeCFGID).forEach(cfid->{
				CoreShapeCFManager cfManager = 
						this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().get(coreShapeCFGID).getCfIndexCoreShapeCFManagerMap().get(cfid.getIndexID());
				if(value.getCoreShapeCFGIDIncludedCFIDSetMap().get(coreShapeCFGID).contains(cfid)) {
					cfManager.setSelected(true);
				}else {
					cfManager.setSelected(false);
				}
			});
		}
		
		this.updateSelectedCoreShapeCFGLayoutOrder();
		this.getOwnerNodeBuilder().checkFinishable();
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	////////////////////////////
	/**
	 * update the layout order of {@link NativeVisInstanceBuilder#getSelectedCoreShapeCFGIDManagerMap()} based on whether the sort by owner record data name and cfg name are selected;
	 * 
	 * should be invoked whenever
	 * 
	 * 1. one or more ShapeCFGs are selected
	 * 
	 * 2. one or more selected ShapeCFG is de-selected
	 * 
	 * 3. {@link #sortByCFGNameCheckBox} and {@link #sortByOwnerRecordDataNameCheckBox} are selected or de-selected;
	 * 
	 * 4. 
	 * @throws SQLException 
	 */
	public void updateSelectedCoreShapeCFGLayoutOrder() throws SQLException {
		this.selectedCoreShapeCFGContainerVBox.getChildren().clear();
		
		List<ShapeCFG> selectedCoreShapeCFGList = new ArrayList<>();
		
		for(CompositionFunctionGroupID shapeCFGID:this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().keySet()){
			ShapeCFG shapeCFG = 
					(ShapeCFG) this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager().lookup(shapeCFGID);
			
			selectedCoreShapeCFGList.add(shapeCFG);
		}
		
		
		//sort the selected core ShapeCFGs based on the sorting orders
		Collections.sort(
				selectedCoreShapeCFGList, 
				(a,b)->{
					if(this.sortByOwnerRecordDataNameCheckBox.isSelected()) {
						if(this.sortByCFGNameCheckBox.isSelected()) {//both sorting order selected, first sort by owner record data name, then by the group name
							if(!a.getOwnerRecordDataMetadataID().equals(b.getOwnerRecordDataMetadataID()))
								return a.getOwnerRecordDataMetadataID().getName().compareTo(b.getOwnerRecordDataMetadataID().getName());
							else
								return a.getName().compareTo(b.getName());
						}else {//only sort by owner record data name
							return a.getOwnerRecordDataMetadataID().getName().compareTo(b.getOwnerRecordDataMetadataID().getName());
						}
					}else {//
						if(this.sortByCFGNameCheckBox.isSelected()) {//only sort by group name
							return a.getName().compareTo(b.getName());
						}else {//no sorting order selected, keep the original order
							return 1;
						}
					}
				}
				);
		
		//add the core ShapeCFGs to the UI based on the sorted order
		selectedCoreShapeCFGList.forEach(coreShapeCFG->{
			this.selectedCoreShapeCFGContainerVBox.getChildren().add(
					this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().get(coreShapeCFG.getID()).getController().getRootNodeContainer()
					);
		});
		
	}
	

	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		//TODO
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField visInstanceUIDTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private HBox cfgSelectionControlHBox;
	@FXML
	private Button selectMoreCoreShapeCFGButton;
	@FXML
	private Button clearAllSelectedCoreShapeCFGButton;
	@FXML
	private Button viewCFDGraphButton;
	@FXML
	private CheckBox sortByCFGNameCheckBox;
	@FXML
	private CheckBox sortByOwnerRecordDataNameCheckBox;
	@FXML
	private Button collapseAllButton;
	@FXML
	private Button expandAllButton;
	@FXML
	private Button checkDuplicateVisInstanceButton;
	@FXML Label finishableLabel;
	@FXML
	private VBox selectedCoreShapeCFGContainerVBox;

	// Event Listener on Button[#selectMoreCoreShapeCFGButton].onAction
	@FXML
	public void selectMoreCoreShapeCFGButtonOnAction(ActionEvent event) throws IOException, SQLException {
		//condition
		//1. must be of ShapeCFG type with all mandatory targets assigned to CompositionFunctions
		//2. cannot be any of the currently selected core ShapeCFGs
		Predicate<CompositionFunctionGroup> entityFilteringCondition = 
				e->{
					//already selected
					if(this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().keySet().contains(e.getID()))
						return false;
					
					//ShapeCFG with all mandatory targets assigned to a CompositionFunction
					return this.getOwnerNodeBuilder().getShapeCFGIDSetWithAllMandatoryTargetAssigned().contains(e.getID());
				};
				
		VisframeUDTTypeMultiSelectorManager<CompositionFunctionGroup, CompositionFunctionGroupID> manager = 
				new VisframeUDTTypeMultiSelectorManager<>(
						this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
						CompositionFunctionGroupBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()),
						null,
						entityFilteringCondition
						);
		
		manager.showAndWait((Stage) this.getRootParentNode().getScene().getWindow());
		
		
		//add newly selected ShapeCFGs
		for(CompositionFunctionGroup cfg:manager.getSelectedEntitySet()){
			this.getOwnerNodeBuilder().addCoreShapeCFG((ShapeCFG)cfg, false);
		}
		
		
		//update layout order
		this.updateSelectedCoreShapeCFGLayoutOrder();
		
		//
		this.getOwnerNodeBuilder().checkFinishable();
	}
	
	
	// Event Listener on Button[#clearAllSelectedCoreShapeCFGButton].onAction
	@FXML
	public void clearAllSelectedCoreShapeCFGButtonOnAction(ActionEvent event) throws SQLException {
		this.getOwnerNodeBuilder().clearAllCoreShapeCFGs();
	}
	
	// Event Listener on Button[#viewCFDGraphButton].onAction
	@FXML
	public void viewCFDGraphButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
		//see project CFD graph viewer
	}
	
	// Event Listener on CheckBox[#sortByCFGNameCheckBox].onAction
	@FXML
	public void sortByCFGNameCheckBoxOnAction(ActionEvent event) throws SQLException {
		this.updateSelectedCoreShapeCFGLayoutOrder();
	}
	
	// Event Listener on CheckBox[#sortByOwnerRecordDataNameCheckBox].onAction
	@FXML
	public void sortByOwnerRecordDataNameCheckBoxOnAction(ActionEvent event) throws SQLException {
		this.updateSelectedCoreShapeCFGLayoutOrder();
	}
	
	
	@FXML
	public void collapseAllButtonOnAction(ActionEvent event) {
		this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().forEach((id,manager)->{
			manager.getController().setCompositionFunctionSelectionTitledPaneExpanded(false);;
		});
	}
	// Event Listener on Button[#expandAllButton].onAction
	@FXML
	public void expandAllButtonOnAction(ActionEvent event) {
		this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDManagerMap().forEach((id,manager)->{
			manager.getController().setCompositionFunctionSelectionTitledPaneExpanded(true);;
		});
	}
	
	
	// Event Listener on Button[#checkDuplicateVisInstanceButton].onAction
	@FXML
	public void checkDuplicateVisInstanceButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
	
}
