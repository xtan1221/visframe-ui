package dependencygraph.solutionset;

import dependencygraph.solutionset.DAGNodeManager.Status;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.FXUtils;

public class DAGNodeController<V,E>{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/solutionset/DAGNode.fxml";
	
	/////////////////////////////
	private DAGNodeManager<V,E> manager;
	
	void setManager(DAGNodeManager<V,E> manager) {
		this.manager = manager;
		this.dagNodeInforLabel.setText(this.manager.getNodeInforStringFunction().apply(this.manager.getNode()));
	}
	
	/**
	 * @return the manager
	 */
	public DAGNodeManager<V, E> getManager() {
		return manager;
	}

	
	public Pane getRootNodeContainer() {
		return this.dagNodeRootStackPane;
	}
	
	/**
	 * set the visuali effect on the UI of this node based on the current status
	 */
	void updateUIVisualEffectBasedOnCurrentStatus() {
		if(this.getManager().getNodeStatus().equals(Status.SELECTABLE)) {
			this.selectionButton.setVisible(true);
			this.selectionButton.setText("SELECT");
			this.backgroundCircle.setFill(Color.GREEN);
		}else if(this.getManager().getNodeStatus().equals(Status.REPRESENTED)) {
			this.selectionButton.setVisible(false);
			this.backgroundCircle.setFill(Color.BLUE);
		}else if(this.getManager().getNodeStatus().equals(Status.SELECTED)) {
			this.selectionButton.setVisible(true);
			this.selectionButton.setText("DE-SELECT");
			this.backgroundCircle.setFill(Color.ORANGE);
		}else if(this.getManager().getNodeStatus().equals(Status.UNSELECTABLE)) {
			this.selectionButton.setVisible(false);
			this.backgroundCircle.setFill(Color.ALICEBLUE);
		}
	}
	
	
	
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.selectionButton, !modifiable);
	}
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private StackPane dagNodeRootStackPane;
	@FXML
	private Circle backgroundCircle;
	@FXML
	private Label dagNodeInforLabel;
	@FXML
	private Button viewNodeDetailsButton;
	@FXML
	private Button selectionButton;

	// Event Listener on Button[#viewNodeDetailsButton].onAction
	@FXML
	public void viewNodeDetailsButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
	@FXML
	public void selectionButtonOnAction(ActionEvent event) {
		if(this.getManager().getNodeStatus().equals(Status.SELECTABLE)) {//
			this.getManager().getHostDAGSolutionSetSelectorManager().getDAGSolutionSetSelector().selectNode(this.getManager().getNode());
			this.getManager().getHostDAGSolutionSetSelectorManager().updateAllNodeStatus();
			
			//
			this.getManager().getHostDAGSolutionSetSelectorManager().getController().enableFinishAndCancelButton();
			
		}else if(this.getManager().getNodeStatus().equals(Status.SELECTED)){
			//
			this.getManager().getHostDAGSolutionSetSelectorManager().getDAGSolutionSetSelector().deselectNode(this.getManager().getNode());
			this.getManager().getHostDAGSolutionSetSelectorManager().updateAllNodeStatus();
			
			//
			this.getManager().getHostDAGSolutionSetSelectorManager().getController().enableFinishAndCancelButton();
			
		}
	}
}
