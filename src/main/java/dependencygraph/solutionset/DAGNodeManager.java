package dependencygraph.solutionset;

import java.io.IOException;
import java.util.Set;
import java.util.function.Function;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

public class DAGNodeManager<V,E>{
	private final DAGSolutionSetSelectorManager<V, E> hostDAGSolutionSetSelectorManager;
	private final V node;
	private final Function<V,String> nodeInforStringFunction;
	
	///////////////////////////
	private DAGNodeController<V,E> controller;
	
	private Set<DAGNodeManager<V,E>> dependingNodeManagerSet;
	private Set<DAGNodeManager<V,E>> dependedNodeManagerSet;
	///////////
	
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
	
	/////////////////////
	/**
	 * 
	 */
	private Status nodeStatus;
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public DAGNodeManager(
			DAGSolutionSetSelectorManager<V, E> hostCopyLinkAssingerManager,
			V node, 
			Function<V,String> nodeInforStringFunction
			){
		
		this.hostDAGSolutionSetSelectorManager = hostCopyLinkAssingerManager;
		this.node = node;
		this.nodeInforStringFunction = nodeInforStringFunction;
		
		
		////////////////////
		this.calRootNodeRealSize();
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
		Pair<Double,Double> realSize = LayoutCoordinateAndSizeUtils.findOutRealWidthAndHeight(this.getController().getRootNodeContainer());
		
		this.realWidth = realSize.getFirst();
		this.realHeight = realSize.getSecond();
	}
	
	///////////////////////////
	/**
	 * @return the node
	 */
	public V getNode() {
		return node;
	}
	
	/**
	 * @return the nodeInforStringFunction
	 */
	public Function<V, String> getNodeInforStringFunction() {
		return nodeInforStringFunction;
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
	
	/////////////////////////////
	/**
	 * build (if not already) and return the controller of this manager;
	 * 
	 * @return
	 */
	public DAGNodeController<V,E> getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
			
		}
		
		return this.controller;
	}


	/**
	 * @return the hostCopyLinkAssingerManager
	 */
	public DAGSolutionSetSelectorManager<V, E> getHostDAGSolutionSetSelectorManager() {
		return hostDAGSolutionSetSelectorManager;
	}
	
	/**
	 * 
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		//if same with current status, do nothing;
		if(this.nodeStatus!=null && this.nodeStatus.equals(status))
			return;
		
		this.nodeStatus = status;
		this.getController().updateUIVisualEffectBasedOnCurrentStatus();
	}
	
	
	/**
	 * @return the nodeStatus
	 */
	public Status getNodeStatus() {
		return nodeStatus;
	}
	
	/**
	 * 
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
	}
	
	/////////////////////
	static enum Status{
		SELECTABLE, //node on one or more RootPath and selectable;
		SELECTED, //node on one or more rootpath and selected
		REPRESENTED,//node on one or more root paths 
		UNSELECTABLE;//node not on any root path
	}
}
