package builder.visframe.importer.vftree.newick;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import basic.VfNotes;
import builder.visframe.importer.vftree.VfTreeDataImporterBaseBuilder;
import context.project.VisProjectDBContext;
import fileformat.vftree.VfTreeDataFileFormatType;
import importer.vftree.newick.SimpleNewickVfTreeDataImporter;
import metadata.MetadataName;

public final class SimpleNewickVfTreeDataImporterBuilder extends VfTreeDataImporterBaseBuilder<SimpleNewickVfTreeDataImporter>{
	public static final String NODE_NAME = "SimpleGEXFGraphDataImporter";
	public static final String NODE_DESCRIPTION = "SimpleGEXFGraphDataImporter";
	
	/**
	 * whether the builder is for {@link VfTreeDataFileFormatType#SIMPLE_NEWICK_1} or not, thus {@link VfTreeDataFileFormatType#SIMPLE_NEWICK_2}
	 */
	private final boolean simpleNewickType1; 
	
	public SimpleNewickVfTreeDataImporterBuilder(VisProjectDBContext hostVisProjectDBContext, boolean simpleNewickType1) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, hostVisProjectDBContext);
		// TODO Auto-generated constructor stub
		
		this.simpleNewickType1 = simpleNewickType1;
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	
	///////////////////////////////////////
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
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
		
		return changed;
	}
	
	
	@Override
	protected SimpleNewickVfTreeDataImporter build() {
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		Path dataSourcePathValue = (Path) this.getChildrenNodeBuilderNameMap().get(dataSourcePath).getCurrentValue();
		MetadataName mainImportedMetadataNameValue = (MetadataName) this.getChildrenNodeBuilderNameMap().get(mainImportedMetadataName).getCurrentValue();
		
		Integer bootstrapIterationValue = (Integer) this.getChildrenNodeBuilderNameMap().get(bootstrapIteration).getCurrentValue();
		
		return new SimpleNewickVfTreeDataImporter(
				notesValue,
				dataSourcePathValue,
				simpleNewickType1?VfTreeDataFileFormatType.SIMPLE_NEWICK_1.getFileFormat().getID():VfTreeDataFileFormatType.SIMPLE_NEWICK_2.getFileFormat().getID(),
				mainImportedMetadataNameValue,
				
				bootstrapIterationValue
			);
	}
}
