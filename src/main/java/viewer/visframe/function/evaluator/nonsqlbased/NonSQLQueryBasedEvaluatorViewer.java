package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.EvaluatorViewer;

public abstract class NonSQLQueryBasedEvaluatorViewer<E extends NonSQLQueryBasedEvaluator, C extends NonSQLQueryBasedEvaluatorViewerController<E>> extends EvaluatorViewer<E, C>{
	
	protected NonSQLQueryBasedEvaluatorViewer(
			E value, String FXMLFileDirString,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
