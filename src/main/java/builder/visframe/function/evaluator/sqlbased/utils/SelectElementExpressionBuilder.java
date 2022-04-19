package builder.visframe.function.evaluator.sqlbased.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegateFactory;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import builder.visframe.function.variable.output.PFConditionEvaluatorBooleanOutputVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import function.evaluator.sqlbased.utils.SelectElementExpression;
import function.variable.input.InputVariable;
import function.variable.output.OutputVariable;
import utils.visframe.SelectElementProcessor;

public final class SelectElementExpressionBuilder extends VfSQLExpressionBuilder<SelectElementExpression>{
	/**
	 * whether this builder is for {@link SelectElementExpression} of a {@link SimpleSQLQueryEvaluator} for a {@link PiecewsiseFunction}'s condition evaluator;
	 * if true, the {@link #outputVariableBuilder} must be of {@link PFConditionEvaluatorBooleanOutputVariableBuilder} type;
	 * otherwise (evaluator for SimpleFunction), the {@link #outputVariableBuilder} must be of {@link ValueTableColumnOutputVariableBuilder} type;
	 */
	private final boolean forPiecewiseFunctionCondition; 
	
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
	public SelectElementExpressionBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder, AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			boolean forPiecewiseFunctionCondition) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder);
		// TODO Auto-generated constructor stub
		this.forPiecewiseFunctionCondition = forPiecewiseFunctionCondition;
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	

	public boolean isForPiecewiseFunctionCondition() {
		return forPiecewiseFunctionCondition;
	}
	
	
	/**
	 * 
	 * @return
	 */
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
	protected static final String selectElementString = "selectElementString";
	protected static final String selectElementString_description = "selectElementString";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";


	//////////////////////////
	////////////
	protected SelectElementProcessorBuilder selectElementProcessorBuilder = new SelectElementProcessorBuilder(
		selectElementString, selectElementString_description, false, this);
	
	//outputVariable
	private NonLeafNodeBuilder<? extends OutputVariable> outputVariableBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
//		super.buildChildrenNodeBuilderNameMap();
		
		//selectElementString
		this.addChildNodeBuilder(this.selectElementProcessorBuilder);
		
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
		
		//outputVariable
		if(this.isForPiecewiseFunctionCondition()) {//must be of PFConditionEvaluatorBooleanOutputVariable type
			this.outputVariableBuilder = new PFConditionEvaluatorBooleanOutputVariableBuilder(
					outputVariable, outputVariable_description, false, this,
					this.getHostEvaluatorBuilder(), null,
					e->{return true;});
		}else {//must be of ValueTableColumnOutputVariable type
			this.outputVariableBuilder = new ValueTableColumnOutputVariableBuilderDelegate(
					outputVariable, outputVariable_description, false, this, 
					this.getHostEvaluatorBuilder().getHostVisProjectDBContext(), 
					this.getHostEvaluatorBuilder().getHostCompositionFunctionBuilder(), 
					this.getHostEvaluatorBuilder().getHostComponentFunctionBuilder(), 
					this.getHostEvaluatorBuilder(),
					e->{return true;}
					);
		}
		
		this.addChildNodeBuilder(this.outputVariableBuilder);
	}

	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
//		super.buildGenericChildrenNodeBuilderConstraintSet();
		
	}

	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
//		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		
		//when selectElementProcessorBuilder's status changes
		//if default empty, set inputVariableSetBuilder to default empty, then set to null?
		//else (a non-null valid string), extract the variables and reset the map key set of inputVariableSetBuilder
		Runnable selectElementStringBuilderStatusChangeEventAction = ()->{
			try {
				if(this.selectElementProcessorBuilder.getCurrentStatus().isDefaultEmpty()) {
					
					this.inputVariableAliasNameMapBuilder.setKeySet(new HashSet<>());
					
	//				this.inputVariableAliasNameMapBuilder.setToNull();//set to null until aliasedSqlStringBuilder has a valid non-null value;
					
				}else if(selectElementProcessorBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					SelectElementProcessor processor = this.selectElementProcessorBuilder.getCurrentValue();
						
	//				if(this.inputVariableAliasNameMapBuilder.isSetToNull())
	//					this.inputVariableAliasNameMapBuilder.setToNonNull();
					
					this.inputVariableAliasNameMapBuilder.setKeySet(processor.getVariableNameStringSet());//this will also clear any existing input variables?
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //clear all existing variables
		};
		
		selectElementProcessorBuilder.addStatusChangedAction(
				selectElementStringBuilderStatusChangeEventAction);
	}
	
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.selectElementProcessorBuilder.setValue(null, isEmpty);
			this.inputVariableAliasNameMapBuilder.setValue(null, isEmpty);
			this.outputVariableBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				SelectElementExpression selectColumnExpression = (SelectElementExpression)value;
				
				this.selectElementProcessorBuilder.setValue(new SelectElementProcessor(selectColumnExpression.getAliasedSqlString()), isEmpty);
				
				Map<String, InputVariable> inputVariableAliasNameMap = new LinkedHashMap<>();
				selectColumnExpression.getInputVariableAliasNameMap().forEach((k,v)->{
					inputVariableAliasNameMap.put(k.getStringValue(), v);
				});
				
				//set key set first
				this.inputVariableAliasNameMapBuilder.setKeySet(inputVariableAliasNameMap.keySet());
				//set value
				this.inputVariableAliasNameMapBuilder.setValue(inputVariableAliasNameMap, isEmpty);
				
				this.outputVariableBuilder.setValue(selectColumnExpression.getOutputVariable(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected SelectElementExpression build() {
		return new SelectElementExpression(
				this.selectElementProcessorBuilder.getCurrentValue().getSingleLineSelectElementString(),
				new LinkedHashSet<>(this.inputVariableAliasNameMapBuilder.getCurrentValue().values()),
				this.outputVariableBuilder.getCurrentValue()
				);
	}

}
