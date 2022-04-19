package builder.visframe.function.variable.delegator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Predicate;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.input.recordwise.RecordwiseInputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import function.variable.input.InputVariable;
import metadata.MetadataID;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public class InputVariableBuilderDelegateFactory extends NodeBuilderFactoryBase<InputVariable, NonLeafNodeBuilderEmbeddedUIContentController<InputVariable>>{
	
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder; 
	
	private final Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	/**
	 * whether {@link NonRecordwiseInputVariable} types can be selected;
	 * note that {@link #allowingNonRecordwiseInputVariableTypes} and {@link #allowingRecordwiseInputVariableTypes} cannot both be false;
	 * 
	 */
	private final boolean allowingNonRecordwiseInputVariableTypes;
	
	/**
	 * whether allows to select ConstantValuedInputVariable type or not;
	 * must be NULL if {@link #allowingNonRecordwiseInputVariableTypes} is false; must be NON-NULL otherwise
	 * 
	 * when {@link #allowingNonRecordwiseInputVariableTypes} is true,
	 * 		{@link #allowingConstantValuedInputVariable} only be true for {@link AbstractStringProcessingEvaluatorBuilder} types; false for others;
	 * 
	 */
	private final Boolean allowingConstantValuedInputVariable;
	
	/**
	 * whether or not {@link RecordwiseInputVariable} types can be selected;
	 * note that {@link #allowingNonRecordwiseInputVariableTypes} and {@link #allowingRecordwiseInputVariableTypes} cannot both be false;
	 * 
	 */
	private final boolean allowingRecordwiseInputVariableTypes;
	
	/**
	 * condition for the target record data metadata that can be selected;
	 * only applicable for {@link RecordwiseInputVariableBuilder} types;
	 * 
	 * thus must be null if {@link #allowingRecordwiseInputVariableTypes} is false; non-null if true;
	 */
	private Predicate<MetadataID> targetRecordDataMetadataIDConditionConstraints;
//	/**
//	 * whether the owner record data should be the same with the owner record data of the host CompositionFunction or not;
//	 * only applicable for {@link RecordwiseInputVariable} types;
//	 * 
//	 * if {@link #allowingRecordwiseInputVariableTypes} is true, this must be non-null; otherwise, this field must be null;
//	 */
//	private final Boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @param hostComponentFunctionBuilder
	 * @param hostEvaluatorBuilder
	 * @param dataTypeConstraints
	 * @param allowingNonRecordwiseInputVariableTypes
	 * @param allowingConstantValuedInputVariable
	 * @param allowingRecordwiseInputVariableTypes
	 * @param targetRecordDataMetadataIDConditionConstraints
	 */
	public InputVariableBuilderDelegateFactory(
			String name, String description, boolean canBeNull,
			
			VisProjectDBContext hostVisProjectDBContext,
			CompositionFunctionBuilder hostCompositionFunctionBuilder,
			ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			
			boolean allowingNonRecordwiseInputVariableTypes,
			Boolean allowingConstantValuedInputVariable,
			boolean allowingRecordwiseInputVariableTypes,
			Predicate<MetadataID> targetRecordDataMetadataIDConditionConstraints) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
		this.dataTypeConstraints = dataTypeConstraints;
		
		this.allowingNonRecordwiseInputVariableTypes = allowingNonRecordwiseInputVariableTypes;
		this.allowingConstantValuedInputVariable = allowingConstantValuedInputVariable;
		this.allowingRecordwiseInputVariableTypes = allowingRecordwiseInputVariableTypes;
		this.targetRecordDataMetadataIDConditionConstraints = targetRecordDataMetadataIDConditionConstraints;
	}

	
	@Override
	public InputVariableBuilderDelegate build() throws SQLException, IOException {
		return new InputVariableBuilderDelegate(
				this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder(),
				
				this.hostEvaluatorBuilder,
				this.dataTypeConstraints,
				
				this.allowingNonRecordwiseInputVariableTypes,
				this.allowingConstantValuedInputVariable,
				this.allowingRecordwiseInputVariableTypes,
				this.targetRecordDataMetadataIDConditionConstraints
				);
	}
	
}
