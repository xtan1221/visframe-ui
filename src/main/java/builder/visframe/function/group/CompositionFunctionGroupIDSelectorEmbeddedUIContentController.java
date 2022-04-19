package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import basic.SimpleName;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import exception.VisframeException;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.group.CompositionFunctionGroupName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CompositionFunctionGroupIDSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<CompositionFunctionGroupID>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/group/CompositionFunctionGroupIDSelectorEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
		//note that since dataTypeTextField's text property change event is listened, when set the value of both dataTypeTextField and metadataNameTextField
		//always set the value of metadataNameTextField first;
		this.cfgNameTextField.textProperty().addListener((o,oldValue,newValue)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);//no need to pop up window if cleared
		});
		
		//initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public CompositionFunctionGroupIDSelector getOwnerNodeBuilder() {
		return (CompositionFunctionGroupIDSelector) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public CompositionFunctionGroupID build() {
		if(this.cfgNameTextField.getText().isEmpty()) {
			throw new VisframeException("No CompositionFunctionGroupID is selected!");
		}
		
		return new CompositionFunctionGroupID(new CompositionFunctionGroupName(this.cfgNameTextField.getText()));
		
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		
		this.cfgNameTextField.setText("");
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(CompositionFunctionGroupID value) {
		
		this.cfgNameTextField.setText(value.getName().getStringValue());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	/////////////////////////////////////////////////////////
	/**
	 * builder method; save memory source without holding it as a field;
	 * @return the fileFormatManagementTableViewerManager
	 * @throws SQLException
	 */
	protected VisframeUDTTypeTableViewerManager<CompositionFunctionGroup, CompositionFunctionGroupID> getManagementTableViewerManager() throws SQLException {
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(CompositionFunctionGroupID.NAME_COLUMN.getName(), this.cfgNameTextField);
		
		Runnable operationAfterSelectionIsDone = ()->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		};
		
		VisframeUDTTypeTableViewerManager<CompositionFunctionGroup, CompositionFunctionGroupID> managementTableViewerManager = 
				new VisframeUDTTypeTableViewerManager<>(
					this.getOwnerNodeBuilder().getVisProjectCompositionFunctionGroupManager(),
					null,//nodeBuilderFactory 
					this.getOwnerNodeBuilder().getConditionSQLString(),//sqlFilterConditionString 
					this.getOwnerNodeBuilder().getFilteringCondition(),
					TableViewerMode.SELECTION,//viewOnly 
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
				);
		
		managementTableViewerManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		
		return managementTableViewerManager;
	}
	
	//////////////////////////
	
	@FXML
	public void initialize() {
		//TODO
		
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private TextField cfgNameTextField;
	@FXML
	private Button chooseButton;

	// Event Listener on Button[#chooseButton].onAction
	@FXML
	public void chooseFileFormatButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.getManagementTableViewerManager().showWindow(this.getStage());
	}

}
