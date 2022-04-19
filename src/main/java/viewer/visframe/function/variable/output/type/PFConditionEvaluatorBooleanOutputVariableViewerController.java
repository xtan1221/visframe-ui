package viewer.visframe.function.variable.output.type;

import function.variable.output.type.PFConditionEvaluatorBooleanOutputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import viewer.visframe.function.variable.output.OutputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class PFConditionEvaluatorBooleanOutputVariableViewerController extends OutputVariableViewerController<PFConditionEvaluatorBooleanOutputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/output/type/PFConditionEvaluatorBooleanOutputVariableViewer.fxml";
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
				
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public PFConditionEvaluatorBooleanOutputVariableViewer getViewer() {
		return (PFConditionEvaluatorBooleanOutputVariableViewer)this.viewer;
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
}
