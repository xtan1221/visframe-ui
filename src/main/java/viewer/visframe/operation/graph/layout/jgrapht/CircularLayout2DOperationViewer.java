package viewer.visframe.operation.graph.layout.jgrapht;

import context.VisframeContext;
import operation.graph.layout.jgrapht.CircularLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewer;

public class CircularLayout2DOperationViewer extends GraphNode2DLayoutOperationBaseViewer<CircularLayout2DOperation, CircularLayout2DOperationViewerController>{
	
	protected CircularLayout2DOperationViewer(CircularLayout2DOperation value, VisframeContext hostVisframeContext) {
		super(value, CircularLayout2DOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
