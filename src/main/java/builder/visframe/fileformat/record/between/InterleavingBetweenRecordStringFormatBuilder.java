package builder.visframe.fileformat.record.between;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.fileformat.record.utils.PlainStringMarkerBuilder;
import builder.visframe.fileformat.record.utils.StringMarkerBuilder;
import core.builder.GenricChildrenNodeBuilderConstraint;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.between.InterleavingBetweenRecordStringFormat;
import fileformat.record.utils.PlainStringMarker;
import fileformat.record.utils.StringMarker;

public class InterleavingBetweenRecordStringFormatBuilder extends BetweenRecordStringFormatBaseBuilder<InterleavingBetweenRecordStringFormat>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public InterleavingBetweenRecordStringFormatBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	////////////////////
	protected static final String segmentDelimiter = "segmentDelimiter";
	protected static final String segmentDelimiter_description = "segmentDelimiter";

	protected static final String everyRecordSegmentHasHeadingIDAttribute = "everyRecordSegmentHasHeadingIDAttribute";
	protected static final String everyRecordSegmentHasHeadingIDAttribute_description = "everyRecordSegmentHasHeadingIDAttribute";

	protected static final String headingIDAttributeStringDelimiter = "headingIDAttributeStringDelimiter";
	protected static final String headingIDAttributeStringDelimiter_description = "headingIDAttributeStringDelimiter";

	protected static final String headingIDAttributeConcatenatingStringWithSucceedingRecordString = "headingIDAttributeConcatenatingStringWithSucceedingRecordString";
	protected static final String headingIDAttributeConcatenatingStringWithSucceedingRecordString_description = "headingIDAttributeConcatenatingStringWithSucceedingRecordString";

	protected static final String recordSegmentsCatenatingString = "recordSegmentsCatenatingString";
	protected static final String recordSegmentsCatenatingString_description = "recordSegmentsCatenatingString";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//
		super.buildChildrenNodeBuilderNameMap();
		
		//StringMarker segmentDelimiter,
		this.addChildNodeBuilder(new StringMarkerBuilder(segmentDelimiter, segmentDelimiter_description, false, this));
		
		//boolean everyRecordSegmentHasHeadingIDAttribute,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				everyRecordSegmentHasHeadingIDAttribute, 
				everyRecordSegmentHasHeadingIDAttribute_description,
				false, this));
		
		//StringMarker headingIDAttributeStringDelimiter,
		this.addChildNodeBuilder(new StringMarkerBuilder(
				headingIDAttributeStringDelimiter, headingIDAttributeStringDelimiter_description, 
				true, this));
		
		//PlainStringMarker headingIDAttributeConcatenatingStringWithSucceedingRecordString,
		this.addChildNodeBuilder(new PlainStringMarkerBuilder(
				headingIDAttributeConcatenatingStringWithSucceedingRecordString, headingIDAttributeConcatenatingStringWithSucceedingRecordString_description, 
				true, this));
		
		//PlainStringMarker recordSegmentsCatenatingString
		this.addChildNodeBuilder(new PlainStringMarkerBuilder(recordSegmentsCatenatingString, recordSegmentsCatenatingString_description, 
				false, this));
		
	}
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//headingIDAttributeStringDelimiter cannot be null if everyRecordSegmentHasHeadingIDAttribute is true;
		//otherwise, must be null;
		Set<String> set1 = new HashSet<>();
		set1.add(headingIDAttributeStringDelimiter);
		set1.add(everyRecordSegmentHasHeadingIDAttribute);
		GenricChildrenNodeBuilderConstraint<InterleavingBetweenRecordStringFormat> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if everyRecordSegmentHasHeadingIDAttribute is true; headingIDAttributeStringDelimiter cannot be null; otherwise, must be null!",
				set1, 
				e->{
					StringMarker headingIDAttributeStringDelimiter = (StringMarker) e.getChildrenNodeBuilderNameMap().get(InterleavingBetweenRecordStringFormatBuilder.headingIDAttributeStringDelimiter).getCurrentValue();
					boolean everyRecordSegmentHasHeadingIDAttribute = (boolean) e.getChildrenNodeBuilderNameMap().get(InterleavingBetweenRecordStringFormatBuilder.everyRecordSegmentHasHeadingIDAttribute).getCurrentValue();
					
					if(everyRecordSegmentHasHeadingIDAttribute)
						return headingIDAttributeStringDelimiter!=null;
					else
						return headingIDAttributeStringDelimiter==null;
					}
				);
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		
		//headingIDAttributeConcatenatingStringWithSucceedingRecordString cannot be null if everyRecordSegmentHasHeadingIDAttribute is true
		//otherwise, must be null;
		Set<String> set2 = new HashSet<>();
		set2.add(headingIDAttributeConcatenatingStringWithSucceedingRecordString);
		set2.add(everyRecordSegmentHasHeadingIDAttribute);
		GenricChildrenNodeBuilderConstraint<InterleavingBetweenRecordStringFormat> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if everyRecordSegmentHasHeadingIDAttribute is true; headingIDAttributeConcatenatingStringWithSucceedingRecordString cannot be null; otherwise, must be null!",
				set2, 
				e->{
					PlainStringMarker headingIDAttributeConcatenatingStringWithSucceedingRecordString = (PlainStringMarker) e.getChildrenNodeBuilderNameMap().get(InterleavingBetweenRecordStringFormatBuilder.headingIDAttributeConcatenatingStringWithSucceedingRecordString).getCurrentValue();
					boolean everyRecordSegmentHasHeadingIDAttribute = (boolean) e.getChildrenNodeBuilderNameMap().get(InterleavingBetweenRecordStringFormatBuilder.everyRecordSegmentHasHeadingIDAttribute).getCurrentValue();
					
					if(everyRecordSegmentHasHeadingIDAttribute)
						return headingIDAttributeConcatenatingStringWithSucceedingRecordString!=null;
					else
						return headingIDAttributeConcatenatingStringWithSucceedingRecordString==null;
					}
				);
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
			this.getChildrenNodeBuilderNameMap().get(segmentDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(everyRecordSegmentHasHeadingIDAttribute).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(headingIDAttributeStringDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(headingIDAttributeConcatenatingStringWithSucceedingRecordString).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(recordSegmentsCatenatingString).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				InterleavingBetweenRecordStringFormat interleavingBetweenRecordStringFormat = (InterleavingBetweenRecordStringFormat)value;
				this.getChildrenNodeBuilderNameMap().get(segmentDelimiter).setValue(interleavingBetweenRecordStringFormat.getSegmentDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(everyRecordSegmentHasHeadingIDAttribute).setValue(interleavingBetweenRecordStringFormat.everyRecordSegmentHasHeadingIDAttribute(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(headingIDAttributeStringDelimiter).setValue(interleavingBetweenRecordStringFormat.getHeadingIDAttributeStringDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(headingIDAttributeConcatenatingStringWithSucceedingRecordString).setValue(interleavingBetweenRecordStringFormat.getHeadingIDAttributeConatenatingStringWithSucceedingRecordString(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(recordSegmentsCatenatingString).setValue(interleavingBetweenRecordStringFormat.getRecordSegmentsCatenatingString(), isEmpty);
		
			}
		}
		return changed;
	}
	
	
	@Override
	protected InterleavingBetweenRecordStringFormat build() {
		int numberOfHeadingLinesToSkipValue = (int) this.getChildrenNodeBuilderNameMap().get(numberOfHeadingLinesToSkip).getCurrentValue(); 
		PlainStringMarker commentStringMarkerValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(commentStringMarker).getCurrentValue();
		StringMarker recordDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(recordDelimiter).getCurrentValue();
		boolean toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecordValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord).getCurrentValue();
		///
		StringMarker segmentDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(segmentDelimiter).getCurrentValue();
		boolean everyRecordSegmentHasHeadingIDAttributeValue = (boolean) this.getChildrenNodeBuilderNameMap().get(everyRecordSegmentHasHeadingIDAttribute).getCurrentValue();
		StringMarker headingIDAttributeStringDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(headingIDAttributeStringDelimiter).getCurrentValue();
		PlainStringMarker headingIDAttributeConcatenatingStringWithSucceedingRecordStringValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(headingIDAttributeConcatenatingStringWithSucceedingRecordString).getCurrentValue();
		PlainStringMarker recordSegmentsCatenatingStringValue = (PlainStringMarker) this.getChildrenNodeBuilderNameMap().get(recordSegmentsCatenatingString).getCurrentValue();
	
		return new InterleavingBetweenRecordStringFormat(
				numberOfHeadingLinesToSkipValue,commentStringMarkerValue,recordDelimiterValue,toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecordValue,
				
				segmentDelimiterValue,everyRecordSegmentHasHeadingIDAttributeValue,headingIDAttributeStringDelimiterValue,
				headingIDAttributeConcatenatingStringWithSucceedingRecordStringValue,recordSegmentsCatenatingStringValue
				);
	}

}
