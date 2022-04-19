package viewer.visframe.operation.vftree.trim;

import context.VisframeContext;
import operation.vftree.trim.SiblingNodesReorderOperation;
import viewer.visframe.operation.vftree.VfTreeTrimmingOperationBaseViewer;

public class SiblingNodesReorderOperationViewer extends VfTreeTrimmingOperationBaseViewer<SiblingNodesReorderOperation, SiblingNodesReorderOperationViewerController>{
	
	protected SiblingNodesReorderOperationViewer(SiblingNodesReorderOperation value, VisframeContext hostVisframeContext) {
		super(value, SiblingNodesReorderOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
