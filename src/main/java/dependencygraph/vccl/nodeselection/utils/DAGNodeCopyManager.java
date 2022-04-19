package dependencygraph.vccl.nodeselection.utils;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import utils.LayoutCoordinateAndSizeUtils;
import utils.Pair;

/**
 * possible status of a copy node
 * 
 * SELECTED
 * 		
 * SELECTABLE
 * 
 * LINKABLE
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 */
public class DAGNodeCopyManager<V,E> {
	private final DAGNodeManager<V,E> ownerDAGNodeManager; 
	
	private final int copyIndex;
	
	/////////////////////
	private DAGNodeCopyController<V,E> controller;
	
	/////////////////////
	
	/**
	 * 
	 */
	private boolean selected;
	
	/////////////////////////////////
	//real height and width of the root node of this DAGNodeCopyManager in the DAG graph canvass
	private double height;
	private double width;
	
	//layout coordinate of the root node of this DAGNodeCopyManager in the DAG graph canvass
	private Point2D layoutCoordinate;

	private Point2D upperCenterCoordinate;
	private Point2D bottomCenterCoordinate;
	
	/**
	 * 
	 * @param ownerDAGNode
	 * @param copyIndex
	 */
	public DAGNodeCopyManager(
			DAGNodeManager<V,E> ownerDAGNode,
			int copyIndex){
		
		this.ownerDAGNodeManager = ownerDAGNode;
		this.copyIndex = copyIndex;
		
		this.selected = true;
	}
	

	/**
	 * @return the upperCenterCoordinate
	 */
	public Point2D getUpperCenterCoordinate() {
		if(this.upperCenterCoordinate==null) {
			this.calculateLayoutOnDAGCanvass();
		}
		
		return upperCenterCoordinate;
	}


	/**
	 * @return the bottomCenterCoordinate
	 */
	public Point2D getBottomCenterCoordinate() {
		if(this.bottomCenterCoordinate==null) {
			this.calculateLayoutOnDAGCanvass();
		}
		return bottomCenterCoordinate;
	}

	/**
	 * calculate the layout coordinate of the root FX node of this node copy;
	 * 
	 */
	private void calculateLayoutOnDAGCanvass() {
		this.layoutCoordinate = 
				LayoutCoordinateAndSizeUtils.getLayoutPoint2D(
						this.getOwnerDAGNodeManager().getHostCopyNodeSelectionManager().getController().getGraphLayoutAnchorPane(), 
						this.getController().getRootContainerPane());
		
		double centerX = this.layoutCoordinate.getX()+0.5*this.width;
		double upperCenterY = this.layoutCoordinate.getY();
		double bottomCenterY = this.layoutCoordinate.getY()+this.height;
		
		this.upperCenterCoordinate = new Point2D(centerX, upperCenterY);
		this.bottomCenterCoordinate = new Point2D(centerX, bottomCenterY);
	}
	
	
	///////////////////////////////////
	/**
	 * @return the ownerDAGNode
	 */
	public DAGNodeManager<V, E> getOwnerDAGNodeManager() {
		return ownerDAGNodeManager;
	}


	/**
	 * @return the copyIndex
	 */
	public int getCopyIndex() {
		return copyIndex;
	}


	/**
	 * 
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
	}
	//////////////////////////
	/**
	 * calculate the real width and height of the root node of this node copy;
	 * 
	 * 1. must be done before the root node is added to the DAGNodeManager's node copy container HBox!!!!!;
	 * because calculation process will 
	 * @return
	 */
	public DAGNodeCopyController<V,E> getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeCopyController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
			
			//
			Pair<Double,Double> realSize = 
					LayoutCoordinateAndSizeUtils.findOutRealWidthAndHeight(this.controller.getRootContainerPane());
			
			this.width = realSize.getFirst();
			this.height = realSize.getSecond();
		}
		
		return this.controller;
	}

	
	////////////////////////////
	/**
	 * @return the status
	 */
	public boolean isSelected() {
		return this.selected;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
		this.getController().updateUIVisualEffectBasedOnCurrentStatus();
	}

	

	//////////////////////////

}
