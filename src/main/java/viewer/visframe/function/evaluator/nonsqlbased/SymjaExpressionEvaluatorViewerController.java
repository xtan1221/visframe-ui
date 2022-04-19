package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import symja.VfSymjaVariableName;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class SymjaExpressionEvaluatorViewerController extends NonSQLQueryBasedEvaluatorViewerController<SymjaExpressionEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/SymjaExpressionEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////
	

	@Override
	protected void setup() {
		//
		this.symjaExpressionStringTextField.setText(this.getViewer().getValue().getSymjaExpression().getExpressionString().getValueString());
		this.dataTypeTextField.setText(this.getViewer().getValue().getSymjaExpression().getSqlDataType().getSQLString());
		
		int index = 1;
		for(VfSymjaVariableName name:this.getViewer().getValue().getVfSymjaVariableNameInputVariableMap().keySet()) {
			TextField symjaVariableNameTextField = new TextField();
			symjaVariableNameTextField.setText(name.getValue());
			
			InputVariableViewer<?,?> inputVariableViewer = 
					InputVariableViewerFactory.build(
							this.getViewer().getValue().getVfSymjaVariableNameInputVariableMap().get(name), this.getViewer());
		
					
			this.symjaVariableNameInputVariableMapGridPane.add(symjaVariableNameTextField, 0, index);//col, row
			this.symjaVariableNameInputVariableMapGridPane.add(inputVariableViewer.getController().getRootContainerPane(), 1, index);//col, row
			index++;
		}
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
	}

	/**
	 * @return the viewer
	 */
	@Override
	public SymjaExpressionEvaluatorViewer getViewer() {
		return (SymjaExpressionEvaluatorViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return rootContainerVBox;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField symjaExpressionStringTextField;
	@FXML
	private TextField dataTypeTextField;
	@FXML
	private GridPane symjaVariableNameInputVariableMapGridPane;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
