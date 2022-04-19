package viewer.visframe.function.variable.input.nonrecordwise;

import function.variable.input.nonrecordwise.NonRecordwiseInputVariable;
import viewer.visframe.function.variable.input.InputVariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class NonRecordwiseInputVariableViewerController<R extends NonRecordwiseInputVariable> extends InputVariableViewerController<R>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public NonRecordwiseInputVariableViewer<R,?> getViewer() {
		return (NonRecordwiseInputVariableViewer<R,?>)this.viewer;
	}
	
	///////////////////////////////////

}
