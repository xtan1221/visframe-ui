package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.attribute.PrimitiveRecordAttributeFormat;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public final class PrimitiveRecordAttributeFormatBuilder extends NonLeafNodeBuilder<PrimitiveRecordAttributeFormat> {

	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected PrimitiveRecordAttributeFormatBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
		///////////
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	//////////////////////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String SQLDataType = "SQLDataType";
	protected static final String SQLDataType_description = "SQLDataType";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//SimpleName name, 
		this.addChildNodeBuilder(new VfNameStringBuilder<>(name, name_description, false, this, SimpleName.class));
	
		
		//VfNotes notes,
		this.addChildNodeBuilder(new VfNotesBuilder(notes, notes_description, false, this));
		
		
		//SQLDataType SQLDataType
		this.addChildNodeBuilder(new VfDefinedPrimitiveSQLDataTypeBuilder(SQLDataType, SQLDataType_description, false, this, e->{return true;}));
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//no such constraints
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//no such dependency
	}

	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				PrimitiveRecordAttributeFormat graphTypeEnforcer = (PrimitiveRecordAttributeFormat)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(graphTypeEnforcer.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(graphTypeEnforcer.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(graphTypeEnforcer.getSQLDataType(), isEmpty);
			}
		}
		
		return changed;
	}
	
	@Override
	protected PrimitiveRecordAttributeFormat build() {
		SimpleName nameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		VfDefinedPrimitiveSQLDataType SQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(SQLDataType).getCurrentValue();
		
		return new PrimitiveRecordAttributeFormat(nameValue, notesValue, SQLDataTypeValue);
	}

}
