package builder.visframe.function.variable.delegator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.evaluator.nonsqlbased.stringprocessing.ConstantValuedInputVariableBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.input.nonrecordwise.FreeInputVariableBuilder;
import builder.visframe.function.variable.input.nonrecordwise.SQLAggregateFunctionBasedInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.CFGTargetInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.RecordAttributeInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.RecordwiseInputVariableBuilder;
import builder.visframe.function.variable.input.recordwise.UpstreamValueTableColumnOutputVariableInputVariableBuilder;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import function.variable.input.InputVariable;
import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;
import function.variable.input.nonrecordwise.type.FreeInputVariable;
import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable;
import function.variable.input.recordwise.type.CFGTargetInputVariable;
import function.variable.input.recordwise.type.RecordAttributeInputVariable;
import function.variable.input.recordwise.type.UpstreamValueTableColumnOutputVariableInputVariable;
import metadata.MetadataID;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * delegate to a subtype of InputVariableBuilder whose type is selected and can be changed on the UI;
 * 
 * step 1. select an input variable type;
 * step 2. build an input variable of the selected type;
 * 
 * @author tanxu
 * 
 */
public final class InputVariableBuilderDelegate extends NonLeafNodeBuilder<InputVariable>{
	
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder; 
	
	
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
	
//	/**
//	 * whether the owner record data should be the same with the owner record data of the host CompositionFunction or not;
//	 * only applicable for {@link RecordwiseInputVariable} types;
//	 * 
//	 * if {@link #allowingRecordwiseInputVariableTypes} is true, this must be non-null; otherwise, this field must be null;
//	 */
//	private final Boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction;
	
	/**
	 * condition for the target record data metadata that can be selected;
	 * only applicable for {@link RecordwiseInputVariableBuilder} types;
	 * 
	 * thus must be null if {@link #allowingRecordwiseInputVariableTypes} is false; non-null if true;
	 */
	private Predicate<MetadataID> targetRecordDataMetadataIDConditionConstraints;
	/**
	 * condition constraint for allowed sql data type for this InputVariableBuilderDelegate
	 */
	private Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints;
	
	
	private SimpleName predefinedAliasName = null;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 * @param dataTypeConstraints
	 * @param allowingNonRecordwiseInputVariableTypes
	 * @param allowingConstantValuedInputVariable
	 * @param allowingRecordwiseInputVariableTypes
	 * @param targetRecordDataMetadataIDCondition must be null if {@link #allowingRecordwiseInputVariableTypes} is false; non-null if true;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public InputVariableBuilderDelegate(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			boolean allowingNonRecordwiseInputVariableTypes,
			Boolean allowingConstantValuedInputVariable,
			boolean allowingRecordwiseInputVariableTypes, 
			Predicate<MetadataID> targetRecordDataMetadataIDCondition
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);

		//validations
		if(allowingNonRecordwiseInputVariableTypes && allowingConstantValuedInputVariable==null)
			throw new IllegalArgumentException("allowingConstantValuedInputVariable cannot be null when allowingNonRecordwiseInputVariableTypes is true!");
		if(!allowingNonRecordwiseInputVariableTypes && allowingConstantValuedInputVariable!=null)
			throw new IllegalArgumentException("allowingConstantValuedInputVariable must be null when allowingNonRecordwiseInputVariableTypes is false!");
		
		if(allowingRecordwiseInputVariableTypes && targetRecordDataMetadataIDCondition==null)
			throw new IllegalArgumentException("targetRecordDataMetadataIDCondition cannot be null when allowingRecordwiseInputVariableTypes is true!");
		if(!allowingRecordwiseInputVariableTypes && targetRecordDataMetadataIDCondition!=null)
			throw new IllegalArgumentException("targetRecordDataMetadataIDCondition must be null when allowingRecordwiseInputVariableTypes is false!");
		
		
		if(!allowingNonRecordwiseInputVariableTypes && !allowingRecordwiseInputVariableTypes)
			throw new IllegalArgumentException("allowingConstantValuedInputVariable and allowingRecordwiseInputVariableTypes cannot both be false!");
		
		
		
		this.dataTypeConstraints = dataTypeConstraints;
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
		
		this.allowingNonRecordwiseInputVariableTypes = allowingNonRecordwiseInputVariableTypes;
		this.allowingConstantValuedInputVariable = allowingConstantValuedInputVariable;
		this.allowingRecordwiseInputVariableTypes = allowingRecordwiseInputVariableTypes;
		this.targetRecordDataMetadataIDConditionConstraints = targetRecordDataMetadataIDCondition;
		
