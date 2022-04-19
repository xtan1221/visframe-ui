package builder.visframe.operation.sql.predefined.type;

import operation.sql.predefined.type.GroupAndBinCountOperation;
import operation.sql.predefined.utils.SqlSortOrderType;
import rdb.table.data.DataTableColumnName;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.basic.misc.SimpleTypeSelector;
import builder.basic.primitive.DoubleTypeBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationBuilder;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import metadata.record.RecordDataMetadata;

import static operation.sql.predefined.type.GroupAndBinCountOperation.*;


public final class GroupAndBinCountOperationBuilder extends SingleInputRecordDataPredefinedSQLOperationBuilder<GroupAndBinCountOperation>{
	
	public static final String NODE_NAME = "GroupAndBinCountOperation";
	public static final String NODE_DESCRIPTION = "GroupAndBinCountOperation";
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public GroupAndBinCountOperationBuilder(VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		// TODO Auto-generated constructor stub
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	
	private List<SqlSortOrderType> getSqlSortOrderTypeList(){
		List<SqlSortOrderType> ret = new ArrayList<>();
		
		for(SqlSortOrderType type:SqlSortOrderType.values()) {
			ret.add(type);
		}
		
		return ret;
	}
	/////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	private Map<SimpleName, Object> buildGroupAndBinCountOperationLevelSpecificParameterNameValueObjectMap(){
		LinkedHashSet<DataTableColumnName> groupByColumnNameSet = (LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue()).getCurrentValue();
		DataTableColumnName numericColumnNameToSortAndBin = (DataTableColumnName) this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue()).getCurrentValue();
		SqlSortOrderType numericColumnSortType = (SqlSortOrderType) this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_SORT_TYPE.getName().getStringValue()).getCurrentValue();
		double binSize = (double) this.getChildrenNodeBuilderNameMap().get(BIN_SIZE.getName().getStringValue()).getCurrentValue();
		Double binMin = (Double) this.getChildrenNodeBuilderNameMap().get(BIN_MIN.getName().getStringValue()).getCurrentValue();
		Double binMax = (Double) this.getChildrenNodeBuilderNameMap().get(BIN_MAX.getName().getStringValue()).getCurrentValue();
		
		
		return GroupAndBinCountOperation.buildGroupAndBinCountOperationLevelSpecificParameterNameValueObjectMap(
				groupByColumnNameSet, numericColumnNameToSortAndBin, numericColumnSortType, binSize, binMin, binMax);
		
	}
	
	
	/////////////////////////////////////////////
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//GROUP_BY_COLUMN_NAME_SET
		//pool is set when INPUT_RECORD_METADATAID is selected
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				GROUP_BY_COLUMN_NAME_SET.getName().getStringValue(), 
				GROUP_BY_COLUMN_NAME_SET.getDescriptiveName(), 
				GROUP_BY_COLUMN_NAME_SET.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		//NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN
		//pool is set when the input record data metadata is selected (INPUT_RECORD_METADATAID)
		this.addChildNodeBuilder(new SimpleTypeSelector<DataTableColumnName>(
				NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue(), 
				NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getDescriptiveName(), 
				NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return e.getStringValue();},
				e->{return e.getStringValue();}
				));

		//NUMERIC_COLUMN_SORT_TYPE
		SimpleTypeSelector<SqlSortOrderType> sqlSortOrderTypeSelector = new SimpleTypeSelector<SqlSortOrderType>(
				NUMERIC_COLUMN_SORT_TYPE.getName().getStringValue(), 
				NUMERIC_COLUMN_SORT_TYPE.getDescriptiveName(), 
				NUMERIC_COLUMN_SORT_TYPE.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return e.toString();},
				e->{return e.getDescription();}
				);
		sqlSortOrderTypeSelector.setPool(this.getSqlSortOrderTypeList());
		
		this.addChildNodeBuilder(sqlSortOrderTypeSelector);
		
		//BIN_SIZE
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				BIN_SIZE.getName().getStringValue(), BIN_SIZE.getDescriptiveName(), 
				BIN_SIZE.canHaveNullValueObject(this.isForReproducing()), this,
				
				BIN_SIZE.getNonNullValueAdditionalConstraints(),
				"BIN Size must be positive number!"
				));
		
		//BIN_MIN
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				BIN_MIN.getName().getStringValue(), BIN_MIN.getDescriptiveName(), 
				BIN_MIN.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return true;},
				"BIN MIN can be of any numeric value"
				));
		
		//BIN_MAX
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				BIN_MAX.getName().getStringValue(), BIN_MAX.getDescriptiveName(), 
				BIN_MAX.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return true;},
				"BIN_MAX can be of any numeric value!"
				));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//GROUP_BY_COLUMN_NAME_SET can be empty set, thus no need to check!!!
		
		
		//GROUP_BY_COLUMN_NAME_SET cannot contain NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN
		Set<String> set1 = new HashSet<>();
		set1.add(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
		set1.add(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<GroupAndBinCountOperation> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "GROUP_BY_COLUMN_NAME_SET cannot contain NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
					
					DataTableColumnName numericColumnNameToSortAndBinColumnName = 
							(DataTableColumnName) e.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue()).getCurrentValue();
					
					return !groupByColumnNameSetBuilder.getCurrentValue().contains(numericColumnNameToSortAndBinColumnName);
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
		//prepare
		MetadataIDSelector inputRecordDataMetadataIDSelector = (MetadataIDSelector) this.getChildrenNodeBuilderNameMap().get(INPUT_RECORD_METADATAID.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
		
		@SuppressWarnings("unchecked")
		SimpleTypeSelector<DataTableColumnName> numericColumnNameToSortAndBinSelector = 
				(SimpleTypeSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue());
		
		//===============================
		//when super.INPUT_RECORD_METADATAID changed, following need to change accordingly
		//1. GROUP_BY_COLUMN_NAME_SET: reset pool and set to default empty
		//2. NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN: reset pool and set to default empty
		Runnable inputRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {//default empty, the nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap cannot be started;
					
					groupByColumnNameSetBuilder.setPool(new ArrayList<>());
					
					groupByColumnNameSetBuilder.setToDefaultEmpty();
					
					
					numericColumnNameToSortAndBinSelector.setPool(new ArrayList<>());
					numericColumnNameToSortAndBinSelector.setToDefaultEmpty();
					
				}else if(inputRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					try {
						
						RecordDataMetadata selectedinputRecordDataMetadata = 
								(RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputRecordDataMetadataIDSelector.getCurrentValue());
						
						groupByColumnNameSetBuilder.setPool(selectedinputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName());
						
						List<DataTableColumnName> numericDataTableColumnNameList = new ArrayList<>();
						selectedinputRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
							if(e.getSqlDataType().isNumeric()) {
								numericDataTableColumnNameList.add(e.getName());
							}
						});
						
						numericColumnNameToSortAndBinSelector.setPool(numericDataTableColumnNameList);
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(1);
					}
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		inputRecordDataMetadataIDSelector.addStatusChangedAction(
				inputRecordDataMetadataIDSelectorStatusChangeEventAction);
				
		
	}
	
	
	////////////////////////////////////////

	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_SORT_TYPE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(BIN_SIZE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(BIN_MIN.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(BIN_MAX.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GroupAndBinCountOperation groupAndBinCountOperation = (GroupAndBinCountOperation)value;
				
				//need to first set the pools of GROUP_BY_COLUMN_NAME_SET and NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN
				try {
					RecordDataMetadata inputRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(groupAndBinCountOperation.getInputRecordDataMetadataID());
					
					//set pools of inputNodeDataTableColumnSetAsNodeIDBuilder and inputNodeDataTableColumnSetAsAdditionalFeatureBuilder
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					SimpleTypeSelector<DataTableColumnName> numericColumnNameToSortAndBinSelector = 
							(SimpleTypeSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue());
					
					
					//
					groupByColumnNameSetBuilder.setPool(inputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					numericColumnNameToSortAndBinSelector.setPool(inputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName()); 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				
				////
				this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getGroupByColumnNameSet().getSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_NAME_TO_SORT_AND_BIN.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getNumericColumnNameToSortAndBin(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(NUMERIC_COLUMN_SORT_TYPE.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getNumericColumnSortType(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(BIN_SIZE.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getBinSize(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(BIN_MIN.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getBinMin(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(BIN_MAX.getName().getStringValue()).setValue(
						groupAndBinCountOperation.getBinMax(), isEmpty);
				/////////////////
				this.checkIfForReproducing(groupAndBinCountOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}

	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since GroupAndBinCountOperation type does not have parameter dependent on input data table content; 
	}
	
	@Override
	protected GroupAndBinCountOperation build() {
		return new GroupAndBinCountOperation(
//				this.isForReproducing(),
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSQLOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildPredefinedSQLBasedOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleInputRecordDataPredefinedSQLOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildGroupAndBinCountOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder ???
				);
	}

}
