package viewer.visframe.function.variable.input.recordwise.type;

import function.variable.input.recordwise.type.RecordAttributeInputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewerController;
import viewer.visframe.metadata.MetadataIDViewer;

/**
 * 
 * @author tanxu
 *
 */
public class RecordAttributeInputVariableViewerController extends RecordwiseInputVariableViewerController<RecordAttributeInputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/input/recordwise/type/RecordAttributeInputVariableViewer.fxml";
	//////////////////////////////////////////////
	
	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		
		MetadataIDViewer targetRecordMetadataIDViewer = 
				new MetadataIDViewer(
						this.getViewer().getValue().getTargetRecordDataMetadataID(),
						this.getViewer().getHostEvaluatorViewer().getHostComponentFunctionViewer().getHostCompositionFunctionViewer().getHostVisframeContext());
		this.targetRecordMetadataIDViewerHBox.getChildren().add(targetRecordMetadataIDViewer.getController().getRootContainerPane());
		
		//
		this.columnNameTextField.setText(this.getViewer().getValue().getColumn().getName().getStringValue());
		
		
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public RecordAttributeInputVariableViewer getViewer() {
		return (RecordAttributeInputVariableViewer)this.viewer;
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
	private HBox targetRecordMetadataIDViewerHBox;
	@FXML
	private TextField columnNameTextField;
}
