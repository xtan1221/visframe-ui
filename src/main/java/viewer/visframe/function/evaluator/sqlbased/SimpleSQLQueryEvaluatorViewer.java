package viewer.visframe.function.evaluator.sqlbased;

import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class SimpleSQLQueryEvaluatorViewer extends SQLQueryBasedEvaluatorViewerBase<SimpleSQLQueryEvaluator, SimpleSQLQueryEvaluatorViewerController>{
	
	/**
	 * 
	 * @param value
	 * @param hostComponentFunctionViewer
	 */
	public SimpleSQLQueryEvaluatorViewer(
			SimpleSQLQueryEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, SimpleSQLQueryEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
	
}
