package viewer.visframe.function.variable.output.type;

import function.variable.output.type.TemporaryOutputVariable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class TemporaryOutputVariableViewerController extends ValueTableColumnOutputVariableViewerController<TemporaryOutputVariable>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/variable/output/type/TemporaryOutputVariableViewer.fxml";
	//////////////////////////////////////////////

	@Override
	protected void setup() {
		this.aliasNameTextField.setText(this.getViewer().getValue().getAliasName().getStringValue());
		this.sqlDataTypeTextField.setText(this.getViewer().getValue().getSQLDataType().getSQLString());
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public TemporaryOutputVariableViewer getViewer() {
		return (TemporaryOutputVariableViewer)this.viewer;
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
	private TextField sqlDataTypeTextField;
}
