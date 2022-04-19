package builder.visframe.function.evaluator.nonsqlbased;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;

/**
 * 
 * @author tanxu
 *
 */
public final class SimpleRNGEvaluatorBuilder extends RNGEvaluatorBuilder<SimpleRNGEvaluator>{
	
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
	public SimpleRNGEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder, int indexID) throws SQLException, IOException {
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
		if(this.range1InputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.range1InputVariableBuilderDelegate.getInputVariableBuilder());
		
		if(this.range2InputVariableBuilderDelegate.getInputVariableBuilder()!=null)
			ret.add(this.range2InputVariableBuilderDelegate.getInputVariableBuilder());
		
		return ret;
	}
	///////////////////////////////////////////
	protected static final String range1InputVariable = "range1InputVariable";
	protected static final String range1InputVariable_description = "range1InputVariable";

	protected static final String range2InputVariable = "range2InputVariable";
	protected static final String range2InputVariable_description = "range2InputVariable";

	
	//////////////////////////
	//must be of numeric type
	private InputVariableBuilderDelegate range1InputVariableBuilderDelegate;
	
	//must be of numeric type
	private InputVariableBuilderDelegate range2InputVariableBuilderDelegate;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		range1InputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				range1InputVariable, range1InputVariable_description, false, this,
				this, 
				e->{return e.isNumeric();},//must be of numeric type
				true,//allowingNonRecordwiseInputVariableTypes
				false,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				false, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.range1InputVariableBuilderDelegate);
		
		
		range2InputVariableBuilderDelegate = new InputVariableBuilderDelegate(
				range2InputVariable, range2InputVariable_description, false, this,
				this,
				e->{return e.isNumeric();},//must be of numeric type
				true,//allowingNonRecordwiseInputVariableTypes
				false,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				false, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.addChildNodeBuilder(this.range2InputVariableBuilderDelegate);
		
	}


	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		
		//selected range1InputVariable must be of numeric type;
		Set<String> set1 = new HashSet<>();
		set1.add(range1InputVariable);
		GenricChildrenNodeBuilderConstraint<SimpleRNGEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected range1InputVariable must be of numeric type!",
				set1, 
				e->{
					SimpleRNGEvaluatorBuilder builder = (SimpleRNGEvaluatorBuilder)e;
					return builder.range1InputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isNumeric();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//selected range2InputVariable must be of numeric type;
		Set<String> set2 = new HashSet<>();
		set2.add(range2InputVariable);
		GenricChildrenNodeBuilderConstraint<SimpleRNGEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected range2InputVariable must be of numeric type!",
				set2, 
				e->{
					SimpleRNGEvaluatorBuilder builder = (SimpleRNGEvaluatorBuilder)e;
					return builder.range2InputVariableBuilderDelegate.getCurrentValue().getSQLDataType().isNumeric();
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		
		//selected range1InputVariable and range2InputVariable must have different alias name; 
		Set<String> set3 = new HashSet<>();
		set3.add(range1InputVariable);
		set3.add(range2InputVariable);
		GenricChildrenNodeBuilderConstraint<SimpleRNGEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "selected range1InputVariable and range2InputVariable must have different alias name!",
				set3, 
				e->{
					SimpleRNGEvaluatorBuilder builder = (SimpleRNGEvaluatorBuilder)e;
					
					return !builder.range1InputVariableBuilderDelegate.getCurrentValue().getAliasName().equals(
							builder.range2InputVariableBuilderDelegate.getCurrentValue().getAliasName());
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
			this.range1InputVariableBuilderDelegate.setValue(null, isEmpty);
			this.range2InputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				SimpleRNGEvaluator simpleRNGEvaluator = (SimpleRNGEvaluator)value;
				this.range1InputVariableBuilderDelegate.setValue(simpleRNGEvaluator.getRange1InputVariable(), isEmpty);
				this.range2InputVariableBuilderDelegate.setValue(simpleRNGEvaluator.getRange2InputVariable(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected SimpleRNGEvaluator build() {
		return new SimpleRNGEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				this.outputVariableBuilderDelegate.getCurrentValue(),
				this.range1InputVariableBuilderDelegate.getCurrentValue(),
				this.range2InputVariableBuilderDelegate.getCurrentValue()
				);
	}

}
