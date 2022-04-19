package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluator;
import function.variable.output.type.CFGTargetOutputVariable;

/**
 * 
 * @author tanxu
 *
 */
public final class StringSplitToTwoBySplitterStringEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringSplitToTwoBySplitterStringEvaluator>{
	
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
	public StringSplitToTwoBySplitterStringEvaluatorBuilder(
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
		
		if(this.splitterStringInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.splitterStringInputVariableBuilderDelegate.getInputVariableBuilder());
		
		return ret;
	}

	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(this.outputVariable1BuilderDelegate.getValueTableColumnOutputVariableBuilder()!=null)
			ret.add(this.outputVariable1BuilderDelegate.getValueTableColumnOutputVariableBuilder());
		
		if(this.outputVariable2BuilderDelegate.getValueTableColumnOutputVariableBuilder()!=null)
			ret.add(this.outputVariable2BuilderDelegate.getValueTableColumnOutputVariableBuilder());
		
		return ret;
	}
	///////////////////////////////////////////
	protected static final String targetInputVariable = "targetInputVariable";
	protected static final String targetInputVariable_description = "targetInputVariable";

	protected static final String splitterStringInputVariable = "splitterStringInputVariable";
	protected static final String splitterStringInputVariable_description = "splitterStringInputVariable";

	protected static final String outputVariable1 = "outputVariable1";
	protected static final String outputVariable1_description = "outputVariable1";
	
	protected static final String outputVariable2 = "outputVariable2";
	protected static final String outputVariable2_description = "outputVariable2";
	
	
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

	private InputVariableBuilderDelegate splitterStringInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			splitterStringInputVariable, splitterStringInputVariable_description, false, this,
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
	
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariable1BuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
			outputVariable1, outputVariable1_description, false, this, 
			this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
			e->{return e.isOfStringType();}
			);
	
	//must be of string type
	private ValueTableColumnOutputVariableBuilderDelegate outputVariable2BuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
			outputVariable2, outputVariable2_description, false, this, 
			this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
			e->{return e.isOfStringType();}
			);
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		this.addChildNodeBuilder(this.targetInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.splitterStringInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.outputVariable1BuilderDelegate);
		
		this.addChildNodeBuilder(this.outputVariable2BuilderDelegate);
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//targetInputVariable and splitterStringInputVariable should have different alias names;
		Set<String> set1 = new HashSet<>();
		set1.add(targetInputVariable);
		set1.add(splitterStringInputVariable);
		GenricChildrenNodeBuilderConstraint<StringSplitToTwoBySplitterStringEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "targetInputVariable and splitterStringInputVariable should have different alias names!",
				set1, 
				e->{
					StringSplitToTwoBySplitterStringEvaluatorBuilder builder = (StringSplitToTwoBySplitterStringEvaluatorBuilder)e;
					
					return !builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName().equals(builder.splitterStringInputVariableBuilderDelegate.getCurrentValue().getAliasName());
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//outputVariable1 must be of string type;
		Set<String> set2 = new HashSet<>();
		set2.add(outputVariable1);
		GenricChildrenNodeBuilderConstraint<StringSplitToTwoBySplitterStringEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "outputVariable1 must be of string type!",
				set2, 
				e->{
					StringSplitToTwoBySplitterStringEvaluatorBuilder builder = (StringSplitToTwoBySplitterStringEvaluatorBuilder)e;
					return builder.outputVariable1BuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//outputVariable2 must be of string type;
		Set<String> set3 = new HashSet<>();
		set3.add(outputVariable2);
		GenricChildrenNodeBuilderConstraint<StringSplitToTwoBySplitterStringEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "outputVariable2 must be of string type!",
				set3, 
				e->{
					StringSplitToTwoBySplitterStringEvaluatorBuilder builder = (StringSplitToTwoBySplitterStringEvaluatorBuilder)e;
					return builder.outputVariable2BuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		//outputVariable1 and outputVariable2 should have different alias names
		Set<String> set4 = new HashSet<>();
		set4.add(outputVariable1);
		set4.add(outputVariable2);
		GenricChildrenNodeBuilderConstraint<StringSplitToTwoBySplitterStringEvaluator> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "outputVariable1 and outputVariable2 should have different alias names!",
				set4, 
				e->{
					StringSplitToTwoBySplitterStringEvaluatorBuilder builder = (StringSplitToTwoBySplitterStringEvaluatorBuilder)e;
					
					return !builder.outputVariable1BuilderDelegate.getCurrentValue().getAliasName().equals(builder.outputVariable2BuilderDelegate.getCurrentValue().getAliasName());
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c4);
		
		//if outputVariable1 and outputVariable2 are both CFGTargetOutputVariable type, should be assigned to different CFGTargets;
		Set<String> set5 = new HashSet<>();
		set5.add(outputVariable1);
		set5.add(outputVariable2);
		GenricChildrenNodeBuilderConstraint<StringSplitToTwoBySplitterStringEvaluator> c5 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if outputVariable1 and outputVariable2 are both CFGTargetOutputVariable type, should be assigned to different CFGTargets!",
				set5, 
				e->{
					StringSplitToTwoBySplitterStringEvaluatorBuilder builder = (StringSplitToTwoBySplitterStringEvaluatorBuilder)e;
					
					if(builder.outputVariable1BuilderDelegate.getCurrentValue() instanceof CFGTargetOutputVariable && 
							builder.outputVariable2BuilderDelegate.getCurrentValue() instanceof CFGTargetOutputVariable) {
						CFGTargetOutputVariable ov1 = (CFGTargetOutputVariable)builder.outputVariable1BuilderDelegate.getCurrentValue();
						CFGTargetOutputVariable ov2 = (CFGTargetOutputVariable)builder.outputVariable2BuilderDelegate.getCurrentValue();
						
						return !ov1.getTargetName().equals(ov2.getTargetName());
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c5);
		
		
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
			this.splitterStringInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.outputVariable1BuilderDelegate.setValue(null, isEmpty);
			this.outputVariable2BuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringSplitToTwoBySplitterStringEvaluator stringSplitToTwoBySplitterStringEvaluator = (StringSplitToTwoBySplitterStringEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringSplitToTwoBySplitterStringEvaluator.getTargetInputVariable(), isEmpty);
				this.splitterStringInputVariableBuilderDelegate.setValue(stringSplitToTwoBySplitterStringEvaluator.getSplitterStringInputVariable(), isEmpty);
				this.outputVariable1BuilderDelegate.setValue(stringSplitToTwoBySplitterStringEvaluator.getOutputVariable1(), isEmpty);
				this.outputVariable2BuilderDelegate.setValue(stringSplitToTwoBySplitterStringEvaluator.getOutputVariable2(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringSplitToTwoBySplitterStringEvaluator build() {
		return new StringSplitToTwoBySplitterStringEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.splitterStringInputVariableBuilderDelegate.getCurrentValue(),
				this.outputVariable1BuilderDelegate.getCurrentValue(),
				this.outputVariable2BuilderDelegate.getCurrentValue()
				);
	}

	
}