		///////////////
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	protected VisProjectDBContext getHostVisProjectDBContext() {
		return this.getHostEvaluatorBuilder().getHostVisProjectDBContext();
	}
	
	/**
	 * @return the hostCompositionFunctionBuilder
	 */
	protected CompositionFunctionBuilder getHostCompositionFunctionBuilder() {
		return this.getHostEvaluatorBuilder().getHostCompositionFunctionBuilder();
	}
	
	/**
	 * @return the hostComponentFunctionBuilder
	 */
	protected ComponentFunctionBuilder<?, ?> getHostComponentFunctionBuilder() {
		return this.getHostEvaluatorBuilder().getHostComponentFunctionBuilder();
	}
	/**
	 * @return the hostEvaluatorBuilder
	 */
	protected AbstractEvaluatorBuilder<?> getHostEvaluatorBuilder() {
		return hostEvaluatorBuilder;
	}
	


	/**
	 * @return the allowingConstantValuedInputVariable
	 */
	protected Boolean isAllowingConstantValuedInputVariable() {
		return allowingConstantValuedInputVariable;
	}
	/**
	 * @return the allowingNonRecordwiseInputVariableTypes
	 */
	public boolean isAllowingNonRecordwiseInputVariableTypes() {
		return allowingNonRecordwiseInputVariableTypes;
	}

	/**
	 * @return the allowingRecordwiseInputVariableTypes
	 */
	public boolean isAllowingRecordwiseInputVariableTypes() {
		return allowingRecordwiseInputVariableTypes;
	}

	public Predicate<MetadataID> getTargetRecordDataMetadataIDConditionConstraints() {
		return targetRecordDataMetadataIDConditionConstraints;
	}

	
	/**
	 * set the data type constraints and re-initialize this InputVariableBuilderDelegate
	 * @return the dataTypeConstraints
	 */
	protected Predicate<VfDefinedPrimitiveSQLDataType> getDataTypeConstraints() {
		return dataTypeConstraints;
	}
	
	/**
	 * set the predefined alias name before the type is selected so that the selected type's predefined alias name will be properly set;
	 * @param predefinedAliasName the predefinedAliasName to set
	 */
	public void setPredefinedAliasName(SimpleName predefinedAliasName) {
		this.predefinedAliasName = predefinedAliasName;
	}
	
	/**
	 * @return the predefinedAliasName
	 */
	public SimpleName getPredefinedAliasName() {
		return predefinedAliasName;
	}
	
	/**
	 * 
	 * @param targetRecordDataMetadataIDCondition
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void resetTargetRecordDataMetadataIDCondition(Predicate<MetadataID> targetRecordDataMetadataIDCondition) throws SQLException, IOException {
		this.targetRecordDataMetadataIDConditionConstraints = targetRecordDataMetadataIDCondition;
		
		//
		this.removeAllChildrenNodeBuilders();
		
		
		//
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/**
	 * reset the {@link #dataTypeConstraints} with the given one;
	 * this will reset all the children node builders;
	 * 
	 * @param dataTypeConstraints the dataTypeConstraints to set
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void resetDataTypeConstraints(Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		this.dataTypeConstraints = dataTypeConstraints;
		
		//
		this.removeAllChildrenNodeBuilders();
		
		
		//
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	/////////////////
	private Set<Class<? extends InputVariable>> getTypeSet(){
		Set<Class<? extends InputVariable>> ret = new LinkedHashSet<>();
		
		if(this.isAllowingNonRecordwiseInputVariableTypes()) {
			ret.add(FreeInputVariable.class);
			ret.add(SQLAggregateFunctionBasedInputVariable.class);
			if(this.allowingConstantValuedInputVariable) {
				ret.add(ConstantValuedInputVariable.class);
			}
		}
		
		if(this.isAllowingRecordwiseInputVariableTypes()) {
			ret.add(CFGTargetInputVariable.class);
			ret.add(RecordAttributeInputVariable.class);
			ret.add(UpstreamValueTableColumnOutputVariableInputVariable.class);
		}
		
		return ret;
	}
	
	///////////////////
	protected static final String inputVariableType = "inputVariableType";
	protected static final String inputVariableType_description = "inputVariableType";
	
	protected static final String inputVariable = "inputVariable";
	protected static final String inputVariable_description = "inputVariable";
	
	
	private SimpleTypeSelector<Class<? extends InputVariable>> inputVariableTypeSelector;
	
	private InputVariableBuilder<?> inputVariableBuilder;
	
	
	/**
	 * @return the inputVariableTypeSelector
	 */
	public SimpleTypeSelector<Class<? extends InputVariable>> getInputVariableTypeSelector() {
		return inputVariableTypeSelector;
	}

