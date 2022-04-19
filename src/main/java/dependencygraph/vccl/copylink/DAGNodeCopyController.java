package dependencygraph.vccl.copylink;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;

import dependency.vccl.utils.NodeCopy;
import dependencygraph.vccl.copylink.DAGNodeCopyManager.Status;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.FXUtils;

public class DAGNodeCopyController<V,E>{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/copylink/DAGNodeCopy.fxml";
	
	/////////////////////////////
	private DAGNodeCopyManager<V,E> manager;

	void setManager(DAGNodeCopyManager<V,E> manager) {
		this.manager = manager;
		this.copyButton.setText(Integer.toString(this.getManager().getCopyIndex()));
		
		//for testing
//		this.copyButton.setOnMouseClicked(e->{
//			System.out.println(this.getManager().getUpperCenterCoordinate());
//			System.out.println(this.getManager().getBottomCenterCoordinate());
//			
//		});
		///////
		this.copyButton.setOnMouseClicked(e->{
			if(e.getClickCount()==1) {
				if(this.getManager().getStatus().equals(Status.SELECTABLE)) {//
					//1. set this copy node to SELECTED
					this.getManager().setStatus(Status.SELECTED);
					//2. find out all other copy nodes that are linkable to this copy node and set their status to LINKABLE
					Map<V, Set<NodeCopy<V>>> dependedNodeLinkableNodeCopySetMap = 
							this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeCopyLinkAssigner().findoutAllDependedNodeLinkableNodeCopySetMap(this.getManager().getNodeCopy());
					
					//3. for all other copy nodes, 
					//3.1 if it is linkable in the dependedNodeLinkableNodeCopySetMap, set status to LINKABLE, 
					//3.2 if it is already linked, set to LINKED, 
					//3.3 otherwise set to UNLINKABLE;
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeManagerMap().forEach((node, dagNodeManager)->{
						dagNodeManager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, copyNodeManager)->{
							if(node.equals(this.getManager().getOwnerDAGNodeManager().getNode()) && 
									copyIndex == this.getManager().getCopyIndex()){
								//skip this node copy
							}else if(dependedNodeLinkableNodeCopySetMap.containsKey(node) && 
									dependedNodeLinkableNodeCopySetMap.get(node).contains(copyNodeManager.getNodeCopy())) {
								//node copy is linkable to this node copy
								copyNodeManager.setStatus(Status.LINKABLE);
							}else if(this.getManager().getNodeCopy().getDependedNodeLinkedNodeCopyMap().containsKey(node)&&
									Objects.equal(this.getManager().getNodeCopy().getDependedNodeLinkedNodeCopyMap().get(node),copyNodeManager.getNodeCopy())){
								copyNodeManager.setStatus(Status.LINKED);
							}else {
								//not linkable to this node copy
								copyNodeManager.setStatus(Status.UNLINKABLE);
							}
						});
					});
					
					//4. set the current selected copy node to this one in the CopyLinkAssignerManager;
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().setSelectedNodeCopyManager(this.getManager());
					
				}else if(this.getManager().getStatus().equals(Status.LINKABLE)){
					
					//1. add the link from the currently selected node copy to this one in the DAGNodeCopyLinkAssigner
					DAGNodeCopyManager<V, E> selectedNodeCopyManager = 
							this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getSelectedNodeCopyManager();
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeCopyLinkAssigner()
					.addLink(
							selectedNodeCopyManager.getOwnerDAGNodeManager().getNode(), 
							selectedNodeCopyManager.getCopyIndex(), 
							this.getManager().getOwnerDAGNodeManager().getNode(), 
							this.getManager().getCopyIndex());
					
					//2. add linking curve from the currently selected node copy to this one;
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().addCopyLinkCurve(
							this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getSelectedNodeCopyManager(),
							this.getManager());
					
					//3. set the currently selected node copy in CopyLinkAssignerManager to null
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().setSelectedNodeCopyManager(null);
					
					//4. set the status of all copies of all nodes to SELECTABLE
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeManagerMap().forEach((node, nodeManager)->{
						nodeManager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, nodeCopyManager)->{
							nodeCopyManager.setStatus(Status.SELECTABLE);
						});
					});
					
					//whenever a link is added
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getController().enableFinishAndCancelButton();
					
				}else if(this.getManager().getStatus().equals(Status.SELECTED)){//
					
					//1. set all copy node to SELECTABLE including the selected one
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeManagerMap().forEach((node, nodeManager)->{
						nodeManager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, nodeCopyManager)->{
							nodeCopyManager.setStatus(Status.SELECTABLE);
						});
					});
					
					//2. set the selected node copy to null in CopyLinkAssignerManager
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().setSelectedNodeCopyManager(null);
					
				}else if(this.getManager().getStatus().equals(Status.LINKED)){
					
					//1. remove the copy link from currently selected node copy to this node copy
					DAGNodeCopyManager<V, E> selectedNodeCopyManager = 
							this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getSelectedNodeCopyManager();
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeCopyLinkAssigner().removeLink(
							selectedNodeCopyManager.getOwnerDAGNodeManager().getNode(), 
							selectedNodeCopyManager.getCopyIndex(), 
							this.getManager().getOwnerDAGNodeManager().getNode(), 
							this.getManager().getCopyIndex());
					//2. remove the copy link curve from the canvass ()
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().removeCopyLinkCurve(this.getManager());
					
					//3. set the selected node copy to null in CopyLinkAssignerManager
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().setSelectedNodeCopyManager(null);
					
					//4. set all copy node to SELECTABLE including the selected one
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getDAGNodeManagerMap().forEach((node, nodeManager)->{
						nodeManager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, nodeCopyManager)->{
							nodeCopyManager.setStatus(Status.SELECTABLE);
						});
					});
					
					//whenever a link is removed
					this.getManager().getOwnerDAGNodeManager().getHostCopyLinkAssingerManager().getController().enableFinishAndCancelButton();
					
				}else if(this.getManager().getStatus().equals(Status.UNLINKABLE)){
					//do nothing
					
				}
			}
		});
		
	}
	
	/**
	 * set the node copy UI status based on current status;
	 */
	void updateUIVisualEffectBasedOnCurrentStatus() {
		if(this.getManager().getStatus().equals(Status.SELECTABLE)) {
			this.copyButton.setStyle("-fx-background-color:cyan");
		}else if(this.getManager().getStatus().equals(Status.SELECTED)) {
			this.copyButton.setStyle("-fx-background-color:orange");
		}else if(this.getManager().getStatus().equals(Status.LINKABLE)) {
			this.copyButton.setStyle("-fx-background-color:green");
		}else if(this.getManager().getStatus().equals(Status.LINKED)) {
			this.copyButton.setStyle("-fx-background-color:blue");
		}else if(this.getManager().getStatus().equals(Status.UNLINKABLE)) {
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
	
}
