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
import function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluator;

/**
 * 
 * @author tanxu
 *
 */
public final class StringRemoveEmptySpaceEvaluatorBuilder extends SimpleStringProcessingEvaluatorBuilder<StringRemoveEmptySpaceEvaluator>{
	
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
	public StringRemoveEmptySpaceEvaluatorBuilder(
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
		
		
		this.addChildNodeBuilder(this.outputVariableBuilderDelegate);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//selected outputVariable must be of string type;
		Set<String> set1 = new HashSet<>();
		set1.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<StringRemoveEmptySpaceEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of string type!",
				set1, 
				e->{
					StringRemoveEmptySpaceEvaluatorBuilder builder = (StringRemoveEmptySpaceEvaluatorBuilder)e;
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isOfStringType();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
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
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				StringRemoveEmptySpaceEvaluator stringCaseChangeEvaluator = (StringRemoveEmptySpaceEvaluator)value;
				this.targetInputVariableBuilderDelegate.setValue(stringCaseChangeEvaluator.getTargetInputVariable(), isEmpty);
				this.outputVariableBuilderDelegate.setValue(stringCaseChangeEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected StringRemoveEmptySpaceEvaluator build() {
		return new StringRemoveEmptySpaceEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.targetInputVariableBuilderDelegate.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue()
				);
	}

	
}
