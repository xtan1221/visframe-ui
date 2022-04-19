package builder.visframe.importer;

import java.io.IOException;
import java.sql.SQLException;

import builder.basic.misc.FilePathChooser;
import builder.visframe.basic.VfNameStringBuilder;
import builder.visframe.basic.VfNotesBuilder;
import builder.visframe.fileformat.FileFormatIDSelector;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import importer.AbstractDataImporter;
import metadata.DataType;
import metadata.MetadataName;

public abstract class AbstractDataImporterBuilder<T extends AbstractDataImporter> extends NonLeafNodeBuilder<T>{

	private final VisProjectDBContext hostVisProjectDBContext;
	private final DataType dataType;
	
	private final boolean toSelectFileFormatInLastStep;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param hostVisProjectDBContext
	 * @param dataType
	 * @param toSelectFileFormatInLastStep whether the File Format is to be selected in the last step or a step before last step; if true, the FileFormatIDSelector should be added in the {@link #buildChildrenNodeBuilderNameMap()} method, otherwise, not added
	 */
	protected AbstractDataImporterBuilder(
			String name, String description,
			
			VisProjectDBContext hostVisProjectDBContext,
			DataType dataType,
			
			boolean toSelectFileFormatInLastStep) {
		super(name, description, false, null);
		// TODO Auto-generated constructor stub
		
		this.dataType = dataType;
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		
		this.toSelectFileFormatInLastStep = toSelectFileFormatInLastStep;
	}
	protected DataType getDataType() {
		return dataType;
	}
	
	/**
	 * @return the visProjectFileFormatManager
	 */
	protected VisProjectDBContext getVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	/**
	 * whether the File Format is to be selected in the last step or a step before last step; if true, the FileFormatIDSelector should be added in the {@link #buildChildrenNodeBuilderNameMap()} method, otherwise, not added
	 * @return the toSelectFileFormatInLastStep
	 */
	protected boolean isToSelectFileFormatInLastStep() {
		return toSelectFileFormatInLastStep;
	}
	
	
	//////////////////////////////////////////
	/////////////////////////////////////////////
	protected static final String notes = "Notes";
	protected static final String notes_description = "Notes";
	
	protected static final String dataSourcePath = "dataSourcePath";
	protected static final String dataSourcePath_description = "dataSourcePath";
	
	protected static final String fileFormatID = "File Format ID";
	protected static final String fileFormatID_description = "File Format ID";
	
	protected static final String mainImportedMetadataName = "Main imported Metadata name";
	protected static final String mainImportedMetadataName_description = "Main imported Metadata name";
	
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		//VfNotes notes,
		this.addChildNodeBuilder(new VfNotesBuilder(notes, notes_description, false, this));

		//Path dataSourcePath, 
		this.addChildNodeBuilder(new FilePathChooser(dataSourcePath, dataSourcePath_description, false, this));
		
		//FileFormatID fileFormatID, 
		if(this.toSelectFileFormatInLastStep) {
			this.addChildNodeBuilder(
					new FileFormatIDSelector(
							fileFormatID, fileFormatID_description, false, this,
							this.getVisProjectDBContext().getHasIDTypeManagerController().getFileFormatManager(), 
							this.getDataType(),
							null));
		}
		
		//MetadataName mainImportedMetadataName
		this.addChildNodeBuilder(
				new VfNameStringBuilder<MetadataName>(mainImportedMetadataName, mainImportedMetadataName_description, false, this, MetadataName.class));
		
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
			this.getChildrenNodeBuilderNameMap().get(notes).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(dataSourcePath).setValue(null, isEmpty);
			if(this.isToSelectFileFormatInLastStep())
				this.getChildrenNodeBuilderNameMap().get(fileFormatID).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(mainImportedMetadataName).setValue(null, isEmpty);
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				@SuppressWarnings("unchecked")
				T abstractDataImporter = (T)value;
				this.getChildrenNodeBuilderNameMap().get(notes).setValue(abstractDataImporter.getNotes(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(dataSourcePath).setValue(abstractDataImporter.getDataSourcePath(), isEmpty);
				if(this.isToSelectFileFormatInLastStep())
					this.getChildrenNodeBuilderNameMap().get(fileFormatID).setValue(abstractDataImporter.getFileFormatID(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(mainImportedMetadataName).setValue(abstractDataImporter.getMainImportedMetadataName(), isEmpty);
				
			}
		}
		
		return changed;
	}
	
	@Override
	protected abstract T build();
	
}
