package builder.visframe.context.scheme.applier.reproduceandinserter.operation.dos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class IntegratedDOSGraphNodeController{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/operation/dos/IntegratedDOSGraphNode.fxml";
	
	/////////////////////////////
	private IntegratedDOSGraphNodeManager manager;
	
	void setManager(IntegratedDOSGraphNodeManager manager) {
		this.manager = manager;
		this.dagNodeInforLabel.setText(this.manager.getHostOperationReproducingAndInsertionManager().getDagNodeInforStringFunction().apply(this.manager.getNode()));
	}
	
	/**
	 * @return the manager
	 */
	public IntegratedDOSGraphNodeManager getManager() {
		return manager;
	}

	
	public Pane getRootNodeContainer() {
		return this.dagNodeRootStackPane;
	}
	
	public Pane getCopyNodeContainerPane() {
		return this.copyNodesHBox;
	}
	
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		
//		this.dagNodeRootStackPane.setOnMouseClicked(e->{
//			System.out.println("width:".concat(Double.toString(this.dagNodeRootStackPane.getWidth())));
//			System.out.println("height:".concat(Double.toString(this.dagNodeRootStackPane.getHeight())));
//			System.out.println("layoutx:".concat(Double.toString(this.dagNodeRootStackPane.getLayoutX())));
//			System.out.println("layouty:".concat(Double.toString(this.dagNodeRootStackPane.getLayoutY())));
//			
//		});
	}
	
	@FXML
	private StackPane dagNodeRootStackPane;
	@FXML
	private Label dagNodeInforLabel;
	@FXML
	private Button viewNodeDetailsButton;
	@FXML
	private HBox copyNodesHBox;

	// Event Listener on Button[#viewNodeDetailsButton].onAction
	@FXML
	public void viewNodeDetailsButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
}
