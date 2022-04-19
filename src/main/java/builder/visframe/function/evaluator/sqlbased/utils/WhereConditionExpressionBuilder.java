package builder.visframe.function.evaluator.sqlbased.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import basic.SimpleName;
import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegateFactory;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.sqlbased.utils.WhereConditionExpression;
import function.variable.input.InputVariable;
import utils.visframe.WhereConditionProcessor;

public final class WhereConditionExpressionBuilder extends VfSQLExpressionBuilder<WhereConditionExpression>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 * @param forPiecewiseFunctionCondition
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public WhereConditionExpressionBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, AbstractEvaluatorBuilder<?> hostEvaluatorBuilder
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder);
		// TODO Auto-generated constructor stub
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	///////////////////////////////////////////
	protected static final String whereConditionString = "whereConditionString";
	protected static final String whereConditionString_description = "whereConditionString";
	

	//////////////////////////
	////////////
	protected WhereConditionProcessorBuilder whereConditionProcessorBuilder = new WhereConditionProcessorBuilder(
		whereConditionString, whereConditionString_description, false, this);
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
//		super.buildChildrenNodeBuilderNameMap();
		
		//whereConditionString
		this.addChildNodeBuilder(this.whereConditionProcessorBuilder);
		
		//vfSymjaVariableNameInputVariableMap
		InputVariableBuilderDelegateFactory inputVariableBuilderDelegateFactory = new InputVariableBuilderDelegateFactory(
				"inputVariable","inputVariable",false, 
				this.getHostEvaluatorBuilder().getHostVisProjectDBContext(), //VisProjectDBContext hostVisProjectDBContext,
				this.getHostEvaluatorBuilder().getHostCompositionFunctionBuilder(),//CompositionFunctionBuilder hostCompositionFunctionBuilder,
				this.getHostEvaluatorBuilder().getHostComponentFunctionBuilder(),//ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
				this.getHostEvaluatorBuilder(),//hostEvaluatorBuilder
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				false,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				false//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return true;} //targetRecordDataMetadataIDConditionConstraints; can be of any record data
//				false,//boolean allowingConstantValuedInputVariable
//				false//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		
		this.inputVariableAliasNameMapBuilder = new FixedKeySetMapValueBuilder<>(
				inputVariableAliasNameMap, inputVariableAliasNameMap_description, false, this,
				inputVariableBuilderDelegateFactory,
				
				e->{return e;},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e;},//Function<K,String> mapKeyToDescriptionFunction,
				(k,nb)->{
					InputVariableBuilderDelegate ivbd = (InputVariableBuilderDelegate)nb;
					ivbd.setPredefinedAliasName(new SimpleName(k)); //set the alias name of the input variable builder;
				},//BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction,
				
				false, //boolean allowingNullMapValue,
				true//boolean allowingDuplicateMapValue
				);
		
		this.addChildNodeBuilder(this.inputVariableAliasNameMapBuilder);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
//		super.buildGenericChildrenNodeBuilderConstraintSet();
	}
	
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
//		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		
		//when whereConditionProcessorBuilder's status changes
		//if default empty, set inputVariableSetBuilder to default empty, then set to null?
		//else (a non-null valid string), extract the variables and reset the map key set of inputVariableSetBuilder
		Runnable selectElementStringBuilderStatusChangeEventAction = ()->{
			try {
				if(this.whereConditionProcessorBuilder.getCurrentStatus().isDefaultEmpty()) {
					
					this.inputVariableAliasNameMapBuilder.setKeySet(new HashSet<>());
					
				////clear all existing variables
	//				this.inputVariableAliasNameMapBuilder.setToNull();//set to null until aliasedSqlStringBuilder has a valid non-null value;
					
				}else if(whereConditionProcessorBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					WhereConditionProcessor processor = this.whereConditionProcessorBuilder.getCurrentValue();
						
	//				if(this.inputVariableAliasNameMapBuilder.isSetToNull())
	//					this.inputVariableAliasNameMapBuilder.setToNonNull();
					
					this.inputVariableAliasNameMapBuilder.setKeySet(processor.getVariableNameStringSet());//this will also clear any existing input variables?
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		whereConditionProcessorBuilder.addStatusChangedAction(
				selectElementStringBuilderStatusChangeEventAction);
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.whereConditionProcessorBuilder.setValue(null, isEmpty);
			this.inputVariableAliasNameMapBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				WhereConditionExpression whereConditionExpression = (WhereConditionExpression)value;
				
				
				this.whereConditionProcessorBuilder.setValue(new WhereConditionProcessor(whereConditionExpression.getAliasedSqlString()), isEmpty);
				
				Map<String, InputVariable> inputVariableAliasNameMap = new LinkedHashMap<>();
				whereConditionExpression.getInputVariableAliasNameMap().forEach((k,v)->{
					inputVariableAliasNameMap.put(k.getStringValue(), v);
				});
				
				//set key set first
				this.inputVariableAliasNameMapBuilder.setKeySet(inputVariableAliasNameMap.keySet());
				//set value
				this.inputVariableAliasNameMapBuilder.setValue(inputVariableAliasNameMap, isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected WhereConditionExpression build() {
		return new WhereConditionExpression(
				this.whereConditionProcessorBuilder.getCurrentValue().getSingleLineWhereConditionString(),
				new LinkedHashSet<>(this.inputVariableAliasNameMapBuilder.getCurrentValue().values())
				);
	}

}
