package viewer.visframe.operation.graph.layout.JUNG;

import context.VisframeContext;
import operation.graph.layout.JUNG.FRLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewer;

public class FRLayout2DOperationViewer extends GraphNode2DLayoutOperationBaseViewer<FRLayout2DOperation, FRLayout2DOperationViewerController>{
	
	protected FRLayout2DOperationViewer(FRLayout2DOperation value, VisframeContext hostVisframeContext) {
		super(value, FRLayout2DOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
