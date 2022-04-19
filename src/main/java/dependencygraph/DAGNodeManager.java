package dependencygraph;

import javafx.geometry.Point2D;

/**
 * base class for a node manager of a DAG
 * @author tanxu
 *
 * @param <V>
 */
public abstract class DAGNodeManager<V, C extends DAGNodeController<V>> {
	protected final DAGMainManager<V,?> mainManager;
	protected final V node;
	protected final Point2D centerCoord;
	
	/**
	 * the height of the root JAVAFX Node that contains the DAG node
	 */
	private final double rootFXNodeHeight;
	/**
	 * the width of the root JAVAFX Node that contains the DAG node
	 */
	private final double rootFXNodeWidth;
	
	
	protected C controller;
	
	
	protected DAGNodeManager(
			DAGMainManager<V,?> mainManager,
			V node, Point2D centerCoord,
			double nodeRootNodeHeight,
			double nodeRootNodeWidth
			){
		this.mainManager = mainManager;
		this.node = node;
		
		this.centerCoord = centerCoord;
		
		this.rootFXNodeHeight = nodeRootNodeHeight;
		this.rootFXNodeWidth = nodeRootNodeWidth;
		
//		this.calculateAndSetLayout();
	}

	public DAGMainManager<V,?> getMainManager() {
		return mainManager;
	}
	
	
	/**
	 * @return the node
	 */
	public V getNode() {
		return node;
	}
	
	/**
	 * @return the centerCoord
	 */
	public Point2D getCenterCoord() {
		return centerCoord;
	}

	/**
	 * @return the rootNodeHeight
	 */
	public double getRootFXNodeHeight() {
		return rootFXNodeHeight;
	}

	/**
	 * @return the rootNodeWidth
	 */
	public double getRootFXNodeWidth() {
		return rootFXNodeWidth;
	}

	/////////////////////////////////////////////////////
	protected abstract C getController();
//	
//	/**
//	 * calculate the layout of the root container of this node on the DAGMainManager's UI;
//	 * 
//	 * then set the layout for the root container fx node of this node;
//	 * 
//	 */
//	protected abstract void calculateAndSetLayout();

}
