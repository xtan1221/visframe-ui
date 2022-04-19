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
import function.evaluator.nonsqlbased.stringprocessing.StringEqualsEvaluator;
import function.variable.output.OutputVariable;

/**
 * 
 * @author tanxu
 * 
 */
public final class StringEqualsEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringEqualsEvaluator>{
	/**
	 * whether this {@link StringEqualsEvaluatorBuilder} is for the conditional evaluator of a {@link PiecewiseFunction};
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
	public StringEqualsEvaluatorBuilder(
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
		if(this.comparedInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.comparedInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(!this.toIgnoreCaseInputVariableBuilderDelegate.isSetToNull())
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
	
	protected static final String comparedInputVariable = "comparedInputVariable";
	protected static final String comparedInputVariable_description = "comparedInputVariable";
	
	protected static final String toIgnoreCaseInputVariable = "toIgnoreCaseInputVariable";
	protected static final String toIgnoreCaseInputVariable_description = "toIgnoreCaseInputVariable";
	
	protected static final String toIgnoreCaseByDefault = "toIgnoreCaseByDefault";
	protected static final String toIgnoreCaseByDefault_description = "toIgnoreCaseByDefault";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//////////////////////////
	private InputVariableBuilderDelegate targetInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			targetInputVariable, targetInputVariable_description, false, this,
			this,
			e->{return true;},
			true,//allowingNonRecordwiseInputVariableTypes
			true,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//			true, //boolean allowingConstantValuedInputVariable,
//			true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			);
	
	private InputVariableBuilderDelegate comparedInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			comparedInputVariable, comparedInputVariable_description, false, this,
			this,
			e->{return true;},
			true,//allowingNonRecordwiseInputVariableTypes
			true,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//			true, //boolean allowingConstantValuedInputVariable,
//			true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			);
	
	//must be of boolean type
	private InputVariableBuilderDelegate toIgnoreCaseInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			toIgnoreCaseInputVariable, toIgnoreCaseInputVariable_description, true, this, //can be null
			this,
			e->{return e.isBoolean();},
			true,//allowingNonRecordwiseInputVariableTypes
			true,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//			true, //boolean allowingConstantValuedInputVariable,
//			true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			);
	
	//toIgnoreCaseByDefault
	private BooleanTypeBuilder toIgnoreCaseByDefaultBuilder = new BooleanTypeBuilder(
			toIgnoreCaseByDefault, toIgnoreCaseByDefault_description, false, this);
	
	//must be of boolean type
	private NonLeafNodeBuilder<? extends OutputVariable> outputVariableBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		this.addChildNodeBuilder(this.targetInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.comparedInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toIgnoreCaseInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toIgnoreCaseByDefaultBuilder);
		
		
		if(this.isForPiecewiseFunctionCondition()) {
			outputVariableBuilder = new PFConditionEvaluatorBooleanOutputVariableBuilder(
					outputVariable, outputVariable_description, false, this,
					this, null,
					e->{return e.isBoolean();});
		}else {
			outputVariableBuilder = new ValueTableColumnOutputVariableBuilderDelegate(
					outputVariable, outputVariable_description, false, this, 
					this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
					e->{return e.isBoolean();}
					);
		}
		this.addChildNodeBuilder(this.outputVariableBuilder);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//selected targetInputVariableBuilder and comparedInputVariableBuilder and toIgnoreCaseInputVariableBuilder(if not null) must have different alias names;
		Set<String> set1 = new HashSet<>();
		set1.add(targetInputVariable);
		set1.add(comparedInputVariable);
		set1.add(toIgnoreCaseInputVariable);
		GenricChildrenNodeBuilderConstraint<StringEqualsEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected targetInputVariableBuilder and comparedInputVariable and toIgnoreCaseInputVariableBuilder(if not null) must have different alias names!",
				set1, 
				e->{
					StringEqualsEvaluatorBuilder builder = (StringEqualsEvaluatorBuilder)e;
					
					Set<SimpleName> aliasNameSet = new HashSet<>();
					//targetInputVariableBuilderDelegate
					aliasNameSet.add(builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					
					//comparedInputVariableBuilderDelegate
					if(aliasNameSet.contains(builder.comparedInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}else {
						aliasNameSet.add(builder.comparedInputVariableBuilderDelegate.getCurrentValue().getAliasName());
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
		GenricChildrenNodeBuilderConstraint<StringEqualsEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected toIgnoreCaseInputVariable (if toIgnoreCaseInputVariableBuilder is not set to null) must be of boolean type!",
				set2, 
				e->{
					StringEqualsEvaluatorBuilder builder = (StringEqualsEvaluatorBuilder)e;
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
		GenricChildrenNodeBuilderConstraint<StringEqualsEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of boolean type!",
				set3, 
				e->{
					StringEqualsEvaluatorBuilder builder = (StringEqualsEvaluatorBuilder)e;
					
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
			this.comparedInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toIgnoreCaseInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toIgnoreCaseByDefaultBuilder.setValue(null, isEmpty);
			this.outputVariableBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringEqualsEvaluator stringEqualsEvaluator = (StringEqualsEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringEqualsEvaluator.getTargetInputVariable(), isEmpty);
				this.comparedInputVariableBuilderDelegate.setValue(stringEqualsEvaluator.getComparedInputVariable(), isEmpty);
				this.toIgnoreCaseInputVariableBuilderDelegate.setValue(stringEqualsEvaluator.getToIgnoreCaseInputVariable(), isEmpty);
				this.toIgnoreCaseByDefaultBuilder.setValue(stringEqualsEvaluator.isToIgnoreCaseByDefault(), isEmpty);
				this.outputVariableBuilder.setValue(stringEqualsEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringEqualsEvaluator build() {
		return new StringEqualsEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.comparedInputVariableBuilderDelegate.getCurrentValue(),
				this.toIgnoreCaseInputVariableBuilderDelegate.getCurrentValue(),
				this.toIgnoreCaseByDefaultBuilder.getCurrentValue(),
				this.outputVariableBuilder.getCurrentValue()
				);
	}

	
}
