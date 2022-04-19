package viewer.visframe.function.variable.output;

import function.variable.output.OutputVariable;
import function.variable.output.type.PFConditionEvaluatorBooleanOutputVariable;
import function.variable.output.type.ValueTableColumnOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.output.type.PFConditionEvaluatorBooleanOutputVariableViewer;

public class OutputVariableViewerFactory {

	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static OutputVariableViewer<?,?> build(OutputVariable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof ValueTableColumnOutputVariable) {
			return ValueTableColumnOutputVariableViewerFactory.build(
					(ValueTableColumnOutputVariable)value, 
					hostEvaluatorViewer);
		}else {//PFConditionEvaluatorBooleanOutputVariable
			return new PFConditionEvaluatorBooleanOutputVariableViewer(
					(PFConditionEvaluatorBooleanOutputVariable)value,
					hostEvaluatorViewer
					);
		}
		
		
	}
	
}
