package builder.visframe.operation.sql.predefined.type;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.misc.DoubleListSelector;
import builder.basic.collection.set.leaf.FixedPoolLinkedHashSetSelector;
import builder.basic.primitive.DoubleTypeBuilder;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import builder.visframe.operation.sql.predefined.SingleInputRecordDataPredefinedSQLOperationBuilder;
import builder.visframe.operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegateBuilder;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import metadata.record.RecordDataMetadata;
import operation.sql.predefined.type.AddNumericCumulativeColumnOperation;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import operation.sql.predefined.utils.SqlSortOrderType;
import rdb.table.data.DataTableColumnName;
import utils.CollectionUtils;
import utils.Pair;

import static operation.sql.predefined.type.AddNumericCumulativeColumnOperation.*;


public final class AddNumericCumulativeColumnOperationBuilder extends SingleInputRecordDataPredefinedSQLOperationBuilder<AddNumericCumulativeColumnOperation>{
	
	public static final String NODE_NAME = "AddNumericCumulativeColumnOperation";
	public static final String NODE_DESCRIPTION = "AddNumericCumulativeColumnOperation";
	
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param resultedFromReproducing
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public AddNumericCumulativeColumnOperationBuilder(
			VisProjectDBContext hostVisProjectDBContext, boolean resultedFromReproducing) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, false, null, hostVisProjectDBContext, resultedFromReproducing);
		
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
	private Map<SimpleName, Object> buildAddNumericCumulativeColumnOperationLevelSpecificParameterNameValueObjectMap(){
		LinkedHashSet<DataTableColumnName> groupByColumnNameSet = (LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue()).getCurrentValue();
		
		DoubleListSelector<DataTableColumnName, SqlSortOrderType> orderByColumnNameSetAndSortTypeListSelector = 
				(DoubleListSelector<DataTableColumnName, SqlSortOrderType>) this.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList);
		
		LinkedHashSet<DataTableColumnName> orderByColumnNameSet = orderByColumnNameSetAndSortTypeListSelector.getCurrentValue().getFirst();
		ArrayList<SqlSortOrderType> orderByColumnSortTypeList = orderByColumnNameSetAndSortTypeListSelector.getCurrentValue().getSecond();
		
		
		LinkedHashSet<DataTableColumnName> otherKeptNonPKColumnNameSet = 
				(LinkedHashSet<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue()).getCurrentValue();
		
		CumulativeColumnSymjaExpressionDelegate cumulativeColumnSymjaExpression = 
				(CumulativeColumnSymjaExpressionDelegate) this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue()).getCurrentValue();
		
		DataTableColumnName cumulativeColumnNameInOutputDataTable = 
				(DataTableColumnName) this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue()).getCurrentValue();
		
		double initialValue = (double) this.getChildrenNodeBuilderNameMap().get(INITIAL_VALUE.getName().getStringValue()).getCurrentValue();
		
		
		return AddNumericCumulativeColumnOperation.buildAddNumericCumulativeColumnOperationLevelSpecificParameterNameValueObjectMap(
				groupByColumnNameSet, orderByColumnNameSet, orderByColumnSortTypeList, otherKeptNonPKColumnNameSet, 
				cumulativeColumnSymjaExpression, cumulativeColumnNameInOutputDataTable, initialValue);
	}
	
	
	/////////////////////////////////////////////
	protected static final String orderByColumnNameSetAndSortTypeList = "orderByColumnNameSetAndSortTypeList";
	protected static final String orderByColumnNameSetAndSortTypeList_description = "orderByColumnNameSetAndSortTypeList";

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
		
		//ORDER_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_SORT_TYPE_LIST
		this.addChildNodeBuilder(new DoubleListSelector<DataTableColumnName, SqlSortOrderType>(
				orderByColumnNameSetAndSortTypeList, 
				orderByColumnNameSetAndSortTypeList_description, 
				false, this,
				
				e->{return e.getStringValue();},
				e->{return e.toString();}
				));
		
		
		//OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET
		this.addChildNodeBuilder(new FixedPoolLinkedHashSetSelector<DataTableColumnName>(
				OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue(), 
				OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getDescriptiveName(), 
				OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.canHaveNullValueObject(this.isForReproducing()), this,
				e->{return e.getStringValue();}
			));
		
		
		
		//CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION
		this.addChildNodeBuilder(new CumulativeColumnSymjaExpressionDelegateBuilder(
				CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue(), 
				CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getDescriptiveName(), 
				CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.canHaveNullValueObject(this.isForReproducing()), this));
		
		
		//CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE
		this.addChildNodeBuilder(new VfNameStringBuilder<DataTableColumnName>(
				CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue(), 
				CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getDescriptiveName(), 
				CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.canHaveNullValueObject(this.isForReproducing()), this,
				DataTableColumnName.class
				));
		
		//INITIAL_VALUE
		this.addChildNodeBuilder(new DoubleTypeBuilder(
				INITIAL_VALUE.getName().getStringValue(), INITIAL_VALUE.getDescriptiveName(), 
				INITIAL_VALUE.canHaveNullValueObject(this.isForReproducing()), this,
				
				e->{return true;},""//domain and description
				));
		
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//GROUP_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_NAME_SET and OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET can all be empty set
		//thus no constraints 
		
		
		
		//GROUP_BY_COLUMN_NAME_SET, ORDER_BY_COLUMN_NAME_SET and OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET must be disjoint with each other (if not empty)
		Set<String> set1 = new HashSet<>();
		set1.add(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
		set1.add(orderByColumnNameSetAndSortTypeList);
		set1.add(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<AddNumericCumulativeColumnOperation> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "GROUP_BY_COLUMN_NAME_SET, ORDER_BY_COLUMN_NAME_SET and OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET must be disjoint with each other (if not empty)!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					DoubleListSelector<DataTableColumnName, SqlSortOrderType> orderByColumnNameSetAndSortTypeListSelector = 
						(DoubleListSelector<DataTableColumnName, SqlSortOrderType>) e.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList);
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> otherKeptNonPKColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
					
					//all three sets are disjoint
					return CollectionUtils.setsAreDisjoint(groupByColumnNameSetBuilder.getCurrentValue(), orderByColumnNameSetAndSortTypeListSelector.getCurrentValue().getFirst()) &&
							CollectionUtils.setsAreDisjoint(groupByColumnNameSetBuilder.getCurrentValue(), otherKeptNonPKColumnNameSetBuilder.getCurrentValue()) &&
							CollectionUtils.setsAreDisjoint(orderByColumnNameSetAndSortTypeListSelector.getCurrentValue().getFirst(), otherKeptNonPKColumnNameSetBuilder.getCurrentValue()) ;
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		
		
		//ORDER_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_SORT_TYPE_LIST should have the same size, which is implicitly enforced by DoubleListSelector with name orderByColumnNameSetAndSortTypeList
		//thus no need to check
		
		
		
		
		//CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE must be different from those in GROUP_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_NAME_SET and OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET
		Set<String> set2 = new HashSet<>();
		set2.add(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
		set2.add(orderByColumnNameSetAndSortTypeList);
		set2.add(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
		set2.add(CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue());
		GenricChildrenNodeBuilderConstraint<AddNumericCumulativeColumnOperation> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE must be different from those in GROUP_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_NAME_SET and OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET!",
				set2, 
				e->{
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
					
					@SuppressWarnings("unchecked")
					DoubleListSelector<DataTableColumnName, SqlSortOrderType> orderByColumnNameSetAndSortTypeListSelector = 
						(DoubleListSelector<DataTableColumnName, SqlSortOrderType>) e.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList);
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> otherKeptNonPKColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) e.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
					
					DataTableColumnName cumulativeColumnNameInOutputDataTable = (DataTableColumnName) e.getChildrenNodeBuilderNameMap().get(CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue()).getCurrentValue();
					
					
					return !groupByColumnNameSetBuilder.getCurrentValue().contains(cumulativeColumnNameInOutputDataTable) && 
							!orderByColumnNameSetAndSortTypeListSelector.getCurrentValue().getFirst().contains(cumulativeColumnNameInOutputDataTable) &&
							!otherKeptNonPKColumnNameSetBuilder.getCurrentValue().contains(cumulativeColumnNameInOutputDataTable);
							
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		
		
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
		DoubleListSelector<DataTableColumnName, SqlSortOrderType> orderByColumnNameSetAndSortTypeListSelector = 
			(DoubleListSelector<DataTableColumnName, SqlSortOrderType>) this.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList);
		
		
		@SuppressWarnings("unchecked")
		FixedPoolLinkedHashSetSelector<DataTableColumnName> otherKeptNonPKColumnNameSetSelector = 
				(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
		
		CumulativeColumnSymjaExpressionDelegateBuilder cumulativeNumericColumnSymjaExpressionBuilder = 
				(CumulativeColumnSymjaExpressionDelegateBuilder) this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue());
		
		//===============================
		//when super.INPUT_RECORD_METADATAID changed, following need to change accordingly
		//1. GROUP_BY_COLUMN_NAME_SET: reset pool and set to default empty
		//2. ORDER_BY_COLUMN_NAME_SET and ORDER_BY_COLUMN_SORT_TYPE_LIST == orderByColumnNameSetAndSortTypeList: reset pool and set to default empty
		//3. OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET: reset pool and set to default empty
		//4. CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION: reset data table schema and set to default empty
		
		Runnable inputRecordDataMetadataIDSelectorStatusChangeEventAction = ()->{
			try {
				if(inputRecordDataMetadataIDSelector.getCurrentStatus().isDefaultEmpty()) {//default empty, the nodeIDColumnNameEdgeSourceAndSourceNodeIDColumnNameMap cannot be started;
					
					groupByColumnNameSetBuilder.setPool(new ArrayList<>());
					
					groupByColumnNameSetBuilder.setToDefaultEmpty();
					
					
					orderByColumnNameSetAndSortTypeListSelector.setPools(new ArrayList<>(), new ArrayList<>());
					orderByColumnNameSetAndSortTypeListSelector.setToDefaultEmpty();
					
					otherKeptNonPKColumnNameSetSelector.setPool(new ArrayList<>());
					otherKeptNonPKColumnNameSetSelector.setToDefaultEmpty();
					
					cumulativeNumericColumnSymjaExpressionBuilder.setDataTableSchema(null);
					cumulativeNumericColumnSymjaExpressionBuilder.setToDefaultEmpty();
					
				}else if(inputRecordDataMetadataIDSelector.getCurrentStatus().isSetToNull()){//never happen since it cannot be null;
					//skip since this will never happen;
				}else {//non-null valid value
					
					RecordDataMetadata selectedinputRecordDataMetadata = 
							(RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(inputRecordDataMetadataIDSelector.getCurrentValue());
					//
					groupByColumnNameSetBuilder.setPool(selectedinputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName());
					
					//
					orderByColumnNameSetAndSortTypeListSelector.setPools(
							selectedinputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName(), this.getSqlSortOrderTypeList());
					
					
					List<DataTableColumnName> nonPKColumnNameList = new ArrayList<>();
					selectedinputRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
						if(!e.isInPrimaryKey()) {
							nonPKColumnNameList.add(e.getName());
						}
					});
					
					otherKeptNonPKColumnNameSetSelector.setPool(nonPKColumnNameList);
					
					
					cumulativeNumericColumnSymjaExpressionBuilder.setDataTableSchema(selectedinputRecordDataMetadata.getDataTableSchema());
						
				}
			} catch (SQLException | IOException e) {
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
			this.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue()).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(INITIAL_VALUE.getName().getStringValue()).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				AddNumericCumulativeColumnOperation addNumericCumulativeColumnOperation = (AddNumericCumulativeColumnOperation)value;
				
				//need to first set the pools of GROUP_BY_COLUMN_NAME_SET and orderByColumnNameSetAndSortTypeList, OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET
				//also set the data table schema of CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION
				try {
					RecordDataMetadata inputRecordDataMetadata = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(addNumericCumulativeColumnOperation.getInputRecordDataMetadataID());
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> groupByColumnNameSetBuilder = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue());
					
					
					@SuppressWarnings("unchecked")
					DoubleListSelector<DataTableColumnName, SqlSortOrderType> orderByColumnNameSetAndSortTypeListSelector = 
						(DoubleListSelector<DataTableColumnName, SqlSortOrderType>) this.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList);
					
					
					@SuppressWarnings("unchecked")
					FixedPoolLinkedHashSetSelector<DataTableColumnName> otherKeptNonPKColumnNameSetSelector = 
							(FixedPoolLinkedHashSetSelector<DataTableColumnName>) this.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue());
					
					CumulativeColumnSymjaExpressionDelegateBuilder cumulativeNumericColumnSymjaExpressionBuilder = 
							(CumulativeColumnSymjaExpressionDelegateBuilder) this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue());
				
					
					////////////////////////////////
					groupByColumnNameSetBuilder.setPool(inputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName());
					
					//
					orderByColumnNameSetAndSortTypeListSelector.setPools(
							inputRecordDataMetadata.getDataTableSchema().getOrderListOfNonRUIDColumnName(), this.getSqlSortOrderTypeList());
					
					//
					List<DataTableColumnName> nonPKColumnNameList = new ArrayList<>();
					inputRecordDataMetadata.getDataTableSchema().getOrderedListOfNonRUIDColumn().forEach(e->{
						if(!e.isInPrimaryKey()) {
							nonPKColumnNameList.add(e.getName());
						}
					});
					otherKeptNonPKColumnNameSetSelector.setPool(nonPKColumnNameList);
					
					
					////
					cumulativeNumericColumnSymjaExpressionBuilder.setDataTableSchema(inputRecordDataMetadata.getDataTableSchema());
					
				} catch (SQLException e) {
					e.printStackTrace();
					System.exit(1);
				}
				
				/////set values
				this.getChildrenNodeBuilderNameMap().get(GROUP_BY_COLUMN_NAME_SET.getName().getStringValue()).setValue(
						addNumericCumulativeColumnOperation.getGroupByColumnNameSet().getSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(orderByColumnNameSetAndSortTypeList).setValue(
						new Pair<>(
								addNumericCumulativeColumnOperation.getOrderByColumnNameSet().getSet(),
								addNumericCumulativeColumnOperation.getOrderByColumnSortTypeList().getList()),
						isEmpty);
				this.getChildrenNodeBuilderNameMap().get(OTHER_KEPT_NON_PRIMARY_KEK_COLUMN_NAME_SET.getName().getStringValue()).setValue(
						addNumericCumulativeColumnOperation.getOtherKeptNonPKColumnNameSet().getSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_NUMERIC_COLUMN_SYMJA_EXPRESSION.getName().getStringValue()).setValue(
						addNumericCumulativeColumnOperation.getCumulativeNumericColumnSymjaExpression(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(CUMULATIVE_COLUMN_NAME_IN_OUTPUT_DATA_TABLE.getName().getStringValue()).setValue(
						addNumericCumulativeColumnOperation.getCumulativeNumericColumnNameInOutputDataTable(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(INITIAL_VALUE.getName().getStringValue()).setValue(
						addNumericCumulativeColumnOperation.getInitialValue(), isEmpty);
				
				/////////////////
				this.checkIfForReproducing(addNumericCumulativeColumnOperation.hasInputDataTableContentDependentParameter());
			}
		}
		
		return changed;
	}
	
	@Override
	protected void setChildNodeBuilderForParameterDependentOnInputDataTableContentToModifiable() {
		//do nothing since AddNumericCumulativeColumnOperation type does not have parameter dependent on input data table content; 
	} 
	
	@Override
	protected AddNumericCumulativeColumnOperation build() {
		return new AddNumericCumulativeColumnOperation(
				this.buildAbstractOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSQLOperationBaseLevelSpecificParameterNameValueObjectMap(),
				this.buildPredefinedSQLBasedOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildSingleInputRecordDataPredefinedSQLOperationLevelSpecificParameterNameValueObjectMap(),
				this.buildAddNumericCumulativeColumnOperationLevelSpecificParameterNameValueObjectMap(),
				true //toCheckConstraintsRelatedWithParameterDependentOnInputDataTableContent always true for operation builder???
				);
	}


}
