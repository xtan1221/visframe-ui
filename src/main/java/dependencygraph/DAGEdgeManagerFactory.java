package dependencygraph;

import javafx.geometry.Point2D;

public abstract class DAGEdgeManagerFactory<E> {
	
	public abstract DAGEdgeManager<E> build(DAGMainManager<?,E> mainManager, E edge, Point2D sourceNodeCenterCoord, Point2D sinkNodeCenterCoord);
	
}
