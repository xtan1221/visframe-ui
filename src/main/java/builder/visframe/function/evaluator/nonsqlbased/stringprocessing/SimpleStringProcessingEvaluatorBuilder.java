package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import builder.visframe.function.component.ComponentFunctionBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;

public abstract class SimpleStringProcessingEvaluatorBuilder<T extends SimpleStringProcessingEvaluator> extends AbstractStringProcessingEvaluatorBuilder<T>{

	protected SimpleStringProcessingEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
	}


}
