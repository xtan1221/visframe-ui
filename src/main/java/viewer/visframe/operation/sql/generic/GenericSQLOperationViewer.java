package viewer.visframe.operation.sql.generic;

import context.VisframeContext;
import operation.sql.generic.GenericSQLOperation;
import viewer.visframe.operation.sql.SQLOperationBaseViewer;

public class GenericSQLOperationViewer extends SQLOperationBaseViewer<GenericSQLOperation, GenericSQLOperationViewerController>{
	
	protected GenericSQLOperationViewer(GenericSQLOperation value, VisframeContext hostVisframeContext) {
		super(value, GenericSQLOperationViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
