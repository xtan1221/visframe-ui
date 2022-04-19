package viewer.visframe.function.variable.output;

import function.variable.output.type.CFGTargetOutputVariable;
import function.variable.output.type.TemporaryOutputVariable;
import function.variable.output.type.ValueTableColumnOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.output.type.CFGTargetOutputVariableViewer;
import viewer.visframe.function.variable.output.type.TemporaryOutputVariableViewer;

public class ValueTableColumnOutputVariableViewerFactory {

	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static ValueTableColumnOutputVariableViewer<?,?> build(ValueTableColumnOutputVariable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof CFGTargetOutputVariable) {
			return new CFGTargetOutputVariableViewer(
					(CFGTargetOutputVariable)value,
					hostEvaluatorViewer
					);
		}else {//TemporaryOutputVariable
			return new TemporaryOutputVariableViewer(
					(TemporaryOutputVariable)value,
					hostEvaluatorViewer
					);
		}
	}
	
}
