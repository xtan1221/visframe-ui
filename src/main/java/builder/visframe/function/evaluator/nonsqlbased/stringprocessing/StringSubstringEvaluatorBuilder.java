package builder.visframe.function.evaluator.nonsqlbased.stringprocessing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import basic.SimpleName;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.stringprocessing.StringSubstringEvaluator;

/**
 * 
 * @author tanxu
 *
 */
public final class StringSubstringEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringSubstringEvaluator>{
	
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
	public StringSubstringEvaluatorBuilder(
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
		
		if(this.startPosInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.startPosInputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(this.lengthInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.lengthInputVariableBuilderDelegate.getInputVariableBuilder());
		
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

	protected static final String startPosInputVariable = "startPosInputVariable";
	protected static final String startPosInputVariable_description = "startPosInputVariable";

	protected static final String lengthInputVariable = "lengthInputVariable";
	protected static final String lengthInputVariable_description = "lengthInputVariable";
	
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
	//must be of int type
	private InputVariableBuilderDelegate startPosInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			startPosInputVariable, startPosInputVariable_description, false, this,
			this,
			e->{return e.isGenericInt();},
			true,//allowingNonRecordwiseInputVariableTypes
			true,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//			true, //boolean allowingConstantValuedInputVariable,
//			true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			);
	
	//must be of int type
	private InputVariableBuilderDelegate lengthInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			lengthInputVariable, lengthInputVariable_description, false, this,
			this,
			e->{return e.isGenericInt();},
			true,//allowingNonRecordwiseInputVariableTypes
			true,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//			true, //boolean allowingConstantValuedInputVariable,
//			true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			);
	
	
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
		
		this.addChildNodeBuilder(this.startPosInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.lengthInputVariableBuilderDelegate);
		
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//startPosInputVariable must be of integer type;
		Set<String> set1 = new HashSet<>();
		set1.add(startPosInputVariable);
		GenricChildrenNodeBuilderConstraint<StringSubstringEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "startPosInputVariable must be of integer type!",
				set1, 
				e->{
					StringSubstringEvaluatorBuilder builder = (StringSubstringEvaluatorBuilder)e;
					return builder.startPosInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isGenericInt();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//lengthInputVariable must be of integer type
		Set<String> set2 = new HashSet<>();
		set2.add(lengthInputVariable);
		GenricChildrenNodeBuilderConstraint<StringSubstringEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "lengthInputVariable must be of integer type!",
				set2, 
				e->{
					StringSubstringEvaluatorBuilder builder = (StringSubstringEvaluatorBuilder)e;
					return builder.lengthInputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isGenericInt();
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//input variables must have different alias name; 
		Set<String> set3 = new HashSet<>();
		set3.add(targetInputVariable);
		set3.add(startPosInputVariable);
		set3.add(lengthInputVariable);
		GenricChildrenNodeBuilderConstraint<StringSubstringEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "input variables must have different alias names!",
				set3, 
				e->{
					StringSubstringEvaluatorBuilder builder = (StringSubstringEvaluatorBuilder)e;
					
					Set<SimpleName> aliasNameSet = new HashSet<>();
					//targetInputVariableBuilderDelegate
					aliasNameSet.add(builder.targetInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					//startPosInputVariable
					if(aliasNameSet.contains(builder.startPosInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}else {
						aliasNameSet.add(builder.startPosInputVariableBuilderDelegate.getCurrentValue().getAliasName());
					}
					//lengthInputVariable
					if(aliasNameSet.contains(builder.lengthInputVariableBuilderDelegate.getCurrentValue().getAliasName())) {
						return false;
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		//selected outputVariable must be of string type;
		Set<String> set4 = new HashSet<>();
		set4.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringSubstringEvaluator> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of string type!",
				set4, 
				e->{
					StringSubstringEvaluatorBuilder builder = (StringSubstringEvaluatorBuilder)e;
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
			this.startPosInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.lengthInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringSubstringEvaluator stringSubstringEvaluator = (StringSubstringEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringSubstringEvaluator.getTargetInputVariable(), isEmpty);
				this.startPosInputVariableBuilderDelegate.setValue(stringSubstringEvaluator.getStartPosInputVariable(), isEmpty);
				this.lengthInputVariableBuilderDelegate.setValue(stringSubstringEvaluator.getLengthInputVariable(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringSubstringEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringSubstringEvaluator build() {
		return new StringSubstringEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.startPosInputVariableBuilderDelegate.getCurrentValue(),
				this.lengthInputVariableBuilderDelegate.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

	
}
