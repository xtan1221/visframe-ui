package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluator;
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
public class StringReplaceEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringReplaceEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringReplaceEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		InputVariableViewer<?,?> targetInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetInputVariable(), this.getViewer());
		this.targetInputVariableViewerVBox.getChildren().add(targetInputVariableViewer.getController().getRootContainerPane());
		
		InputVariableViewer<?,?> targetSubstringInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getTargetSubstringInputVariable(), this.getViewer());
		this.targetSubstringInputVariableViewerHBox.getChildren().add(targetSubstringInputVariableViewer.getController().getRootContainerPane());
		
		InputVariableViewer<?,?> replacingStringInputVariableViewer = 
				InputVariableViewerFactory.build(this.getViewer().getValue().getReplacingStringInputVariable(), this.getViewer());
		this.replacingStringInputVariableViewerHBox.getChildren().add(replacingStringInputVariableViewer.getController().getRootContainerPane());
		
		if(this.getViewer().getValue().getToReplaceAllInputVariable()!=null) {
			InputVariableViewer<?,?> toReplaceAllInputVariableViewer = 
					InputVariableViewerFactory.build(this.getViewer().getValue().getToReplaceAllInputVariable(), this.getViewer());
			this.toReplaceAllInputVariableViewerHBox.getChildren().add(toReplaceAllInputVariableViewer.getController().getRootContainerPane());
		}
		//
		this.toReplaceAllByDefaultCheckBox.setSelected(this.getViewer().getValue().isToReplaceAllByDefault());
		
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
	public StringReplaceEvaluatorViewer getViewer() {
		return (StringReplaceEvaluatorViewer)this.viewer;
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
	private HBox targetSubstringInputVariableViewerHBox;
	@FXML
	private HBox replacingStringInputVariableViewerHBox;
	@FXML
	private HBox toReplaceAllInputVariableViewerHBox;
	@FXML
	private CheckBox toReplaceAllByDefaultCheckBox;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;

}