	/**
	 * @return the inputVariableBuilder
	 */
	public InputVariableBuilder<?> getInputVariableBuilder() {
		return inputVariableBuilder;
	}

	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		
		//inputVariableType
		inputVariableTypeSelector = new SimpleTypeSelector<>(
				inputVariableType, inputVariableType_description, false, this,
				e->{return e.getSimpleName();},
				e->{return e.getSimpleName();}
				);
		this.inputVariableTypeSelector.setPool(this.getTypeSet());
		this.addChildNodeBuilder(this.inputVariableTypeSelector);
		
		
		//inputVariable
		//not initialized here, but initialized to the corresponding Variable builder after a valid type of Variable is selected with inputVariableTypeSelector; 
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		//when inputVariableTypeSelector's status is changed
		//inputVariableBuilder is set to the corresponding sub-type of AbstractVariableBuilder
		Runnable inputVariableTypeSelectorStatusChangeEventAction = ()->{
			try {
				if(inputVariableTypeSelector.getCurrentStatus().isDefaultEmpty()) {
					//need to remove the inputVariableBuilder if no type is selected;
					if(this.inputVariableBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.inputVariableBuilder.getName())) {
						
						this.removeChildNodeBuilder(this.inputVariableBuilder.getName());
						
					}
					this.inputVariableBuilder = null;
				}else if(inputVariableTypeSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					//remove it first
					if(this.inputVariableBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.inputVariableBuilder.getName())) {
						this.removeChildNodeBuilder(this.inputVariableBuilder.getName());
					}
					
					//create a corresponding builder to the selected type;
					if(this.inputVariableTypeSelector.getCurrentValue().equals(FreeInputVariable.class)) {
						this.inputVariableBuilder = new FreeInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
	//					//whenever the status of this FreeInputVariableBuilder changes, notify the host CompositionFunctionBuilder's update() method;
	//					//this is to update the original IndependentFreeInputVariableTypes in time for within- (Evaluator builder) IndependentFreeInputVariableType selection;
	//					this.inputVariableBuilder.addStatusChangeEventAction(()->{
	//						this.getHostCompositionFunctionBuilder().update();
	//					});
						
					}else if(this.inputVariableTypeSelector.getCurrentValue().equals(ConstantValuedInputVariable.class)) {
						this.inputVariableBuilder = new ConstantValuedInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
					}else if(this.inputVariableTypeSelector.getCurrentValue().equals(SQLAggregateFunctionBasedInputVariable.class)) {
						this.inputVariableBuilder = new SQLAggregateFunctionBasedInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
					}else if(this.inputVariableTypeSelector.getCurrentValue().equals(CFGTargetInputVariable.class)) {
						this.inputVariableBuilder = new CFGTargetInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints(),
								this.getTargetRecordDataMetadataIDConditionConstraints());
					}else if(this.inputVariableTypeSelector.getCurrentValue().equals(RecordAttributeInputVariable.class)) {
						this.inputVariableBuilder = new RecordAttributeInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints(),
								this.getTargetRecordDataMetadataIDConditionConstraints());
					}else if(this.inputVariableTypeSelector.getCurrentValue().equals(UpstreamValueTableColumnOutputVariableInputVariable.class)) {
						this.inputVariableBuilder = new UpstreamValueTableColumnOutputVariableInputVariableBuilder(
								inputVariable, inputVariable_description, false, this,
								this.getHostEvaluatorBuilder(), this.getPredefinedAliasName(), this.getDataTypeConstraints());
					}else {
						//
					}
					
					
					//add to parent node builder after the inputVariableBuilder is initialized again
					this.addChildNodeBuilder(this.inputVariableBuilder);
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		inputVariableTypeSelector.addStatusChangedAction(
				inputVariableTypeSelectorStatusChangeEventAction);
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.inputVariableTypeSelector.setValue(null, isEmpty);
			
			if(this.inputVariableBuilder!=null&&this.getChildrenNodeBuilderNameMap().containsKey(this.inputVariableBuilder.getName())) {
				this.removeChildNodeBuilder(this.inputVariableBuilder.getName());
			}
			
			this.inputVariableBuilder = null;
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				InputVariable inputVariable = (InputVariable)value;
				
				//this will trigger the action that create a corresponding subtype of variable builder and assign to inputVariableBuilder;
				this.inputVariableTypeSelector.setValue(inputVariable.getClass(), isEmpty);
				
				//
				this.inputVariableBuilder.setValue(inputVariable, isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected InputVariable build() {
		if(this.inputVariableBuilder==null)
			return null;
		
		return this.inputVariableBuilder.getCurrentValue();
	}
	
}
