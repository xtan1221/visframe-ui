package viewer.visframe.operation.sql;

import context.VisframeContext;
import operation.sql.SQLOperationBase;
import viewer.visframe.operation.OperationViewerBase;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class SQLOperationBaseViewer<G extends SQLOperationBase, C extends SQLOperationBaseViewerController<G>> extends OperationViewerBase<G, C>{
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected SQLOperationBaseViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
