package viewer.visframe.operation.sql.predefined.type;

import context.VisframeContext;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import viewer.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationViewer;

public class AddNumericCumulativeColumnOperationViewer extends SingleInputRecordDataPredefinedSQLOperationViewer<AddNumericCumulativeColumnOperation, AddNumericCumulativeColumnOperationViewerController>{
	
	protected AddNumericCumulativeColumnOperationViewer(AddNumericCumulativeColumnOperation value, VisframeContext hostVisframeContext) {
		super(value, AddNumericCumulativeColumnOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
