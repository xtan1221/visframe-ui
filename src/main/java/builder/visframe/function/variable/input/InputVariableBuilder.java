package builder.visframe.function.variable.input;

import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.AbstractVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.input.InputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class InputVariableBuilder<T extends InputVariable> extends AbstractVariableBuilder<T>{
	
	protected InputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder, 
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
	}

}
