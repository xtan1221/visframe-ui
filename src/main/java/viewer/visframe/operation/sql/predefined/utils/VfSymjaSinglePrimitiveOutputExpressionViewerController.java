package viewer.visframe.operation.sql.predefined.utils;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import symja.VfSymjaSinglePrimitiveOutputExpression;
import symja.VfSymjaVariableName;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class VfSymjaSinglePrimitiveOutputExpressionViewerController extends AbstractViewerController<VfSymjaSinglePrimitiveOutputExpression>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/sql/predefined/utils/VfSymjaSinglePrimitiveOutputExpressionViewer.fxml";
	
	///////////////////////////
	@Override
	protected void setup() {
		this.outputExpressionSqlDataTypeTextField.setText(this.getViewer().getValue().getSqlDataType().getSQLString());
		
		this.expressionStringTextField.setText(this.getViewer().getValue().getExpressionString().getValueString());
		
		int index = 1;
		for(VfSymjaVariableName variableName:this.getViewer().getValue().getVariableNameSQLDataTypeMap().keySet()) {
			TextField variableNameTF = new TextField(variableName.getValue());
			variableNameTF.setEditable(false);
			
			TextField sqlDataTypeTF = new TextField(this.getViewer().getValue().getVariableNameSQLDataTypeMap().get(variableName).getSQLString());
			sqlDataTypeTF.setEditable(false);
			
			this.variableNameSQLDataTypeMapGridPane.add(variableNameTF, 0, index);
			this.variableNameSQLDataTypeMapGridPane.add(sqlDataTypeTF, 1, index);
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
	public VfSymjaSinglePrimitiveOutputExpressionViewer getViewer() {
		return (VfSymjaSinglePrimitiveOutputExpressionViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}

	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField outputExpressionSqlDataTypeTextField;
	@FXML
	private TextField expressionStringTextField;
	@FXML
	private GridPane variableNameSQLDataTypeMapGridPane;
}
