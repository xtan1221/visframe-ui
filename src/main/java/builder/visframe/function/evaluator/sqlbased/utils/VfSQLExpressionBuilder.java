package builder.visframe.function.evaluator.sqlbased.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.sqlbased.utils.VfSQLExpression;
import function.variable.input.InputVariable;


public abstract class VfSQLExpressionBuilder<T extends VfSQLExpression> extends NonLeafNodeBuilder<T>{
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder;
	
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostEvaluatorBuilder
	 */
	protected VfSQLExpressionBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder,
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
	}
	
	protected AbstractEvaluatorBuilder<?> getHostEvaluatorBuilder() {
		return hostEvaluatorBuilder;
	}
	
	///////////////////////////////////////////

	protected static final String inputVariableAliasNameMap = "inputVariableAliasNameMap";
	protected static final String inputVariableAliasNameMap_description = "inputVariableAliasNameMap";
	
	
	
	protected FixedKeySetMapValueBuilder<String, InputVariable> inputVariableAliasNameMapBuilder;

	//////////////////////////////////
	/**
	 * return the current set of InputVariableBuilders
	 * @return
	 */
	public Set<InputVariableBuilder<?>> getInputVariableBuilderSet() {
		Set<InputVariableBuilder<?>> ret = new LinkedHashSet<>();
		
		//
		this.inputVariableAliasNameMapBuilder.getValueNodeBuilderSet().forEach(e->{
			InputVariableBuilderDelegate delegate = (InputVariableBuilderDelegate)e;
			ret.add(delegate.getInputVariableBuilder());
		});
		
		return ret;
	}
	
	
	
//	@Override
//	protected void buildChildrenNodeBuilderNameMap() {
//		
//		
////		//vfSymjaVariableNameInputVariableMap
////		InputVariableBuilderDelegateFactory inputVariableBuilderDelegateFactory = new InputVariableBuilderDelegateFactory(
////				"inputVariable","inputVariable",false, 
////				this.getHostEvaluatorBuilder().getHostVisProjectDBContext(), //VisProjectDBContext hostVisProjectDBContext,
////				this.getHostEvaluatorBuilder().getHostCompositionFunctionBuilder(),//CompositionFunctionBuilder hostCompositionFunctionBuilder,
////				this.getHostEvaluatorBuilder().getHostComponentFunctionBuilder(),//ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
////				this.getHostEvaluatorBuilder(),//hostEvaluatorBuilder
////				e->{return true;},
////				false,//boolean allowingConstantValuedInputVariable
////				false//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
////				
////				);
////		
////		this.inputVariableAliasNameMapBuilder = new FixedKeySetMapValueBuilder<>(
////				inputVariableAliasNameMap, inputVariableAliasNameMap_description, false, this,
////				inputVariableBuilderDelegateFactory,
////				
////				e->{return e;},//Function<K,String> mapKeyToStringRepresentationFunction,
////				e->{return e;},//Function<K,String> mapKeyToDescriptionFunction,
////				(k,nb)->{
////					InputVariableBuilderDelegate ivbd = (InputVariableBuilderDelegate)nb;
////					ivbd.setPredefinedAliasName(new SimpleName(k)); //set the alias name of the input variable builder;
////				},//BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction,
////				
////				false, //boolean allowingNullMapValue,
////				true//boolean allowingDuplicateMapValue
////				);
////		
////		this.addChildNodeBuilder(this.inputVariableAliasNameMapBuilder);
//	}
//
//
//	@Override
//	protected void buildGenericChildrenNodeBuilderConstraintSet() {
//		//input variables must have different alias names;
//		//this constraint is enforced by 
//		//1. all variable names will be transformed to upper case
//		//2. the inputVariableSetBuilder of type FixedKeySetMapValueBuilder
//		
//	}
//	
//	/**
//	 * status change event action of aliasedSqlStringBuilder should be implemented in subclasses;
//	 * 
//	 */
//	@Override
//	protected void addStatusChangeEventActionOfChildNodeBuilders() {
//		//
//	}
//	
//	
//	///////////////////////////////////////////////////////////
//	@Override
//	public boolean setValue(Object value, boolean isEmpty) {
//		boolean changed = super.setValue(value, isEmpty);
//		
//		if(isEmpty) {
//			this.inputVariableAliasNameMapBuilder.setToDefaultEmpty();
//		}else {
//			if(value==null) {
//				this.setToNull();
//				
//			}else {
//				@SuppressWarnings("unchecked")
//				T vfSQLExpression = (T)value;
//				//
//				Map<String, InputVariable> inputVariableAliasNameMap = new LinkedHashMap<>();
//				vfSQLExpression.getInputVariableAliasNameMap().forEach((k,v)->{
//					inputVariableAliasNameMap.put(k.getStringValue(), v);
//				});
//				
//				//set key set first
//				this.inputVariableAliasNameMapBuilder.setKeySet(inputVariableAliasNameMap.keySet());
//				//set value
//				this.inputVariableAliasNameMapBuilder.setValue(inputVariableAliasNameMap, isEmpty);
//			}
//		}
//		
//		return changed;
//	}

}
