package builder.visframe.function.evaluator.nonsqlbased;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluator;

public abstract class NonSqlQueryBasedEvaluatorBuilder<T extends NonSQLQueryBasedEvaluator> extends AbstractEvaluatorBuilder<T>{

	protected NonSqlQueryBasedEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
	}

	
}
