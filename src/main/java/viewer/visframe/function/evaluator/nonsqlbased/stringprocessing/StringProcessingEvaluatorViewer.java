package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing;

import function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluatorViewer;

public abstract class StringProcessingEvaluatorViewer<E extends StringProcessingEvaluator, C extends StringProcessingEvaluatorViewerController<E>> extends NonSQLQueryBasedEvaluatorViewer<E, C>{
	
	protected StringProcessingEvaluatorViewer(E value, String FXMLFileDirString,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
