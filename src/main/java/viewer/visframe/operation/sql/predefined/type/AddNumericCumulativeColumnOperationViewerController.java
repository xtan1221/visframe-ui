package viewer.visframe.operation.sql.predefined.type;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import operation.sql.predefined.utils.SqlSortOrderType;
import rdb.table.data.DataTableColumnName;
import viewer.visframe.metadata.MetadataIDViewer;
import viewer.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationViewerController;
import viewer.visframe.operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegateViewer;

/**
 * 
 * @author tanxu
 *
 */
public class AddNumericCumulativeColumnOperationViewerController extends SingleInputRecordDataPredefinedSQLOperationViewerController<AddNumericCumulativeColumnOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/predefined/type/AddNumericCumulativeColumnOperationViewer.fxml";
	
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
		
		////////
		int index = 1;
		for(DataTableColumnName colName:this.getViewer().getValue().getOrderByColumnNameSet().getSet()) {
			TextField tf = new TextField(colName.getStringValue());
			tf.setEditable(false);
			this.orderByColumnNameAndSortOrderTypeGridPane.add(tf, 0, index);
			index++;
		}
		
		index = 1;
		for(SqlSortOrderType type:this.getViewer().getValue().getOrderByColumnSortTypeList().getList()) {
			TextField tf = new TextField(type.getSqlSymbol());
			tf.setEditable(false);
			this.orderByColumnNameAndSortOrderTypeGridPane.add(tf, 1, index);
			index++;
		}
		
		/////////////
		this.getViewer().getValue().getOtherKeptNonPKColumnNameSet().getSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.otherKeptNonPKColumnNameSetVBox.getChildren().add(tf);
		});
		
		
		//////////
		CumulativeColumnSymjaExpressionDelegateViewer cumulativeColumnSymjaExpressionViewer = 
				new CumulativeColumnSymjaExpressionDelegateViewer(this.getViewer().getValue().getCumulativeNumericColumnSymjaExpression(), this.getViewer().getHostVisframeContext());
		this.cumulativeColumnSymjaExpressionViewerHBox.getChildren().add(cumulativeColumnSymjaExpressionViewer.getController().getRootContainerPane());
		
		////////
		this.cumulativeColumnNameInOutputDataTableTextField.setText(this.getViewer().getValue().getCumulativeNumericColumnNameInOutputDataTable().getStringValue());
		
		//////
		this.initialValueTextField.setText(Double.toString(this.getViewer().getValue().getInitialValue()));
	}
	

	@Override
	public AddNumericCumulativeColumnOperationViewer getViewer() {
		return (AddNumericCumulativeColumnOperationViewer)this.viewer;
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
	private GridPane orderByColumnNameAndSortOrderTypeGridPane;
	@FXML
	private VBox otherKeptNonPKColumnNameSetVBox;
	@FXML
	private HBox cumulativeColumnSymjaExpressionViewerHBox;
	@FXML
	private TextField cumulativeColumnNameInOutputDataTableTextField;
	@FXML
	private TextField initialValueTextField;

}
