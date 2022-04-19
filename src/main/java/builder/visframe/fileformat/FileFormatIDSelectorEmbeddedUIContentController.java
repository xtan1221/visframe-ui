package builder.visframe.fileformat;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.DataType;
import sql.derby.TableContentSQLStringFactory;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class FileFormatIDSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<FileFormatID>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/fileformat/FileFormatIDSelectorEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.dataTypeTextField.textProperty().addListener((o,oldValue,newValue)->{
//			if(this.dataTypeTextField.getText().isEmpty()) {//
//				//set to default empty, do nothing
//			}else {//has a valid value
//				try{
//					FileFormatID type = this.build();
//					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update!");
//				}
//			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);//no need to popup alert if selection is cleared
		});
		
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public FileFormatIDSelector getOwnerNodeBuilder() {
		return (FileFormatIDSelector) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public FileFormatID build() {
		return new FileFormatID(
				new SimpleName(this.fileFormatNameTextField.getText()),
				DataType.valueOf(this.dataTypeTextField.getText()));
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.dataTypeTextField.setText("");
		this.fileFormatNameTextField.setText("");
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(FileFormatID value) {
		this.dataTypeTextField.setText(value.getType().toString());
		this.fileFormatNameTextField.setText(value.getName().getStringValue());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	
	/////////////////////////////////////////////////////////
	/**
	 * builder method; save memory source without holding it as a field;
	 * @return the fileFormatManagementTableViewerManager
	 * @throws SQLException
	 */
	protected VisframeUDTTypeTableViewerManager<FileFormat, FileFormatID> getFileFormatManagementTableViewerManager() throws SQLException {
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.TYPE_COLUMN.getName(), this.dataTypeTextField);
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.NAME_COLUMN.getName(), this.fileFormatNameTextField);
		
		Runnable operationAfterSelectionIsDone = ()->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		};
		
		VisframeUDTTypeTableViewerManager<FileFormat,FileFormatID> fileFormatManagementTableViewerManager = 
				new VisframeUDTTypeTableViewerManager<>(
					this.getOwnerNodeBuilder().getVisProjectFileFormatManager(),
					FileFormatBuilderFactory.singleton(),
					TableContentSQLStringFactory.buildColumnValueEquityCondition(
							FileFormatID.TYPE_COLUMN.getName().getStringValue(), this.getOwnerNodeBuilder().getDataType().toString(), true, true),
					this.getOwnerNodeBuilder().getFilteringCondition(),
					TableViewerMode.SELECTION,
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
//					operationAfterSelectionIsDone
				);
		fileFormatManagementTableViewerManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		return fileFormatManagementTableViewerManager;
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
	private TextField fileFormatNameTextField;
	@FXML
	private Button chooseFileFormatButton;

	// Event Listener on Button[#chooseFileFormatButton].onAction
	@FXML
	public void chooseFileFormatButtonOnAction(ActionEvent event) {
		try {
			this.getFileFormatManagementTableViewerManager().showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show2(this.getClass().getSimpleName().concat(".cancelButtonOnAction"), e);
		}
	}
}
