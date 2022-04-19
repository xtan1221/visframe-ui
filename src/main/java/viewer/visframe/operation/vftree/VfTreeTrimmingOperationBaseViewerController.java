package viewer.visframe.operation.vftree;

import operation.vftree.VfTreeTrimmingOperationBase;
import viewer.visframe.operation.OperationViewerControllerBase;

/**
 * 
 * @author tanxu
 *
 */
public abstract class VfTreeTrimmingOperationBaseViewerController<G extends VfTreeTrimmingOperationBase> extends OperationViewerControllerBase<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public VfTreeTrimmingOperationBaseViewer<G,?> getViewer() {
		return (VfTreeTrimmingOperationBaseViewer<G,?>)this.viewer;
	}
	
}
