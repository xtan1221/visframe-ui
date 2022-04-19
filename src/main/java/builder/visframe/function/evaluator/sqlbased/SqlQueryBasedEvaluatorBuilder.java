package builder.visframe.function.evaluator.sqlbased;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.sqlbased.SQLQueryBasedEvaluator;

public abstract class SqlQueryBasedEvaluatorBuilder<T extends SQLQueryBasedEvaluator> extends AbstractEvaluatorBuilder<T>{
	
	protected SqlQueryBasedEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
	}

	
}
