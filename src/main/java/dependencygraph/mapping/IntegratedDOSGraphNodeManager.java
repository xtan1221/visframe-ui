package dependencygraph.mapping;

import java.io.IOException;
import java.util.Set;

import dependency.dos.integrated.IntegratedDOSGraphNode;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

public class IntegratedDOSGraphNodeManager{
	private final SolutionSetMetadataMappingManager hostIntegratedDOSGraphViewerManager;
	private final IntegratedDOSGraphNode node;
	
	///////////////////////////
	private IntegratedDOSGraphNodeController controller;
	
	private Set<IntegratedDOSGraphNodeManager> dependingNodeManagerSet;
	private Set<IntegratedDOSGraphNodeManager> dependedNodeManagerSet;
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
	//////////////////////////////////////
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public IntegratedDOSGraphNodeManager(
			SolutionSetMetadataMappingManager hostIntegratedDOSGraphViewerManager,
			IntegratedDOSGraphNode node
			){
		
		
		this.hostIntegratedDOSGraphViewerManager = hostIntegratedDOSGraphViewerManager;
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
	public SolutionSetMetadataMappingManager getHostIntegratedDOSGraphViewerManager() {
		return hostIntegratedDOSGraphViewerManager;
	}
	
	/**
	 * @return the node
	 */
	public IntegratedDOSGraphNode getNode() {
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
	public Set<IntegratedDOSGraphNodeManager> getDependingNodeManagerSet() {
		return dependingNodeManagerSet;
	}


	/**
	 * @return the dependedNodeManagerSet
	 */
	public Set<IntegratedDOSGraphNodeManager> getDependedNodeManagerSet() {
		return dependedNodeManagerSet;
	}

	
	public void setDependingDAGNodeManagerSet(Set<IntegratedDOSGraphNodeManager> dependingNodeManagerSet) {
		this.dependingNodeManagerSet = dependingNodeManagerSet;
	}
	
	public void setDependedDAGNodeManagerSet(Set<IntegratedDOSGraphNodeManager> dependedNodeManagerSet) {
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
	 * set all UI features related with whether this node is in solution set
	 * 
	 * @param inSolutionSet
	 */
	public void setInSolutionSet(boolean inSolutionSet) {
		this.getController().setInSolutionSet(inSolutionSet);
	}
	
	//////////////////////////////
	/**
	 * build (if not already) and return the controller of this manager;
	 * 
	 * @return
	 */
	public IntegratedDOSGraphNodeController getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(IntegratedDOSGraphNodeController.FXML_FILE_DIR_STRING));
			
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

	
	////////////////////////////
	/**
	 * @return the modifiable
	 */
	public boolean isModifiable() {
		return modifiable;
	}



	private boolean modifiable = true;
	public void setModifiable(boolean modifiable) {
		this.modifiable = modifiable;
	}

}
