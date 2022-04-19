package viewer.visframe.operation.sql.predefined;

import context.VisframeContext;
import operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class SingleInputRecordDataPredefinedSQLOperationViewer<G extends SingleInputRecordDataPredefinedSQLOperation, C extends SingleInputRecordDataPredefinedSQLOperationViewerController<G>> extends PredefinedSQLBasedOperationViewer<G, C>{
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected SingleInputRecordDataPredefinedSQLOperationViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
