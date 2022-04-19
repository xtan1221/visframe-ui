package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.fileformat.record.utils.StringMarkerBuilder;
import core.builder.NonLeafNodeBuilder;
import fileformat.record.attribute.CompositeTagRecordAttributeFormat;
import fileformat.record.attribute.TagFormat;
import fileformat.record.utils.StringMarker;

public class CompositeTagRecordAttributeFormatBuilder extends NonLeafNodeBuilder<CompositeTagRecordAttributeFormat>{
	
	protected CompositeTagRecordAttributeFormatBuilder(String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) throws SQLException, IOException {
		super(name, description, canBeNull, parentNodeBuilder);
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String tagFormat = "tagFormat";
	protected static final String tagFormat_description = "tagFormat";
	
	protected static final String tagDelimiter = "tagDelimiter";
	protected static final String tagDelimiter_description = "tagDelimiter";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//SimpleName name, 
		this.addChildNodeBuilder(new VfNameStringBuilder<>(name, name_description, false, this, SimpleName.class));
	
		//VfNotes notes,
		this.addChildNodeBuilder(new VfNotesBuilder(notes, notes_description, false, this));
		
		//TagFormat tagFormat,
		this.addChildNodeBuilder(new TagFormatBuilder(tagFormat, tagFormat_description, false, this));
		
		//StringMarker tagDelimiter
		this.addChildNodeBuilder(new StringMarkerBuilder(tagDelimiter, tagDelimiter_description, false, this));
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
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(tagFormat).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(tagDelimiter).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				CompositeTagRecordAttributeFormat compositeTagRecordAttributeFormat = (CompositeTagRecordAttributeFormat)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(compositeTagRecordAttributeFormat.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(compositeTagRecordAttributeFormat.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(tagFormat).setValue(compositeTagRecordAttributeFormat.getTagFormat(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(tagDelimiter).setValue(compositeTagRecordAttributeFormat.getTagDelimiter(), isEmpty);
			}
		}
		return changed;
	}
	
	@Override
	protected CompositeTagRecordAttributeFormat build() {
		SimpleName nameValue = (SimpleName) this.getChildrenNodeBuilderNameMap().get(name).getCurrentValue();
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		////
		TagFormat tagFormatValue = (TagFormat) this.getChildrenNodeBuilderNameMap().get(tagFormat).getCurrentValue();
		StringMarker tagDelimiterValue = (StringMarker) this.getChildrenNodeBuilderNameMap().get(tagDelimiter).getCurrentValue();
		
		return new CompositeTagRecordAttributeFormat(nameValue, notesValue, tagFormatValue, tagDelimiterValue);
	}

}
