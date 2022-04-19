package viewer.visframe.function.variable.output;

import function.variable.output.OutputVariable;
import viewer.visframe.function.variable.VariableViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class OutputVariableViewerController<O extends OutputVariable> extends VariableViewerController<O>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public OutputVariableViewer<O,?> getViewer() {
		return (OutputVariableViewer<O,?>)this.viewer;
	}
	
	///////////////////////////////////

}
