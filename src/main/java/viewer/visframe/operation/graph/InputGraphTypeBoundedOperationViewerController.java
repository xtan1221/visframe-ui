package viewer.visframe.operation.graph;

import operation.graph.InputGraphTypeBoundedOperation;

/**
 * 
 * @author tanxu
 *
 */
public abstract class InputGraphTypeBoundedOperationViewerController<G extends InputGraphTypeBoundedOperation> extends SingleGenericGraphAsInputOperationViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public SingleGenericGraphAsInputOperationViewer<G,?> getViewer() {
		return (SingleGenericGraphAsInputOperationViewer<G,?>)this.viewer;
	}
	
}
