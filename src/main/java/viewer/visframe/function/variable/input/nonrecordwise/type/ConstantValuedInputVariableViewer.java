package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewer;

public class ConstantValuedInputVariableViewer extends NonRecordwiseInputVariableViewer<ConstantValuedInputVariable, ConstantValuedInputVariableViewerController>{

	
	
	/**
	 * 
	 * @param value
	 * @param hostEvaluatorViewer
	 */
	public ConstantValuedInputVariableViewer(
			ConstantValuedInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, ConstantValuedInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
