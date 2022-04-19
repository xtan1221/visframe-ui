package builder.visframe.rdb.table.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.list.ArrayListNonLeafNodeBuilder;
import builder.visframe.basic.VfNameStringBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;
import rdb.table.data.DataTableName;
import rdb.table.data.DataTableSchema;
import rdb.table.data.DataTableSchemaFactory;

public class DataTableSchemaBuilder extends NonLeafNodeBuilder<DataTableSchema>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public DataTableSchemaBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////////////////////
	protected static final String tableName = "tableName";
	protected static final String tableName_description = "tableName";
	
	protected static final String orderedListOfNonRUIDColumn = "orderedListOfNonRUIDColumn";
	protected static final String orderedListOfNonRUIDColumn_description = "orderedListOfNonRUIDColumn";
	
	private VfNameStringBuilder<DataTableName> tableNameBuilder;
	
	private ArrayListNonLeafNodeBuilder<DataTableColumn> orderedListOfNonRUIDColumnBuilder;
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.tableNameBuilder = new VfNameStringBuilder<>(
				tableName, tableName_description, false, this, DataTableName.class);
		this.addChildNodeBuilder(this.tableNameBuilder);
		
		//
		DataTableColumnBuilderFactory dataTableColumnBuilderFactory = new DataTableColumnBuilderFactory(
				"dataTableColumn", "dataTableColumn", false);
		
		this.orderedListOfNonRUIDColumnBuilder = new ArrayListNonLeafNodeBuilder<>(
				orderedListOfNonRUIDColumn, orderedListOfNonRUIDColumn_description, false, this,
				dataTableColumnBuilderFactory,
				false
				);
		this.addChildNodeBuilder(this.orderedListOfNonRUIDColumnBuilder);
		
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
		//orderedListOfNonRUIDColumnBuilder cannot be empty;
		Set<String> involvedSiblingNodeNameSet1 = new HashSet<>();
		involvedSiblingNodeNameSet1.add(orderedListOfNonRUIDColumnBuilder.getName());
		GenricChildrenNodeBuilderConstraint<DataTableSchema> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "orderedListOfNonRUIDColumnBuilder cannot be empty!",
				involvedSiblingNodeNameSet1, 
				e->{
					DataTableSchemaBuilder builder = (DataTableSchemaBuilder)e;
					return !builder.orderedListOfNonRUIDColumnBuilder.getCurrentValue().isEmpty();
					
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//columns in orderedListOfNonRUIDColumnBuilder must have different names;
		Set<String> involvedSiblingNodeNameSet2 = new HashSet<>();
		involvedSiblingNodeNameSet2.add(orderedListOfNonRUIDColumnBuilder.getName());
		GenricChildrenNodeBuilderConstraint<DataTableSchema> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "columns in orderedListOfNonRUIDColumnBuilder must have different names!",
				involvedSiblingNodeNameSet2, 
				e->{
					DataTableSchemaBuilder builder = (DataTableSchemaBuilder)e;
					
					Set<DataTableColumnName> colNameSet = new HashSet<>();
					
					for(DataTableColumn col:builder.orderedListOfNonRUIDColumnBuilder.getCurrentValue()) {
						if(colNameSet.contains(col.getName()))
							return false;
						colNameSet.add(col.getName());
					}
					
					return true;
						
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//at least one column in orderedListOfNonRUIDColumnBuilder should be in primary key;
		Set<String> involvedSiblingNodeNameSet3 = new HashSet<>();
		involvedSiblingNodeNameSet3.add(orderedListOfNonRUIDColumnBuilder.getName());
		GenricChildrenNodeBuilderConstraint<DataTableSchema> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "at least one column in orderedListOfNonRUIDColumnBuilder should be in primary key!",
				involvedSiblingNodeNameSet3, 
				e->{
					DataTableSchemaBuilder builder = (DataTableSchemaBuilder)e;
					for(DataTableColumn col:builder.orderedListOfNonRUIDColumnBuilder.getCurrentValue()) {
						if(col.isInPrimaryKey())
							return true;
					}
					return false;
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c3);
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.tableNameBuilder.setToDefaultEmpty();
			this.orderedListOfNonRUIDColumnBuilder.setToDefaultEmpty();
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				DataTableSchema dataTableSchema = (DataTableSchema)value;
				this.tableNameBuilder.setValue(dataTableSchema.getName(), isEmpty);
				this.orderedListOfNonRUIDColumnBuilder.setValue(dataTableSchema.getOrderedListOfNonRUIDColumn(), isEmpty);
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected DataTableSchema build() {
		List<DataTableColumn> tableColumnList = new ArrayList<>();
		tableColumnList.add(DataTableSchemaFactory.makeRUIDColumn());
		tableColumnList.addAll(this.orderedListOfNonRUIDColumnBuilder.getCurrentValue());
		
		return new DataTableSchema(
				this.tableNameBuilder.getCurrentValue(),
				tableColumnList
				);
		
	}
}
