package viewer.visframe.function.evaluator.nonsqlbased.rng;

import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;
import viewer.visframe.function.variable.output.OutputVariableViewer;
import viewer.visframe.function.variable.output.OutputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class SimpleRNGEvaluatorViewerController extends RNGEvaluatorViewerController<SimpleRNGEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/rng/SimpleRNGEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////
	
	
	@Override
	protected void setup() {
		InputVariableViewer<?,?> range1InputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getRange1InputVariable(), this.getViewer());
		this.range1InputVariableViewerHBox.getChildren().add(range1InputVariableViewer.getController().getRootContainerPane());
		
		InputVariableViewer<?,?> range2InputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getRange2InputVariable(), this.getViewer());
		this.range2InputVariableViewerHBox.getChildren().add(range2InputVariableViewer.getController().getRootContainerPane());
		
		OutputVariableViewer<?,?> outputVariableViewer = 
				OutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable(), this.getViewer());
		this.outputVariableViewerHBox.getChildren().add(outputVariableViewer.getController().getRootContainerPane());
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
	}

	/**
	 * @return the viewer
	 */
	@Override
	public SimpleRNGEvaluatorViewer getViewer() {
		return (SimpleRNGEvaluatorViewer)this.viewer;
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
	private HBox range1InputVariableViewerHBox;
	@FXML
	private HBox range2InputVariableViewerHBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
