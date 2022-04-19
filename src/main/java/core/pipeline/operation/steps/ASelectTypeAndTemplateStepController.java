package core.pipeline.operation.steps;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import basic.lookup.project.type.udt.VisProjectOperationManager;
import builder.visframe.operation.OperationBuilderForViewOnlyFactory;
import core.pipeline.AbstractProcessStepController;
import core.pipeline.operation.utils.OperationTypeTreeViewUtils;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.Operation;
import operation.OperationID;
import operation.OperationName;
import sql.derby.TableContentSQLStringFactory;
import utils.AlertUtils;

public class ASelectTypeAndTemplateStepController extends AbstractProcessStepController<Operation, ASelectTypeAndTemplateStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/operation/steps/ASelectTypeAndTemplateStep.fxml";
	
	@Override
	public ASelectTypeAndTemplateStepManager getManager() {
		return(ASelectTypeAndTemplateStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectTypeAndTemplateStepSettings settings) {
		this.operationTypeSelectionTreeView.getSelectionModel().select(
				settings.getSelectedOperationType()==null?null:new TreeItem<>(settings.getSelectedOperationType()));///????
		
		this.templateOperationInstanceNameTextField.setText(
				settings.getSelectedTemplateOperationID()==null?"":settings.getSelectedTemplateOperationID().getInstanceName().getStringValue());
		
	}
	
	@Override
	public ASelectTypeAndTemplateStepSettings getStepSettings() {
		TreeItem<Class<? extends Operation>>  selectedItem = this.operationTypeSelectionTreeView.getSelectionModel().getSelectedItem();
		Class<? extends Operation> selectedOperationType;
		OperationID selectedTemplateOperationID;
		if(selectedItem==null) {
			selectedOperationType = null;
			selectedTemplateOperationID = null;
		}else {
			selectedOperationType = selectedItem.getValue();
			
			if(!Modifier.isFinal(selectedOperationType.getModifiers())){
				selectedOperationType = null;
				selectedTemplateOperationID = null;
    		}else {
    			if(this.templateOperationInstanceNameTextField.getText().isEmpty()) {
    				selectedTemplateOperationID = null;
    			}else {
    				selectedTemplateOperationID = new OperationID(new OperationName(this.templateOperationInstanceNameTextField.getText()));
    			}
    		}
		}
		
		return new ASelectTypeAndTemplateStepSettings(selectedOperationType, selectedTemplateOperationID);
	}
	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getSelectedOperationType()==null) {
			AlertUtils.popAlert("Error", "Operation Type is not selected!");
			return false;
		}
		
		return true;
	}
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Pane getRootNode() {
		return rootVBox;
	}
	
	///////////////////////////////////////////////////
	private TreeView<Class<? extends Operation>> operationTypeSelectionTreeView;
	
	@FXML
	@Override
	public void initialize() {
		//add the tree view for all supported operation types
		
		operationTypeSelectionTreeView = OperationTypeTreeViewUtils.makeTreeView(selectedOperationTypeTextField);
        
		this.operationTypeSelectionVBox.getChildren().add(operationTypeSelectionTreeView);
		
//		this.selectedOperationTypeTextField.setOnMouseClicked(e->{
//			System.out.println(this.operationTypeSelectionTreeView.getSelectionModel().getSelectedItem());
//		});
	}
	
	@FXML
	public VBox rootVBox;
	@FXML
	private VBox operationTypeSelectionVBox;
	@FXML
	private TextField selectedOperationTypeTextField;
	@FXML
	private Button clearSelectedOperationTypeButton;
	@FXML
	private TextField templateOperationInstanceNameTextField;
	@FXML
	private Button chooseTemplateOperationInstanceButton;
	@FXML
	private Button clearSelectedTemplateOperationInstanceButton;

	// Event Listener on Button[#clearSelectedOperationTypeButton].onAction
	@FXML
	public void clearSelectedOperationTypeButtonOnAction(ActionEvent event) {
		//first clear the selected item of the tree view
		this.operationTypeSelectionTreeView.getSelectionModel().clearSelection();
		
		//also clear any selected operation template
		this.clearSelectedTemplateOperationInstanceButtonOnAction(event);
		
	}
	
	// Event Listener on Button[#chooseTemplateOperationInstanceButton].onAction
	@FXML
	public void chooseTemplateOperationInstanceButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.selectedOperationTypeTextField.getText().isEmpty()) {
			AlertUtils.popAlert("warning", "No Operation type is selected!");
			return;
		}
		
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(OperationID.NAME_COLUMN.getName(), this.templateOperationInstanceNameTextField);
		
		final VisframeUDTTypeTableViewerManager<Operation,OperationID> operationTableViewManager = new VisframeUDTTypeTableViewerManager<>(
				this.getManager().getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getOperationManager(),
				OperationBuilderForViewOnlyFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext()),
				TableContentSQLStringFactory.buildColumnValueEquityCondition(
						VisProjectOperationManager.OPERATION_TYPE_COLUMN.getName().getStringValue(), 
						this.operationTypeSelectionTreeView.getSelectionModel().getSelectedItem().getValue().getSimpleName(), 
						true, true),//sqlFilterConditionString
				null,
				TableViewerMode.SELECTION,
				invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
				);
		
		Runnable operationAfterSelectionIsDone = ()->{
			if(operationTableViewManager.getSelectedItem()==null) {
				this.templateOperationInstanceNameTextField.setText("");
			}else {
				this.templateOperationInstanceNameTextField.setText(operationTableViewManager.getSelectedItem().getID().getInstanceName().getStringValue());
			}
		};
		
//		System.out.println("1");
		operationTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
//		System.out.println("2");
		/////////
		operationTableViewManager.refresh();
//		System.out.println("0");
		operationTableViewManager.showWindow(this.getStage());
//		System.out.println("4");
	}
	
	
	// Event Listener on Button[#clearSelectedTemplateOperationInstanceButton].onAction
	@FXML
	public void clearSelectedTemplateOperationInstanceButtonOnAction(ActionEvent event) {
		this.templateOperationInstanceNameTextField.setText("");
	}

}
