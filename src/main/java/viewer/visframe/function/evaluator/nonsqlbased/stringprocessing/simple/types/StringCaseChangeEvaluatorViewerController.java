package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringCaseChangeEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewerController;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewer;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class StringCaseChangeEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringCaseChangeEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringCaseChangeEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		//
		InputVariableViewer<?,?> targetInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetInputVariable(), this.getViewer());
		this.targetInputVariableViewerHBox.getChildren().add(targetInputVariableViewer.getController().getRootContainerPane());
		//
		InputVariableViewer<?,?> caseChangeIndicatorInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getCaseChangeIndicatorInputVariable(), this.getViewer());
		this.caseChangeIndicatorInputVariableViewerHBox.getChildren().add(caseChangeIndicatorInputVariableViewer.getController().getRootContainerPane());
		//
		this.toUpperCaseWhenTrueCheckBox.setSelected(this.getViewer().getValue().isToUpperCaseWhenTrue());
		//
		ValueTableColumnOutputVariableViewer<?,?> outputVariableViewer = 
				ValueTableColumnOutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable(), this.getViewer());
		this.outputVariableViewerHBox.getChildren().add(outputVariableViewer.getController().getRootContainerPane());
		//
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public StringCaseChangeEvaluatorViewer getViewer() {
		return (StringCaseChangeEvaluatorViewer)this.viewer;
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
	private HBox targetInputVariableViewerHBox;
	@FXML
	private HBox caseChangeIndicatorInputVariableViewerHBox;
	@FXML
	private CheckBox toUpperCaseWhenTrueCheckBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;

}
