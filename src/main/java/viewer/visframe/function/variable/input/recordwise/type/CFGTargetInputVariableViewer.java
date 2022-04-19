package viewer.visframe.function.variable.input.recordwise.type;

import function.variable.input.recordwise.type.CFGTargetInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.recordwise.RecordwiseInputVariableViewer;

public class CFGTargetInputVariableViewer extends RecordwiseInputVariableViewer<CFGTargetInputVariable, CFGTargetInputVariableViewerController>{

	public CFGTargetInputVariableViewer(
			CFGTargetInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, CFGTargetInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
