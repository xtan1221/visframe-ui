package viewer.visframe.operation.sql.predefined.utils;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import rdb.table.data.DataTableColumnName;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class CumulativeColumnSymjaExpressionDelegateViewerController extends AbstractViewerController<CumulativeColumnSymjaExpressionDelegate>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/predefined/utils/CumulativeColumnSymjaExpressionDelegateViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		VfSymjaSinglePrimitiveOutputExpressionViewer vfSymjaSinglePrimitiveOutputExpressionViewer = 
				new VfSymjaSinglePrimitiveOutputExpressionViewer(this.getViewer().getValue().getSymjaExpression());
		this.symjaExpressionViewerHBox.getChildren().add(vfSymjaSinglePrimitiveOutputExpressionViewer.getController().getRootContainerPane());
		
		this.previouseRecordCumulativeColumnSymjaVariableNameTextField.setText(this.getViewer().getValue().getPreviouseRecordCumulativeColumnSymjaVariableName().getValue());
		
		int index = 1;
		for(DataTableColumnName colName:this.getViewer().getValue().getColumnSymjaVariableNameMap().keySet()) {
			TextField colNameTF = new TextField(colName.getStringValue());
			colNameTF.setEditable(false);

			TextField variableNameTF = new TextField(this.getViewer().getValue().getColumnSymjaVariableNameMap().get(colName).getValue());
			variableNameTF.setEditable(false);
			
			this.columnSymjaVariableNameMapGridPane.add(colNameTF, 0, index);
			this.columnSymjaVariableNameMapGridPane.add(variableNameTF, 1, index);
			
			index++;
		}
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CumulativeColumnSymjaExpressionDelegateViewer getViewer() {
		return (CumulativeColumnSymjaExpressionDelegateViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private HBox symjaExpressionViewerHBox;
	@FXML
	private TextField previouseRecordCumulativeColumnSymjaVariableNameTextField;
	@FXML
	private GridPane columnSymjaVariableNameMapGridPane;
}
