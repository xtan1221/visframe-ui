package dependencygraph.vccl.copynumber.node;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import dependency.vccl.utils.NodeCopy;
import dependencygraph.vccl.copynumber.DAGNodeCopyNumberAssignmentManager;
import dependencygraph.vccl.copynumber.notesassignment.DAGNodeNotesAssignmentManager;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

/**
 * 
 * @author tanxu
 * 
 * @param <V>
 * @param <E>
 */
public class DAGNodeManager<V,E>{
	private final DAGNodeCopyNumberAssignmentManager<V,E> hostDAGNodeCopyNumberAssignmentManager;
	private final V node;
	private final String nodeInforString;
	
	
	///////////////////////////
	private DAGNodeController<V,E> controller;
	
	
	//////////////
	private IntegerProperty copyNumberProperty;
	
	private Set<DAGNodeManager<V,E>> dependingNodeManagerSet;
	private Set<DAGNodeManager<V,E>> dependedNodeManagerSet;
	
	
	/**
	 * the real height of the root container StackPane of this DAG Node after all copy node are added
	 */
	private double realHeight;
	/**
	 * the real width of the root container StackPane of this DAG Node after all copy node are added
	 */
	private double realWidth;
	
	private double layoutX;
	
	private double layoutY;
	/**
	 * coordinate of the center point of this DAG node;
	 * Facilitate to calculate the starting and ending point of the dag edge curve;
	 */
	private Point2D centerCoord;
	
	
	//////////////////
	private DAGNodeNotesAssignmentManager<V,E> notesAssignmentManager;
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public DAGNodeManager(
			DAGNodeCopyNumberAssignmentManager<V,E> hostDAGNodeCopyNumberAssignmentManager,
			V node, String nodeInforString
			){
		
		this.hostDAGNodeCopyNumberAssignmentManager = hostDAGNodeCopyNumberAssignmentManager;
		this.node = node;
		this.nodeInforString = nodeInforString;
//		this.centerCoord = centerCoord;
		
		this.copyNumberProperty = new SimpleIntegerProperty();
		
		this.calRootNodeRealSize();
		
		this.initializeNotesAssignmentManager();
		
	}
	
	/**
	 * Calculate the real size of the root FX node of this DAG node;
	 * can only be invoked after all copy nodes have been added!!!!!!
	 * 
	 * note that the method of calculation of the real size 
	 * 		{@link LayoutCoordinateAndSizeUtils#findOutRealWidthAndHeight(javafx.scene.layout.Pane)}
	 * will add the root node to a dummy group and scene, thus, must be calculated before adding to the DAG graph canvass AnchorPane!!!!
	 * 
	 */
	private void calRootNodeRealSize() {
		Pane rootPane = this.getController().getRootNodeContainer();
		
		Pair<Double,Double> realSize = LayoutCoordinateAndSizeUtils.findOutRealWidthAndHeight(rootPane);
		
		this.realWidth = realSize.getFirst();
		this.realHeight = realSize.getSecond();
	}
	
	/**
	 * initialize the {@link #notesAssignmentManager}
	 * then add the root node to the UI
	 */
	private void initializeNotesAssignmentManager() {
		this.notesAssignmentManager = new DAGNodeNotesAssignmentManager<>(this);
		this.getHostDAGNodeCopyNumberAssignmentManager().getController().getNodeNotesAssignmentVBox().getChildren().add(this.notesAssignmentManager.getController().getRootNodeContainer());
	}
	
	
	
	//////////////////
	/**
	 * @return the hostDAGNodeCopyNumberAssignmentManager
	 */
	public DAGNodeCopyNumberAssignmentManager<V, E> getHostDAGNodeCopyNumberAssignmentManager() {
		return hostDAGNodeCopyNumberAssignmentManager;
	}


	/**
	 * @return the node
	 */
	public V getNode() {
		return node;
	}


	/**
	 * @return the nodeInforStringFunction
	 */
	public String getNodeInforString() {
		return this.nodeInforString;
	}

	/////////////////////////////
	/**
	 * 
	 * @param layoutX
	 * @param layoutY
	 */
	public void setLayoutAndCalculateCenterCoord(double layoutX, double layoutY) {
		this.layoutX = layoutX;
		this.layoutY = layoutY;
		this.getController().getRootNodeContainer().setLayoutX(layoutX);
		this.getController().getRootNodeContainer().setLayoutY(layoutY);
		
		double centerX = this.layoutX + 0.5*this.getRealWidth();
		double centerY = this.layoutY + 0.5*this.getRealHeight();
		this.centerCoord = new Point2D(centerX, centerY);
	}
	
	/**
	 * @return the centerCoord
	 */
	public Point2D getCenterCoord() {
		return centerCoord;
	}

	/**
	 * @return the height
	 */
	public double getRealHeight() {
		return realHeight;
	}
	/**
	 * @return the widht
	 */
	public double getRealWidth() {
		return realWidth;
	}
	
	
	/**
	 * @return the copyNumberProperty
	 */
	public IntegerProperty getCopyNumberProperty() {
		return copyNumberProperty;
	}


