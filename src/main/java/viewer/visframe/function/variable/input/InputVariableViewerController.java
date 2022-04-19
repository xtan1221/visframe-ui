package viewer.visframe.function.variable.input;

import function.variable.input.InputVariable;
import viewer.visframe.function.variable.VariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class InputVariableViewerController<I extends InputVariable> extends VariableViewerController<I>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public InputVariableViewer<I,?> getViewer() {
		return (InputVariableViewer<I,?>)this.viewer;
	}
	
	///////////////////////////////////

}
