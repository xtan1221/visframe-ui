package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluator;
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
public class StringSplitToTwoBySplitterStringEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringSplitToTwoBySplitterStringEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringSplitToTwoBySplitterStringEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		InputVariableViewer<?,?> targetInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetInputVariable(), this.getViewer());
		this.targetInputVariableViewerVBox.getChildren().add(targetInputVariableViewer.getController().getRootContainerPane());

		InputVariableViewer<?,?> splitterStringInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getSplitterStringInputVariable(), this.getViewer());
		this.splitterStringInputVariableViewerHBox.getChildren().add(splitterStringInputVariableViewer.getController().getRootContainerPane());

		//
		ValueTableColumnOutputVariableViewer<?,?> outputVariable1Viewer = 
				ValueTableColumnOutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable1(), this.getViewer());
		this.outputVariable1ViewerHBox.getChildren().add(outputVariable1Viewer.getController().getRootContainerPane());
		
		//
		ValueTableColumnOutputVariableViewer<?,?> outputVariable2Viewer = 
				ValueTableColumnOutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable2(), this.getViewer());
		this.outputVariable2ViewerHBox.getChildren().add(outputVariable2Viewer.getController().getRootContainerPane());
		
		//
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public StringSplitToTwoBySplitterStringEvaluatorViewer getViewer() {
		return (StringSplitToTwoBySplitterStringEvaluatorViewer)this.viewer;
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
	private HBox splitterStringInputVariableViewerHBox;
	@FXML
	private HBox outputVariable1ViewerHBox;
	@FXML
	private HBox outputVariable2ViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
