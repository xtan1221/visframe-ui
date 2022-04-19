package builder.visframe.rdb.table.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;

/**
 * builder for a DataTableColumn;
 * currently not supporting defaultStringValue and additionalConstraints features (both will be null in the built DataTableColumn);
 * 
 * 
 * @author tanxu
 *
 */
public class DataTableColumnBuilder extends NonLeafNodeBuilder<DataTableColumn>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public DataTableColumnBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String sqlDataType = "sqlDataType";
	protected static final String sqlDataType_description = "sqlDataType";
	
	protected static final String inPrimaryKey = "inPrimaryKey";
	protected static final String inPrimaryKey_description = "inPrimaryKey";
	
	protected static final String unique = "unique";
	protected static final String unique_description = "unique";
	
	protected static final String notNull = "notNull";
	protected static final String notNull_description = "notNull";
	
//	protected static final String defaultStringValue = "defaultStringValue";
//	protected static final String defaultStringValue_description = "defaultStringValue";
//	
//	protected static final String additionalConstraints = "additionalConstraints";
//	protected static final String additionalConstraints_description = "additionalConstraints";
	
	private VfNameStringBuilder<DataTableColumnName> nameBuilder;
	
	private VfNotesBuilder notesBuilder;
	
	private VfDefinedPrimitiveSQLDataTypeBuilder sqlDataTypeBuilder;
	
	private BooleanTypeBuilder inPrimaryKeyBuilder;
	
	private BooleanTypeBuilder uniqueBuilder;
	
	private BooleanTypeBuilder notNullBuilder;
	
//	private StringTypeBuilder defaultStringValueBuilder;
//	
//	private StringTypeBuilder additionalConstraintsBuilder;
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		this.nameBuilder = new VfNameStringBuilder<>(
				name, name_description, false, this, DataTableColumnName.class);
		this.addChildNodeBuilder(this.nameBuilder);
		
		//
		this.notesBuilder = new VfNotesBuilder(
				notes, notes_description, false, this);
		this.addChildNodeBuilder(this.notesBuilder);
		
		//
		this.sqlDataTypeBuilder = new VfDefinedPrimitiveSQLDataTypeBuilder(
				sqlDataType, sqlDataType_description, false, this, 
				e->{return true;});
		this.addChildNodeBuilder(this.sqlDataTypeBuilder);
		
		//
		this.inPrimaryKeyBuilder = new BooleanTypeBuilder(
				inPrimaryKey, inPrimaryKey_description, false, this);
		this.addChildNodeBuilder(this.inPrimaryKeyBuilder);
		
		//
		this.uniqueBuilder = new BooleanTypeBuilder(
				unique, unique_description, true, this);
		this.addChildNodeBuilder(this.uniqueBuilder);
		
		//
		this.notNullBuilder = new BooleanTypeBuilder(
				notNull, notNull_description, true, this);
		this.addChildNodeBuilder(this.notNullBuilder);
		
		//
//		this.defaultStringValueBuilder = new StringTypeBuilder(
//				defaultStringValue, defaultStringValue_description, true, this);
//		this.addChildNodeBuilder(this.defaultStringValueBuilder);
//		
//		//
//		this.additionalConstraintsBuilder = new StringTypeBuilder(
//				additionalConstraints, additionalConstraints_description, true, this);
//		this.addChildNodeBuilder(this.additionalConstraintsBuilder);
		
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
		//when inPrimaryKeyBuilder is true, uniqueBuilder and notNullBuilder should be null;
		//when inPrimaryKeyBuilder is false, uniqueBuilder and notNullBuilder should be non-null
		Set<String> involvedSiblingNodeNameSet1 = new HashSet<>();
		involvedSiblingNodeNameSet1.add(inPrimaryKeyBuilder.getName());
		involvedSiblingNodeNameSet1.add(uniqueBuilder.getName());
		involvedSiblingNodeNameSet1.add(notNullBuilder.getName());
		GenricChildrenNodeBuilderConstraint<DataTableColumn> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "when inPrimaryKeyBuilder is true, uniqueBuilder and notNullBuilder should be null; when inPrimaryKeyBuilder is false, uniqueBuilder and notNullBuilder should be non-null!",
				involvedSiblingNodeNameSet1, 
				e->{
					DataTableColumnBuilder builder = (DataTableColumnBuilder)e;
					if(builder.inPrimaryKeyBuilder.getCurrentValue()) {
						return builder.uniqueBuilder.isSetToNull() && builder.notNullBuilder.isSetToNull();
					}else {
						return !builder.uniqueBuilder.isSetToNull() && !builder.notNullBuilder.isSetToNull();
					}
					
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() throws SQLException, IOException {
		////when inPrimaryKeyBuilder is changed
		Runnable inPrimaryKeyBuilderChangeEventAction = ()->{
			if(inPrimaryKeyBuilder.isDefaultEmpty()) {
				//TODO
				
				
			}else {
				if(inPrimaryKeyBuilder.getCurrentValue()) {
					this.notNullBuilder.setToNull();
					this.uniqueBuilder.setToNull();
				}else {
					this.notNullBuilder.setToNonNull();
					this.uniqueBuilder.setToNonNull();
				}
			}
		};
		
		
		this.inPrimaryKeyBuilder.setValue(true, false);
		
		this.inPrimaryKeyBuilder.addStatusChangedAction(
				inPrimaryKeyBuilderChangeEventAction);
	}

	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.nameBuilder.setToDefaultEmpty();
			this.notesBuilder.setToDefaultEmpty();
			this.sqlDataTypeBuilder.setToDefaultEmpty();
			this.inPrimaryKeyBuilder.setToDefaultEmpty();
			this.uniqueBuilder.setToDefaultEmpty();
			this.notNullBuilder.setToDefaultEmpty();
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				DataTableColumn dataTableColumn = (DataTableColumn)value;
				this.nameBuilder.setValue(dataTableColumn.getName(), isEmpty);
				this.notesBuilder.setValue(dataTableColumn.getNotes(), isEmpty);
				this.sqlDataTypeBuilder.setValue(dataTableColumn.getSqlDataType(), isEmpty);
				this.inPrimaryKeyBuilder.setValue(dataTableColumn.isInPrimaryKey(), isEmpty);
				this.uniqueBuilder.setValue(dataTableColumn.isUnique(), isEmpty);
				this.notNullBuilder.setValue(dataTableColumn.isNotNull(), isEmpty);
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected DataTableColumn build() {
		
		return new DataTableColumn(
				this.nameBuilder.getCurrentValue(),
				this.sqlDataTypeBuilder.getCurrentValue(),
				this.inPrimaryKeyBuilder.getCurrentValue(),
				this.uniqueBuilder.getCurrentValue(),
				this.notNullBuilder.getCurrentValue(),
				null,
				null,
				this.notesBuilder.getCurrentValue()
				);
		
	}
}
