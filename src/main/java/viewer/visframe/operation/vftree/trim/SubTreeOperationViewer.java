package viewer.visframe.operation.vftree.trim;

import context.VisframeContext;
import operation.vftree.trim.SubTreeOperation;
import viewer.visframe.operation.vftree.VfTreeTrimmingOperationBaseViewer;

public class SubTreeOperationViewer extends VfTreeTrimmingOperationBaseViewer<SubTreeOperation, SubTreeOperationViewerController>{
	
	protected SubTreeOperationViewer(SubTreeOperation value, VisframeContext hostVisframeContext) {
		super(value, SubTreeOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
