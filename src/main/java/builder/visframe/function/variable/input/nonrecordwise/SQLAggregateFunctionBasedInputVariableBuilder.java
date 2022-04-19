package builder.visframe.function.variable.input.nonrecordwise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.misc.SimpleTypeSelector;
import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import builder.visframe.function.variable.delegator.InputVariableBuilderDelegate;
import builder.visframe.metadata.MetadataIDSelector;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable;
import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable.VfSQLAggregateFunctionType;
import function.variable.input.recordwise.RecordwiseInputVariable;
import metadata.DataType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;


/**
 * note that SQLDataType is set by the selected VfSQLAggregateFunctionType
 * 
 * @author tanxu
 * 
 */
public final class SQLAggregateFunctionBasedInputVariableBuilder extends NonRecordwiseInputVariableBuilder<SQLAggregateFunctionBasedInputVariable>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @param hostComponentFunctionBuilder
	 * @param hostEvaluatorBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SQLAggregateFunctionBasedInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	
	private List<VfSQLAggregateFunctionType> getVfSQLAggregateFunctionTypeList(){
		List<VfSQLAggregateFunctionType> ret = new ArrayList<>();
		for(VfSQLAggregateFunctionType type:VfSQLAggregateFunctionType.values()) {
			if(this.getDataTypeConstraints().test(type.getOutputSqlDataType()))
				ret.add(type);
		}
		
		return ret;
	}
	
	private Set<DataType> getAllowedMetadataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		
		ret.add(DataType.RECORD);
		
		return ret;
	}
	
	
	////////////////////////////////////
	protected static final String aggregateFunctionType = "aggregateFunctionType";
	protected static final String aggregateFunctionType_description = "aggregateFunctionType";
	
	protected static final String targetRecordMetadataID = "targetRecordMetadataID";
	protected static final String targetRecordMetadataID_description = "targetRecordMetadataID";
	
	protected static final String recordwiseInputVariable1 = "recordwiseInputVariable1";
	protected static final String recordwiseInputVariable1_description = "recordwiseInputVariable1";
	
	protected static final String recordwiseInputVariable2 = "recordwiseInputVariable2";
	protected static final String recordwiseInputVariable2_description = "recordwiseInputVariable2";
	
	
	////////////////////////
	private SimpleTypeSelector<VfSQLAggregateFunctionType> aggregateFunctionTypeSelector;
	
	private MetadataIDSelector targetRecordMetadataIDSelector;
	

	private InputVariableBuilderDelegate recordwiseInputVariable1Builder;
	
	private InputVariableBuilderDelegate recordwiseInputVariable2Builder;

	/**
	 * @return the targetRecordMetadataIDSelector
	 */
	public MetadataIDSelector getTargetRecordMetadataIDSelector() {
		return targetRecordMetadataIDSelector;
	}

	/**
	 * @return the recordwiseInputVariable1Builder
	 */
	public InputVariableBuilderDelegate getRecordwiseInputVariable1Builder() {
		return recordwiseInputVariable1Builder;
	}


	/**
	 * @return the recordwiseInputVariable2Builder
	 */
	public InputVariableBuilderDelegate getRecordwiseInputVariable2Builder() {
		return recordwiseInputVariable2Builder;
	}


	////////////////////////////////////////
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		/////////////////////////////
		//aggregateFunctionType
		aggregateFunctionTypeSelector = new SimpleTypeSelector<>(
				aggregateFunctionType, aggregateFunctionType_description, false, this,
				e->{return e.name();},//Function<T,String> typeToStringRepresentationFunction,
				e->{return e.name();}//Function<T,String> typeToDescriptionFunction
				);
		
		aggregateFunctionTypeSelector.setPool(this.getVfSQLAggregateFunctionTypeList());
		this.addChildNodeBuilder(aggregateFunctionTypeSelector);
		
		
		//recordMetadataID
		targetRecordMetadataIDSelector = new MetadataIDSelector(
				targetRecordMetadataID, targetRecordMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(), 
				this.getAllowedMetadataTypeSet(),
				null);
		this.addChildNodeBuilder(targetRecordMetadataIDSelector);
		
		//recordwiseInputVariable1Builder
		recordwiseInputVariable1Builder = new InputVariableBuilderDelegate(
				recordwiseInputVariable1, recordwiseInputVariable1_description, true, this, //should be null if the selected VfSQLAggregateFunctionType does not need input columns
				
				this.getHostEvaluatorBuilder(), 
				e->{return true;},//must be of numeric type
				false,//allowingNonRecordwiseInputVariableTypes
				null,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return true;}//Predicate<MetadataID> targetRecordDataMetadataIDCondition; this is trivial; will be reset whenever a change is made
