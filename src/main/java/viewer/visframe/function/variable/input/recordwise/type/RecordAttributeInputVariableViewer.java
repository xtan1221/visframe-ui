package viewer.visframe.function.variable.input.recordwise.type;

import function.variable.input.recordwise.type.RecordAttributeInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewer;

public class RecordAttributeInputVariableViewer extends RecordwiseInputVariableViewer<RecordAttributeInputVariable, RecordAttributeInputVariableViewerController>{

	public RecordAttributeInputVariableViewer(
			RecordAttributeInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, RecordAttributeInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
