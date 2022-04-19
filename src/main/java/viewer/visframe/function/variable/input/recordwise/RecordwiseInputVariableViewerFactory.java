package viewer.visframe.function.variable.input.recordwise;

import function.variable.input.recordwise.RecordwiseInputVariable;
import function.variable.input.recordwise.type.CFGTargetInputVariable;
import function.variable.input.recordwise.type.RecordAttributeInputVariable;
import function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.recordwise.type.CFGTargetInputVariableViewer;
import viewer.visframe.function.variable.input.recordwise.type.RecordAttributeInputVariableViewer;
import viewer.visframe.function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariableViewer;

public class RecordwiseInputVariableViewerFactory {
	
	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static RecordwiseInputVariableViewer<?,?> build(RecordwiseInputVariable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof CFGTargetInputVariable) {
			return new CFGTargetInputVariableViewer((CFGTargetInputVariable)value, hostEvaluatorViewer);
		}else if(value instanceof RecordAttributeInputVariable) {
			return new RecordAttributeInputVariableViewer((RecordAttributeInputVariable)value, hostEvaluatorViewer);
		}else {//UpstreamValueTableColumnOutputVariableInputVariable
			return new UpstreamValueTableColumnOutputVariableInputVariableViewer((UpstreamValueTableColumnOutputVariableInputVariable)value, hostEvaluatorViewer);
		}
	}
}
