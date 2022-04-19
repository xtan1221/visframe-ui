package viewer.visframe.operation.sql.predefined.type;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.sql.predefined.type.GroupAndBinCountOperation;
import viewer.visframe.metadata.MetadataIDViewer;
import viewer.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class GroupAndBinCountOperationViewerController extends SingleInputRecordDataPredefinedSQLOperationViewerController<GroupAndBinCountOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/predefined/type/GroupAndBinCountOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {
		this.operationTypeTextField.setText(this.getViewer().getValue().getOperationTypeName().getStringValue());
		this.operationInstanceNameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
	
		MetadataIDViewer inputRecordDataMetadataIDViewer = 
				new MetadataIDViewer(this.getViewer().getValue().getInputRecordDataMetadataID(), this.getViewer().getHostVisframeContext());
		this.inputRecordDataMetadataIDViewerHBox.getChildren().add(inputRecordDataMetadataIDViewer.getController().getRootContainerPane());
		
		////
		this.getViewer().getValue().getGroupByColumnNameSet().getSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.groupByColumnNameSetVBox.getChildren().add(tf);
		});
		
		//////
		this.numericColumnNameToSortAndBinTextField.setText(this.getViewer().getValue().getNumericColumnNameToSortAndBin().getStringValue());
		
		this.numericColumnSortTypeTextField.setText(this.getViewer().getValue().getNumericColumnSortType().getSqlSymbol());
		
		this.binSizeTextField.setText(Double.toString(this.getViewer().getValue().getBinSize()));
		
		if(this.getViewer().getValue().getBinMax()!=null)
			this.binMaxTextField.setText(Double.toString(this.getViewer().getValue().getBinMax()));
		else
			this.binMaxTextField.setDisable(true);
			
		if(this.getViewer().getValue().getBinMin()!=null)
			this.binMinTextField.setText(Double.toString(this.getViewer().getValue().getBinMin()));
		else
			this.binMinTextField.setDisable(true);
	}
	

	@Override
	public GroupAndBinCountOperationViewer getViewer() {
		return (GroupAndBinCountOperationViewer)this.viewer;
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
	private HBox inputRecordDataMetadataIDViewerHBox;
	@FXML
	private VBox groupByColumnNameSetVBox;
	@FXML
	private TextField numericColumnNameToSortAndBinTextField;
	@FXML
	private TextField numericColumnSortTypeTextField;
	@FXML
	private TextField binSizeTextField;
	@FXML
	private TextField binMinTextField;
	@FXML
	private TextField binMaxTextField;
}
