package viewer.visframe.operation.vftree.trim;

import context.VisframeContext;
import operation.vftree.trim.RerootTreeOperation;
import viewer.visframe.operation.vftree.VfTreeTrimmingOperationBaseViewer;

public class RerootTreeOperationViewer extends VfTreeTrimmingOperationBaseViewer<RerootTreeOperation, RerootTreeOperationViewerController>{
	
	protected RerootTreeOperationViewer(RerootTreeOperation value, VisframeContext hostVisframeContext) {
		super(value, RerootTreeOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
