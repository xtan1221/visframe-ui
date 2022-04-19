package viewer.visframe.operation.graph.transform;

import context.VisframeContext;
import operation.graph.transform.TransformGraphOperation;
import viewer.visframe.operation.graph.SingleGenericGraphAsInputOperationViewer;

public class TransformGraphOperationViewer extends SingleGenericGraphAsInputOperationViewer<TransformGraphOperation, TransformGraphOperationViewerController>{
	
	protected TransformGraphOperationViewer(TransformGraphOperation value, VisframeContext hostVisframeContext) {
		super(value, TransformGraphOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
