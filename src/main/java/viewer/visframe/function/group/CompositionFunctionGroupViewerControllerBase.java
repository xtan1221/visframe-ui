package viewer.visframe.function.group;

import function.group.CompositionFunctionGroup;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class CompositionFunctionGroupViewerControllerBase<G extends CompositionFunctionGroup> extends AbstractViewerController<G>{
	
	//////////////////////////////////////////////
	
	
	/**
	 * @return the viewer
	 */
	@Override
	public CompositionFunctionGroupViewerBase<G,?> getViewer() {
		return (CompositionFunctionGroupViewerBase<G,?>)this.viewer;
	}
	
}
