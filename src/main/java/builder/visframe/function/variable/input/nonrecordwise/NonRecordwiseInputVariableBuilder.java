package builder.visframe.function.variable.input.nonrecordwise;

import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.input.nonrecordwise.NonRecordwiseInputVariable;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class NonRecordwiseInputVariableBuilder<T extends NonRecordwiseInputVariable> extends InputVariableBuilder<T>{
	
	protected NonRecordwiseInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
	}

}
