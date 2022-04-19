package dependencygraph.vccl.nodeselection.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.FXUtils;

public class DAGNodeCopyController<V,E>{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/nodeselection/utils/DAGNodeCopy.fxml";
	
	/////////////////////////////
	private DAGNodeCopyManager<V,E> manager;

	void setManager(DAGNodeCopyManager<V,E> manager) {
		this.manager = manager;
		
		this.copyButton.setText(Integer.toString(this.getManager().getCopyIndex()));
		
		//Initialize the visual effect
		this.updateUIVisualEffectBasedOnCurrentStatus();
	}
	
	/**
	 * set the node copy UI status based on current status;
	 */
	void updateUIVisualEffectBasedOnCurrentStatus() {
		if(this.getManager().isSelected()) {
			this.copyButton.setStyle("-fx-background-color:green");
		}else{
			this.copyButton.setStyle("-fx-background-color:red");
		}
	}
	
	/**
	 * @return the manager
	 */
	public DAGNodeCopyManager<V, E> getManager() {
		return manager;
	}
	
	
	/**
	 * the layout coordinate of this node must be the upper-left corner;
	 * 
	 * @return
	 */
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * 
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.copyButton, !modifiable);
	}
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
	}
	
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private Button copyButton;
	
	// Event Listener on Button[#copyButton].onAction
	@FXML
	public void copyButtonOnAction(ActionEvent event) {
		
		if(this.getManager().isSelected()) {//
			//1. set this copy node to SELECTED
			this.getManager().setSelected(false);
			
		}else{
			this.getManager().setSelected(true);
		}
		
		//trigger the CopySelectionChangedAction if not null
		if(this.getManager().getOwnerDAGNodeManager().getHostCopyNodeSelectionManager().getCopySelectionChangedAction()!=null)
			this.getManager().getOwnerDAGNodeManager().getHostCopyNodeSelectionManager().getCopySelectionChangedAction().run();
		
		//update selection visual effect
		this.updateUIVisualEffectBasedOnCurrentStatus();
			
	}
}
