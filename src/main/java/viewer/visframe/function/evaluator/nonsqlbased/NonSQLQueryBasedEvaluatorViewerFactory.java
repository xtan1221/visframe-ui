package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluator;
import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.rng.RNGEvaluatorViewerFactory;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluatorViewerFactory;

public class NonSQLQueryBasedEvaluatorViewerFactory {
	/**
	 * 
	 * @param eval
	 * @param hostComponentFunctionViewer
	 * @return
	 */
	public static NonSQLQueryBasedEvaluatorViewer<?,?> build(NonSQLQueryBasedEvaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof RecordwiseInputVariableIsNullValuedEvaluator) {
			return new RecordwiseInputVariableIsNullValuedEvaluatorViewer((RecordwiseInputVariableIsNullValuedEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof RNGEvaluator) {
			return RNGEvaluatorViewerFactory.build((RNGEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringProcessingEvaluator) {
			return StringProcessingEvaluatorViewerFactory.build((StringProcessingEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof SymjaExpressionEvaluator) {
			return new SymjaExpressionEvaluatorViewer((SymjaExpressionEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized NonSQLQueryBasedEvaluator type!");
		}
	}
}
