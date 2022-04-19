package viewer.visframe.function.evaluator.sqlbased;

import function.evaluator.sqlbased.SQLQueryBasedEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.EvaluatorViewer;

public abstract class SQLQueryBasedEvaluatorViewerBase<E extends SQLQueryBasedEvaluator, C extends SQLQueryBasedEvaluatorViewerControllerBase<E>> extends EvaluatorViewer<E, C>{
	
	protected SQLQueryBasedEvaluatorViewerBase(
			E value, String FXMLFileDirString,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
