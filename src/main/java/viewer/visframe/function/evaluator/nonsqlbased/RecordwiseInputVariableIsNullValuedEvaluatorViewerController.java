package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewer;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewerFactory;
import viewer.visframe.function.variable.output.OutputVariableViewer;
import viewer.visframe.function.variable.output.OutputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class RecordwiseInputVariableIsNullValuedEvaluatorViewerController extends NonSQLQueryBasedEvaluatorViewerController<RecordwiseInputVariableIsNullValuedEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/RecordwiseInputVariableIsNullValuedEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////
	

	@Override
	protected void setup() {
		RecordwiseInputVariableViewer<?,?> recordwiseInputVariableViewer = 
				RecordwiseInputVariableViewerFactory.build(this.getViewer().getValue().getRecordwiseInputVariable(), this.getViewer());
		this.recordwiseInputVariableViewerHBox.getChildren().add(recordwiseInputVariableViewer.getController().getRootContainerPane());
		
		OutputVariableViewer<?,?> outputVariableViewer = 
				OutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable(), this.getViewer());
		this.outputVariableViewerHBox.getChildren().add(outputVariableViewer.getController().getRootContainerPane());
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
	}

	/**
	 * @return the viewer
	 */
	@Override
	public RecordwiseInputVariableIsNullValuedEvaluatorViewer getViewer() {
		return (RecordwiseInputVariableIsNullValuedEvaluatorViewer)this.viewer;
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
	private HBox recordwiseInputVariableViewerHBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
