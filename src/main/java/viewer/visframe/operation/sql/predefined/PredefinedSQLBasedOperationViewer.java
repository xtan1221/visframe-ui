package viewer.visframe.operation.sql.predefined;

import context.VisframeContext;
import operation.sql.predefined.PredefinedSQLBasedOperation;
import viewer.visframe.operation.sql.SQLOperationBaseViewer;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class PredefinedSQLBasedOperationViewer<G extends PredefinedSQLBasedOperation, C extends PredefinedSQLBasedOperationViewerController<G>> extends SQLOperationBaseViewer<G, C>{
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected PredefinedSQLBasedOperationViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
