package builder.visframe.function.variable.output;

import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.output.type.ValueTableColumnOutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class ValueTableColumnOutputVariableBuilder<T extends ValueTableColumnOutputVariable> extends OutputVariableBuilder<T>{
	
	protected ValueTableColumnOutputVariableBuilder(
			String name, String description, boolean canBeNull,	NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
	}

}
