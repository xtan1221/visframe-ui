package dependencygraph.vccl.nodeselection.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import dependencygraph.vccl.nodeselection.CopyNodeSelectionManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

public class DAGNodeManager<V,E>{
	private final CopyNodeSelectionManager<V, E> hostCopyNodeSelectionManager;
	private final V node;
	private final int copyNumber;
	private final Function<V,String> nodeInforStringFunction;
	
	///////////////////////////
	private DAGNodeController<V,E> controller;
	
	private Set<DAGNodeManager<V,E>> dependingNodeManagerSet;
	private Set<DAGNodeManager<V,E>> dependedNodeManagerSet;
	///////////
	private Map<Integer, DAGNodeCopyManager<V,E>> copyIndexNodeCopyManage;
	
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
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public DAGNodeManager(
			CopyNodeSelectionManager<V, E> hostCopyNodeSelectionManager,
			V node, 
			int copyNumber,
			Function<V,String> nodeInforStringFunction
			){
		
		this.hostCopyNodeSelectionManager = hostCopyNodeSelectionManager;
		this.node = node;
		this.copyNumber = copyNumber;
		this.nodeInforStringFunction = nodeInforStringFunction;
		
		
		////////////////////
		this.createAndAddCopyNodeManager();
		this.calRootNodeRealSize();
	}
	
	
	/**
	 * create a CopyNodeManager for each of the copy of this node; and add to this DAG node on UI;
	 */
	private void createAndAddCopyNodeManager() {
		this.copyIndexNodeCopyManage = new HashMap<>();
		for(int i=1;i<=this.copyNumber;i++) {
			DAGNodeCopyManager<V, E> nodeCopyManager = new DAGNodeCopyManager<>(this, i);
			
			this.copyIndexNodeCopyManage.put(i, nodeCopyManager);
			
			//add to dag node UI
			this.getController().getCopyNodeContainerPane().getChildren().add(nodeCopyManager.getController().getRootContainerPane());
		}
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


	public int getCopyNumber() {
		return this.copyNumber;
	}
	
	
	/**
	 * @return the copyIndexNodeManagerMap
	 */
	public Map<Integer, DAGNodeCopyManager<V, E>> getCopyIndexNodeCopyManagerMap() {
		return copyIndexNodeCopyManage;
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
	
	/**
	 * 
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getCopyIndexNodeCopyManagerMap().forEach((copy,manager)->{
			manager.setModifiable(modifiable);
		});
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
	 * @return the hostCopyNodeSelectionManager
	 */
	public CopyNodeSelectionManager<V, E> getHostCopyNodeSelectionManager() {
		return hostCopyNodeSelectionManager;
	}



}
