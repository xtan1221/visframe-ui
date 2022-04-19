package viewer.visframe.function.evaluator;

import function.evaluator.Evaluator;
import function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluator;
import function.evaluator.sqlbased.SQLQueryBasedEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluatorViewerFactory;
import viewer.visframe.function.evaluator.sqlbased.SQLQueryBasedEvaluatorViewerFactory;

public class EvaluatorViewerFactory {
	/**
	 * 
	 * @param eval
	 * @param hostComponentFunctionViewer
	 * @return
	 */
	public static EvaluatorViewer<?,?> build(Evaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof NonSQLQueryBasedEvaluator) {
			return NonSQLQueryBasedEvaluatorViewerFactory.build((NonSQLQueryBasedEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof SQLQueryBasedEvaluator) {
			return SQLQueryBasedEvaluatorViewerFactory.build((SQLQueryBasedEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized Evaluator type!");
		}
	}
}
