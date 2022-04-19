package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.FreeInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewer;

public class FreeInputVariableViewer extends NonRecordwiseInputVariableViewer<FreeInputVariable, FreeInputVariableViewerController>{
	
	public FreeInputVariableViewer(
			FreeInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, FreeInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
