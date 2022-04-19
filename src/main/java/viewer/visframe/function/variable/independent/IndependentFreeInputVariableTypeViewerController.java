package viewer.visframe.function.variable.independent;

import function.variable.independent.IndependentFreeInputVariableType;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import viewer.AbstractViewerController;
import viewer.visframe.function.composition.CompositionFunctionIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class IndependentFreeInputVariableTypeViewerController extends AbstractViewerController<IndependentFreeInputVariableType>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/independent/IndependentFreeInputVariableTypeViewer.fxml";
	
	///////////////////////////////
	@Override
	protected void setup() {
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		this.dataTypeTextField.setText(this.getViewer().getValue().getSQLDataType().getSQLString());
		
		//
		CompositionFunctionIDViewer compositionFunctionIDViewer = 
				new CompositionFunctionIDViewer(this.getViewer().getValue().getOwnerCompositionFunctionID(), this.getViewer().getHostVisframeContext());
		this.ownerCFVBox.getChildren().add(compositionFunctionIDViewer.getController().getRootContainerPane());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public IndependentFreeInputVariableTypeViewer getViewer() {
		return (IndependentFreeInputVariableTypeViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private TextField dataTypeTextField;
	@FXML
	private VBox ownerCFVBox;

}
