package viewer.visframe.function.variable.output;

import function.variable.output.type.ValueTableColumnOutputVariable;

/**
 * 
 * @author tanxu
 *
 */
public abstract class ValueTableColumnOutputVariableViewerController<O extends ValueTableColumnOutputVariable> extends OutputVariableViewerController<O>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public ValueTableColumnOutputVariableViewer<O,?> getViewer() {
		return (ValueTableColumnOutputVariableViewer<O,?>)this.viewer;
	}
	
	///////////////////////////////////

}
