package viewer.visframe.operation.sql.predefined;

import operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperation;

/**
 * 
 * @author tanxu
 *
 */
public abstract class SingleInputRecordDataPredefinedSQLOperationViewerController<G extends SingleInputRecordDataPredefinedSQLOperation> extends PredefinedSQLBasedOperationViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public SingleInputRecordDataPredefinedSQLOperationViewer<G,?> getViewer() {
		return (SingleInputRecordDataPredefinedSQLOperationViewer<G,?>)this.viewer;
	}
	
}
