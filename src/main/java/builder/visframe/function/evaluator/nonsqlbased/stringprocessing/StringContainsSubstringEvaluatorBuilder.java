package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import basic.SimpleName;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import builder.visframe.function.variable.output.PFConditionEvaluatorBooleanOutputVariableBuilder;
import builder.visframe.function.variable.output.ValueTableColumnOutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringContainsSubstringEvaluator;
import function.variable.output.OutputVariable;

/**
 * 
 * @author tanxu
 * 
 */
public final class StringContainsSubstringEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringContainsSubstringEvaluator>{
	/**
	 * whether this {@link StringContainsSubstringEvaluatorBuilder} is for the conditional evaluator of a {@link PiecewiseFunction};
	 * 
	 * if true, the {@link #outputVariableBuilder} should be of type {@link PFConditionEvaluatorBooleanOutputVariableBuilder} 
	 * else, {@link #outputVariableBuilder} should be of type {@link ValueTableColumnOutputVariableBuilder} 
	 */
	private final boolean forPiecewiseFunctionCondition;
	
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public StringContainsSubstringEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder, int indexID,
			
			boolean forPiecewiseFunctionCondition) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
		this.forPiecewiseFunctionCondition = forPiecewiseFunctionCondition;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	public boolean isForPiecewiseFunctionCondition() {
		return forPiecewiseFunctionCondition;
	}
	
	//////////////////////////////////
	@Override
	public Set<InputVariableBuilder<?>> getInputVariableBuilderSet() {
		Set<InputVariableBuilder<?>> ret = new LinkedHashSet<>();
		if(this.targetInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.targetInputVariableBuilderDelegate.getInputVariableBuilder());
		if(this.substringInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.substringInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(!toIgnoreCaseInputVariableBuilderDelegate.isSetToNull())
			if(this.toIgnoreCaseInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
				ret.add(this.toIgnoreCaseInputVariableBuilderDelegate.getInputVariableBuilder());
		
		return ret;
	}

	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(this.isForPiecewiseFunctionCondition()) {
			ret.add((PFConditionEvaluatorBooleanOutputVariableBuilder)this.outputVariableBuilder);
		}else {
			ValueTableColumnOutputVariableBuilderDelegate builderDelegate = (ValueTableColumnOutputVariableBuilderDelegate)this.outputVariableBuilder;
			if(builderDelegate.getValueTableColumnOutputVariableBuilder()!=null) {
				ret.add(builderDelegate.getValueTableColumnOutputVariableBuilder());
			}
		}
		
		
		return ret;
	}
	///////////////////////////////////////////
	protected static final String targetInputVariable = "targetInputVariable";
	protected static final String targetInputVariable_description = "targetInputVariable";
	
	protected static final String substringInputVariable = "substringInputVariable";
	protected static final String substringInputVariable_description = "substringInputVariable";
	
	protected static final String toIgnoreCaseInputVariable = "toIgnoreCaseInputVariable";
	protected static final String toIgnoreCaseInputVariable_description = "toIgnoreCaseInputVariable";
	
	protected static final String toIgnoreCaseByDefault = "toIgnoreCaseByDefault";
	protected static final String toIgnoreCaseByDefault_description = "toIgnoreCaseByDefault";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//////////////////////////
	private InputVariableBuilderDelegate targetInputVariableBuilderDelegate;
	
	private InputVariableBuilderDelegate substringInputVariableBuilderDelegate;
	
	//must be of boolean type
	private InputVariableBuilderDelegate toIgnoreCaseInputVariableBuilderDelegate;
	
	//toIgnoreCaseByDefault
	private BooleanTypeBuilder toIgnoreCaseByDefaultBuilder;
	
	//must be of boolean type
	private NonLeafNodeBuilder<? extends OutputVariable> outputVariableBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		//
		targetInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				targetInputVariable, targetInputVariable_description, false, this,
				this,
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.targetInputVariableBuilderDelegate);
		//
		substringInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				substringInputVariable, substringInputVariable_description, false, this,
				this,
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.substringInputVariableBuilderDelegate);
		
		//
		toIgnoreCaseInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				toIgnoreCaseInputVariable, toIgnoreCaseInputVariable_description, true, this, //can be null
				this,
				e->{return e.isBoolean();},
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.toIgnoreCaseInputVariableBuilderDelegate);
		
		//
		toIgnoreCaseByDefaultBuilder = new BooleanTypeBuilder(
				toIgnoreCaseByDefault, toIgnoreCaseByDefault_description, false, this);
		this.addChildNodeBuilder(this.toIgnoreCaseByDefaultBuilder);
		
		
		//
		if(this.isForPiecewiseFunctionCondition()) {
			outputVariableBuilder = new PFConditionEvaluatorBooleanOutputVariableBuilder(
					outputVariable, outputVariable_description, false, this,
					this, null,
					e->{return e.isBoolean();});
		}else {
			outputVariableBuilder = new ValueTableColumnOutputVariableBuilderDelegate(
					outputVariable, outputVariable_description, false, this, 
					this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
					e->{return e.isBoolean();});
		}
		
		this.addChildNodeBuilder(this.outputVariableBuilder);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//selected targetInputVariableBuilder and substringInputVariableBuilder and toIgnoreCaseInputVariableBuilder must have different alias names;
		Set<String> set1 = new HashSet<>();
		set1.add(targetInputVariable);
		set1.add(substringInputVariable);
		set1.add(toIgnoreCaseInputVariable);
		GenricChildrenNodeBuilderConstraint<StringContainsSubstringEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected targetInputVariableBuilder and substringInputVariableBuilder and toIgnoreCaseInputVariableBuilder must have different alias names!",
				set1, 
				e->{
					StringContainsSubstringEvaluatorBuilder builder = (StringContainsSubstringEvaluatorBuilder)e;
					
					Set<SimpleName> aliasNameSet = new HashSet<>();
					//targetInputVariableBuilderDelegate
					aliasNameSet.add(builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					
					//substringInputVariableBuilderDelegate
					if(aliasNameSet.contains(builder.substringInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}else {
						aliasNameSet.add(builder.substringInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}
					
					//toIgnoreCaseInputVariableBuilderDelegate
					if(builder.toIgnoreCaseInputVariableBuilderDelegate.getCurrentStatus().hasValidValue()) {
						return !aliasNameSet.contains(builder.toIgnoreCaseInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}else {
						return true;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//selected toIgnoreCaseInputVariable (if toIgnoreCaseInputVariableBuilder is not set to null) must be of boolean type; 
		Set<String> set2 = new HashSet<>();
		set2.add(toIgnoreCaseInputVariable);
		GenricChildrenNodeBuilderConstraint<StringContainsSubstringEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected toIgnoreCaseInputVariable (if toIgnoreCaseInputVariableBuilder is not set to null) must be of boolean type!",
				set2, 
				e->{
					StringContainsSubstringEvaluatorBuilder builder = (StringContainsSubstringEvaluatorBuilder)e;
					if(builder.toIgnoreCaseInputVariableBuilderDelegate.getCurrentStatus().hasValidValue()) {
						return builder.toIgnoreCaseInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isBoolean();
					}else {
						return true;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		
		//selected outputVariable must be of boolean type;
		Set<String> set3 = new HashSet<>();
		set3.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringContainsSubstringEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of boolean type!",
				set3, 
				e->{
					StringContainsSubstringEvaluatorBuilder builder = (StringContainsSubstringEvaluatorBuilder)e;
					
					return builder.outputVariableBuilder.getCurrentValue().getSQLDataType().isBoolean();
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
	
	}
	

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		// TODO Auto-generated method stub
		
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.targetInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.substringInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toIgnoreCaseInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toIgnoreCaseByDefaultBuilder.setValue(null, isEmpty);
			this.outputVariableBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringContainsSubstringEvaluator stringContainsSubstringEvaluator = (StringContainsSubstringEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringContainsSubstringEvaluator.getTargetInputVariable(), isEmpty);
				this.substringInputVariableBuilderDelegate.setValue(stringContainsSubstringEvaluator.getSubstringInputVariable(), isEmpty);
				this.toIgnoreCaseInputVariableBuilderDelegate.setValue(stringContainsSubstringEvaluator.getToIgnoreCaseInputVariable(), isEmpty);
				this.toIgnoreCaseByDefaultBuilder.setValue(stringContainsSubstringEvaluator.isToIgnoreCaseByDefault(), isEmpty);
				this.outputVariableBuilder.setValue(stringContainsSubstringEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringContainsSubstringEvaluator build() {
		return new StringContainsSubstringEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.substringInputVariableBuilderDelegate.getCurrentValue(),
				this.toIgnoreCaseInputVariableBuilderDelegate.getCurrentValue(),
				this.toIgnoreCaseByDefaultBuilder.getCurrentValue(),
				this.outputVariableBuilder.getCurrentValue()
				);
	}

	
}
