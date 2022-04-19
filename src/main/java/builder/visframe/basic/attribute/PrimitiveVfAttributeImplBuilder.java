package builder.visframe.basic.attribute;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import basic.SimpleName;
import basic.VfNotes;
import basic.attribute.PrimitiveTypeVfAttributeFactory;
import basic.attribute.VfAttribute;
import basic.attribute.VfAttributeImpl;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.basic.primitive.StringTypeBuilder;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import rdb.sqltype.SQLDataTypeFactory;
import rdb.sqltype.SQLIntegerType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

/**
 * builder for a primitive type VfAttributeImpl defined in factory class {@link PrimitiveTypeVfAttributeFactory};
 * 
 * @author tanxu
 * 
 * @param <T>
 */
public final class PrimitiveVfAttributeImplBuilder extends NonLeafNodeBuilder<VfAttribute<?>>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	protected PrimitiveVfAttributeImplBuilder(String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		
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
	
	protected static final String defaultValue = "defaultValue";
	protected static final String defaultValue_description = "defaultValue";
	
	protected static final String canBeNull = "canBeNull";
	protected static final String canBeNull_description = "canBeNull";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//name
		this.addChildNodeBuilder(new VfNameStringBuilder<SimpleName>(
				name, name_description,
				false, this, SimpleName.class
				));
		
		//notes,
		this.addChildNodeBuilder(new VfNotesBuilder(
				notes, notes_description, 
				false, this));
		
		//SQLDataType
		this.addChildNodeBuilder(new VfDefinedPrimitiveSQLDataTypeBuilder(SQLDataType, SQLDataType_description, false, this, e->{return true;}));
		
		//defaultValue
		this.addChildNodeBuilder(new StringTypeBuilder(defaultValue, defaultValue_description, true, this));
		
		//canBeNull
		this.addChildNodeBuilder(new BooleanTypeBuilder(canBeNull, canBeNull_description, false, this));
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		
		//defaultValue value string must be consistent and valid with the selected SQLDataType
		Set<String> set1 = new HashSet<>();
		set1.add(defaultValue);
		set1.add(SQLDataType);
		
		GenricChildrenNodeBuilderConstraint<VfAttribute<?>> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "DefaultValue value string must be consistent and valid with the selected SQLDataType!",
				set1,
				e->{
					VfDefinedPrimitiveSQLDataTypeBuilder SQLDataTypeBuilder = (VfDefinedPrimitiveSQLDataTypeBuilder) e.getChildrenNodeBuilderNameMap().get(SQLDataType);
					//
					StringTypeBuilder defaultValueBuilder = (StringTypeBuilder) e.getChildrenNodeBuilderNameMap().get(defaultValue);
					
					if(!defaultValueBuilder.isSetToNull()) {
						if(SQLDataTypeBuilder.getCurrentValue().isGenericInt()) {
							SQLIntegerType type = (SQLIntegerType) SQLDataTypeBuilder.getCurrentValue();
							
							return type.getIntegerType().isValidStringValue(defaultValueBuilder.getCurrentValue());
//							
						}else if(SQLDataTypeBuilder.getCurrentValue().equals(SQLDataTypeFactory.doubleType())) {
							try {
								Double.parseDouble(defaultValueBuilder.getCurrentValue());
							}catch(NumberFormatException ex) {
								return false;
							}
						}else if(SQLDataTypeBuilder.getCurrentValue().equals(SQLDataTypeFactory.booleanType())){
							return defaultValueBuilder.getCurrentValue().equalsIgnoreCase("true")||defaultValueBuilder.getCurrentValue().equalsIgnoreCase("false");
						}else {//string type
							
							//do nothing
						}
					}
					
					return true;
					
				});
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		
		VfDefinedPrimitiveSQLDataTypeBuilder SQLDataTypeBuilder = (VfDefinedPrimitiveSQLDataTypeBuilder) this.getChildrenNodeBuilderNameMap().get(SQLDataType);
		
		StringTypeBuilder defaultValueBuilder = (StringTypeBuilder) this.getChildrenNodeBuilderNameMap().get(defaultValue);
		
		
		//when SQLDataType changed, the defaultValue will be set to default empty;
		Runnable SQLDataTypeStatusChangeEventAction = ()->{
			
			try {
				defaultValueBuilder.setToDefaultEmpty();
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		};
		
		SQLDataTypeBuilder.addStatusChangedAction(
				SQLDataTypeStatusChangeEventAction);
		
		
	}
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(defaultValue).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(canBeNull).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				
				VfAttributeImpl<?> primitiveVfAttribute = (VfAttributeImpl<?>)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(primitiveVfAttribute.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(primitiveVfAttribute.getNotes(), isEmpty);
				
				this.getChildrenNodeBuilderNameMap().get(SQLDataType).setValue(primitiveVfAttribute.getSQLDataType(), isEmpty);
				
				this.getChildrenNodeBuilderNameMap().get(defaultValue).setValue(primitiveVfAttribute.getDefaultStringValue(), isEmpty);
				
				this.getChildrenNodeBuilderNameMap().get(canBeNull).setValue(primitiveVfAttribute.canBeNull(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected VfAttribute<?> build() {
		SimpleName nameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		
		VfDefinedPrimitiveSQLDataType SQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(SQLDataType).getCurrentValue();
		boolean canBeNullValue = (boolean) this.getChildrenNodeBuilderNameMap().get(canBeNull).getCurrentValue();
		
		String defaultValueString = (String) this.getChildrenNodeBuilderNameMap().get(defaultValue).getCurrentValue();
		
		if(SQLDataTypeValue.isGenericInt()) {
			//TODO
			//short and long are allowed to be selected, while here only allow int????????need to modify
			return PrimitiveTypeVfAttributeFactory.intTypeVfAttribute(
					nameValue, notesValue, null, SQLDataTypeValue,
					defaultValueString==null?null:Integer.parseInt(defaultValueString), canBeNullValue);
//			
		}else if(SQLDataTypeValue.equals(SQLDataTypeFactory.doubleType())) {
			return PrimitiveTypeVfAttributeFactory.doubleTypeVfAttribute(
					nameValue, notesValue, null,
					defaultValueString==null?null:Double.parseDouble(defaultValueString), canBeNullValue);
//			
		}else if(SQLDataTypeValue.equals(SQLDataTypeFactory.booleanType())){
			
			return PrimitiveTypeVfAttributeFactory.booleanTypeVfAttribute(
					nameValue, notesValue, null,
					defaultValueString==null?null:Boolean.parseBoolean(defaultValueString), canBeNullValue);
		}else {//string type
			return PrimitiveTypeVfAttributeFactory.stringTypeVfAttribute(
					nameValue, notesValue, null, SQLDataTypeValue,
					defaultValueString, canBeNullValue);
		}
		
	}

	
	
	
}
