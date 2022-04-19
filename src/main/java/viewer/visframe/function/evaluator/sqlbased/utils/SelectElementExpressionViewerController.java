package viewer.visframe.function.evaluator.sqlbased.utils;

import basic.SimpleName;
import function.evaluator.sqlbased.utils.SelectElementExpression;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import viewer.AbstractViewerController;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;
import viewer.visframe.function.variable.output.OutputVariableViewer;
import viewer.visframe.function.variable.output.OutputVariableViewerFactory;

/**
 * 
 * @author tanxu
 * 
 */
public class SelectElementExpressionViewerController extends AbstractViewerController<SelectElementExpression>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/group/CompositionFunctionGroupIDViewer.fxml";
	
	
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
		
		//
		OutputVariableViewer<?,?> outputVariableViewer = 
				OutputVariableViewerFactory.build(
						this.getViewer().getValue().getOutputVariable(), 
						this.getViewer().getHostSimpleSQLQueryEvaluatorViewer());
		this.outputVariableViewerHBox.getChildren().add(outputVariableViewer.getController().getRootContainerPane());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public SelectElementExpressionViewer getViewer() {
		return (SelectElementExpressionViewer)this.viewer;
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
	@FXML
	private HBox outputVariableViewerHBox;

}
