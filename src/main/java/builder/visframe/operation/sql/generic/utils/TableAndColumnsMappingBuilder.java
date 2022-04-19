package builder.visframe.operation.sql.generic.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import builder.basic.collection.map.leaf.FixedKeySetMapValueBuilder;
import builder.basic.misc.SimpleTypeSelectorFactory;
import builder.visframe.metadata.MetadataIDSelector;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import metadata.DataType;
import metadata.record.RecordDataMetadata;
import operation.sql.generic.DataTableAndColumnsMapping;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;

public class TableAndColumnsMappingBuilder extends NonLeafNodeBuilder<DataTableAndColumnsMapping>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	
	//
	private String tableAliasName;
	private Set<String> columnAliasNameSet;
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public TableAndColumnsMappingBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectDBContext hostVisProjectDBContext) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
//		this.tableAliasName = tableAliasName;
//		this.columnAliasNameSet = columnAliasNameSet;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	public void setTableAndColumnNameSet(String tableAliasName, Set<String> columnAliasNameSet) {
		this.tableAliasName = tableAliasName;
		this.columnAliasNameSet = columnAliasNameSet;
//		this.columnAliasNameDataTableColumnMapBuilder.setKeySet(this.columnAliasNameSet);
	}
	
	private Set<DataType> getAllowedMetadataDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.RECORD);
		return ret;
	}
	
	///////////////////////////////////////////
	protected static final String recordMetadataID = "recordMetadataID";
	protected static final String recordMetadataID_description = "recordMetadataID";
	
	protected static final String columnAliasNameDataTableColumnNameMap = "columnAliasNameDataTableColumnNameMap";
	protected static final String columnAliasNameDataTableColumnNameMap_description = "columnAliasNameDataTableColumnNameMap";
	
	
	private MetadataIDSelector recordMetadataIDSelector;
	
	private FixedKeySetMapValueBuilder<String, DataTableColumn> columnAliasNameDataTableColumnMapBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.recordMetadataIDSelector = new MetadataIDSelector(
				recordMetadataID, recordMetadataID_description, false, this,
				this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
				this.getAllowedMetadataDataTypeSet(),
				null
				);
		this.addChildNodeBuilder(this.recordMetadataIDSelector);
		
		//
		SimpleTypeSelectorFactory<DataTableColumn> dataTableColumnSelectorFactory = new SimpleTypeSelectorFactory<>(
				"dataTableColumnSelector", "dataTableColumnSelector", false,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());},//Function<T,String> typeToStringRepresentationFunction,
				e->{return e.getName().getStringValue().concat(";").concat(e.getSqlDataType().getSQLString());}//Function<T,String> typeToDescriptionFunction
				);
		
		this.columnAliasNameDataTableColumnMapBuilder = new FixedKeySetMapValueBuilder<>(
				columnAliasNameDataTableColumnNameMap, columnAliasNameDataTableColumnNameMap_description, false, this,
				
				dataTableColumnSelectorFactory,
				e->{return e;},//Function<K,String> mapKeyToStringRepresentationFunction,
				e->{return e;},//Function<K,String> mapKeyToDescriptionFunction,
				null,//BiConsumer<K,NodeBuilder<V,?>> mapValueBuilderAction, TODO
				
				false,//boolean allowingNullMapValue,
				true//boolean allowingDuplicateMapValue
				);
		this.addChildNodeBuilder(this.columnAliasNameDataTableColumnMapBuilder);
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//when recordMetadataIDSelector's status changes
		//set the pool of dataTableColumnSelectorFactory of the columnAliasNameDataTableColumnMapBuilder
		//set columnAliasNameDataTableColumnMapBuilder to default empty
		//reset key set of columnAliasNameDataTableColumnMapBuilder with columnAliasNameSet
		Runnable recordMetadataIDSelectorChangeEventAction = ()->{
			try {
				if(recordMetadataIDSelector.isDefaultEmpty()) {
					
					//set the pool of the dataTableColumnSelectorFactory to empty set
					@SuppressWarnings("unchecked")
					SimpleTypeSelectorFactory<DataTableColumn> mapValueFactory = (SimpleTypeSelectorFactory<DataTableColumn>)this.columnAliasNameDataTableColumnMapBuilder.getMapValueNodeBuilderFactory();
					mapValueFactory.setPool(new HashSet<>());
					//reset map value builders by reseting the key set
					
					this.columnAliasNameDataTableColumnMapBuilder.setToDefaultEmpty();
					
					this.columnAliasNameDataTableColumnMapBuilder.setKeySet(this.columnAliasNameSet);
					
				}else {//valid value
					try {
						RecordDataMetadata recordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(this.recordMetadataIDSelector.getCurrentValue());
						
						//set the pool of the dataTableColumnSelectorFactory to the column set of the selected record data's data table schema
						@SuppressWarnings("unchecked")
						SimpleTypeSelectorFactory<DataTableColumn> mapValueFactory = (SimpleTypeSelectorFactory<DataTableColumn>)this.columnAliasNameDataTableColumnMapBuilder.getMapValueNodeBuilderFactory();
						mapValueFactory.setPool(recordData.getDataTableSchema().getOrderedListOfNonRUIDColumn());
						//reset map value builders by reseting the key set
						this.columnAliasNameDataTableColumnMapBuilder.setToDefaultEmpty();
						this.columnAliasNameDataTableColumnMapBuilder.setKeySet(this.columnAliasNameSet);
						
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
		
		
		
		this.recordMetadataIDSelector.addStatusChangedAction(
				recordMetadataIDSelectorChangeEventAction);
		
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.recordMetadataIDSelector.setToDefaultEmpty();
			this.columnAliasNameDataTableColumnMapBuilder.setToDefaultEmpty();
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				DataTableAndColumnsMapping tableAndColumnsMapping = (DataTableAndColumnsMapping)value;
				this.recordMetadataIDSelector.setValue(tableAndColumnsMapping.getRecordMetadataID(), isEmpty);
				
				try {
					RecordDataMetadata recordData = (RecordDataMetadata) this.getHostVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager().lookup(tableAndColumnsMapping.getRecordMetadataID());
					
					Map<String, DataTableColumn> columnAliasNameDataTableColumnMap = new LinkedHashMap<>();
					tableAndColumnsMapping.getColumnAliasNameDataTableColumnNameMap().forEach((k,v)->{
						columnAliasNameDataTableColumnMap.put(k, recordData.getDataTableSchema().getColumn(v));
					});
					
					this.columnAliasNameDataTableColumnMapBuilder.setKeySet(this.columnAliasNameSet);
					this.columnAliasNameDataTableColumnMapBuilder.setValue(columnAliasNameDataTableColumnMap, isEmpty);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected DataTableAndColumnsMapping build() {
		Map<String, DataTableColumnName> columnAliasNameDataTableColumnNameMap = new LinkedHashMap<>();
		this.columnAliasNameDataTableColumnMapBuilder.getCurrentValue().forEach((k,v)->{
			columnAliasNameDataTableColumnNameMap.put(k, v.getName());
		});
		
		return new DataTableAndColumnsMapping(
				this.tableAliasName,
				this.recordMetadataIDSelector.getCurrentValue(),
				columnAliasNameDataTableColumnNameMap
				);
		
	}
}
