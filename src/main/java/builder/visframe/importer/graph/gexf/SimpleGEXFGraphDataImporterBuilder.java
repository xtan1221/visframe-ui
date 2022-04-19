package builder.visframe.importer.graph.gexf;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import basic.VfNotes;
import builder.visframe.importer.graph.GraphDataImporterBaseBuilder;
import context.project.VisProjectDBContext;
import importer.graph.gexf.SimpleGEXFGraphDataImporter;
import metadata.MetadataName;
import metadata.graph.type.GraphTypeEnforcer;


public final class SimpleGEXFGraphDataImporterBuilder extends GraphDataImporterBaseBuilder<SimpleGEXFGraphDataImporter>{
	public static final String NODE_NAME = "SimpleGEXFGraphDataImporter";
	public static final String NODE_DESCRIPTION = "SimpleGEXFGraphDataImporter";
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SimpleGEXFGraphDataImporterBuilder(VisProjectDBContext hostVisProjectDBContext) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, hostVisProjectDBContext);
		// TODO Auto-generated constructor stub
		
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
	protected SimpleGEXFGraphDataImporter build() {
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		Path dataSourcePathValue = (Path) this.getChildrenNodeBuilderNameMap().get(dataSourcePath).getCurrentValue();
		MetadataName mainImportedMetadataNameValue = (MetadataName) this.getChildrenNodeBuilderNameMap().get(mainImportedMetadataName).getCurrentValue();
		
		GraphTypeEnforcer typeEnforcerValue = (GraphTypeEnforcer) this.getChildrenNodeBuilderNameMap().get(graphTypeEnforcer).getCurrentValue();
		return new SimpleGEXFGraphDataImporter(
				notesValue,
				dataSourcePathValue,
				mainImportedMetadataNameValue,
				
				typeEnforcerValue
			);
	}
}
