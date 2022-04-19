package builder.visframe.operation.sql.generic.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import builder.basic.collection.list2.FixedSizeArrayListNonLeafNodeBuilder;
import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.visframe.rdb.table.data.DataTableColumnBuilderFactory;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import operation.sql.generic.GenericSQLQuery;
import operation.sql.generic.utils.GenericSQLQueryProcessor;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;
import operation.sql.generic.DataTableAndColumnsMapping;

/**
 * 
 * 
 * 
 * @author tanxu
 *
 */
public class GenericSqlQueryBuilder extends NonLeafNodeBuilder<GenericSQLQuery>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public GenericSqlQueryBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext
			) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		//
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}


	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	///////////////////////////////////////////
	protected static final String sqlStringWithAliasedIDs = "sqlStringWithAliasedIDs";
	protected static final String sqlStringWithAliasedIDs_description = "sqlStringWithAliasedIDs";
	
	protected static final String tableAliasNameMappingMap = "tableAliasNameMappingMap";
	protected static final String tableAliasNameMappingMap_description = "tableAliasNameMappingMap";
	
	protected static final String orderedListOfColumnInOutputDataTable = "orderedListOfColumnInOutputDataTable";
	protected static final String orderedListOfColumnInOutputDataTable_description = "orderedListOfColumnInOutputDataTable";
	
	private GenericSqlQueryProcessorBuilder genericSqlQueryProcessorBuilder;
	
	private FixedKeySetMapValueBuilder<String, DataTableAndColumnsMapping> tableAliasNameMappingMapBuilder;	
	
	private FixedSizeArrayListNonLeafNodeBuilder<DataTableColumn> orderedListOfColumnInOutputDataTableBuilder;
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.genericSqlQueryProcessorBuilder = new GenericSqlQueryProcessorBuilder(
				sqlStringWithAliasedIDs, sqlStringWithAliasedIDs_description, false, this);
		this.addChildNodeBuilder(this.genericSqlQueryProcessorBuilder);
		
		
		//
		TableAndColumnsMappingBuilderFactory tableAndColumnsMappingBuilderFactory = new TableAndColumnsMappingBuilderFactory(
				"","",false,this.getHostVisProjectDBContext());
		tableAliasNameMappingMapBuilder = new FixedKeySetMapValueBuilder<>(
				tableAliasNameMappingMap, tableAliasNameMappingMap_description, false, this, 
				tableAndColumnsMappingBuilderFactory,
				
				e->{return e;},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e;},//Function<K,String> mapKeyToDescriptionFunction,
				null,//BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction, TODO
				
				false, //boolean allowingNullMapValue,
				false //boolean allowingDuplicateMapValue
				);
		this.addChildNodeBuilder(tableAliasNameMappingMapBuilder);
		
		//
		DataTableColumnBuilderFactory dataTableColumnBuilderFactory = new DataTableColumnBuilderFactory(
				"dataTableColumn", "dataTableColumn", false);
		
		this.orderedListOfColumnInOutputDataTableBuilder = new FixedSizeArrayListNonLeafNodeBuilder<>(
				orderedListOfColumnInOutputDataTable, orderedListOfColumnInOutputDataTable_description, false, this,
				dataTableColumnBuilderFactory, //listElementNodeBuilderFactory
				false);//duplicateElementAllowed
		
		this.addChildNodeBuilder(this.orderedListOfColumnInOutputDataTableBuilder);
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//columns in orderedListOfColumnInOutputDataTableBuilder must have different names;
		Set<String> involvedSiblingNodeNameSet = new HashSet<>();
		involvedSiblingNodeNameSet.add(orderedListOfColumnInOutputDataTableBuilder.getName());
		this.addGenricChildrenNodeBuilderConstraint(
				new GenricChildrenNodeBuilderConstraint<>(
						this, "columns in orderedListOfColumnInOutputDataTableBuilder must have different names!",
						involvedSiblingNodeNameSet, 
						e->{
							GenericSqlQueryBuilder builder = (GenericSqlQueryBuilder) e;
							
							Set<DataTableColumnName> colNameSet = new HashSet<>();
							
							for(DataTableColumn col:builder.orderedListOfColumnInOutputDataTableBuilder.getCurrentValue()) {
								if(colNameSet.contains(col.getName()))
									return false;
								colNameSet.add(col.getName());
							}
							
							return true;
						}
					));
		
		//at least one column in orderedListOfColumnInOutputDataTableBuilder should be in primary key;
		Set<String> involvedSiblingNodeNameSet2 = new HashSet<>();
		involvedSiblingNodeNameSet2.add(orderedListOfColumnInOutputDataTableBuilder.getName());
		this.addGenricChildrenNodeBuilderConstraint(
				new GenricChildrenNodeBuilderConstraint<>(
						this, "at least one column in orderedListOfColumnInOutputDataTableBuilder should be in primary key!",
						involvedSiblingNodeNameSet2, 
						e->{
							GenericSqlQueryBuilder builder = (GenericSqlQueryBuilder)e;
							for(DataTableColumn col:builder.orderedListOfColumnInOutputDataTableBuilder.getCurrentValue()) {
								if(col.isInPrimaryKey())
									return true;
							}
							
							return false;
						}
					));
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//when genericSqlQueryProcessorBuilder's status changes
		Runnable genericSqlQueryProcessorBuilderChangeEventAction = ()->{
			try {
				if(genericSqlQueryProcessorBuilder.isDefaultEmpty()) {
					
					this.tableAliasNameMappingMapBuilder.setToDefaultEmpty();
					
					this.orderedListOfColumnInOutputDataTableBuilder.setToDefaultEmpty();
					
				}else {//valid value
					GenericSQLQueryProcessor genericSqlQueryProcessor = this.genericSqlQueryProcessorBuilder.getCurrentValue();
					
					//
					this.tableAliasNameMappingMapBuilder.setKeySet(genericSqlQueryProcessor.getTableNameSet());
					
					this.tableAliasNameMappingMapBuilder.getMapKeyValueBuilderMap().forEach((k,v)->{//k is the table name, v is the TableAndColumnsMappingBuilder
						TableAndColumnsMappingBuilder tableAndColumnsMappingBuilder = (TableAndColumnsMappingBuilder)v;
						Set<String> columnNameStringSet = new LinkedHashSet<>();
						genericSqlQueryProcessor.getTableNameDottedFullColumnNameSetMap().get(k).forEach(e->{
							columnNameStringSet.add(e.getColName());
						});
						tableAndColumnsMappingBuilder.setTableAndColumnNameSet(k, columnNameStringSet);
					});
					
					//reset the size of the list builder for columns to the number of the expression/terms in the SELECT clause;
					this.orderedListOfColumnInOutputDataTableBuilder.setSize(genericSqlQueryProcessor.getSelectElementNum());
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		};
		
		
		this.genericSqlQueryProcessorBuilder.addStatusChangedAction(
				genericSqlQueryProcessorBuilderChangeEventAction);
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.genericSqlQueryProcessorBuilder.setToDefaultEmpty();
			this.tableAliasNameMappingMapBuilder.setToDefaultEmpty();
			this.orderedListOfColumnInOutputDataTableBuilder.setToDefaultEmpty();
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				GenericSQLQuery genericSqlQuery = (GenericSQLQuery)value;
				this.genericSqlQueryProcessorBuilder.setValue(new GenericSQLQueryProcessor(genericSqlQuery.getSqlStringWithAliasedIDs()), isEmpty);
				
				Map<String, DataTableAndColumnsMapping> tableNameTableAndColumnsMappingMap = new LinkedHashMap<>();
				genericSqlQuery.getTableAliasNameRecordDataMetadataIDMap().forEach((k,v)->{
					DataTableAndColumnsMapping mapping = new DataTableAndColumnsMapping(
							k, v, genericSqlQuery.getTableAliasNameColumnAliasNameDataTableColumnNameMapMap().get(k));
					tableNameTableAndColumnsMappingMap.put(k, mapping);
				});
				
				this.tableAliasNameMappingMapBuilder.setValue(tableNameTableAndColumnsMappingMap, isEmpty);
				
				this.orderedListOfColumnInOutputDataTableBuilder.setValue(
						genericSqlQuery.getOrderedListOfColumnInOutputDataTable(), isEmpty);
			}
		}
		return changed;
	}
	
	
	@Override
	protected GenericSQLQuery build() {
		return new GenericSQLQuery(
				this.genericSqlQueryProcessorBuilder.getCurrentValue().getSingleLineSqlQueryString(),
				this.tableAliasNameMappingMapBuilder.getCurrentValue(),
				this.orderedListOfColumnInOutputDataTableBuilder.getCurrentValue()
				);
	}
}
