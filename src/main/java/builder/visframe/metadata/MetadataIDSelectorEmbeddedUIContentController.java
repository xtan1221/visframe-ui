package builder.visframe.metadata;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.DataType;
import metadata.Metadata;
import metadata.MetadataID;
import metadata.MetadataName;
import sql.LogicOperator;
import sql.derby.TableContentSQLStringFactory;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class MetadataIDSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<MetadataID>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/metadata/MetadataIDSelectorEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
		//note that since dataTypeTextField's text property change event is listened, when set the value of both dataTypeTextField and metadataNameTextField
		//always set the value of metadataNameTextField first;
		this.dataTypeTextField.textProperty().addListener((o,oldValue,newValue)->{

			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);//no need to pop up window if cleared
		});
		
		//initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public MetadataIDSelector getOwnerNodeBuilder() {
		return (MetadataIDSelector) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public MetadataID build() {
		if(this.metadataNameTextField.getText().isEmpty()) {
			throw new VisframeException("No MetadataID is selected!");
		}
		
		return new MetadataID(
				new MetadataName(this.metadataNameTextField.getText()),
				DataType.valueOf(this.dataTypeTextField.getText()));
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		
		this.metadataNameTextField.setText("");
		this.dataTypeTextField.setText("");
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(MetadataID value) {
		
		this.metadataNameTextField.setText(value.getName().getStringValue());
		this.dataTypeTextField.setText(value.getDataType().toString());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	/////////////////////////////////////////////////////////
	/**
	 * builder method; save memory source without holding it as a field;
	 * @return the fileFormatManagementTableViewerManager
	 * @throws SQLException
	 */
	protected VisframeUDTTypeTableViewerManager<Metadata, MetadataID> getMetadataManagementTableViewerManager() throws SQLException {
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(MetadataID.TYPE_COLUMN.getName(), this.dataTypeTextField);
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(MetadataID.NAME_COLUMN.getName(), this.metadataNameTextField);
		
		Runnable operationAfterSelectionIsDone = ()->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		};
		
		
		Set<String> allowedDataTypeNameStringSet = new HashSet<>();
		this.getOwnerNodeBuilder().getAllowedDataTypeSet().forEach(e->{
			allowedDataTypeNameStringSet.add(e.toString());
		});
		
		VisframeUDTTypeTableViewerManager<Metadata, MetadataID> metadataManagementTableViewerManager = 
				new VisframeUDTTypeTableViewerManager<>(
					this.getOwnerNodeBuilder().getVisProjectMetadataManager(),
					null,//FileFormatBuilderFactory.singleton(),
					TableContentSQLStringFactory.buildColumnValueEquityCondition(
							MetadataID.TYPE_COLUMN.getName().getStringValue(), allowedDataTypeNameStringSet, true, true, LogicOperator.OR),
					this.getOwnerNodeBuilder().getFilteringCondition(),
					TableViewerMode.SELECTION,
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
//					operationAfterSelectionIsDone
				);
		
		metadataManagementTableViewerManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		
		return metadataManagementTableViewerManager;
	}
	
	//////////////////////////
	
	@FXML
	public void initialize() {
		//TODO
		
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private TextField dataTypeTextField;
	@FXML
	private TextField metadataNameTextField;
	@FXML
	private Button chooseMetadataButton;

	// Event Listener on Button[#chooseMetadataButton].onAction
	@FXML
	public void chooseFileFormatButtonOnAction(ActionEvent event) {
		try {
			this.getMetadataManagementTableViewerManager().showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show2(this.getClass().getSimpleName().concat(".chooseFileFormatButtonOnAction"), e);
		}
	}
}
