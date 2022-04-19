package viewer.visframe.function.evaluator.nonsqlbased.rng;

import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class RNGEvaluatorViewerFactory {
	
	/**
	 * 
	 * @param eval
	 * @param hostComponentFunctionViewer
	 * @return
	 */
	public static RNGEvaluatorViewer<?,?> build(RNGEvaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof SimpleRNGEvaluator) {
			return new SimpleRNGEvaluatorViewer((SimpleRNGEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized RNGEvaluator type");
		}
	}
}
