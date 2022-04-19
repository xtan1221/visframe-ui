package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringLengthEvaluator;

/**
 * 
 * @author tanxu
 *
 */
public final class StringLengthEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringLengthEvaluator>{
	
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
	public StringLengthEvaluatorBuilder(
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
		
		if(!this.toTrimInputVariableBuilderDelegate.isSetToNull())
			if(this.toTrimInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
				ret.add(this.toTrimInputVariableBuilderDelegate.getInputVariableBuilder());
		
		
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

	protected static final String toTrimInputVariable = "toTrimInputVariable";
	protected static final String toTrimInputVariable_description = "toTrimInputVariable";

	protected static final String toTrimInputVariableByDefault = "toTrimInputVariableByDefault";
	protected static final String toTrimInputVariableByDefault_description = "toTrimInputVariableByDefault";
	
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
	//must be of boolean type
	private InputVariableBuilderDelegate toTrimInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			toTrimInputVariable, toTrimInputVariable_description, true, this, // can be null
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
	
	private BooleanTypeBuilder toTrimInputVariableByDefaultBuilder = new BooleanTypeBuilder(
			toTrimInputVariableByDefault, toTrimInputVariableByDefault_description, false, this);
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariableBuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
			outputVariable, outputVariable_description, false, this, 
			this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
			e->{return e.isGenericInt();}
			);
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		this.addChildNodeBuilder(this.targetInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toTrimInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.toTrimInputVariableByDefaultBuilder);
		
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//toTrimInputVariable must be of boolean type if not null
		Set<String> set1 = new HashSet<>();
		set1.add(toTrimInputVariable);
		GenricChildrenNodeBuilderConstraint<StringLengthEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "toTrimInputVariable must be of boolean type if not null!",
				set1, 
				e->{
					StringLengthEvaluatorBuilder builder = (StringLengthEvaluatorBuilder)e;
					
					if(builder.toTrimInputVariableBuilderDelegate.getCurrentStatus().hasValidValue())
						return builder.toTrimInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isBoolean();
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//selected targetInputVariable and toTrimInputVariable(if not null) must have different alias name; 
		Set<String> set2 = new HashSet<>();
		set2.add(targetInputVariable);
		set2.add(toTrimInputVariable);
		GenricChildrenNodeBuilderConstraint<StringLengthEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected targetInputVariable and toTrimInputVariable(if not null) must have different alias name!",
				set2, 
				e->{
					StringLengthEvaluatorBuilder builder = (StringLengthEvaluatorBuilder)e;
					
					if(builder.toTrimInputVariableBuilderDelegate.getCurrentStatus().hasValidValue())
						return !builder.toTrimInputVariableBuilderDelegate.getCurrentValue().getAliasName().equals(builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		
		
		//outputVariable must be of int type;
		Set<String> set3 = new HashSet<>();
		set3.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringLengthEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of integer type!",
				set3, 
				e->{
					StringLengthEvaluatorBuilder builder = (StringLengthEvaluatorBuilder)e;
					
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isGenericInt();
					
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
			this.toTrimInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toTrimInputVariableByDefaultBuilder.setValue(null, isEmpty);
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringLengthEvaluator stringLengthEvaluator = (StringLengthEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringLengthEvaluator.getTargetInputVariable(), isEmpty);
				this.toTrimInputVariableBuilderDelegate.setValue(stringLengthEvaluator.getToTrimInputVariable(), isEmpty);
				this.toTrimInputVariableByDefaultBuilder.setValue(stringLengthEvaluator.isToTrimInputVariableByDefault(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringLengthEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringLengthEvaluator build() {
		return new StringLengthEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.toTrimInputVariableBuilderDelegate.getCurrentValue(),
				this.toTrimInputVariableByDefaultBuilder.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

	
}