	/**
	 * @return the dependingNodeManagerSet
	 */
	public Set<DAGNodeManager<V, E>> getDependingNodeManagerSet() {
		return dependingNodeManagerSet;
	}


	/**
	 * @return the dependedNodeManagerSet
	 */
	public Set<DAGNodeManager<V, E>> getDependedNodeManagerSet() {
		return dependedNodeManagerSet;
	}


	public void setDependingDAGNodeManagerSet(Set<DAGNodeManager<V,E>> dependingNodeManagerSet) {
		this.dependingNodeManagerSet = dependingNodeManagerSet;
	}
	
	public void setDependedDAGNodeManagerSet(Set<DAGNodeManager<V,E>> dependedNodeManagerSet) {
		this.dependedNodeManagerSet = dependedNodeManagerSet;
	}
	
	
	/**
	 * set the event listener for change of the {@link #copyNumberProperty}
	 * 
	 * if the {@link #copyNumberProperty} is changed from 0 to positive or from positive to 0
	 * 		invoke the {@link #updateStatus()} of every depending DAGNodeManager;
	 * 
	 */
	public void setCopyNumberChangeEventListener() {
		this.copyNumberProperty.addListener((obj, ov, nv)->{
			if(ov!=null && nv !=null) {
				int oldValue = (int) ov;
				int newValue = (int) nv;
				
				if(oldValue == 0 && newValue > 0 || oldValue > 0 && newValue == 0) {//copy number changed from 0 to positive number
					System.out.println(this.nodeInforString.concat(":copy number changed from 0 to positive or from positive to 0"));
					this.dependingNodeManagerSet.forEach(v->{
						v.updateStatus();
					});
				}
				
				if(oldValue==0 && newValue > 0) {
					this.getController().setDecreaseByOneButtonDisable(false);
				}
				
				if(oldValue > 0 && newValue == 0) {
					this.getController().setDecreaseByOneButtonDisable(true);
				}
				
				//update the notes
				this.getNotesAssignmentManager().updateCopyNumber(newValue);
			}
		});
	}
	
	/**
	 * update the status of this node based on the copy number of all depended nodes;
	 * 		invoked whenever one of the depended nodes' copy number is changed from o to positive or from positive to 0;
	 * 
	 * 
	 * if all depended nodes have positive copy numbers, set the status of this node to assignable;
	 * 		{@link DAGNodeController#setToAssignable()}
	 * otherwise, set the copy number of this node to 0 and status to un-assignable;
	 * 		{@link DAGNodeController#setToUnAssignable()}
	 * 
	 * note that for node with no depended nodes, the status should always be assignable;
	 */
	public void updateStatus() {
		
		if(this.dependedNodeManagerSet.isEmpty()) {
			System.out.println(this.getClass().getSimpleName().concat(":").concat(this.nodeInforString.concat(":no depended nodes")));
		}else {
			System.out.println(this.getClass().getSimpleName().concat(":").concat(this.nodeInforString.concat(":update status")));
			boolean noDependedNodeWith0CopyNumber = true;
			
			for(DAGNodeManager<V, E> node:this.dependedNodeManagerSet){
				if(node.getCopyNumber()==0) {
					noDependedNodeWith0CopyNumber = false;
					break;
				}
			}
			
			if(noDependedNodeWith0CopyNumber) {
				System.out.println(this.getClass().getSimpleName().concat(":").concat(this.nodeInforString.concat(":assignable")));
				this.getController().setToAssignable();
			}else {
//				if(this.getCopyNumber()!=0) {
				System.out.println(this.getClass().getSimpleName().concat(":").concat(this.nodeInforString.concat(":unassignable")));
				this.setCopyNumber(0);
				this.getController().setToUnAssignable();
//				}
			}
		}
		
	}
	
	public int getCopyNumber() {
		return this.copyNumberProperty.get();
	}
	
	/**
	 * set the value of the {@link #copyNumberProperty};
	 * also set the text of the TextField for copy number on the UI;
	 * 
	 * @param copyNumber
	 */
	void setCopyNumber(int copyNumber) {
		this.copyNumberProperty.set(copyNumber);
		this.getController().setCopyNumberTextField(copyNumber);
	}
	
	public void setCopyIndexNodeCopyMap(Map<Integer,NodeCopy<V>> map) {
		this.setCopyNumber(map.size());
		
		this.getNotesAssignmentManager().setCopyIndexNodeCopyMap(map);
	}
	
	
	/**
	 * set modifiable
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
	}
	
	/////////////////////////////
	public DAGNodeController<V,E> getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}

	/**
	 * @return the notesAssignmentManager
	 */
	public DAGNodeNotesAssignmentManager<V,E> getNotesAssignmentManager() {
		return notesAssignmentManager;
	}
	
}
