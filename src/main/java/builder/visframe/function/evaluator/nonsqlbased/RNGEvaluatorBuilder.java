package builder.visframe.function.evaluator.nonsqlbased;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import rdb.sqltype.SQLDataTypeFactory;

public abstract class RNGEvaluatorBuilder<T extends RNGEvaluator> extends NonSqlQueryBasedEvaluatorBuilder<T>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 */
	protected RNGEvaluatorBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder,
			int indexID) {
		super(name, description, canBeNull, parentNodeBuilder, hostComponentFunctionBuilder, indexID);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder()!=null)
			ret.add(this.outputVariableBuilderDelegate.getValueTableColumnOutputVariableBuilder());
		
		return ret;
	}
	
	//////////////////////////////
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	
	//must be of double type
	protected ValueTableColumnOutputVariableBuilderDelegate outputVariableBuilderDelegate;
	////////////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		outputVariableBuilderDelegate = new ValueTableColumnOutputVariableBuilderDelegate(
				outputVariable, outputVariable_description, false, this, 
				this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), 
				this,
				e->{return e.isDouble();}//must be of double type
				);
		this.addChildNodeBuilder(outputVariableBuilderDelegate);
		
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		
		//built outputVariable must be of double type;
		Set<String> set1 = new HashSet<>();
		set1.add(outputVariable);
		GenricChildrenNodeBuilderConstraint<T> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "built outputVariable must be of double type!",
				set1, 
				e->{
					RNGEvaluatorBuilder<T> builder = (RNGEvaluatorBuilder<T>)e;
//					SQLDataType type = builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType();
//					SQLDataType type2 = SQLDataTypeFactory.doubleType();
//					boolean value = type.isDouble();
					return builder.outputVariableBuilderDelegate.getCurrentValue().getSQLDataType().equals(SQLDataTypeFactory.doubleType());
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
	}
	

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.outputVariableBuilderDelegate.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T RNGEvaluator = (T)value;
				this.outputVariableBuilderDelegate.setValue(RNGEvaluator.getOutputVariable(), isEmpty);
			}
		}
		
		return changed;
	}
}
