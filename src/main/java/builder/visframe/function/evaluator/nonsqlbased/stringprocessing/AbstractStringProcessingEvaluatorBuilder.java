package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.evaluator.nonsqlbased.NonSqlQueryBasedEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluator;

public abstract class AbstractStringProcessingEvaluatorBuilder<T extends StringProcessingEvaluator> extends NonSqlQueryBasedEvaluatorBuilder<T>{

	protected AbstractStringProcessingEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
	}
}
