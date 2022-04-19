package viewer.visframe.function.variable.input.recordwise.type;

import function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewer;

public class UpstreamValueTableColumnOutputVariableInputVariableViewer extends RecordwiseInputVariableViewer<UpstreamValueTableColumnOutputVariableInputVariable, UpstreamValueTableColumnOutputVariableInputVariableViewerController>{
	
	public UpstreamValueTableColumnOutputVariableInputVariableViewer(
			UpstreamValueTableColumnOutputVariableInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, UpstreamValueTableColumnOutputVariableInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
