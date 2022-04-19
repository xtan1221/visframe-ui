package viewer.visframe.function.variable.output.type;

import function.variable.output.type.CFGTargetOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewer;

public class CFGTargetOutputVariableViewer extends ValueTableColumnOutputVariableViewer<CFGTargetOutputVariable, CFGTargetOutputVariableViewerController>{

	public CFGTargetOutputVariableViewer(
			CFGTargetOutputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, CFGTargetOutputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
