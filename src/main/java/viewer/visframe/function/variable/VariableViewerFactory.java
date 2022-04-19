package viewer.visframe.function.variable;

import function.variable.Variable;
import function.variable.input.InputVariable;
import function.variable.output.OutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.InputVariableViewerFactory;
import viewer.visframe.function.variable.output.OutputVariableViewerFactory;

public class VariableViewerFactory {

	/**
	 * constructor
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static VariableViewer<?,?> buildVariableViewer(Variable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof InputVariable) {
			return InputVariableViewerFactory.build((InputVariable)value, hostEvaluatorViewer);
		}else {//OutputVariable
			return OutputVariableViewerFactory.build((OutputVariable)value, hostEvaluatorViewer);
		}
	}
	
}
