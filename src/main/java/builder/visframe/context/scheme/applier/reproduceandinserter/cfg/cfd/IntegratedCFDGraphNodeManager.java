package builder.visframe.context.scheme.applier.reproduceandinserter.cfg.cfd;

import java.io.IOException;
import java.util.Set;

import builder.visframe.context.scheme.applier.reproduceandinserter.cfg.CFGReproducingAndInsertionManager;
import dependency.cfd.integrated.IntegratedCFDGraphNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

public class IntegratedCFDGraphNodeManager{
	private final CFGReproducingAndInsertionManager hostCFGReproducingAndInsertionManager;
	
	private final IntegratedCFDGraphNode node;
	
	///////////////////////////
	private IntegratedCFDGraphNodeController controller;
	
	private Set<IntegratedCFDGraphNodeManager> dependingNodeManagerSet;
	private Set<IntegratedCFDGraphNodeManager> dependedNodeManagerSet;
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
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public IntegratedCFDGraphNodeManager(
			CFGReproducingAndInsertionManager hostCFGReproducingAndInsertionManager,
			IntegratedCFDGraphNode node
			){
		
		
		this.hostCFGReproducingAndInsertionManager = hostCFGReproducingAndInsertionManager;
		this.node = node;
		
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
	 * @return the hostIntegratedDOSGraphViewerManager
	 */
	public CFGReproducingAndInsertionManager getHostCFGReproducingAndInsertionManager() {
		return this.hostCFGReproducingAndInsertionManager;
	}
	
	/**
	 * @return the node
	 */
	public IntegratedCFDGraphNode getNode() {
		return node;
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
	public Set<IntegratedCFDGraphNodeManager> getDependingNodeManagerSet() {
		return dependingNodeManagerSet;
	}


	/**
	 * @return the dependedNodeManagerSet
	 */
	public Set<IntegratedCFDGraphNodeManager> getDependedNodeManagerSet() {
		return dependedNodeManagerSet;
	}

	
	public void setDependingDAGNodeManagerSet(Set<IntegratedCFDGraphNodeManager> dependingNodeManagerSet) {
		this.dependingNodeManagerSet = dependingNodeManagerSet;
	}
	
	public void setDependedDAGNodeManagerSet(Set<IntegratedCFDGraphNodeManager> dependedNodeManagerSet) {
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
	public IntegratedCFDGraphNodeController getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(IntegratedCFDGraphNodeController.FXML_FILE_DIR_STRING));
			
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



}
