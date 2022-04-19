package viewer.visframe.operation.sql.generic;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.sql.generic.GenericSQLOperation;
import viewer.visframe.operation.sql.SQLOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class GenericSQLOperationViewerController extends SQLOperationBaseViewerController<GenericSQLOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/generic/GenericSQLOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {
		this.operationTypeTextField.setText(this.getViewer().getValue().getOperationTypeName().getStringValue());
		this.operationInstanceNameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		//
		this.outputRecordDataNameTextField.setText(this.getViewer().getValue().getOutputRecordDataID().getName().getStringValue());
		
		//
		GenericSQLQueryViewer genericSQLQueryViewer = new GenericSQLQueryViewer(this.getViewer().getValue().getGenericSQLQuery(), this.getViewer().getHostVisframeContext());
		this.genericSQLQueryViewerHBox.getChildren().add(genericSQLQueryViewer.getController().getRootContainerPane());
	}
	

	@Override
	public GenericSQLOperationViewer getViewer() {
		return (GenericSQLOperationViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return rootContainerVBox;
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField operationTypeTextField;
	@FXML
	private TextField operationInstanceNameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private TextField outputRecordDataNameTextField;
	@FXML
	private HBox genericSQLQueryViewerHBox;
}
