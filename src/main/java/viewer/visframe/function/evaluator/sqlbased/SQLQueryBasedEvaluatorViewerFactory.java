package viewer.visframe.function.evaluator.sqlbased;

import function.evaluator.sqlbased.SQLQueryBasedEvaluator;
import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class SQLQueryBasedEvaluatorViewerFactory {
	/**
	 * 
	 * @param cf
	 * @param hostCompositionFunctionViewer
	 * @return
	 */
	public static SQLQueryBasedEvaluatorViewerBase<?,?> build(SQLQueryBasedEvaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof SimpleSQLQueryEvaluator) {
			return new SimpleSQLQueryEvaluatorViewer((SimpleSQLQueryEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized SQLQueryBasedEvaluator type");
		}
	}
}
