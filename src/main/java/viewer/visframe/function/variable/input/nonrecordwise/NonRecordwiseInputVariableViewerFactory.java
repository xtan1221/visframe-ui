package viewer.visframe.function.variable.input.nonrecordwise;

import function.variable.input.nonrecordwise.NonRecordwiseInputVariable;
import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;
import function.variable.input.nonrecordwise.type.FreeInputVariable;
import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.type.ConstantValuedInputVariableViewer;
import viewer.visframe.function.variable.input.nonrecordwise.type.FreeInputVariableViewer;
import viewer.visframe.function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariableViewer;

public class NonRecordwiseInputVariableViewerFactory {
	
	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static NonRecordwiseInputVariableViewer<?,?> build(NonRecordwiseInputVariable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof ConstantValuedInputVariable) {
			return new ConstantValuedInputVariableViewer((ConstantValuedInputVariable)value, hostEvaluatorViewer);
		}else if(value instanceof FreeInputVariable) {
			return new FreeInputVariableViewer((FreeInputVariable)value, hostEvaluatorViewer);
		}else {//SQLAggregateFunctionBasedInputVariable
			return new SQLAggregateFunctionBasedInputVariableViewer((SQLAggregateFunctionBasedInputVariable)value, hostEvaluatorViewer);
		}
	}
}
