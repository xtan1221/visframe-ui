package builder.visframe.function.evaluator.nonsqlbased;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import basic.SimpleName;
import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.function.component.ComponentFunctionBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegateFactory;
import builder.visframe.function.variable.delegator.ValueTableColumnOutputVariableBuilderDelegate;
import builder.visframe.function.variable.input.InputVariableBuilder;
import builder.visframe.function.variable.output.OutputVariableBuilder;
import builder.visframe.function.variable.output.PFConditionEvaluatorBooleanOutputVariableBuilder;
import builder.visframe.symja.VfSymjaSinglePrimitiveOutputExpressionBuilder;
import core.builder.NonLeafNodeBuilder;
import function.evaluator.nonsqlbased.SymjaExpressionEvaluator;
import function.variable.input.InputVariable;
import function.variable.output.OutputVariable;
import rdb.sqltype.SQLDataType;
import symja.VfSymjaVariableName;
import utils.Pair;

public final class SymjaExpressionEvaluatorBuilder extends NonSqlQueryBasedEvaluatorBuilder<SymjaExpressionEvaluator>{
	/**
	 * whether this builder is for {@link SymjaExpressionEvaluator} as a {@link PiecewsiseFunction}'s condition evaluator;
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
	 * @param hostComponentFunctionBuilder
	 * @param indexID
	 * @param forPiecewiseFunctionCondition
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SymjaExpressionEvaluatorBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			ComponentFunctionBuilder<?, ?> hostComponentFunctionBuilder, int indexID,
			
			boolean forPiecewiseFunctionCondition
			) throws SQLException, IOException {
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
		//
		this.vfSymjaVariableInputVariableMapBuilder.getValueNodeBuilderSet().forEach(e->{
			InputVariableBuilderDelegate delegate = (InputVariableBuilderDelegate)e;
			
			if(delegate!=null && delegate.getInputVariableBuilder()!=null)
				ret.add(delegate.getInputVariableBuilder());
		});
		
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
	protected static final String symjaExpression = "symjaExpression";
	protected static final String symjaExpression_description = "symjaExpression";

	protected static final String vfSymjaVariableNameInputVariableMap = "vfSymjaVariableNameInputVariableMap";
	protected static final String vfSymjaVariableNameInputVariableMap_description = "vfSymjaVariableNameInputVariableMap";
	
	protected static final String outputVariable = "outputVariable";
	protected static final String outputVariable_description = "outputVariable";
	
	//////////////////////////
	//symjaExpression
	private VfSymjaSinglePrimitiveOutputExpressionBuilder symjaExpressionBuilder;
	
	//vfSymjaVariableNameInputVariableMap
	private FixedKeySetMapValueBuilder<Pair<VfSymjaVariableName,SQLDataType>, InputVariable> vfSymjaVariableInputVariableMapBuilder;
	
	//outputVariable
	private NonLeafNodeBuilder<? extends OutputVariable> outputVariableBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//symjaExpression
		symjaExpressionBuilder = 
				new VfSymjaSinglePrimitiveOutputExpressionBuilder(symjaExpression, symjaExpression_description, false, this);
		this.addChildNodeBuilder(this.symjaExpressionBuilder);
		
		
		//vfSymjaVariableNameInputVariableMap
		InputVariableBuilderDelegateFactory inputVariableBuilderDelegateFactory = new InputVariableBuilderDelegateFactory(
				"inputVariable","inputVariable",false, 
				this.getHostVisProjectDBContext(), //VisProjectDBContext hostVisProjectDBContext,
				this.getHostCompositionFunctionBuilder(),//CompositionFunctionBuilder hostCompositionFunctionBuilder,
				this.getHostComponentFunctionBuilder(),//ComponentFunctionBuilder<?,?> hostComponentFunctionBuilder,
				this,//hostEvaluatorBuilder
				e->{return true;},
				true,//allowingNonRecordwiseInputVariableTypes
				false,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return e.equals(this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());}//Predicate<MetadataID> targetRecordDataMetadataIDCondition
//				false,//boolean allowingConstantValuedInputVariable
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		this.vfSymjaVariableInputVariableMapBuilder = new FixedKeySetMapValueBuilder<>(
				vfSymjaVariableNameInputVariableMap, vfSymjaVariableNameInputVariableMap_description, false, this,
				inputVariableBuilderDelegateFactory,
				e->{return e.getFirst().getValue().concat(";").concat(e.getSecond().getSQLString());},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e.getFirst().getValue().concat(";").concat(e.getSecond().getSQLString());},//Function<K,String> mapKeyToDescriptionFunction,
				(k,vnb)->{
					InputVariableBuilderDelegate ivbd = (InputVariableBuilderDelegate)vnb;
					try {
						ivbd.resetDataTypeConstraints(e->{return e.equals(k.getSecond());});
					} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ivbd.setPredefinedAliasName(new SimpleName(k.getFirst().toString()));
				},
				false,//boolean allowingNullMapValue,
				false//boolean allowingDuplicateMapValue
				);
		
		this.addChildNodeBuilder(this.vfSymjaVariableInputVariableMapBuilder);
		
		
		//outputVariable
		if(this.isForPiecewiseFunctionCondition()) {//must be of PFConditionEvaluatorBooleanOutputVariable type
			this.outputVariableBuilder = new PFConditionEvaluatorBooleanOutputVariableBuilder(
					outputVariable, outputVariable_description, false, this,
					this, 
					null,//
					e->{return true;});
		}else {//must be of ValueTableColumnOutputVariable type
			this.outputVariableBuilder = new ValueTableColumnOutputVariableBuilderDelegate(
					outputVariable, outputVariable_description, false, this, 
					this.getHostVisProjectDBContext(), this.getHostCompositionFunctionBuilder(), this.getHostComponentFunctionBuilder(), this,
					e->{return true;}
					);
		}
		
		this.addChildNodeBuilder(this.outputVariableBuilder);
	}
	
	/**
	 * constraints are enforced programmatically, thus no need to explicitly define here!?
	 */
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//symja variable data type should be consistent with the selected input variable;
		//TODO
		
		
		//symja expression data type should be consistent with the selected output variable;
		//TODO
		
		
		//input variables must have different alias names;
		//TODO
	}
	

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		
		//when symjaExpressionBuilder's status is changed, vfSymjaVariableNameInputVariableMapBuilder should be updated accordingly;
		Runnable symjaExpressionBuilderStatusChangeEventAction = ()->{
			try {
				if(symjaExpressionBuilder.getCurrentStatus().isDefaultEmpty()) {
					
					this.vfSymjaVariableInputVariableMapBuilder.setKeySet(new HashSet<>());
					
					this.vfSymjaVariableInputVariableMapBuilder.setToDefaultEmpty();
					
					
				}else if(symjaExpressionBuilder.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					//if variable name set is changed from previous
	//				if(!Objects.equal(this.vfSymjaVariableInputVariableMapBuilder.getKeySet(),this.symjaExpressionBuilder.getExpressionStringBuilder().getCurrentValue().getVfSymjaVariableNameSet())) {
	//					this.vfSymjaVariableInputVariableMapBuilder.setKeySet(this.symjaExpressionBuilder.getExpressionStringBuilder().getCurrentValue().getVfSymjaVariableNameSet());
	//				}else {//symja variable names are not changed, 
	//					//TODO ?
	//					
	//					
	//				}
					
					//reset the vfSymjaVariableInputVariableMapBuilder
	//				this.vfSymjaVariableInputVariableMapBuilder.setToDefaultEmpty();
					Set<Pair<VfSymjaVariableName,SQLDataType>> keySet= new LinkedHashSet<>();
	//				this.symjaExpressionBuilder.getVariableNameSQLDataTypeMapBuilder().getCurrentValue().forEach((k,v)->{
	//					keySet.add(new Pair<>(k,v));
	//				});
					
	//				BiMap<Pair<VfSymjaVariableName,SQLDataType>, InputVariable> map = HashBiMap.create();
					this.symjaExpressionBuilder.getCurrentValue().getVariableNameSQLDataTypeMap().forEach((k,v)->{
						keySet.add(new Pair<>(k,v));
					});
					
					this.vfSymjaVariableInputVariableMapBuilder.setKeySet(keySet);
					
					//the data type constraints of the outputVariableBuilder must be consistent with the output data type of the updated symjaExpressionBuilder
					if(this.isForPiecewiseFunctionCondition()) {
						//TODO
						PFConditionEvaluatorBooleanOutputVariableBuilder ovb = (PFConditionEvaluatorBooleanOutputVariableBuilder)this.outputVariableBuilder;
						
						ovb.setToDefaultEmpty();
						ovb.setDataTypeConstraints(e->{return e.equals(this.symjaExpressionBuilder.getSqlDataTypeBuilder().getCurrentValue());});
						
					}else {
						ValueTableColumnOutputVariableBuilderDelegate delegate = (ValueTableColumnOutputVariableBuilderDelegate)this.outputVariableBuilder;
						
						delegate.setToDefaultEmpty();
						
						delegate.setDataTypeConstraints(e->{return e.equals(this.symjaExpressionBuilder.getSqlDataTypeBuilder().getCurrentValue());});
						
					}
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		symjaExpressionBuilder.addStatusChangedAction(
				symjaExpressionBuilderStatusChangeEventAction);
	}
	
	
	///////////////////////////////////////////////////////////
	/**
	 * {@inheritDoc}
	 * 
	 * must set value of {@link #symjaExpressionBuilder} before {@link #vfSymjaVariableInputVariableMapBuilder} and {@link #outputVariableBuilder}
	 * since the latter two's values are listening to the status change of the first one (see {@link #addStatusChangeEventActionOfChildNodeBuilders()})
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.symjaExpressionBuilder.setValue(null, isEmpty);
			this.vfSymjaVariableInputVariableMapBuilder.setValue(null, isEmpty);
			this.outputVariableBuilder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				SymjaExpressionEvaluator symjaExpressionEvaluator = (SymjaExpressionEvaluator)value;
				this.symjaExpressionBuilder.setValue(symjaExpressionEvaluator.getSymjaExpression(), isEmpty);
				//
				BiMap<Pair<VfSymjaVariableName,SQLDataType>, InputVariable> map = HashBiMap.create();
				symjaExpressionEvaluator.getVfSymjaVariableNameInputVariableMap().forEach((k,v)->{
					map.put(new Pair<>(k,v.getSQLDataType()), v);
				});
				this.vfSymjaVariableInputVariableMapBuilder.setValue(map, isEmpty);
				//
				this.outputVariableBuilder.setValue(symjaExpressionEvaluator.getOutputVariable(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected SymjaExpressionEvaluator build() {
		BiMap<VfSymjaVariableName, InputVariable> vfSymjaVariableNameInputVariableMap = HashBiMap.create();
		this.vfSymjaVariableInputVariableMapBuilder.getCurrentValue().forEach((k,v)->{
			vfSymjaVariableNameInputVariableMap.put(k.getFirst(), v);
		});
		
		
		return new SymjaExpressionEvaluator(
				this.getHostCompositionFunctionBuilder().getCompositionFunctionID(),
				this.getHostComponentFunctionBuilder().getIndexID(),
				this.getIndexID(),
				
				this.notesBuilder.getCurrentValue(),
				//
				this.symjaExpressionBuilder.getCurrentValue(),
				vfSymjaVariableNameInputVariableMap,
				this.outputVariableBuilder.getCurrentValue()
				);
	}

}
