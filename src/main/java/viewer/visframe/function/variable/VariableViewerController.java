package viewer.visframe.function.variable;

import function.variable.Variable;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class VariableViewerController<V extends Variable> extends AbstractViewerController<V>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public VariableViewer<V,?> getViewer() {
		return (VariableViewer<V,?>)this.viewer;
	}
	
	///////////////////////////////////

}
