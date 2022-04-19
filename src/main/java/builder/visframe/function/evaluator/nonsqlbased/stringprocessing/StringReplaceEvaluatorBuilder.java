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
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluator;
import function.variable.input.nonrecordwise.type.ConstantValuedInputVariable;

/**
 * 
 * @author tanxu
 *
 */
public final class StringReplaceEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringReplaceEvaluator>{
	
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
	public StringReplaceEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	//////////////////////////////////
	@Override
	public Set<InputVariableBuilder<?>> getInputVariableBuilderSet() {
		Set<InputVariableBuilder<?>> ret = new LinkedHashSet<>();
		if(this.targetInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.targetInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(this.targetSubstringInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.targetSubstringInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(this.replacingStringInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.replacingStringInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(!this.toReplaceAllInputVariableBuilderDelegate.isSetToNull())
			if(this.toReplaceAllInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
				ret.add(this.toReplaceAllInputVariableBuilderDelegate.getInputVariableBuilder());
		
		return ret;
	}

	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder()!=null)
			ret.add(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder());
		
		return ret;
	}
	///////////////////////////////////////////
	protected static final String targetInputVariable = "targetInputVariable";
	protected static final String targetInputVariable_description = "targetInputVariable";

	protected static final String targetSubstringInputVariable = "targetSubstringInputVariable";
	protected static final String targetSubstringInputVariable_description = "targetSubstringInputVariable";

	protected static final String replacingStringInputVariable = "replacingStringInputVariable";
	protected static final String replacingStringInputVariable_description = "replacingStringInputVariable";
	
	protected static final String toReplaceAllInputVariable = "toReplaceAllInputVariable";
	protected static final String toReplaceAllInputVariable_description = "toReplaceAllInputVariable";
	
	protected static final String toReplaceAllByDefault = "toReplaceAllByDefault";
	protected static final String toReplaceAllByDefault_description = "toReplaceAllByDefault";
	
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

	private InputVariableBuilderDelegate targetSubstringInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			targetSubstringInputVariable, targetSubstringInputVariable_description, false, this,
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
	
	private InputVariableBuilderDelegate replacingStringInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			replacingStringInputVariable, replacingStringInputVariable_description, false, this,
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
	
	private InputVariableBuilderDelegate toReplaceAllInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			toReplaceAllInputVariable, toReplaceAllInputVariable_description, true, this, //can be null
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
	
	private BooleanTypeBuilder toReplaceAllByDefaultBuilder = new BooleanTypeBuilder(
			toReplaceAllByDefault, toReplaceAllByDefault_description, false, this);
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariableBuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
			outputVariable, outputVariable_description, false, this, 
			this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
			e->{return e.isOfStringType();}
			);
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		this.addChildNodeBuilder(this.targetInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.targetSubstringInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.replacingStringInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toReplaceAllInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toReplaceAllByDefaultBuilder);
		
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//toReplaceAllInputVariable must be of boolean type if not null;
		Set<String> set1 = new HashSet<>();
		set1.add(toReplaceAllInputVariable);
		GenricChildrenNodeBuilderConstraint<StringReplaceEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "toReplaceAllInputVariable must be of boolean type if not null!",
				set1, 
				e->{
					StringReplaceEvaluatorBuilder builder = (StringReplaceEvaluatorBuilder)e;
					
					if(builder.toReplaceAllInputVariableBuilderDelegate.getCurrentStatus().hasValidValue())
						return builder.toReplaceAllInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isBoolean();
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//ConstantValuedInputVariable type targetSubstringInputVariable cannot have empty string value
		Set<String> set2 = new HashSet<>();
		set2.add(targetSubstringInputVariable);
		GenricChildrenNodeBuilderConstraint<StringReplaceEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "ConstantValuedInputVariable type targetSubstringInputVariable cannot have empty string value!",
				set2, 
				e->{
					StringReplaceEvaluatorBuilder builder = (StringReplaceEvaluatorBuilder)e;
					
					if(builder.targetSubstringInputVariableBuilderDelegate.getCurrentValue() instanceof ConstantValuedInputVariable) {
						ConstantValuedInputVariable cviv = (ConstantValuedInputVariable) builder.targetInputVariableBuilderDelegate.getCurrentValue();
						return !cviv.getValueString().isEmpty();
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);

		
		//input variables must have different alias names; 
		Set<String> set3 = new HashSet<>();
		set3.add(targetInputVariable);
		set3.add(targetSubstringInputVariable);
		set3.add(replacingStringInputVariable);
		set3.add(toReplaceAllInputVariable);
		GenricChildrenNodeBuilderConstraint<StringReplaceEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "input variables must have different alias names!",
				set3, 
				e->{
					StringReplaceEvaluatorBuilder builder = (StringReplaceEvaluatorBuilder)e;
					
					Set<SimpleName> aliasNameSet = new HashSet<>();
					//targetInputVariableBuilderDelegate
					aliasNameSet.add(builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					//targetSubstringInputVariableBuilderDelegate
					if(aliasNameSet.contains(builder.targetSubstringInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}else {
						aliasNameSet.add(builder.targetSubstringInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}
					//replacingStringInputVariableBuilderDelegate
					if(aliasNameSet.contains(builder.replacingStringInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}else {
						aliasNameSet.add(builder.replacingStringInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}
					
					//toReplaceAllInputVariableBuilderDelegate if not null
					if(builder.toReplaceAllInputVariableBuilderDelegate.getCurrentStatus().hasValidValue()) {
						return !aliasNameSet.contains(builder.toReplaceAllInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		//selected outputVariable must be of string type;
		Set<String> set4 = new HashSet<>();
		set4.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringReplaceEvaluator> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of string type!",
				set4, 
				e->{
					StringReplaceEvaluatorBuilder builder = (StringReplaceEvaluatorBuilder)e;
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
				});
		this.addGenricChildrenNodeBuilderConstraint(c4);
		
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
			this.targetSubstringInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.replacingStringInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toReplaceAllInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toReplaceAllByDefaultBuilder.setValue(null, isEmpty);
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringReplaceEvaluator stringReplaceEvaluator = (StringReplaceEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringReplaceEvaluator.getTargetInputVariable(), isEmpty);
				this.targetSubstringInputVariableBuilderDelegate.setValue(stringReplaceEvaluator.getTargetSubstringInputVariable(), isEmpty);
				this.replacingStringInputVariableBuilderDelegate.setValue(stringReplaceEvaluator.getReplacingStringInputVariable(), isEmpty);
				this.toReplaceAllInputVariableBuilderDelegate.setValue(stringReplaceEvaluator.getToReplaceAllInputVariable(), isEmpty);
				this.toReplaceAllByDefaultBuilder.setValue(stringReplaceEvaluator.isToReplaceAllByDefault(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringReplaceEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringReplaceEvaluator build() {
		return new StringReplaceEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.targetSubstringInputVariableBuilderDelegate.getCurrentValue(),
				this.replacingStringInputVariableBuilderDelegate.getCurrentValue(),
				this.toReplaceAllInputVariableBuilderDelegate.getCurrentValue(),
				this.toReplaceAllByDefaultBuilder.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

	
}
