package viewer.visframe.operation.graph.layout.JUNG;

import context.VisframeContext;
import operation.graph.layout.JUNG.SpringLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewer;

public class SpringLayout2DOperationViewer extends GraphNode2DLayoutOperationBaseViewer<SpringLayout2DOperation, SpringLayout2DOperationViewerController>{
	
	protected SpringLayout2DOperationViewer(SpringLayout2DOperation value, VisframeContext hostVisframeContext) {
		super(value, SpringLayout2DOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
