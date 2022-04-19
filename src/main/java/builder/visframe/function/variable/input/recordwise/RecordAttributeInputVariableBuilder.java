package builder.visframe.function.variable.input.recordwise;

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
import builder.visframe.metadata.MetadataIDSelector;
import core.builder.NonLeafNodeBuilder;
import function.composition.CompositionFunctionID;
import function.variable.input.recordwise.type.RecordAttributeInputVariable;
import metadata.DataType;
import metadata.MetadataID;
import metadata.record.RecordDataMetadata;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableSchemaID;


/**
 *  
 * 
 * @author tanxu
 *
 */
public final class RecordAttributeInputVariableBuilder extends RecordwiseInputVariableBuilder<RecordAttributeInputVariable>{
	
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
	 * @param mustBeOfSameOwnerRecordDataWithHostCompositionFunction 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public RecordAttributeInputVariableBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder, 
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder,
			SimpleName predefinedAliasName,
			Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints,
			Predicate<MetadataID> targetRecordDataMetadataIDCondition) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder, hostEvaluatorBuilder, predefinedAliasName, dataTypeConstraints, targetRecordDataMetadataIDCondition);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	private Set<DataType> getDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.RECORD);
		return ret;
	}
	
	
	////////////////////////////////////
	protected static final String targetRecordDataMetadataID = "targetRecordDataMetadataID";
	protected static final String targetRecordDataMetadataID_description = "targetRecordDataMetadataID";
	
	protected static final String column = "column";
	protected static final String column_description = "column";
	
	////////////////////////
	private MetadataIDSelector targetRecordDataMetadataIDSelector;
	
	private SimpleTypeSelector<DataTableColumn> columnSelector;
	
	/**
	 * @return the targetRecordDataMetadataIDSelector
	 */
	public MetadataIDSelector getTargetRecordDataMetadataIDSelector() {
		return targetRecordDataMetadataIDSelector;
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
		//recordDataMetadataID
		targetRecordDataMetadataIDSelector = new MetadataIDSelector(
				targetRecordDataMetadataID, targetRecordDataMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
				this.getDataTypeSet(),
				e->{return this.getTargetRecordDataMetadataIDCondition().test(e.getID());}//Predicate<Metadata> filteringCondition for allowed record metadata
				);
//		if(this.getHostComponentFunctionBuilder().getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID()) {
//			//the record data must be the same with owner record data of host CompositionFunction, which is already known, thus do not need to be selected;
//		}else {
		this.addChildNodeBuilder(targetRecordDataMetadataIDSelector);
//		}
		
		
		
		//target
		columnSelector = new SimpleTypeSelector<>(
				column, column_description, false, this, 
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return "name=".concat(e.getName().getStringValue()).concat("; data type=").concat(e.getSqlDataType().getSQLString());}//Function<T,String> typeToDescriptionFunction
				);
		this.addChildNodeBuilder(this.columnSelector);
		
//		if(this.mustBeOfSameOwnerRecordDataWithHostCompositionFunction()) {//set the pool and never change again
//			try {
//				RecordDataMetadata ownerRecordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(
//						this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());
//				
//				List<DataTableColumn> pool = new ArrayList<>();
//				ownerRecordData.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(col->{
//					if(this.getDataTypeConstraints().test(col.getSqlDataType()))
//						pool.add(col);
//				});
//				
//				this.columnSelector.setPool(pool);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.exit(1);
//			}
//		}
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//when recordDataMetadataIDSelector's status is changed (this also implies that mustBeOfSameOwnerRecordDataWithHostCompositionFunction is false)
		//columnSelector should change accordingly based on the status of recordDataMetadataIDSelector accordingly;
		Runnable recordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(targetRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {
					//no cfgID selected, set the pool of the target selector to empty set;
					this.columnSelector.setPool(new ArrayList<>());
					this.columnSelector.setToDefaultEmpty();
					
				}else if(targetRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
						RecordDataMetadata ownerRecordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(
								this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID());
						
						List<DataTableColumn> pool = new ArrayList<>();
						ownerRecordData.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(col->{
							if(this.getDataTypeConstraints().test(col.getSqlDataType()))
								pool.add(col);
						});
						this.columnSelector.setPool(pool);
					
				}
			} catch (SQLException |IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
		};
		
		targetRecordDataMetadataIDSelector.addStatusChangedAction(
				recordDataMetadataIDSelectorStatusChangeEventAction);
		
	}
	
	///////////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
//			if(!this.mustBeOfSameOwnerRecordDataWithHostCompositionFunction())//
			this.targetRecordDataMetadataIDSelector.setValue(null, isEmpty);
			
			this.columnSelector.setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				RecordAttributeInputVariable recordDataTableColumnInputVariable = (RecordAttributeInputVariable)value;
				
//				if(!this.mustBeOfSameOwnerRecordDataWithHostCompositionFunction())//
				this.targetRecordDataMetadataIDSelector.setValue(recordDataTableColumnInputVariable.getTargetRecordDataMetadataID(), isEmpty);
				
				this.columnSelector.setValue(recordDataTableColumnInputVariable.getColumn(), isEmpty);
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
	protected RecordAttributeInputVariable build() {
		CompositionFunctionID hostCompositionFunctionID = this.getHostCompositionFunctionBuilder().getCompositionFunctionID();
		
		SimpleName aliasNameValue = this.getAliasName();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		int hostComponentFunctionIndexID = this.getHostComponentFunctionBuilder().getIndexID();
		int hostEvaluatorIndexID = this.getHostEvaluatorBuilder().getIndexID();
		
		/////
		MetadataID targetRecordDataMetadataID;
//		if(this.mustBeOfSameOwnerRecordDataWithHostCompositionFunction()) {
//			targetRecordDataMetadataID = this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID();
//		}else {
		targetRecordDataMetadataID = this.targetRecordDataMetadataIDSelector.getCurrentValue();
//		}
		
		try {
			RecordDataMetadata ownerRecordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(
					targetRecordDataMetadataID);
			DataTableSchemaID schemaID = ownerRecordData.getDataTableSchema().getID();
			DataTableColumn column = this.columnSelector.getCurrentValue();
			
			return new RecordAttributeInputVariable(
//					this.getHostCompositionFunctionBuilder().getOwnerCompositionFunctionGroup().getOwnerRecordDataMetadataID(),
					hostCompositionFunctionID, hostComponentFunctionIndexID, hostEvaluatorIndexID,
					aliasNameValue, notesValue,
					
					targetRecordDataMetadataID, schemaID, column
					);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	
		//should never be reached! TODO?
		return null;
	}		

}
