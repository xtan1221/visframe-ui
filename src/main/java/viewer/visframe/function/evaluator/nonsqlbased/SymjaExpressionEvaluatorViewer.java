package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class SymjaExpressionEvaluatorViewer extends NonSQLQueryBasedEvaluatorViewer<SymjaExpressionEvaluator, SymjaExpressionEvaluatorViewerController>{
	
	public SymjaExpressionEvaluatorViewer(
			SymjaExpressionEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, SymjaExpressionEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
