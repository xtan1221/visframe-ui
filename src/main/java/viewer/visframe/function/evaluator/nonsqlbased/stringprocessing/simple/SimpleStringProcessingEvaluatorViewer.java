package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple;

import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluatorViewerController;

public abstract class SimpleStringProcessingEvaluatorViewer<E extends SimpleStringProcessingEvaluator, C extends StringProcessingEvaluatorViewerController<E>> extends StringProcessingEvaluatorViewer<E, C>{
	
	protected SimpleStringProcessingEvaluatorViewer(E value, String FXMLFileDirString,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
