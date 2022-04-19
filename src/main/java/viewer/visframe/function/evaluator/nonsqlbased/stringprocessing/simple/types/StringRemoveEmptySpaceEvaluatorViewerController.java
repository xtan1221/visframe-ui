package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
public class StringRemoveEmptySpaceEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringRemoveEmptySpaceEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringRemoveEmptySpaceEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		InputVariableViewer<?,?> targetInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetInputVariable(), this.getViewer());
		this.targetInputVariableViewerVBox.getChildren().add(targetInputVariableViewer.getController().getRootContainerPane());
		
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
	public StringRemoveEmptySpaceEvaluatorViewer getViewer() {
		return (StringRemoveEmptySpaceEvaluatorViewer)this.viewer;
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
	private VBox targetInputVariableViewerVBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
