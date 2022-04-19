package viewer.visframe.operation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import operation.OperationID;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class OperationIDViewerController extends AbstractViewerController<OperationID>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/OperationIDViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.nameTextField.setText(this.getViewer().getValue().getInstanceName().getStringValue());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public OperationIDViewer getViewer() {
		return (OperationIDViewer)this.viewer;
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
	private OperationViewerBase<?,?> operationViewer;
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) {
		if(operationViewer==null) {
			this.operationViewer = 
					OperationViewerFactory.build(this.getViewer().getOperation(), this.getViewer().getHostVisframeContext());
		}
		
		this.operationViewer.show(this.getRootContainerPane().getScene().getWindow());
	}
}
