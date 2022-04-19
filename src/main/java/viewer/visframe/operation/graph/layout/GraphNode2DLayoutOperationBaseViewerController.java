package viewer.visframe.operation.graph.layout;

import operation.graph.layout.GraphNode2DLayoutOperationBase;
import viewer.visframe.operation.graph.InputGraphTypeBoundedOperationViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class GraphNode2DLayoutOperationBaseViewerController<G extends GraphNode2DLayoutOperationBase> extends InputGraphTypeBoundedOperationViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public GraphNode2DLayoutOperationBaseViewer<G,?> getViewer() {
		return (GraphNode2DLayoutOperationBaseViewer<G,?>)this.viewer;
	}
	
}
