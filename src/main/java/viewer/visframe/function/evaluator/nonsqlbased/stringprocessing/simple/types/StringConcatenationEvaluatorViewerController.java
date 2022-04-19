package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringConcatenationEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewerController;
import viewer.visframe.function.variable.input.InputVariableViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;

/**
 * 
 * @author tanxu
 *
 */
public class StringConcatenationEvaluatorViewerController extends SimpleStringProcessingEvaluatorViewerController<StringConcatenationEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/nonsqlbased/stringprocessing/simple/types/StringConcatenationEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.getViewer().getValue().getConcatenatedInputVariableList().forEach(iv->{
			InputVariableViewer<?,?> ivv = 
					InputVariableViewerFactory.build(iv, this.getViewer());
			
			this.concatenatedInputVariableViewerListVBox.getChildren().add(ivv.getController().getRootContainerPane());
		});
		
		if(this.getViewer().getValue().getConcatenatingStringInputVariable()!=null) {
			InputVariableViewer<?,?> ivv = 
					InputVariableViewerFactory.build(this.getViewer().getValue().getConcatenatingStringInputVariable(), this.getViewer());
			
			this.concatenatingStringInputVariableViewerHBox.getChildren().add(ivv.getController().getRootContainerPane());
		}
		
		if(this.getViewer().getValue().getDefaultConcatenatingString()!=null) {
			this.defaultConcatenatingStringViewerTextField.setText(this.getViewer().getValue().getDefaultConcatenatingString());
		}
		
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public StringConcatenationEvaluatorViewer getViewer() {
		return (StringConcatenationEvaluatorViewer)this.viewer;
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
	private VBox concatenatedInputVariableViewerListVBox;
	@FXML
	private HBox concatenatingStringInputVariableViewerHBox;
	@FXML
	private TextField defaultConcatenatingStringViewerTextField;
	@FXML
	private HBox outputVariableViewerHBox;
	@FXML
	private TextArea notesTextArea;

}
