package core.pipeline.cfg.steps;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import basic.lookup.project.type.udt.VisProjectCompositionFunctionGroupManager;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import core.pipeline.AbstractProcessStepController;
import core.pipeline.cfg.utils.CompositionFunctionGroupTypeTreeViewUtils;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.group.CompositionFunctionGroupName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import sql.derby.TableContentSQLStringFactory;
import utils.AlertUtils;

public class ASelectTypeAndTemplateStepController extends AbstractProcessStepController<CompositionFunctionGroup, ASelectTypeAndTemplateStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/cfg/steps/ASelectTypeAndTemplateStep.fxml";
	
	@Override
	public ASelectTypeAndTemplateStepManager getManager() {
		return(ASelectTypeAndTemplateStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectTypeAndTemplateStepSettings settings) {
		this.cfgTypeSelectionTreeView.getSelectionModel().select(
				settings.getSelectedCompositionFunctionGroupType()==null?null:new TreeItem<>(settings.getSelectedCompositionFunctionGroupType()));///????
		
		this.templateNameTextField.setText(
				settings.getSelectedTemplateCompositionFunctionGroupID()==null?"":settings.getSelectedTemplateCompositionFunctionGroupID().getName().getStringValue());
	}
	
	@Override
	public ASelectTypeAndTemplateStepSettings getStepSettings() {
		TreeItem<Class<? extends CompositionFunctionGroup>>  selectedItem = this.cfgTypeSelectionTreeView.getSelectionModel().getSelectedItem();
		Class<? extends CompositionFunctionGroup> selectedOperationType;
		CompositionFunctionGroupID selectedTemplateOperationID;
		if(selectedItem==null) {
			selectedOperationType = null;
			selectedTemplateOperationID = null;
		}else {
			selectedOperationType = selectedItem.getValue();
			
			if(!Modifier.isFinal(selectedOperationType.getModifiers())){
				selectedOperationType = null;
				selectedTemplateOperationID = null;
    		}else {
    			if(this.templateNameTextField.getText().isEmpty()) {
    				selectedTemplateOperationID = null;
    			}else {
    				selectedTemplateOperationID = new CompositionFunctionGroupID(new CompositionFunctionGroupName(this.templateNameTextField.getText()));
    			}
    		}
		}
		
		
		return new ASelectTypeAndTemplateStepSettings(selectedOperationType, selectedTemplateOperationID);
	}

	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getSelectedCompositionFunctionGroupType()==null) {
			AlertUtils.popAlert("Error", "CompositionFunctionGroup Type is not selected!");
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
	private TreeView<Class<? extends CompositionFunctionGroup>> cfgTypeSelectionTreeView;
	
	@FXML
	@Override
	public void initialize() {
		//add the tree view for all supported operation types
		
		cfgTypeSelectionTreeView = CompositionFunctionGroupTypeTreeViewUtils.makeTreeView(this.selectedCFGTypeTextField);
        
		this.typeSelectionVBox.getChildren().add(cfgTypeSelectionTreeView);
		
		cfgTypeSelectionTreeView.getSelectionModel().selectedItemProperty().addListener((a,b,c)->{
			this.templateNameTextField.setText("");
		});
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private VBox typeSelectionVBox;
	@FXML
	private TextField selectedCFGTypeTextField;
	@FXML
	private Button clearTypeSelectionButton;
	@FXML
	private TextField templateNameTextField;
	@FXML
	private Button chooseTemplateButton;
	@FXML
	private Button clearTemplateSelectionButton;

	
	// Event Listener on Button[#clearTypeSelectionButton].onAction
	@FXML
	public void clearTypeSelectionButtonOnAction(ActionEvent event) {
		//first clear the selected item of the tree view
		this.cfgTypeSelectionTreeView.getSelectionModel().clearSelection();
		
		//also clear any selected operation template
		this.clearTemplateSelectionButtonOnAction(event);
	}
	
	
	// Event Listener on Button[#chooseTemplateButton].onAction
	@FXML
	public void chooseTemplateButtonOnAction(ActionEvent event) throws SQLException, IOException {
		
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(CompositionFunctionGroupID.NAME_COLUMN.getName(), this.templateNameTextField);
		
		final VisframeUDTTypeTableViewerManager<CompositionFunctionGroup,CompositionFunctionGroupID> cfgTableViewManager = 
				new VisframeUDTTypeTableViewerManager<>(
					this.getManager().getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
					CompositionFunctionGroupBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext()),
					TableContentSQLStringFactory.buildColumnValueEquityCondition(
							VisProjectCompositionFunctionGroupManager.CFG_TYPE_COLUMN.getName().getStringValue(), 
							this.cfgTypeSelectionTreeView.getSelectionModel().getSelectedItem().getValue().getSimpleName(), 
							true, true),//sqlFilterConditionString
					null,
					TableViewerMode.SELECTION,
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
					);
		
		Runnable operationAfterSelectionIsDone = ()->{
			if(cfgTableViewManager.getSelectedItem()==null) {
				this.templateNameTextField.setText("");
			}else {
				this.templateNameTextField.setText(cfgTableViewManager.getSelectedItem().getID().getName().getStringValue());
			}
		};
		
		cfgTableViewManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		
		/////////
		cfgTableViewManager.refresh();
		cfgTableViewManager.showWindow(this.getStage());
	}
	
	
	// Event Listener on Button[#clearTemplateSelectionButton].onAction
	@FXML
	public void clearTemplateSelectionButtonOnAction(ActionEvent event) {
		this.templateNameTextField.setText("");
	}


}
