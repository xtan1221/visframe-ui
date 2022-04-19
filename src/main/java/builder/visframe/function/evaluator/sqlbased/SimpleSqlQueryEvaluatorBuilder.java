package builder.visframe.function.evaluator.sqlbased;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.list.ArrayListNonLeafNodeBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.evaluator.sqlbased.utils.SelectElementExpressionBuilder;
import builder.visframe.function.evaluator.sqlbased.utils.SelectElementExpressionBuilderFactory;
import builder.visframe.function.evaluator.sqlbased.utils.WhereConditionExpressionBuilder;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import builder.visframe.function.variable.output.PFConditionEvaluatorBooleanOutputVariableBuilder;
import builder.visframe.function.variable.output.ValueTableColumnOutputVariableBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import function.evaluator.sqlbased.utils.SelectElementExpression;
import function.variable.output.type.CFGTargetOutputVariable;


public final class SimpleSqlQueryEvaluatorBuilder extends SqlQueryBasedEvaluatorBuilder<SimpleSQLQueryEvaluator>{
	/**
	 * whether this {@link SimpleSqlQueryEvaluatorBuilder} is for the conditional evaluator of a {@link PiecewiseFunction};
	 * 
	 * if true, the selectClauseExpressionList contains one single SelectColumnExpression with output variables of type {@link PFConditionEvaluatorBooleanOutputVariableBuilder} 
	 * else, the selectClauseExpressionList can contains any positive number of SelectColumnExpression with output variables of type {@link ValueTableColumnOutputVariableBuilder} 
	 */
	private final boolean forPiecewiseFunctionCondition;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SimpleSqlQueryEvaluatorBuilder(
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
	public Set<InputVariableBuilder<?>> getInputVariableBuilderSet() throws SQLException, IOException {
		Set<InputVariableBuilder<?>> ret = new LinkedHashSet<>();
		//SELECT clause
		if(!this.isForPiecewiseFunctionCondition()) {
			this.selectElementExpressionListBuilder.getNodeBuilderSet().forEach(e->{
				SelectElementExpressionBuilder eb = (SelectElementExpressionBuilder)e;
				
				ret.addAll(eb.getInputVariableBuilderSet());
			});
		}else {
			ret.addAll(this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder.getInputVariableBuilderSet());
		}
		
		//WHERE clause
		if(!this.whereConditionExpressionBuilder.isSetToNull())//only relevant if not set to null;
			ret.addAll(this.whereConditionExpressionBuilder.getInputVariableBuilderSet());
		
		return ret;
	}

	
	@Override
	public Set<OutputVariableBuilder<?>> getOutputVariableBuilderSet() throws SQLException, IOException {
		Set<OutputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		if(!this.isForPiecewiseFunctionCondition()) {
			this.selectElementExpressionListBuilder.getNodeBuilderSet().forEach(e->{
				SelectElementExpressionBuilder eb = (SelectElementExpressionBuilder)e;
				
				ret.addAll(eb.getOutputVariableBuilderSet());
			});
		}else {
			ret.addAll(this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder.getOutputVariableBuilderSet());
			
		}
		
		return ret;
	}
	
	
	///////////////////////////////////////////
	protected static final String selectClauseExpressionList = "selectClauseExpressionList";
	protected static final String selectClauseExpressionList_description = "selectClauseExpressionList";

	protected static final String whereConditionExpression = "whereConditionExpression";
	protected static final String whereConditionExpression_description = "whereConditionExpression";
	
	
	//////////////////////////
	//selectClauseExpressionList for SimpleFunction
	private ArrayListNonLeafNodeBuilder<SelectElementExpression> selectElementExpressionListBuilder;
	
	//single SelectColumnExpression for PiecewiseFunction
	private SelectElementExpressionBuilder piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder;
	
	
	//whereConditionExpression
	private WhereConditionExpressionBuilder whereConditionExpressionBuilder = new WhereConditionExpressionBuilder(
			whereConditionExpression, whereConditionExpression_description, 
			true, //boolean canBeNull,
			this, //NonLeafNodeBuilder<?> parentNodeBuilder, 
			this //AbstractEvaluatorBuilder<?> hostEvaluatorBuilder
			);
	
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		if(!this.isForPiecewiseFunctionCondition()) {//simple function
			//selectClauseExpressionList
			SelectElementExpressionBuilderFactory selectColumnExpressionBuilderFactory = new SelectElementExpressionBuilderFactory(
					"selectClauseExpression","selectClauseExpression",false, this//hostEvaluatorBuilder
					);
			this.selectElementExpressionListBuilder = new ArrayListNonLeafNodeBuilder<>(
					selectClauseExpressionList, selectClauseExpressionList_description, false, this,
					selectColumnExpressionBuilderFactory, false
					);
			
			this.addChildNodeBuilder(this.selectElementExpressionListBuilder);
		}else {//piecewise function
			this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder = new SelectElementExpressionBuilder(
					"", "", false, this, this,
					true//boolean forPiecewiseFunctionCondition
					);
			
			this.addChildNodeBuilder(this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder);
		}
		
		//whereConditionExpression
		this.addChildNodeBuilder(this.whereConditionExpressionBuilder);
		
	}
	

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//if forPiecewiseFunctionCondition is false, selectClauseExpressionList cannot be empty;
		Set<String> set1 = new HashSet<>();
		set1.add(selectClauseExpressionList);
		GenricChildrenNodeBuilderConstraint<SimpleSQLQueryEvaluator> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if forPiecewiseFunctionCondition is false, selectClauseExpressionList cannot be empty!",
				set1, 
				e->{
					SimpleSqlQueryEvaluatorBuilder builder = (SimpleSqlQueryEvaluatorBuilder)e;
					
					if(!builder.isForPiecewiseFunctionCondition()) {
						return !builder.selectElementExpressionListBuilder.getCurrentValue().isEmpty();
					}
					
					return true;
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//if forPiecewiseFunctionCondition is false, output variables in selectClauseExpressionList must have different alias names;
		Set<String> set2 = new HashSet<>();
		set2.add(selectClauseExpressionList);
		GenricChildrenNodeBuilderConstraint<SimpleSQLQueryEvaluator> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if forPiecewiseFunctionCondition is false, output variables in selectClauseExpressionList must have different alias names!",
				set2, 
				e->{
					SimpleSqlQueryEvaluatorBuilder builder = (SimpleSqlQueryEvaluatorBuilder)e;
					
					if(!builder.isForPiecewiseFunctionCondition()) {
						Set<SimpleName> outputVariableNameSet = new HashSet<>();
						
						for(SelectElementExpression sce:builder.selectElementExpressionListBuilder.getCurrentValue()) {
							if(outputVariableNameSet.contains(sce.getOutputVariable().getAliasName())) {
								return false;
							}else {
								outputVariableNameSet.add(sce.getOutputVariable().getAliasName());
							}
						}
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//if forPiecewiseFunctionCondition is false and multiple OutputVariables are of CFGTargetOutputVariable type in the selectClauseExpressionList, they must be assigned to different CFGTargets
		Set<String> set3 = new HashSet<>();
		set3.add(selectClauseExpressionList);
		GenricChildrenNodeBuilderConstraint<SimpleSQLQueryEvaluator> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if forPiecewiseFunctionCondition is false and multiple OutputVariables are of CFGTargetOutputVariable type in the selectClauseExpressionList, they must be assigned to different CFGTargets!",
				set3, 
				e->{
					SimpleSqlQueryEvaluatorBuilder builder = (SimpleSqlQueryEvaluatorBuilder)e;
					
					if(!builder.isForPiecewiseFunctionCondition()) {
						Set<SimpleName> targetNameSet = new HashSet<>();
						
						for(SelectElementExpression sce:builder.selectElementExpressionListBuilder.getCurrentValue()) {
							if(sce.getOutputVariable() instanceof CFGTargetOutputVariable) {
								CFGTargetOutputVariable ov = (CFGTargetOutputVariable)sce.getOutputVariable();
								if(targetNameSet.contains(ov.getTargetName())) {
									return false;
								}else {
									targetNameSet.add(ov.getTargetName());
								}
							}
							
						}
					}
					
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
	}
	

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			if(this.isForPiecewiseFunctionCondition()) {
				this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder.setValue(null, isEmpty);
			}else {
				this.selectElementExpressionListBuilder.setValue(null, isEmpty);
			}
			
			this.whereConditionExpressionBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				SimpleSQLQueryEvaluator simpleSqlQueryEvaluator = (SimpleSQLQueryEvaluator)value;
				if(this.isForPiecewiseFunctionCondition()) {
					//single SelectColumnExpression
					this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder.setValue(simpleSqlQueryEvaluator.getSelectClauseExpressionList().iterator().next(), isEmpty);
				}else {
					this.selectElementExpressionListBuilder.setValue(simpleSqlQueryEvaluator.getSelectClauseExpressionList(), isEmpty);
				}
				
				this.whereConditionExpressionBuilder.setValue(simpleSqlQueryEvaluator.getWhereConditionExpression(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected SimpleSQLQueryEvaluator build() {
		List<SelectElementExpression> selectClauseExpressionList;
		if(this.isForPiecewiseFunctionCondition()) {
			selectClauseExpressionList = new ArrayList<>();
			selectClauseExpressionList.add(this.piecewiseFunctionConditionEvaluatorSingleSelectColumnExpressionBuilder.getCurrentValue());
		}else {
			selectClauseExpressionList = this.selectElementExpressionListBuilder.getCurrentValue();
		}
		
		
		return new SimpleSQLQueryEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				//
				selectClauseExpressionList,
				this.whereConditionExpressionBuilder.getCurrentValue()
				);
	}
	
	

}
