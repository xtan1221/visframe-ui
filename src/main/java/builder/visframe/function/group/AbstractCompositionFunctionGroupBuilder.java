package builder.visframe.function.group;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import basic.lookup.project.type.udt.VisProjectMetadataManager;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.metadata.MetadataIDSelector;
import core.builder.NonLeafNodeBuilder;
import function.group.AbstractCompositionFunctionGroup;
import function.group.CompositionFunctionGroupName;
import metadata.DataType;

public abstract class AbstractCompositionFunctionGroupBuilder<T extends AbstractCompositionFunctionGroup> extends NonLeafNodeBuilder<T>{
	private final VisProjectMetadataManager hostVisProjectMetadataManager;
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	protected AbstractCompositionFunctionGroupBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder,
			
			VisProjectMetadataManager hostVisProjectMetadataManager) {
		super(name, description, canBeNull, parentNodeBuilder);
		
		this.hostVisProjectMetadataManager = hostVisProjectMetadataManager;
		
	}
	


	protected VisProjectMetadataManager getHostVisProjectMetadataManager() {
		return hostVisProjectMetadataManager;
	}
	
	
	private Set<DataType> getSelectedDataTypeSet(){
		Set<DataType> ret = new HashSet<>();
		ret.add(DataType.RECORD);
		
		return ret;
	}
	
	/////////////////////////////////////////////
	protected static final String name = "name";
	protected static final String name_description = "name";
	
	protected static final String notes = "notes";
	protected static final String notes_description = "notes";
	
	protected static final String ownerRecordDataMetadataID = "ownerRecordDataMetadataID";
	protected static final String ownerRecordDataMetadataID_description = "ownerRecordDataMetadataID";
	
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//name
		this.addChildNodeBuilder(new VfNameStringBuilder<CompositionFunctionGroupName>(
				name, name_description,
				false, this, CompositionFunctionGroupName.class
				));
		
		//notes,
		this.addChildNodeBuilder(new VfNotesBuilder(
				notes, notes_description, 
				false, this));
		
		//ownerRecordDataMetadataID
		this.addChildNodeBuilder(new MetadataIDSelector(
				ownerRecordDataMetadataID, ownerRecordDataMetadataID_description,
				false, this,
				this.getHostVisProjectMetadataManager(), this.getSelectedDataTypeSet(),
				null
				));
	}
	
	
	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		//
	}

	@Override
	protected void addStatusChangeEventActionOfChildNodeBuilders() {
		//
	}
	
	
	////////////////////////////////////////
	@Override
	public boolean setValue(Object value, boolean isEmpty) throws SQLException, IOException {
		boolean changed = super.setValue(value, isEmpty);
		
		if(isEmpty) {
			this.getChildrenNodeBuilderNameMap().get(name).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(ownerRecordDataMetadataID).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T abstractCompositionFunctionGroup = (T)value;
				this.getChildrenNodeBuilderNameMap().get(name).setValue(abstractCompositionFunctionGroup.getName(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(abstractCompositionFunctionGroup.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(ownerRecordDataMetadataID).setValue(abstractCompositionFunctionGroup.getOwnerRecordDataMetadataID(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected abstract T build();

}
