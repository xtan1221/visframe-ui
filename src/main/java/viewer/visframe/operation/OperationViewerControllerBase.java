package viewer.visframe.operation;

import operation.Operation;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class OperationViewerControllerBase<G extends Operation> extends AbstractViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public OperationViewerBase<G,?> getViewer() {
		return (OperationViewerBase<G,?>)this.viewer;
	}
	
}
