package viewer.visframe.function.variable.input;

import function.variable.input.InputVariable;
import function.variable.input.nonrecordwise.NonRecordwiseInputVariable;
import function.variable.input.recordwise.RecordwiseInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewerFactory;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewerFactory;

public class InputVariableViewerFactory {

	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 * @return
	 */
	public static InputVariableViewer<?,?> build(InputVariable value, EvaluatorViewer<?,?> hostEvaluatorViewer){
		if(value instanceof NonRecordwiseInputVariable) {
			return NonRecordwiseInputVariableViewerFactory.build((NonRecordwiseInputVariable)value, hostEvaluatorViewer);
		}else {//RecordwiseInputVariable
			return RecordwiseInputVariableViewerFactory.build((RecordwiseInputVariable)value, hostEvaluatorViewer);
		}
	}
	
}
