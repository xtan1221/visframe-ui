package builder.visframe.function.evaluator.nonsqlbased;

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
import builder.visframe.function.variable.output.PFConditionEvaluatorBooleanOutputVariableBuilder;
import builder.visframe.function.variable.output.ValueTableColumnOutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.RecordwiseInputVariableIsNullValuedEvaluator;
import function.variable.input.recordwise.RecordwiseInputVariable;
import function.variable.output.OutputVariable;


public class RecordwiseInputVariableIsNullValuedEvaluatorBuilder extends NonSqlQueryBasedEvaluatorBuilder<RecordwiseInputVariableIsNullValuedEvaluator>{
	/**
	 * whether this {@link RecordwiseInputVariableIsNullValuedEvaluatorBuilder} is for the conditional evaluator of a {@link PiecewiseFunction};
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
	public RecordwiseInputVariableIsNullValuedEvaluatorBuilder(
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
		
		if(this.recordwiseInputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.recordwiseInputVariableBuilderDelegate.getInputVariableBuilder());
		
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
	protected static final String recordwiseInputVariable = "recordwiseInputVariable";
	protected static final String recordwiseInputVariable_description = "recordwiseInputVariable";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//////////////////////////
	/**
	 * only allow RecordwiseInputVariableTypes to be selected and mustBeOfSameOwnerRecordDataWithHostCompositionFunction
	 */
	private InputVariableBuilderDelegate recordwiseInputVariableBuilderDelegate = new InputVariableBuilderDelegate(
			recordwiseInputVariable, recordwiseInputVariable_description, false, this,
			this,
			e->{return true;},
			false,//allowingNonRecordwiseInputVariableTypes
			null,//allowingConstantValuedInputVariable
			true,//allowingRecordwiseInputVariableTypes
//			true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
			e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
			);
	
	//must be of boolean type
	private NonLeafNodeBuilder<? extends OutputVariable> outputVariableBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		this.addChildNodeBuilder(this.recordwiseInputVariableBuilderDelegate);
		
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
		
		
		
		
		//selected outputVariable must be of boolean type;
		Set<String> set3 = new HashSet<>();
		set3.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<RecordwiseInputVariableIsNullValuedEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected outputVariable must be of boolean type!",
				set3, 
				e->{
					RecordwiseInputVariableIsNullValuedEvaluatorBuilder builder = (RecordwiseInputVariableIsNullValuedEvaluatorBuilder)e;
					
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
			this.recordwiseInputVariableBuilderDelegate.setValue(null, isEmpty);
			this.outputVariableBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				RecordwiseInputVariableIsNullValuedEvaluator stringEndsWithEvaluator = (RecordwiseInputVariableIsNullValuedEvaluator)value;
				this.recordwiseInputVariableBuilderDelegate.setValue(stringEndsWithEvaluator.getRecordwiseInputVariable(), isEmpty);
				this.outputVariableBuilder.setValue(stringEndsWithEvaluator.getOutputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected RecordwiseInputVariableIsNullValuedEvaluator build() {
		return new RecordwiseInputVariableIsNullValuedEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				(RecordwiseInputVariable) this.recordwiseInputVariableBuilderDelegate.getCurrentValue(),
				this.outputVariableBuilder.getCurrentValue()
				);
	}

}
