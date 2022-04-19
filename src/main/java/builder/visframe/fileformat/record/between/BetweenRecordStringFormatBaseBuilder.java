package builder.visframe.fileformat.record.between;

import java.io.IOException;
import java.sql.SQLException;

import builder.basic.primitive.BooleanTypeBuilder;
import builder.basic.primitive.IntegerTypeBuilder;
import builder.visframe.fileformat.record.utils.PlainStringMarkerBuilder;
import builder.visframe.fileformat.record.utils.StringMarkerBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.between.BetweenRecordStringFormatBase;

public abstract class BetweenRecordStringFormatBaseBuilder<T extends BetweenRecordStringFormatBase> extends NonLeafNodeBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected BetweenRecordStringFormatBaseBuilder(String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
	}
	
	////////////////////////////////////
	protected static final String numberOfHeadingLinesToSkip = "numberOfHeadingLinesToSkip";
	protected static final String numberOfHeadingLinesToSkip_description = "numberOfHeadingLinesToSkip";

	protected static final String commentStringMarker = "commentStringMarker";
	protected static final String commentStringMarker_description = "commentStringMarker";

	protected static final String recordDelimiter = "recordDelimiter";
	protected static final String recordDelimiter_description = "recordDelimiter";

	protected static final String toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord = "toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord";
	protected static final String toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord_description = "toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord";

	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//int numberOfHeadingLinesToSkip, 
		this.addChildNodeBuilder(new IntegerTypeBuilder(
				numberOfHeadingLinesToSkip, numberOfHeadingLinesToSkip_description, false, this,
				e->{return e>=0;},"numberOfHeadingLinesToSkip must be non-negative integer!"));
		
		//PlainStringMarker commentStringMarker,
		this.addChildNodeBuilder(new PlainStringMarkerBuilder(commentStringMarker, commentStringMarker_description, true, this));
		
		//StringMarker recordDelimiter, 
		this.addChildNodeBuilder(new StringMarkerBuilder(recordDelimiter, recordDelimiter_description, false, this));
		
		//boolean toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord, 
				toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord_description,
				false, this));
	}

	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}
	////////////////////////////////////////
	/**
	 * set value of fields of {@link BetweenRecordStringFormatBase} class
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(numberOfHeadingLinesToSkip).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(commentStringMarker).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(recordDelimiter).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				BetweenRecordStringFormatBase betweenRecordStringFormatBase = (BetweenRecordStringFormatBase)value;
				this.getChildrenNodeBuilderNameMap().get(numberOfHeadingLinesToSkip).setValue(betweenRecordStringFormatBase.getNumberOfHeadingLinesToSkip(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(commentStringMarker).setValue(betweenRecordStringFormatBase.getCommentingStringMarker(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(recordDelimiter).setValue(betweenRecordStringFormatBase.getRecordDelimiter(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord).setValue(betweenRecordStringFormatBase.isToKeepNewLineCharactersBetweenStringPiecesOfTheSameRecord(), isEmpty);
			}
		}
		
		return changed;
	}
	
	/**
	 * 
	 */
	@Override
	protected abstract T build();

}
