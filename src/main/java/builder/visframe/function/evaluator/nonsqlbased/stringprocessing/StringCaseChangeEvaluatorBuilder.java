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
import function.evaluator.nonsqlbased.stringprocessing.StringCaseChangeEvaluator;

/**
 * 
 * @author tanxu
 *
 */
public final class StringCaseChangeEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringCaseChangeEvaluator>{
	
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
	public StringCaseChangeEvaluatorBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder, int indexID) throws SQLException, IOException {
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
		if(this.caseChangeIndicatorInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.caseChangeIndicatorInputVariableBuilderDelegate.getInputVariableBuilder());
		
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
	
	protected static final String caseChangeIndicatorInputVariable = "caseChangeIndicatorInputVariable";
	protected static final String caseChangeIndicatorInputVariable_description = "caseChangeIndicatorInputVariable";

	protected static final String toUpperCaseWhenTrue = "toUpperCaseWhenTrue";
	protected static final String toUpperCaseWhenTrue_description = "toUpperCaseWhenTrue";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//////////////////////////
	private InputVariableBuilderDelegate targetInputVariableBuilderDelegate;
	
	//must be of boolean type
	private InputVariableBuilderDelegate caseChangeIndicatorInputVariableBuilderDelegate;
	
	private BooleanTypeBuilder toUpperCaseWhenTrueBuilder;
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariableBuilderDelegate;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//set data type constraints;
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
		caseChangeIndicatorInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				caseChangeIndicatorInputVariable, caseChangeIndicatorInputVariable, false, this,
				this,
				e->{return e.isBoolean();},//must be of boolean type
				true,//allowingNonRecordwiseInputVariableTypes
				true,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				true, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.caseChangeIndicatorInputVariableBuilderDelegate);
		
		//
		toUpperCaseWhenTrueBuilder = new BooleanTypeBuilder(
				toUpperCaseWhenTrue, toUpperCaseWhenTrue_description, false, this);
		this.addChildNodeBuilder(this.toUpperCaseWhenTrueBuilder);
		
		
		//
		outputVariableBuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
				outputVariable, outputVariable_description, false, this, 
				
				this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
				e->{return e.isOfStringType();}//must be of string type
				);
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//selected caseChangeIndicatorInputVariable must be of boolean type;
		Set<String> set1 = new HashSet<>();
		set1.add(caseChangeIndicatorInputVariable);
		GenricChildrenNodeBuilderConstraint<StringCaseChangeEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected caseChangeIndicatorInputVariable must be of boolean type!",
				set1, 
				e->{
					StringCaseChangeEvaluatorBuilder builder = (StringCaseChangeEvaluatorBuilder)e;
					return builder.caseChangeIndicatorInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isBoolean();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//selected targetInputVariable and caseChangeIndicatorInputVariable must have different alias name; 
		Set<String> set2 = new HashSet<>();
		set2.add(targetInputVariable);
		set2.add(caseChangeIndicatorInputVariable);
		GenricChildrenNodeBuilderConstraint<StringCaseChangeEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected targetInputVariable and caseChangeIndicatorInputVariable must have different alias name!",
				set2, 
				e->{
					StringCaseChangeEvaluatorBuilder builder = (StringCaseChangeEvaluatorBuilder)e;
					return !builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName()
							.equals(builder.caseChangeIndicatorInputVariableBuilderDelegate.getCurrentValue().getAliasName());
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//selected outputVariable must be of string type;
		Set<String> set3 = new HashSet<>();
		set3.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringCaseChangeEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of string type!",
				set3, 
				e->{
					StringCaseChangeEvaluatorBuilder builder = (StringCaseChangeEvaluatorBuilder)e;
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
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
			this.caseChangeIndicatorInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.toUpperCaseWhenTrueBuilder.setValue(null, isEmpty);
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringCaseChangeEvaluator stringCaseChangeEvaluator = (StringCaseChangeEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringCaseChangeEvaluator.getTargetInputVariable(), isEmpty);
				this.caseChangeIndicatorInputVariableBuilderDelegate.setValue(stringCaseChangeEvaluator.getCaseChangeIndicatorInputVariable(), isEmpty);
				this.toUpperCaseWhenTrueBuilder.setValue(stringCaseChangeEvaluator.isToUpperCaseWhenTrue(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringCaseChangeEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringCaseChangeEvaluator build() {
		return new StringCaseChangeEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.caseChangeIndicatorInputVariableBuilderDelegate.getCurrentValue(),
				this.toUpperCaseWhenTrueBuilder.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

}
