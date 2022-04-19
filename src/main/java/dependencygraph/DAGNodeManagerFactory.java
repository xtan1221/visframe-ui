package dependencygraph;

import javafx.geometry.Point2D;

public abstract class DAGNodeManagerFactory<V, C extends DAGNodeController<V>> {
	
	public abstract DAGNodeManager<V,C> build(DAGMainManager<V, ?> mainManager, V node, Point2D centerCoord, double nodeRootFXNodeHeight, double nodeRootFXNodeWidth);
	
}
