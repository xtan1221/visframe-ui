package builder.visframe.function.evaluator.delegator;

import java.util.LinkedHashSet;
import java.util.Set;

import function.evaluator.CanBeUsedForPiecewiseFunctionConditionEvaluatorType;
import function.evaluator.Evaluator;
import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;
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
import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;

public final class EvaluatorTypes {
	private static Set<Class<? extends Evaluator>> allEvaluatorTypes;
	private static Set<Class<? extends Evaluator>> allEvaluatorTypesForPicewiseFunctionCondition;
	
	static {
		allEvaluatorTypes = new LinkedHashSet<>();
		
		//SimpleStringProcessingEvaluator
		allEvaluatorTypes.add(StringCaseChangeEvaluator.class);
		allEvaluatorTypes.add(StringConcatenationEvaluator.class);
		allEvaluatorTypes.add(StringContainsSubstringEvaluator.class);
		allEvaluatorTypes.add(StringEndsWithEvaluator.class);
		allEvaluatorTypes.add(StringEqualsEvaluator.class);
		allEvaluatorTypes.add(StringLengthEvaluator.class);
		allEvaluatorTypes.add(StringRemoveEmptySpaceEvaluator.class);
		allEvaluatorTypes.add(StringReplaceEvaluator.class);
		allEvaluatorTypes.add(StringSplitToTwoBySplitterStringEvaluator.class);
		allEvaluatorTypes.add(StringStartsWithEvaluator.class);
		allEvaluatorTypes.add(StringSubstringEvaluator.class);
		
		//RNGEvaluator
		allEvaluatorTypes.add(SimpleRNGEvaluator.class);
		
		//RecordwiseInputVariableIsNullValuedEvaluatorBuilder
		allEvaluatorTypes.add(RecordwiseInputVariableIsNullValuedEvaluator.class);
		
		
		//SymjaExpressionEvaluator
		allEvaluatorTypes.add(SymjaExpressionEvaluator.class);
		
		//SqlQueryBasedEvaluator
		allEvaluatorTypes.add(SimpleSQLQueryEvaluator.class);
	}
	

	/**
	 * get all types of Evaluator classes;
	 * for SimpleFunction evaluators
	 * @return
	 */
	public static Set<Class<? extends Evaluator>> getAllEvaluatorTypes(){
		return allEvaluatorTypes;
	}
	
	
	/**
	 * get all types of evaluator classes for PiecewiseFunction condition evaluators;
	 * 
	 * StringContainsSubstringEvaluator;
	 * StringEndsWithEvaluator;
	 * StringEqualsEvaluator;
	 * StringStartsWithEvaluator;
	 * SymjaExpressionEvaluator;
	 * SimpleSqlQueryEvaluator;
	 * 
	 * @return
	 */
	public static Set<Class<? extends Evaluator>> getAllEvaluatorTypesForPicewiseFunctionCondition(){
		if(allEvaluatorTypesForPicewiseFunctionCondition==null) {
			allEvaluatorTypesForPicewiseFunctionCondition = new LinkedHashSet<>();
			
			getAllEvaluatorTypes().forEach(e->{
				if(CanBeUsedForPiecewiseFunctionConditionEvaluatorType.class.isAssignableFrom(e))
					allEvaluatorTypesForPicewiseFunctionCondition.add(e);
			});
		}
		
		return allEvaluatorTypesForPicewiseFunctionCondition;
	}
	
	
}
