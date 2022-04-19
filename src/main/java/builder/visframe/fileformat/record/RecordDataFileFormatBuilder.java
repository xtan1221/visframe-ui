package builder.visframe.fileformat.record;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.fileformat.AbstractFileFormatBuilder;
import builder.visframe.fileformat.record.between.InterleavingBetweenRecordStringFormatBuilder;
import builder.visframe.fileformat.record.between.SequentialBetweenRecordStringFormatBuilder;
import builder.visframe.fileformat.record.within.StringDelimitedRecordAttributeStringFormatBuilder;
import fileformat.record.RecordDataFileFormat;
import fileformat.record.between.BetweenRecordStringFormatBase;
import fileformat.record.within.WithinRecordAttributeStringFormatBase;

public final class RecordDataFileFormatBuilder extends AbstractFileFormatBuilder<RecordDataFileFormat>{
	public static final String NODE_NAME = "RecordDataFileFormat";
	public static final String NODE_DESCRIPTION = "RecordDataFileFormat";
	
	/**
	 * whether the between record format is of sequential type
	 */
	private Boolean sequential;
	
	private Boolean stringDelimited;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 * @param sequential
	 * @param stringDelimited
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public RecordDataFileFormatBuilder(boolean sequential, boolean stringDelimited) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION); // no parent node builder

		this.sequential = sequential;
		this.stringDelimited = stringDelimited;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}

	///////////////////////////
	protected static final String betweenRecordStringFormat = "betweenRecordStringFormat";
	protected static final String betweenRecordStringFormat_description = "betweenRecordStringFormat";
	
	protected static final String withinRecordAttributeStringFormat = "withinRecordAttributeStringFormat";
	protected static final String withinRecordAttributeStringFormat_description = "withinRecordAttributeStringFormat";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//BetweenRecordStringFormatBase betweenRecordStringFormat,
		if(this.sequential) {
			this.addChildNodeBuilder(
					new SequentialBetweenRecordStringFormatBuilder(
							betweenRecordStringFormat, betweenRecordStringFormat_description,
							false, this));
		}else {
			this.addChildNodeBuilder(
					new InterleavingBetweenRecordStringFormatBuilder(
						betweenRecordStringFormat, betweenRecordStringFormat_description,
						false, this));
		}
		
		//WithinRecordAttributeStringFormatBase withinRecordAttributeStringFormat
		if(this.stringDelimited) {
			this.addChildNodeBuilder(new StringDelimitedRecordAttributeStringFormatBuilder(
					withinRecordAttributeStringFormat, withinRecordAttributeStringFormat_description,
					false, this));
		}else {
			throw new UnsupportedOperationException("not implemented yet");
		}
	}


	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
	}

	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		super.addStatusChangeEventActionOfChildNodeBuilders();
	}

	
	//////////////////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(betweenRecordStringFormat).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(withinRecordAttributeStringFormat).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				RecordDataFileFormat recordDataFileFormat = (RecordDataFileFormat)value;
				
				this.getChildrenNodeBuilderNameMap().get(betweenRecordStringFormat).setValue(recordDataFileFormat.getBetweenRecordStringFormat(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(withinRecordAttributeStringFormat).setValue(recordDataFileFormat.getWithinRecordAttributeStringFormat(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	
	
	@Override
	protected RecordDataFileFormat build() {
		SimpleName nameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		BetweenRecordStringFormatBase betweenRecordStringFormatValue = (BetweenRecordStringFormatBase) this.getChildrenNodeBuilderNameMap().get(betweenRecordStringFormat).getCurrentValue();
		WithinRecordAttributeStringFormatBase withinRecordAttributeStringFormatValue = (WithinRecordAttributeStringFormatBase) this.getChildrenNodeBuilderNameMap().get(withinRecordAttributeStringFormat).getCurrentValue();
	
		return new RecordDataFileFormat(nameValue, notesValue, betweenRecordStringFormatValue, withinRecordAttributeStringFormatValue);
	}
	
}
