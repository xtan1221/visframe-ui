package builder.visframe.fileformat.record.within;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.list.ArrayListNonLeafNodeBuilder;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.fileformat.record.utils.StringMarkerBuilder;
import builder.visframe.fileformat.record.utils.StringMarkerBuilderFactory;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.RecordDataFileFormat.PrimaryKeyAttributeNameSet;
import fileformat.record.attribute.AbstractRecordAttributeFormat;
import fileformat.record.attribute.TagFormat;
import fileformat.record.utils.PlainStringMarker;
import fileformat.record.utils.StringMarker;
import fileformat.record.within.StringDelimitedRecordAttributeStringFormat;

public class StringDelimitedRecordAttributeStringFormatBuilder extends WithinRecordAttributeStringFormatBaseBuilder<StringDelimitedRecordAttributeStringFormat>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public StringDelimitedRecordAttributeStringFormatBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////////////
	protected static final String hasSingleMandatoryAttributeDelimiter = "hasSingleMandatoryAttributeDelimiter";
	protected static final String hasSingleMandatoryAttributeDelimiter_description = "hasSingleMandatoryAttributeDelimiter";

	protected static final String singleMandatoryAttributeDelimiter = "singleMandatoryAttributeDelimiter";
	protected static final String singleMandatoryAttributeDelimiter_description = "singleMandatoryAttributeDelimiter";

	protected static final String mandatoryAttributeDelimiterList = "mandatoryAttributeDelimiterList";
	protected static final String mandatoryAttributeDelimiterList_description = "mandatoryAttributeDelimiterList";

	protected static final String recordStringStartingWithMandatoryAttributeDelimiter = "recordStringStartingWithMandatoryAttributeDelimiter";
	protected static final String recordStringStartingWithMandatoryAttributeDelimiter_description = "recordStringStartingWithMandatoryAttributeDelimiter";

	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		
		//boolean hasSingleMandatoryAttributeDelimiter,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				hasSingleMandatoryAttributeDelimiter, hasSingleMandatoryAttributeDelimiter_description,
				false, this));
		
		//StringMarker singleMandatoryAttributeDelimiter,
		this.addChildNodeBuilder(new StringMarkerBuilder(
				singleMandatoryAttributeDelimiter, singleMandatoryAttributeDelimiter_description,
				true, this));
		
		
		//List<StringMarker> mandatoryAttributeDelimiterList,
		StringMarkerBuilderFactory mandatoryAttributeLeafNodeBuilderFactory = 
				new StringMarkerBuilderFactory("mandatoryAttributeDelimiter", "mandatoryAttributeDelimiter", false);
		this.addChildNodeBuilder(
				new ArrayListNonLeafNodeBuilder<>(
						mandatoryAttributeDelimiterList, mandatoryAttributeDelimiterList_description, true, this,
						mandatoryAttributeLeafNodeBuilderFactory,
						true//allowed to have duplicates
        		));
		
		
		//boolean recordStringStartingWithMandatoryAttributeDelimiter
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				recordStringStartingWithMandatoryAttributeDelimiter, recordStringStartingWithMandatoryAttributeDelimiter_description,
				false, this));
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//singleMandatoryAttributeDelimiter cannot be null if hasSingleMandatoryAttributeDelimiter is true; must be null otherwise
		Set<String> set1 = new HashSet<>();
		set1.add(singleMandatoryAttributeDelimiter);
		set1.add(hasSingleMandatoryAttributeDelimiter);
		GenricChildrenNodeBuilderConstraint<StringDelimitedRecordAttributeStringFormat> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "singleMandatoryAttributeDelimiter cannot be null if hasSingleMandatoryAttributeDelimiter is true; must be null otherwise!",
				set1, 
				e->{
					boolean hasSingleMandatoryAttributeDelimiter = 
							(boolean) e.getChildrenNodeBuilderNameMap().get(StringDelimitedRecordAttributeStringFormatBuilder.hasSingleMandatoryAttributeDelimiter).getCurrentValue();
					StringMarker singleMandatoryAttributeDelimiter = 
							(StringMarker) e.getChildrenNodeBuilderNameMap().get(StringDelimitedRecordAttributeStringFormatBuilder.singleMandatoryAttributeDelimiter).getCurrentValue();
					
					if(hasSingleMandatoryAttributeDelimiter) {
						return singleMandatoryAttributeDelimiter!=null;
					}else {
						return singleMandatoryAttributeDelimiter==null;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//mandatoryAttributeDelimiterList cannot be null and empty if hasSingleMandatoryAttributeDelimiter is false, must be null otherwise;
		Set<String> set2 = new HashSet<>();
		set2.add(mandatoryAttributeDelimiterList);
		set2.add(hasSingleMandatoryAttributeDelimiter);
		GenricChildrenNodeBuilderConstraint<StringDelimitedRecordAttributeStringFormat> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "mandatoryAttributeDelimiterList cannot be null and empty if hasSingleMandatoryAttributeDelimiter is false, must be null otherwise!",
				set2, 
				e->{
					boolean hasSingleMandatoryAttributeDelimiter = 
							(boolean) e.getChildrenNodeBuilderNameMap().get(StringDelimitedRecordAttributeStringFormatBuilder.hasSingleMandatoryAttributeDelimiter).getCurrentValue();
					@SuppressWarnings("unchecked")
					List<StringMarker> mandatoryAttributeDelimiterList = 
							(List<StringMarker>) e.getChildrenNodeBuilderNameMap().get(StringDelimitedRecordAttributeStringFormatBuilder.mandatoryAttributeDelimiterList).getCurrentValue();
					
					if(hasSingleMandatoryAttributeDelimiter) {
						return mandatoryAttributeDelimiterList==null;
					}else {
						return mandatoryAttributeDelimiterList!=null;
					}
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
		
	}
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(hasSingleMandatoryAttributeDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(singleMandatoryAttributeDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(mandatoryAttributeDelimiterList).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(recordStringStartingWithMandatoryAttributeDelimiter).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				StringDelimitedRecordAttributeStringFormat stringDelimitedRecordAttributeStringFormat = (StringDelimitedRecordAttributeStringFormat)value;
				this.getChildrenNodeBuilderNameMap().get(hasSingleMandatoryAttributeDelimiter).setValue(stringDelimitedRecordAttributeStringFormat.hasSingleMandatoryAttributeDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(singleMandatoryAttributeDelimiter).setValue(stringDelimitedRecordAttributeStringFormat.getSingleMandatoryAttributeDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(mandatoryAttributeDelimiterList).setValue(stringDelimitedRecordAttributeStringFormat.getMandatoryAttributeDelimiterList(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(recordStringStartingWithMandatoryAttributeDelimiter).setValue(stringDelimitedRecordAttributeStringFormat.isRecordStringStartingWithMandatoryAttributeDelimiter(), isEmpty);
			}
		}
		return changed;
	}
	
	
	
	@Override
	protected StringDelimitedRecordAttributeStringFormat build(){
		@SuppressWarnings("unchecked")
		List<AbstractRecordAttributeFormat> orderedListOfMandatoryAttributeValue = (List<AbstractRecordAttributeFormat>) this.getChildrenNodeBuilderNameMap().get(orderedListOfMandatoryAttribute).getCurrentValue();
		PrimaryKeyAttributeNameSet defaultPrimaryKeyAttributeNameSetValue = (PrimaryKeyAttributeNameSet) this.getChildrenNodeBuilderNameMap().get(defaultPrimaryKeyAttributeNameSet).getCurrentValue();
		PlainStringMarker nullValueMandatoryAttributeStringValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(nullValueMandatoryAttributeString).getCurrentValue();
		boolean hasTailingTagAttributesValue = (boolean) this.getChildrenNodeBuilderNameMap().get(hasTailingTagAttributes).getCurrentValue();
		TagFormat tailingTagAttributesFormatValue = (TagFormat) this.getChildrenNodeBuilderNameMap().get(tailingTagAttributesFormat).getCurrentValue();
		boolean toMergeTailingExtraDelimitedStringsToLastMandatoryAttributeValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toMergeTailingExtraDelimitedStringsToLastMandatoryAttribute).getCurrentValue();
		PlainStringMarker concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttributeValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttribute).getCurrentValue();
		///////
		boolean hasSingleMandatoryAttributeDelimiterValue = (boolean) this.getChildrenNodeBuilderNameMap().get(hasSingleMandatoryAttributeDelimiter).getCurrentValue();
		StringMarker singleMandatoryAttributeDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(singleMandatoryAttributeDelimiter).getCurrentValue();
		@SuppressWarnings("unchecked")
		List<StringMarker> mandatoryAttributeDelimiterListValue = (List<StringMarker>) this.getChildrenNodeBuilderNameMap().get(mandatoryAttributeDelimiterList).getCurrentValue();
		boolean recordStringStartingWithMandatoryAttributeDelimiterValue = (boolean) this.getChildrenNodeBuilderNameMap().get(recordStringStartingWithMandatoryAttributeDelimiter).getCurrentValue();
		
		return new StringDelimitedRecordAttributeStringFormat(
				orderedListOfMandatoryAttributeValue,
				defaultPrimaryKeyAttributeNameSetValue,
				nullValueMandatoryAttributeStringValue,
				hasTailingTagAttributesValue,
				tailingTagAttributesFormatValue,
				toMergeTailingExtraDelimitedStringsToLastMandatoryAttributeValue,
				concatenatingStringToMergeTailingDelimitedStringToLastMandatoryAttributeValue,
				
				hasSingleMandatoryAttributeDelimiterValue,
				singleMandatoryAttributeDelimiterValue,
				mandatoryAttributeDelimiterListValue,
				recordStringStartingWithMandatoryAttributeDelimiterValue
				
				);
	}

}
