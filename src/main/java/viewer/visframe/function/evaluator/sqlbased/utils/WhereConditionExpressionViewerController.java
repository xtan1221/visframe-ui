package viewer.visframe.function.evaluator.sqlbased.utils;

import basic.SimpleName;
import function.evaluator.sqlbased.utils.WhereConditionExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import viewer.AbstractViewerController;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class WhereConditionExpressionViewerController extends AbstractViewerController<WhereConditionExpression>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/sqlbased/utils/WhereConditionExpressionViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.sqlStringTextField.setText(this.getViewer().getValue().getAliasedSqlString());
		
		//
		int i = 0;
		for(SimpleName name:this.getViewer().getValue().getInputVariableAliasNameMap().keySet()) {
			TextField nameTextField = new TextField();
			nameTextField.setText(name.getStringValue());
			nameTextField.setEditable(false);
			
			Button viewDetailButton = new Button("view detail");
			viewDetailButton.setOnMouseClicked(e->{
				InputVariableViewer<?,?> ivv = 
						InputVariableViewerFactory.build(
								this.getViewer().getValue().getInputVariableAliasNameMap().get(name), 
								this.getViewer().getHostSimpleSQLQueryEvaluatorViewer());
				
				ivv.show(this.getRootContainerPane().getScene().getWindow());
			});
			
			this.inputVariableGridPane.add(nameTextField, 0, i+1);
			this.inputVariableGridPane.add(viewDetailButton, 1, i+1);
			i++;
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
	public WhereConditionExpressionViewer getViewer() {
		return (WhereConditionExpressionViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField sqlStringTextField;
	@FXML
	private GridPane inputVariableGridPane;
}
