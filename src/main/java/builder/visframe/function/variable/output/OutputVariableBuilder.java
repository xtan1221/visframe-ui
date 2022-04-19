package builder.visframe.function.variable.output;

import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.AbstractVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.output.OutputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class OutputVariableBuilder<T extends OutputVariable> extends AbstractVariableBuilder<T>{
	
	protected OutputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
	}
	
}
