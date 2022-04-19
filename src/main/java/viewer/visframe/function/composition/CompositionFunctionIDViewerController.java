package viewer.visframe.function.composition;

import function.composition.CompositionFunctionID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.AlertUtils;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class CompositionFunctionIDViewerController extends AbstractViewerController<CompositionFunctionID>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/composition/CompositionFunctionIDViewer.fxml";
	//////////////////////////
	
	@Override
	protected void setup() {
		this.cfgNameTextField.setText(this.getViewer().getValue().getHostCompositionFunctionGroupID().getName().getStringValue());
		
		this.cfIndexTextField.setText(Integer.toString(this.getViewer().getValue().getIndexID()));
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CompositionFunctionIDViewer getViewer() {
		return (CompositionFunctionIDViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField cfgNameTextField;
	@FXML
	private TextField cfIndexTextField;
	@FXML
	private Button viewCFDetailsButton;

	// Event Listener on Button[#viewOwnerCFDetailsButton].onAction
	@FXML
	public void viewCFDetailsButtonOnAction(ActionEvent event) {
		AlertUtils.popAlert("Warning", "not implemented yet!");
	}
}
