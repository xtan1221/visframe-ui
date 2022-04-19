package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class RecordwiseInputVariableIsNullValuedEvaluatorViewer extends NonSQLQueryBasedEvaluatorViewer<RecordwiseInputVariableIsNullValuedEvaluator, RecordwiseInputVariableIsNullValuedEvaluatorViewerController>{
	
	public RecordwiseInputVariableIsNullValuedEvaluatorViewer(
			RecordwiseInputVariableIsNullValuedEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, RecordwiseInputVariableIsNullValuedEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
