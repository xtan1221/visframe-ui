package viewer.visframe.operation.sql;

import operation.sql.SQLOperationBase;
import viewer.visframe.operation.OperationViewerControllerBase;

/**
 * 
 * @author tanxu
 *
 */
public abstract class SQLOperationBaseViewerController<G extends SQLOperationBase> extends OperationViewerControllerBase<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public SQLOperationBaseViewer<G,?> getViewer() {
		return (SQLOperationBaseViewer<G,?>)this.viewer;
	}
	
}
