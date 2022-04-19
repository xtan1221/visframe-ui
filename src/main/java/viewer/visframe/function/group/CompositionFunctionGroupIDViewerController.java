package viewer.visframe.function.group;

import function.group.CompositionFunctionGroupID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class CompositionFunctionGroupIDViewerController extends AbstractViewerController<CompositionFunctionGroupID>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/group/CompositionFunctionGroupIDViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public CompositionFunctionGroupIDViewer getViewer() {
		return (CompositionFunctionGroupIDViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private Button viewDetailsButton;

	// Event Listener on Button[#viewDetailsButton].onAction
	private CompositionFunctionGroupViewerBase<?,?> cfgViewer;
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) {
		if(cfgViewer==null) {
			this.cfgViewer = 
					CompositionFunctionGroupViewerFactory.build(this.getViewer().getCompositionFunctionGroup(), this.getViewer().getHostVisframeContext());
		}
		
		this.cfgViewer.show(this.getRootContainerPane().getScene().getWindow());
	}
}
