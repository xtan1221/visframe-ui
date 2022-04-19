package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple;

import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringCaseChangeEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringConcatenationEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringContainsSubstringEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringEndsWithEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringEqualsEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringLengthEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringStartsWithEvaluator;
import function.evaluator.nonsqlbased.stringprocessing.StringSubstringEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringCaseChangeEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringConcatenationEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringContainsSubstringEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringEndsWithEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringEqualsEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringLengthEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringRemoveEmptySpaceEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringReplaceEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringSplitToTwoBySplitterStringEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringStartsWithEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types.StringSubstringEvaluatorViewer;

public class SimpleStringProcessingEvaluatorViewerFactory {
	/**
	 * 
	 * @param eval
	 * @param hostComponentFunctionViewer
	 * @return
	 */
	public static SimpleStringProcessingEvaluatorViewer<?,?> build(SimpleStringProcessingEvaluator eval, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer){
		if(eval instanceof StringCaseChangeEvaluator) {
			return new StringCaseChangeEvaluatorViewer((StringCaseChangeEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringConcatenationEvaluator) {
			return new StringConcatenationEvaluatorViewer((StringConcatenationEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringContainsSubstringEvaluator) {
			return new StringContainsSubstringEvaluatorViewer((StringContainsSubstringEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringEndsWithEvaluator) {
			return new StringEndsWithEvaluatorViewer((StringEndsWithEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringEqualsEvaluator) {
			return new StringEqualsEvaluatorViewer((StringEqualsEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringLengthEvaluator) {
			return new StringLengthEvaluatorViewer((StringLengthEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringRemoveEmptySpaceEvaluator) {
			return new StringRemoveEmptySpaceEvaluatorViewer((StringRemoveEmptySpaceEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringReplaceEvaluator) {
			return new StringReplaceEvaluatorViewer((StringReplaceEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringSplitToTwoBySplitterStringEvaluator) {
			return new StringSplitToTwoBySplitterStringEvaluatorViewer((StringSplitToTwoBySplitterStringEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringStartsWithEvaluator) {
			return new StringStartsWithEvaluatorViewer((StringStartsWithEvaluator)eval, hostComponentFunctionViewer);
		}else if(eval instanceof StringSubstringEvaluator) {
			return new StringSubstringEvaluatorViewer((StringSubstringEvaluator)eval, hostComponentFunctionViewer);
		}else {
			throw new IllegalArgumentException("unrecognized SimpleStringProcessingEvaluator type!");
		}
	}
}
