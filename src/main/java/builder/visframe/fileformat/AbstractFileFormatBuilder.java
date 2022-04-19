package builder.visframe.fileformat;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import core.builder.NonLeafNodeBuilder;
import importer.AbstractFileFormat;

public abstract class AbstractFileFormatBuilder<T extends AbstractFileFormat> extends NonLeafNodeBuilder<T>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected AbstractFileFormatBuilder(
			String name, String description) {
		super(name, description, false, null);
		// TODO Auto-generated constructor stub
	}
	
	//////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";

	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//SimpleName name, 
		this.addChildNodeBuilder(new VfNameStringBuilder<>(name, name_description, false, this, SimpleName.class));
	
		
		//VfNotes notes,
		this.addChildNodeBuilder(new VfNotesBuilder(notes, notes_description, false, this));
	
	}
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T abstractFileFormat = (T)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(abstractFileFormat.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(abstractFileFormat.getNotes(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected abstract T build();

}
