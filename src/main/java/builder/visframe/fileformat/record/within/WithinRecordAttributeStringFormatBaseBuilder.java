package builder.visframe.fileformat.record.within;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import basic.SimpleName;
import builder.basic.collection.list.ArrayListNonLeafNodeBuilder;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.fileformat.record.PrimaryKeyAttributeNameSetBuilder;
import builder.visframe.fileformat.record.attribute.AbstractRecordAttributeFormatLeafNodeBuilderFactory;
import builder.visframe.fileformat.record.attribute.TagFormatBuilder;
import builder.visframe.fileformat.record.utils.PlainStringMarkerBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.RecordDataFileFormat.PrimaryKeyAttributeNameSet;
import fileformat.record.attribute.AbstractRecordAttributeFormat;
import fileformat.record.attribute.PrimitiveRecordAttributeFormat;
import fileformat.record.attribute.TagFormat;
import fileformat.record.utils.PlainStringMarker;
import fileformat.record.within.WithinRecordAttributeStringFormatBase;


public abstract class WithinRecordAttributeStringFormatBaseBuilder<T extends WithinRecordAttributeStringFormatBase> extends NonLeafNodeBuilder<T> {
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected WithinRecordAttributeStringFormatBaseBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
	}
	
	//////////////////////////////////////
	protected static final String orderedListOfMandatoryAttribute = "orderedListOfMandatoryAttribute";
	protected static final String orderedListOfMandatoryAttribute_description = "orderedListOfMandatoryAttribute";
	
	protected static final String defaultPrimaryKeyAttributeNameSet = "defaultPrimaryKeyAttributeNameSet";
	protected static final String defaultPrimaryKeyAttributeNameSet_description = "defaultPrimaryKeyAttributeNameSet";
	
	protected static final String nullValueMandatoryAttributeString = "nullValueMandatoryAttributeString";
	protected static final String nullValueMandatoryAttributeString_description = "nullValueMandatoryAttributeString";
	
	protected static final String hasTailingTagAttributes = "hasTailingTagAttributes";
	protected static final String hasTailingTagAttributes_description = "hasTailingTagAttributes";
	
	protected static final String tailingTagAttributesFormat = "tailingTagAttributesFormat";
	protected static final String tailingTagAttributesFormat_description = "tailingTagAttributesFormat";
	
	protected static final String toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute = "toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute";
	protected static final String toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute_description = "toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute";
	
	protected static final String concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute = "concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute";
	protected static final String concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute_description = "concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//List<AbstractRecordAttributeFormat> orderedListOfMandatoryAttribute,
		AbstractRecordAttributeFormatLeafNodeBuilderFactory mandatoryAttributeLeafNodeBuilderFactory = new AbstractRecordAttributeFormatLeafNodeBuilderFactory("MandatoryAttribute", "MandatoryAttribute", false);
		this.addChildNodeBuilder(
				new ArrayListNonLeafNodeBuilder<>(
						orderedListOfMandatoryAttribute, orderedListOfMandatoryAttribute_description, false, this,
						mandatoryAttributeLeafNodeBuilderFactory,
						false//not allowed to have duplicates
        		));
		
		//PrimaryKeyAttributeNameSet defaultPrimaryKeyAttributeNameSet,
		this.addChildNodeBuilder(new PrimaryKeyAttributeNameSetBuilder(
				defaultPrimaryKeyAttributeNameSet,defaultPrimaryKeyAttributeNameSet_description,
				false, this));
		
		//PlainStringMarker nullValueMandatoryAttributeString,
		this.addChildNodeBuilder(new PlainStringMarkerBuilder(
				nullValueMandatoryAttributeString, nullValueMandatoryAttributeString_description,
				true, this));
		
		//boolean hasTailingTagAttributes,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				hasTailingTagAttributes, hasTailingTagAttributes_description,
				false, this));
		
		//TagFormat tailingTagAttributesFormat,
		this.addChildNodeBuilder(new TagFormatBuilder(
				tailingTagAttributesFormat, tailingTagAttributesFormat_description,
				true, this));
		
		//boolean toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute, toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute_description,
				false, this));
		
		//PlainStringMarker concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute
		this.addChildNodeBuilder(new PlainStringMarkerBuilder(
				concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute, concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute_description,
				true, this));
		
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//orderedListOfMandatoryAttribute cannot be empty; 
		Set<String> set1 = new HashSet<>();
		set1.add(orderedListOfMandatoryAttribute);
		GenricChildrenNodeBuilderConstraint<T> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "orderedListOfMandatoryAttribute cannot be empty!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					List<AbstractRecordAttributeFormat> orderedListOfMandatoryAttribute = 
							(List<AbstractRecordAttributeFormat>) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.orderedListOfMandatoryAttribute).getCurrentValue();
					
					return !orderedListOfMandatoryAttribute.isEmpty();
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//the name of all SimpleRecordAttribute type attributes in orderedListOfMandatoryAttribute must be distinct;
		Set<String> set2 = new HashSet<>();
		set2.add(orderedListOfMandatoryAttribute);
		GenricChildrenNodeBuilderConstraint<T> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "the name of all SimpleRecordAttribute type attributes in orderedListOfMandatoryAttribute must be distinct!",
				set2, 
				e->{
					@SuppressWarnings("unchecked")
					List<AbstractRecordAttributeFormat> orderedListOfMandatoryAttribute = 
							(List<AbstractRecordAttributeFormat>) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.orderedListOfMandatoryAttribute).getCurrentValue();
					
					Set<SimpleName> primitiveAttributeNameSet = new HashSet<>();
					for(AbstractRecordAttributeFormat attribute:orderedListOfMandatoryAttribute) {
						if(attribute instanceof PrimitiveRecordAttributeFormat) {
							PrimitiveRecordAttributeFormat primitiveAttribute = (PrimitiveRecordAttributeFormat)attribute;
							if(primitiveAttributeNameSet.contains(primitiveAttribute.getName())) {
								return false;
							}
							primitiveAttributeNameSet.add(primitiveAttribute.getName());
						}
					}
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
	
		//primary key attributes in defaultPrimaryKeyAttributeNameSet from primitive attributes should be found in orderedListOfMandatoryAttribute;
		Set<String> set3 = new HashSet<>();
		set3.add(defaultPrimaryKeyAttributeNameSet);
		set3.add(orderedListOfMandatoryAttribute);
		GenricChildrenNodeBuilderConstraint<T> c3 = new GenricChildrenNodeBuilderConstraint<>(
				this, "primary key attributes in defaultPrimaryKeyAttributeNameSet from primitive attributes should be found in orderedListOfMandatoryAttribute!",
				set3, 
				e->{
					@SuppressWarnings("unchecked")
					List<AbstractRecordAttributeFormat> orderedListOfMandatoryAttribute = 
							(List<AbstractRecordAttributeFormat>) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.orderedListOfMandatoryAttribute).getCurrentValue();
					PrimaryKeyAttributeNameSet defaultPrimaryKeyAttributeNameSet = 
							(PrimaryKeyAttributeNameSet) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.defaultPrimaryKeyAttributeNameSet).getCurrentValue();
					//
					Set<SimpleName> primitiveAttributeNameSet = new HashSet<>();
					for(AbstractRecordAttributeFormat attribute:orderedListOfMandatoryAttribute) {
						if(attribute instanceof PrimitiveRecordAttributeFormat) {
							PrimitiveRecordAttributeFormat primitiveAttribute = (PrimitiveRecordAttributeFormat)attribute;
							primitiveAttributeNameSet.add(primitiveAttribute.getName());
						}
					}
					for(SimpleName primitiveAttributeName: defaultPrimaryKeyAttributeNameSet.getSimpleMandatoryAttributeNameSet()) {
						if(!primitiveAttributeNameSet.contains(primitiveAttributeName)) {
							return false;
						}
					}
					return true;
				});
		this.addGenricChildrenNodeBuilderConstraint(c3);
		
		
		//tailingTagAttributesFormat cannot be null if hasTailingTagAttributes is true; must be null otherwise;
		Set<String> set4 = new HashSet<>();
		set4.add(tailingTagAttributesFormat);
		set4.add(hasTailingTagAttributes);
		GenricChildrenNodeBuilderConstraint<T> c4 = new GenricChildrenNodeBuilderConstraint<>(
				this, "tailingTagAttributesFormat cannot be null if hasTailingTagAttributes is true; must be null otherwise!",
				set4, 
				e->{
					boolean hasTailingTagAttributes = (boolean) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.hasTailingTagAttributes).getCurrentValue();
					TagFormat tailingTagAttributesFormat = (TagFormat) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.tailingTagAttributesFormat).getCurrentValue();
					
					if(hasTailingTagAttributes) {
						return tailingTagAttributesFormat!=null;
					}else {
						return tailingTagAttributesFormat==null;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c4);
		
		
		
		//concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute cannot be null if hasTailingTagAttributes is false and toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute is true;
		Set<String> set5 = new HashSet<>();
		set5.add(hasTailingTagAttributes);
		set5.add(concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute);
		set5.add(toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute);
		GenricChildrenNodeBuilderConstraint<T> c5 = new GenricChildrenNodeBuilderConstraint<>(
				this, "concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute cannot be null if hasTailingTagAttributes is false and toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute is true!",
				set5, 
				e->{
					boolean hasTailingTagAttributes = (boolean) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.hasTailingTagAttributes).getCurrentValue();
					boolean toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute = 
							(boolean) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute).getCurrentValue();
					PlainStringMarker concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute = 
							(PlainStringMarker) e.getChildrenNodeBuilderNameMap().get(WithinRecordAttributeStringFormatBaseBuilder.concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute).getCurrentValue();
					
					if(!hasTailingTagAttributes && toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute) {
						return concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute!=null;
					}else {
						return true;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c5);
	
	}
	
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(orderedListOfMandatoryAttribute).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(defaultPrimaryKeyAttributeNameSet).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(nullValueMandatoryAttributeString).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(hasTailingTagAttributes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(tailingTagAttributesFormat).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T withinRecordAttributeStringFormatBase = (T)value;
				this.getChildrenNodeBuilderNameMap().get(orderedListOfMandatoryAttribute).setValue(withinRecordAttributeStringFormatBase.getOrderedListOfMandatoryAttribute(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(defaultPrimaryKeyAttributeNameSet).setValue(withinRecordAttributeStringFormatBase.getDefaultPrimaryKeyAttributeNameSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(nullValueMandatoryAttributeString).setValue(withinRecordAttributeStringFormatBase.getNullValueMandatoryAttributeString(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(hasTailingTagAttributes).setValue(withinRecordAttributeStringFormatBase.hasTailingTagAttributes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(tailingTagAttributesFormat).setValue(withinRecordAttributeStringFormatBase.getTailingTagAttributesFormat(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute).setValue(withinRecordAttributeStringFormatBase.isToMergeTailingExtraDelimitedStringsToLastMandatoryAttribute(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute).setValue(withinRecordAttributeStringFormatBase.getConcatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute(), isEmpty);
			}
		}
		return changed;
	}
	
	@Override
	protected abstract T build();

}
