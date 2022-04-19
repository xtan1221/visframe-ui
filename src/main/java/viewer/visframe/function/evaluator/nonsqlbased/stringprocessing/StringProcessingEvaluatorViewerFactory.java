package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing;

import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewerFactory;

public class StringProcessingEvaluatorViewerFactory {
	/**
	 * 
	 * @param cf
	 * @param hostCompositionFunctionViewer
	 * @return
	 */
	public static StringProcessingEvaluatorViewer<?,?> build(StringProcessingEvaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof SimpleStringProcessingEvaluator) {
			return SimpleStringProcessingEvaluatorViewerFactory.build(
					(SimpleStringProcessingEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized StringProcessingEvaluator type!");
		}
	}
}
