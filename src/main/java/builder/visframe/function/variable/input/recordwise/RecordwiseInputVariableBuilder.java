package builder.visframe.function.variable.input.recordwise;

import java.util.function.Predicate;

import basic.SimpleName;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.variable.input.recordwise.RecordwiseInputVariable;
import metadata.MetadataID;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public abstract class RecordwiseInputVariableBuilder<T extends RecordwiseInputVariable> extends InputVariableBuilder<T>{
	
//	/**
//	 * whether or not the owner record of the built RecordwiseInputVariable must be the same with the owner record data of the host CompositionFunction;
//	 * 
//	 * for RecordwiseInputVariable of evaluator of type {@link SqlQueryBasedEvaluator}, this value should be false;
//	 * otherwise, this value should be set to true;
//	 */
//	private final boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction;
	
	
	/**
	 * condition for the target record data metadata that can be selected for this {@link RecordwiseInputVariableBuilder};
	 */
	private final Predicate<MetadataID> targetRecordDataMetadataIDCondition;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 * @param predefinedAliasName
	 * @param dataTypeConstraints
	 * @param targetRecordDataMetadataIDCondition
	 */
	protected RecordwiseInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			Predicate<MetadataID> targetRecordDataMetadataIDCondition
			) {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
		
		this.targetRecordDataMetadataIDCondition = targetRecordDataMetadataIDCondition;
	}
	

	
	public Predicate<MetadataID> getTargetRecordDataMetadataIDCondition() {
		return targetRecordDataMetadataIDCondition;
	}
	
}
