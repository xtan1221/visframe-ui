package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class ConstantValuedInputVariableViewerController extends NonRecordwiseInputVariableViewerController<ConstantValuedInputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/input/nonrecordwise/type/ConstantValuedInputVariableViewer.fxml";
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		this.dataTypeTextField.setText(this.getViewer().getValue().getSQLDataType().getSQLString());
		this.valueStringTextField.setText(this.getViewer().getValue().getValueString());
		
		//TODO notes
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public ConstantValuedInputVariableViewer getViewer() {
		return (ConstantValuedInputVariableViewer)this.viewer;
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
	private TextField dataTypeTextField;
	@FXML
	private TextField valueStringTextField;
	@FXML
	private Button notesButton;
}
