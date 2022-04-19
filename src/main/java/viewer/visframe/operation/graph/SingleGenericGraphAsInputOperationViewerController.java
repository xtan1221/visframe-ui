package viewer.visframe.operation.graph;

import operation.graph.SingleGenericGraphAsInputOperation;
import viewer.visframe.operation.OperationViewerControllerBase;

/**
 * 
 * @author tanxu
 *
 */
public abstract class SingleGenericGraphAsInputOperationViewerController<G extends SingleGenericGraphAsInputOperation> extends OperationViewerControllerBase<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public SingleGenericGraphAsInputOperationViewer<G,?> getViewer() {
		return (SingleGenericGraphAsInputOperationViewer<G,?>)this.viewer;
	}
	
}
