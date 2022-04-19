package viewer.visframe.operation.graph.build;

import operation.graph.build.BuildGraphFromExistingRecordOperationBase;
import viewer.visframe.operation.OperationViewerControllerBase;

/**
 * 
 * @author tanxu
 *
 */
public abstract class BuildGraphFromExistingRecordOperationViewerControllerBase<G extends BuildGraphFromExistingRecordOperationBase> extends OperationViewerControllerBase<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public BuildGraphFromExistingRecordOperationViewerBase<G,?> getViewer() {
		return (BuildGraphFromExistingRecordOperationViewerBase<G,?>)this.viewer;
	}
	
}
