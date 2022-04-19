package viewer.visframe.operation.graph.layout;

import context.VisframeContext;
import operation.graph.layout.GraphNode2DLayoutOperationBase;
import viewer.visframe.operation.graph.InputGraphTypeBoundedOperationViewer;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class GraphNode2DLayoutOperationBaseViewer<G extends GraphNode2DLayoutOperationBase, C extends GraphNode2DLayoutOperationBaseViewerController<G>> extends InputGraphTypeBoundedOperationViewer<G, C>{

	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected GraphNode2DLayoutOperationBaseViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
