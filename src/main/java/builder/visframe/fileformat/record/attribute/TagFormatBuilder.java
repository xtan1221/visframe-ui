package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import builder.basic.collection.map.nonleaf.HashMapNonLeafNodeBuilder;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.basic.primitive.IntegerTypeBuilder;
import builder.basic.primitive.StringTypeBuilderFactory;
import builder.visframe.fileformat.record.utils.StringMarkerBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilder;
import builder.visframe.rdb.sqltype.VfDefinedPrimitiveSQLDataTypeBuilderFactory;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.attribute.TagFormat;
import fileformat.record.utils.StringMarker;
import rdb.sqltype.SQLDataType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public final class TagFormatBuilder extends NonLeafNodeBuilder<TagFormat>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public TagFormatBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////////////
	protected static final String hasDataTypeIndicatorComponent = "hasDataTypeIndicatorComponent";
	protected static final String hasDataTypeIndicatorComponent_description = "hasDataTypeIndicatorComponent";
	
	protected static final String dataTypeIndicatorComponentStringSQLDataTypeMap = "dataTypeIndicatorComponentStringSQLDataTypeMap";
	protected static final String dataTypeIndicatorComponentStringSQLDataTypeMap_description = "dataTypeIndicatorComponentStringSQLDataTypeMap";
	
	protected static final String defaultSQLDataType = "defaultSQLDataType";
	protected static final String defaultSQLDataType_description = "defaultSQLDataType";
	
	protected static final String componentDelimiter = "componentDelimiter";
	protected static final String componentDelimiter_description = "componentDelimiter";

	protected static final String nameComponentStringIndex = "nameComponentStringIndex";
	protected static final String nameComponentStringIndex_description = "nameComponentStringIndex";

	protected static final String valueComponentStringIndex = "valueComponentStringIndex";
	protected static final String valueComponentStringIndex_description = "valueComponentStringIndex";

	protected static final String dataTypeIndicatorComponentStringIndex = "dataTypeIndicatorComponentStringIndex";
	protected static final String dataTypeIndicatorComponentStringIndex_description = "dataTypeIndicatorComponentStringIndex";

	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//boolean hasDataTypeIndicatorComponent, 
		this.addChildNodeBuilder(new BooleanTypeBuilder(hasDataTypeIndicatorComponent, hasDataTypeIndicatorComponent_description, false, this));

		//Map<String,SQLDataType> dataTypeIndicatorComponentStringSQLDataTypeMap,
		StringTypeBuilderFactory stringTypeBuilderFactory = new StringTypeBuilderFactory("dataTypeIndicatorComponentString","dataTypeIndicatorComponentString",false);
		VfDefinedPrimitiveSQLDataTypeBuilderFactory SQLDataTypeBuilderFactory = new VfDefinedPrimitiveSQLDataTypeBuilderFactory("SQLDataType","SQLDataType",false, e->{return true;});
		HashMapNonLeafNodeBuilder<String, VfDefinedPrimitiveSQLDataType> dataTypeIndicatorComponentStringSQLDataTypeMapBuilder = 
				new HashMapNonLeafNodeBuilder<>(
						dataTypeIndicatorComponentStringSQLDataTypeMap, dataTypeIndicatorComponentStringSQLDataTypeMap_description,
						true, this, 
						stringTypeBuilderFactory, SQLDataTypeBuilderFactory);
		this.addChildNodeBuilder(dataTypeIndicatorComponentStringSQLDataTypeMapBuilder);
		
		//SQLDataType defaultSQLDataType,
		this.addChildNodeBuilder(new VfDefinedPrimitiveSQLDataTypeBuilder(defaultSQLDataType, defaultSQLDataType_description, true, this, e->{return true;}));
		
		//StringMarker componentDelimiter,
		this.addChildNodeBuilder(new StringMarkerBuilder(componentDelimiter, componentDelimiter_description, false, this));
		
		//int nameComponentStringIndex,
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				nameComponentStringIndex, nameComponentStringIndex_description, false, this,
				e->{return e>=0;}, "index must be non-negative integer!"));
		
		//int valueComponentStringIndex,
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				valueComponentStringIndex, valueComponentStringIndex_description, false, this,
				e->{return e>=0;}, "index must be non-negative integer!"));
		
		//Integer dataTypeIndicatorComponentStringIndex
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				dataTypeIndicatorComponentStringIndex, dataTypeIndicatorComponentStringIndex_description, true, this,
				e->{return e>=0;}, "index must be non-negative integer!"));
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//when hasDataTypeIndicatorComponent is true, dataTypeIndicatorComponentStringIndex should be non-null
		Set<String> set1 = new HashSet<>();
		set1.add(hasDataTypeIndicatorComponent);
		set1.add(dataTypeIndicatorComponentStringIndex);
		GenricChildrenNodeBuilderConstraint<TagFormat> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "when hasDataTypeIndicatorComponent is true, dataTypeIndicatorComponentStringIndex should be non-null!",
				set1, 
				e->{
					boolean hasDataTypeIndicatorComponent = (boolean) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.hasDataTypeIndicatorComponent).getCurrentValue();
					Integer dataTypeIndicatorComponentStringIndex = (Integer) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.dataTypeIndicatorComponentStringIndex).getCurrentValue();
					
					if(hasDataTypeIndicatorComponent)
						return dataTypeIndicatorComponentStringIndex!=null;
					else
						return true;
					}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//when hasDataTypeIndicatorComponent is false, defaultSQLDataType should be non-null
		Set<String> set2 = new HashSet<>();
		set2.add(hasDataTypeIndicatorComponent);
		set2.add(defaultSQLDataType);
		GenricChildrenNodeBuilderConstraint<TagFormat> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "when hasDataTypeIndicatorComponent is false, defaultSQLDataType should be non-null!",
				set2, 
				e->{
					boolean hasDataTypeIndicatorComponent = (boolean) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.hasDataTypeIndicatorComponent).getCurrentValue();
					SQLDataType defaultSQLDataType = (SQLDataType) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.defaultSQLDataType).getCurrentValue();
					
					if(hasDataTypeIndicatorComponent) {
						return true;
					}else {
						return defaultSQLDataType!=null;
					}
					
				}
				);
		
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
		//when hasDataTypeIndicatorComponent is true, dataTypeIndicatorComponentStringSQLDataTypeMap should be non-null and non-empty
		Set<String> set3 = new HashSet<>();
		set3.add(hasDataTypeIndicatorComponent);
		set3.add(dataTypeIndicatorComponentStringSQLDataTypeMap);
		GenricChildrenNodeBuilderConstraint<TagFormat> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "when hasDataTypeIndicatorComponent is true, dataTypeIndicatorComponentStringSQLDataTypeMap should be non-null and non-empty!",
				set3, 
				e->{
					boolean hasDataTypeIndicatorComponent = (boolean) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.hasDataTypeIndicatorComponent).getCurrentValue();
					@SuppressWarnings("unchecked")
					Map<String,SQLDataType> dataTypeIndicatorComponentStringSQLDataTypeMap = 
					(Map<String, SQLDataType>) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.dataTypeIndicatorComponentStringSQLDataTypeMap).getCurrentValue();
					
					if(hasDataTypeIndicatorComponent) {
						return dataTypeIndicatorComponentStringSQLDataTypeMap!=null&& !dataTypeIndicatorComponentStringSQLDataTypeMap.isEmpty();
					}else {
						return true;
					}
				}
				);
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		//nameComponentStringIndex and valueComponentStringIndex should be unequal
		Set<String> set4 = new HashSet<>();
		set4.add(nameComponentStringIndex);
		set4.add(valueComponentStringIndex);
		GenricChildrenNodeBuilderConstraint<TagFormat> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "nameComponentStringIndex and valueComponentStringIndex should be unequal!",
				set4,
				e->{
					int nameComponentStringIndex = (int) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.nameComponentStringIndex).getCurrentValue();
					int valueComponentStringIndex = (int) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.valueComponentStringIndex).getCurrentValue();
					
					return nameComponentStringIndex!=valueComponentStringIndex;
				}
				);
		this.addGenricChildrenNodeBuilderConstraint(c4);
		
		//when dataTypeIndicatorComponentStringIndex is non-null, it should be different from both nameComponentStringIndex and valueComponentStringIndex
		Set<String> set5 = new HashSet<>();
		set5.add(dataTypeIndicatorComponentStringIndex);
		set5.add(nameComponentStringIndex);
		set5.add(valueComponentStringIndex);
		GenricChildrenNodeBuilderConstraint<TagFormat> c5 = new GenricChildrenNodeBuilderConstraint<>(
				this, "when dataTypeIndicatorComponentStringIndex is non-null, it should be different from both nameComponentStringIndex and valueComponentStringIndex!",
				set5, 
				e->{
					int nameComponentStringIndex = (int) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.nameComponentStringIndex).getCurrentValue();
					int valueComponentStringIndex = (int) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.valueComponentStringIndex).getCurrentValue();
					Integer dataTypeIndicatorComponentStringIndex = (Integer) e.getChildrenNodeBuilderNameMap().get(TagFormatBuilder.dataTypeIndicatorComponentStringIndex).getCurrentValue();
					
					if(dataTypeIndicatorComponentStringIndex!=null) {
						return dataTypeIndicatorComponentStringIndex!=nameComponentStringIndex && dataTypeIndicatorComponentStringIndex!=valueComponentStringIndex;
					}else {
						return true;
					}
				}
				);
		this.addGenricChildrenNodeBuilderConstraint(c5);
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		//
		//dataTypeIndicatorComponentStringIndex depends on hasDataTypeIndicatorComponent
		
		//defaultSQLDataType depends on hasDataTypeIndicatorComponent
		
		//
		
	}
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(hasDataTypeIndicatorComponent).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringSQLDataTypeMap).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(defaultSQLDataType).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(componentDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(nameComponentStringIndex).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(valueComponentStringIndex).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringIndex).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				TagFormat tagFormat = (TagFormat)value;
				this.getChildrenNodeBuilderNameMap().get(hasDataTypeIndicatorComponent).setValue(tagFormat.hasDataTypeIndicatorComponent(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringSQLDataTypeMap).setValue(tagFormat.getDataTypeIndicatorComponentStringSQLDataTypeMap(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(defaultSQLDataType).setValue(tagFormat.getDefaultSQLDataType(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(componentDelimiter).setValue(tagFormat.getComponentDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(nameComponentStringIndex).setValue(tagFormat.getNameComponentStringIndex(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(valueComponentStringIndex).setValue(tagFormat.getValueComponentStringIndex(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringIndex).setValue(tagFormat.getDataTypeIndicatorComponentStringIndex(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected TagFormat build() {
		boolean hasDataTypeIndicatorComponentValue = (boolean) this.getChildrenNodeBuilderNameMap().get(hasDataTypeIndicatorComponent).getCurrentValue();
		
		@SuppressWarnings("unchecked")
		Map<String,VfDefinedPrimitiveSQLDataType> dataTypeIndicatorComponentStringSQLDataTypeMapValue = //note that even the value is null, this will not cause exception
				(Map<String, VfDefinedPrimitiveSQLDataType>) this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringSQLDataTypeMap).getCurrentValue();
		
		VfDefinedPrimitiveSQLDataType defaultSQLDataTypeValue = (VfDefinedPrimitiveSQLDataType) this.getChildrenNodeBuilderNameMap().get(defaultSQLDataType).getCurrentValue();
		StringMarker componentDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(componentDelimiter).getCurrentValue(); 
		int nameComponentStringIndexValue = (int) this.getChildrenNodeBuilderNameMap().get(nameComponentStringIndex).getCurrentValue(); 
		int valueComponentStringIndexValue = (int) this.getChildrenNodeBuilderNameMap().get(valueComponentStringIndex).getCurrentValue(); 
		Integer dataTypeIndicatorComponentStringIndexValue = (Integer) this.getChildrenNodeBuilderNameMap().get(dataTypeIndicatorComponentStringIndex).getCurrentValue(); 
		
		return new TagFormat(
				hasDataTypeIndicatorComponentValue,
				dataTypeIndicatorComponentStringSQLDataTypeMapValue,
				defaultSQLDataTypeValue,
				componentDelimiterValue,
				nameComponentStringIndexValue,
				valueComponentStringIndexValue,
				dataTypeIndicatorComponentStringIndexValue
				);
	}

}
