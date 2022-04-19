package viewer.visframe.operation.sql.predefined;

import operation.sql.predefined.PredefinedSQLBasedOperation;
import viewer.visframe.operation.sql.SQLOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class PredefinedSQLBasedOperationViewerController<G extends PredefinedSQLBasedOperation> extends SQLOperationBaseViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public PredefinedSQLBasedOperationViewer<G,?> getViewer() {
		return (PredefinedSQLBasedOperationViewer<G,?>)this.viewer;
	}
	
}
