package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.FreeInputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import viewer.visframe.function.variable.independent.IndependentFreeInputVariableTypeIDViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class FreeInputVariableViewerController extends NonRecordwiseInputVariableViewerController<FreeInputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/input/nonrecordwise/type/FreeInputVariableViewer.fxml";
	//////////////////////////////////////////////
	
	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());

		IndependentFreeInputVariableTypeIDViewer independentFreeInputVariableTypeIDViewer = 
				new IndependentFreeInputVariableTypeIDViewer(
						this.getViewer().getValue().getIndependentFreeInputVariableType().getID(), 
						this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext()); 
		this.independentFreeInputVariableTypeIDViewerHBox.getChildren().add(independentFreeInputVariableTypeIDViewer.getController().getRootContainerPane());
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public FreeInputVariableViewer getViewer() {
		return (FreeInputVariableViewer)this.viewer;
	}


	@Override
	public Parent getRootContainerPane() {
		return rootContainerGridPane;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private TextField aliasNameTextField;
	@FXML
	private Button notesButton;
	@FXML
	private HBox independentFreeInputVariableTypeIDViewerHBox;
}