//				false, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		//DO NOT add it yet; only be added based on the status change event listeners;
//		this.addChildNodeBuilder(recordwiseInputVariable1Builder);
		
		//recordwiseInputVariable2Builder
		recordwiseInputVariable2Builder = new InputVariableBuilderDelegate(
				recordwiseInputVariable2, recordwiseInputVariable2_description, true, this, //should be null if the selected VfSQLAggregateFunctionType does not need input columns
				
				this.getHostEvaluatorBuilder(), 
				e->{return true;},//must be of numeric type
				false,//allowingNonRecordwiseInputVariableTypes
				null,//allowingConstantValuedInputVariable
				true,//allowingRecordwiseInputVariableTypes
//				true//mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				e->{return true;}//Predicate<MetadataID> targetRecordDataMetadataIDCondition; this is trivial; will be reset whenever a change is made
//				false, //boolean allowingConstantValuedInputVariable,
//				true//boolean mustBeOfSameOwnerRecordDataWithHostCompositionFunction
				);
		//DO NOT add it yet; only be added based on the status change event listeners;
//		this.addChildNodeBuilder(recordwiseInputVariable2Builder);
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		
		//if selected VfSQLAggregateFunctionType does not require input columns but only input table, both firstColumnNameSelector and secondColumnNameSelector should be null;
		//enforced by this buider's status change event listener
		
		//if selected VfSQLAggregateFunctionType requires one single input column, the secondColumnNameSelector must set to null;
		//enforced by this buider's status change event listener

		
		//if selected VfSQLAggregateFunctionType requires two single input columns, the secondColumnNameSelector must be non-null;
		//enforced by this buider's status change event listener

	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//when aggregateFunctionTypeSelector's status is changed
		//firstColumnNameSelector, secondColumnNameSelector should change accordingly based on the status of aggregateFunctionTypeSelector and recordMetadataIDSelector
		Runnable aggregateFunctionTypeSelectorStatusChangeEventAction = ()->{
			try {
				if(aggregateFunctionTypeSelector.getCurrentStatus().isDefaultEmpty()) {
					//always set to default empty no matter whether there is a record data selected in recordMetadataIDSelector or not;
					
					this.recordwiseInputVariable1Builder.setToDefaultEmpty();
					
					if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
						this.removeChildNodeBuilder(this.recordwiseInputVariable1Builder.getName());
					
					this.recordwiseInputVariable2Builder.setToDefaultEmpty();
					if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
						this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
					
				}else if(aggregateFunctionTypeSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					VfSQLAggregateFunctionType type = aggregateFunctionTypeSelector.getCurrentValue();
					
					//change the status of firstColumnNameSelector and secondColumnNameSelector based on how many input columns the selected type requires;
					if(this.targetRecordMetadataIDSelector.getCurrentStatus().hasValidValue()) {
						if(type.noInputColumnIsNeeded()) {//if no input column is required, remove both recordwiseInputVariable1Builder and recordwiseInputVariable2Builder
							
							this.recordwiseInputVariable1Builder.setToNull();
							if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
								this.removeChildNodeBuilder(this.recordwiseInputVariable1Builder.getName());
							
							this.recordwiseInputVariable2Builder.setToNull();
							if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
								this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
							
						}else {
							if(type.getRequiredNumOfColumn()==1) {//only requires 1 input column; add recordwiseInputVariable1Builder and remove recordwiseInputVariable2Builder
								this.recordwiseInputVariable1Builder.setToDefaultEmpty();
								this.recordwiseInputVariable1Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable1Builder);
								
								this.recordwiseInputVariable2Builder.setToNull();
								if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
									this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
							}else {//2
								this.recordwiseInputVariable1Builder.setToDefaultEmpty();
								this.recordwiseInputVariable1Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable1Builder);
								
								this.recordwiseInputVariable2Builder.setToDefaultEmpty();
								this.recordwiseInputVariable2Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable2Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable2Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable2Builder);
							}
						}
					}else {//target record data is not selected; do nothing
						
					}
					
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		aggregateFunctionTypeSelector.addStatusChangedAction(
				aggregateFunctionTypeSelectorStatusChangeEventAction);
		
		///========================================
		//when recordMetadataIDSelector's status is changed
		//firstColumnNameSelector, secondColumnNameSelector should change accordingly based on the status of aggregateFunctionTypeSelector and recordMetadataIDSelector
		Runnable recordMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(targetRecordMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					//only set both firstColumnNameSelector, secondColumnNameSelector's pool empty set; 
					//note that the status of firstColumnNameSelector, secondColumnNameSelector is determined by the aggregateFunctionTypeSelector; thus do not set it here
					
					this.recordwiseInputVariable1Builder.setToDefaultEmpty();
					
					if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
						this.removeChildNodeBuilder(this.recordwiseInputVariable1Builder.getName());
					
					this.recordwiseInputVariable2Builder.setToDefaultEmpty();
					if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
						this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
					
					
				}else if(targetRecordMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					//reset the target record data condition
					this.recordwiseInputVariable1Builder.resetTargetRecordDataMetadataIDCondition(e->{return e.equals(this.targetRecordMetadataIDSelector.getCurrentValue());});
					this.recordwiseInputVariable2Builder.resetTargetRecordDataMetadataIDCondition(e->{return e.equals(this.targetRecordMetadataIDSelector.getCurrentValue());});
					
					if(this.aggregateFunctionTypeSelector.getCurrentStatus().hasValidValue()) {
						VfSQLAggregateFunctionType type = aggregateFunctionTypeSelector.getCurrentValue();
						
						if(type.noInputColumnIsNeeded()) {//if no input column is required, remove both recordwiseInputVariable1Builder and recordwiseInputVariable2Builder
							
							this.recordwiseInputVariable1Builder.setToNull();
							if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
								this.removeChildNodeBuilder(this.recordwiseInputVariable1Builder.getName());
							
							this.recordwiseInputVariable2Builder.setToNull();
							if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
								this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
							
						}else {
							if(type.getRequiredNumOfColumn()==1) {//only requires 1 input column; add recordwiseInputVariable1Builder and remove recordwiseInputVariable2Builder
								this.recordwiseInputVariable1Builder.setToDefaultEmpty();
								this.recordwiseInputVariable1Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable1Builder);
								
								this.recordwiseInputVariable2Builder.setToNull();
								if(this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
									this.removeChildNodeBuilder(this.recordwiseInputVariable2Builder.getName());
							}else {//2
								this.recordwiseInputVariable1Builder.setToDefaultEmpty();
								this.recordwiseInputVariable1Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable1Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable1Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable1Builder);
								
								this.recordwiseInputVariable2Builder.setToDefaultEmpty();
								this.recordwiseInputVariable2Builder.setToNonNull();
								if(type.getInputColumnsMustBeNumeric()) {//must be numeric
									this.recordwiseInputVariable2Builder.resetDataTypeConstraints(e->{return e.isNumeric();});
								}else {//can be of any data type
									this.recordwiseInputVariable2Builder.resetDataTypeConstraints(e->{return true;});
								}
								if(!this.getChildrenNodeBuilderNameMap().containsKey(this.recordwiseInputVariable2Builder.getName()))
									this.addChildNodeBuilder(this.recordwiseInputVariable2Builder);
							}
						}
						
						
					}else {//do nothing
						
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		targetRecordMetadataIDSelector.addStatusChangedAction(
				recordMetadataIDSelectorStatusChangeEventAction);
		
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.aggregateFunctionTypeSelector.setValue(null, isEmpty);
			this.targetRecordMetadataIDSelector.setValue(null, isEmpty);
			this.recordwiseInputVariable1Builder.setValue(null, isEmpty);
			this.recordwiseInputVariable2Builder.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				SQLAggregateFunctionBasedInputVariable SQLAggregateFunctionBasedInputVariable = (SQLAggregateFunctionBasedInputVariable)value;
				
				this.aggregateFunctionTypeSelector.setValue(SQLAggregateFunctionBasedInputVariable.getAggregateFunctionType(), isEmpty);
				this.targetRecordMetadataIDSelector.setValue(SQLAggregateFunctionBasedInputVariable.getTargetRecordMetadataID(), isEmpty);
				
				
				if(SQLAggregateFunctionBasedInputVariable.getAggregateFunctionType().noInputColumnIsNeeded()) {
					//do nothing since the status change of aggregateFunctionTypeSelector and targetRecordMetadataIDSelector will set both recordwiseInputVariable1Builder
					//and recordwiseInputVariable2Builder to null
				}else {
					this.recordwiseInputVariable1Builder.setValue(SQLAggregateFunctionBasedInputVariable.getRecordwiseInputVariable1(), isEmpty);
					
					if(SQLAggregateFunctionBasedInputVariable.getAggregateFunctionType().getRequiredNumOfColumn()==2) {
						this.recordwiseInputVariable2Builder.setValue(SQLAggregateFunctionBasedInputVariable.getRecordwiseInputVariable2(), isEmpty);
					}
				}
			}
		}
		
		return changed;
	}
	
	
	/**
	 * build and return a FreeInputVariable;
	 * 
	 * the IndependentFreeInputVariableTypeImpl parameter should be built based on the value of the toBuildNewIndependentFreeInputVariableTypefromScratchBuilder
	 */
	@Override
	protected SQLAggregateFunctionBasedInputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = this.notesBuilder.getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		return new SQLAggregateFunctionBasedInputVariable(
//				this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
				hostCompositionFunctionID,  
				hostComponentFunctionIndexID, 
				hostEvaluatorIndexID,
				aliasNameValue, 
				notesValue,
				
				this.aggregateFunctionTypeSelector.getCurrentValue(),
				this.targetRecordMetadataIDSelector.getCurrentValue(),
				(RecordwiseInputVariable)this.recordwiseInputVariable1Builder.getCurrentValue(),
				(RecordwiseInputVariable)this.recordwiseInputVariable2Builder.getCurrentValue()
				);

	}		

}
