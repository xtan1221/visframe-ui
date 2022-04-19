package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringEndsWithEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewerController;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;
import viewer.visframe.function.variable.output.OutputVariableViewer;
import viewer.visframe.function.variable.output.OutputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class StringEndsWithEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringEndsWithEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringEndsWithEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		InputVariableViewer<?,?> targetInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetInputVariable(), this.getViewer());
		this.targetInputVariableViewerVBox.getChildren().add(targetInputVariableViewer.getController().getRootContainerPane());
		
		InputVariableViewer<?,?> substringInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getSubstringInputVariable(), this.getViewer());
		this.substringInputVariableViewerHBox.getChildren().add(substringInputVariableViewer.getController().getRootContainerPane());
		
		if(this.getViewer().getValue().getToIgnoreCaseInputVariable()!=null) {
			InputVariableViewer<?,?> toIgnoreCaseInputVariableViewer = 
					InputVariableViewerFactory.build(this.getViewer().getValue().getToIgnoreCaseInputVariable(), this.getViewer());
			this.toIgnoreCaseInputVariableViewerHBox.getChildren().add(toIgnoreCaseInputVariableViewer.getController().getRootContainerPane());
		}
		
		//
		this.toIgnoreCaseByDefaultCheckBox.setSelected(this.getViewer().getValue().isToIgnoreCaseByDefault());
		
		//
		OutputVariableViewer<?,?> outputVariableViewer = 
				OutputVariableViewerFactory.build(this.getViewer().getValue().getOutputVariable(), this.getViewer());
		this.outputVariableViewerHBox.getChildren().add(outputVariableViewer.getController().getRootContainerPane());
		//
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public StringEndsWithEvaluatorViewer getViewer() {
		return (StringEndsWithEvaluatorViewer)this.viewer;
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
	private HBox substringInputVariableViewerHBox;
	@FXML
	private HBox toIgnoreCaseInputVariableViewerHBox;
	@FXML
	private CheckBox toIgnoreCaseByDefaultCheckBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
