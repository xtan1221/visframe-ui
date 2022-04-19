package viewer.visframe.operation.sql.predefined.type;

import context.VisframeContext;
import operation.sql.predefined.type.GroupAndBinCountOperation;
import viewer.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationViewer;

public class GroupAndBinCountOperationViewer extends SingleInputRecordDataPredefinedSQLOperationViewer<GroupAndBinCountOperation, GroupAndBinCountOperationViewerController>{
	
	protected GroupAndBinCountOperationViewer(GroupAndBinCountOperation value, VisframeContext hostVisframeContext) {
		super(value, GroupAndBinCountOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
