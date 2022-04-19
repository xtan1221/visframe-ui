package viewer.visframe.function.evaluator.nonsqlbased.rng;

import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluatorViewer;

public abstract class RNGEvaluatorViewer<E extends RNGEvaluator, C extends RNGEvaluatorViewerController<E>> extends NonSQLQueryBasedEvaluatorViewer<E, C>{
	
	protected RNGEvaluatorViewer(E value, String FXMLFileDirString,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
