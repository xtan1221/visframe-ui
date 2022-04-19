package viewer.visframe.function.variable.input.recordwise;

import function.variable.input.recordwise.RecordwiseInputVariable;
import viewer.visframe.function.variable.input.InputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class RecordwiseInputVariableViewerController<R extends RecordwiseInputVariable> extends InputVariableViewerController<R>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public RecordwiseInputVariableViewer<R,?> getViewer() {
		return (RecordwiseInputVariableViewer<R,?>)this.viewer;
	}
	
	///////////////////////////////////

}
