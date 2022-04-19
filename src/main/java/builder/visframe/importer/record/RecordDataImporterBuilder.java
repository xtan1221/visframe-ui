package builder.visframe.importer.record;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import basic.SimpleName;
import basic.VfNotes;
import builder.basic.collection.set.nonleaf.HashSetNonLeafNodeBuilder;
import builder.basic.primitive.BooleanTypeBuilder;
import builder.visframe.basic.VfNameStringBuilderFactory;
import builder.visframe.fileformat.record.PrimaryKeyAttributeNameSetBuilder;
import builder.visframe.importer.AbstractDataImporterBuilder;
import context.project.VisProjectDBContext;
import core.builder.GenricChildrenNodeBuilderConstraint;
import fileformat.FileFormatID;
import fileformat.record.RecordDataFileFormat.PrimaryKeyAttributeNameSet;
import importer.record.RecordDataImporter;
import metadata.DataType;
import metadata.MetadataName;

public final class RecordDataImporterBuilder extends AbstractDataImporterBuilder<RecordDataImporter>{
	public static final String NODE_NAME = "RecordDataImporter";
	public static final String NODE_DESCRIPTION = "RecordDataImporter";
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @param dataType
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public RecordDataImporterBuilder(VisProjectDBContext hostVisProjectDBContext) throws SQLException, IOException {
		super(NODE_NAME, NODE_DESCRIPTION, hostVisProjectDBContext, DataType.RECORD, true);//toSelectFileFormatInLastStep
		// TODO Auto-generated constructor stub
		
		this.buildChildrenNodeBuilderNameMap();
		
		this.buildGenericChildrenNodeBuilderConstraintSet();
		
		this.addStatusChangeEventActionOfChildNodeBuilders();
	}
	
	///////////////////////////
	protected static final String alternativePrimaryKeyAttributeNameSet = "alternativePrimaryKeyAttributeNameSet";
	protected static final String alternativePrimaryKeyAttributeNameSet_description = "alternativePrimaryKeyAttributeNameSet";
	
	protected static final String toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable = "toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable";
	protected static final String toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable_description = "toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable";
	
	protected static final String mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable = "mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable";
	protected static final String mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable_description = "mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable";
	
	protected static final String toIncludeAllDiscoverdTagAttriubteInResultedDataTable = "toIncludeAllDiscoverdTagAttriubteInResultedDataTable";
	protected static final String toIncludeAllDiscoverdTagAttriubteInResultedDataTable_description = "toIncludeAllDiscoverdTagAttriubteInResultedDataTable";
	
	protected static final String discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable = "discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable";
	protected static final String discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable_description = "discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable";
		
	@Override
	protected void buildChildrenNodeBuilderNameMap() throws SQLException, IOException {
		super.buildChildrenNodeBuilderNameMap();
		
		//PrimaryKeyAttributeNameSet alternativePrimaryKeyAttributeNameSet,
		this.addChildNodeBuilder(new PrimaryKeyAttributeNameSetBuilder(
				alternativePrimaryKeyAttributeNameSet, alternativePrimaryKeyAttributeNameSet_description,
				true, this));//can be null if the default one is to be used;
		
		//boolean toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable, toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable_description,
				false, this));
		
		//Set<SimpleName> mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable,
		VfNameStringBuilderFactory<SimpleName> mandatorySimpleRecordAttributeNameBuilderFactory = 
				new VfNameStringBuilderFactory<>("mandatorySimpleRecordAttributeName","mandatorySimpleRecordAttributeName", false, SimpleName.class);
		this.addChildNodeBuilder(
				new HashSetNonLeafNodeBuilder<>(
						mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable, mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable_description, 
						true, null, //can be null if all are to be included
						mandatorySimpleRecordAttributeNameBuilderFactory
	        		));
		
		//boolean toIncludeAllDiscoverdTagAttriubteInResultedDataTable,
		this.addChildNodeBuilder(new BooleanTypeBuilder(
				toIncludeAllDiscoverdTagAttriubteInResultedDataTable, toIncludeAllDiscoverdTagAttriubteInResultedDataTable_description,
				false, this));
		
		//Set<SimpleName> discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable
		VfNameStringBuilderFactory<SimpleName> discoveredTagSimpleRecordAttributeNameBuilderFactory = 
				new VfNameStringBuilderFactory<>("discoveredTagSimpleRecordAttributeName","discoveredTagSimpleRecordAttributeName", false, SimpleName.class);
		this.addChildNodeBuilder(
				new HashSetNonLeafNodeBuilder<>(
						discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable, discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable_description, 
						true, null, //can be null if all are to be included
						discoveredTagSimpleRecordAttributeNameBuilderFactory
	        		));
	}


	@Override
	protected void buildGenericChildrenNodeBuilderConstraintSet() {
		super.buildGenericChildrenNodeBuilderConstraintSet();
		
		//mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable can never be empty set; can be null only if toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable is true; otherwise, must be non-empty;
		Set<String> set1 = new HashSet<>();
		set1.add(mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable);
		set1.add(toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable);
		GenricChildrenNodeBuilderConstraint<RecordDataImporter> c1 = new GenricChildrenNodeBuilderConstraint<>(
				this, "mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable can never be empty set; can be null only if toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable is true; otherwise, must be non-empty!",
				set1, 
				e->{
					@SuppressWarnings("unchecked")
					Set<SimpleName> mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable = 
							(Set<SimpleName>) e.getChildrenNodeBuilderNameMap().get(RecordDataImporterBuilder.mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable).getCurrentValue();
					boolean toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable = 
							(boolean) e.getChildrenNodeBuilderNameMap().get(RecordDataImporterBuilder.toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable).getCurrentValue();
					
					if(mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable!=null&&mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable.isEmpty()) {
						return false;
					}
					
					if(toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable) {
						return mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable==null;
					}else {
						return mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable!=null;
					}
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c1);
		
		//if toIncludeAllDiscoverdTagAttriubteInResultedDataTable is true, discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable must be null; otherwise, must be non-null but can be empty; 
		Set<String> set2 = new HashSet<>();
		set2.add(toIncludeAllDiscoverdTagAttriubteInResultedDataTable);
		set2.add(discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable);
		GenricChildrenNodeBuilderConstraint<RecordDataImporter> c2 = new GenricChildrenNodeBuilderConstraint<>(
				this, "if toIncludeAllDiscoverdTagAttriubteInResultedDataTable is true, discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable must be null; otherwise, must be non-null but can be empty!",
				set2, 
				e->{
					@SuppressWarnings("unchecked")
					Set<SimpleName> discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable = 
							(Set<SimpleName>) e.getChildrenNodeBuilderNameMap().get(RecordDataImporterBuilder.discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable).getCurrentValue();
					boolean toIncludeAllDiscoverdTagAttriubteInResultedDataTable = 
							(boolean) e.getChildrenNodeBuilderNameMap().get(RecordDataImporterBuilder.toIncludeAllDiscoverdTagAttriubteInResultedDataTable).getCurrentValue();
					
					
					if(toIncludeAllDiscoverdTagAttriubteInResultedDataTable) {
						return discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable==null;
					}else {
						return discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable!=null;
					}
					
				});
		this.addGenricChildrenNodeBuilderConstraint(c2);
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
			this.getChildrenNodeBuilderNameMap().get(alternativePrimaryKeyAttributeNameSet).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(toIncludeAllDiscoverdTagAttriubteInResultedDataTable).setValue(null, isEmpty);
			this.getChildrenNodeBuilderNameMap().get(discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable).setValue(null, isEmpty);
			
		}else {
			if(value==null) {
				this.setToNull();
			}else {
				RecordDataImporter recordDataImporter = (RecordDataImporter)value;
				this.getChildrenNodeBuilderNameMap().get(alternativePrimaryKeyAttributeNameSet).setValue(recordDataImporter.getAlternativePrimaryKeyAttributeNameSet(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable).setValue(recordDataImporter.isToIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable).setValue(recordDataImporter.getMandatoryPrimitiveRecordAttributeNameSetIncludedInResultedDataTable(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(toIncludeAllDiscoverdTagAttriubteInResultedDataTable).setValue(recordDataImporter.isToIncludeAllDiscoverdTagAttriubteInResultedDataTable(), isEmpty);
				this.getChildrenNodeBuilderNameMap().get(discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable).setValue(recordDataImporter.getDiscoveredTagAttributeNameSetIncludedInResultedDataTable(), isEmpty);
			}
		}
		
		return changed;
	}
	
	
	@Override
	protected RecordDataImporter build() {
		VfNotes notesValue = (VfNotes) this.getChildrenNodeBuilderNameMap().get(notes).getCurrentValue();
		Path dataSourcePathValue = (Path) this.getChildrenNodeBuilderNameMap().get(dataSourcePath).getCurrentValue();
		FileFormatID fileFormatIDValue = (FileFormatID) this.getChildrenNodeBuilderNameMap().get(fileFormatID).getCurrentValue();
		MetadataName mainImportedMetadataNameValue = (MetadataName) this.getChildrenNodeBuilderNameMap().get(mainImportedMetadataName).getCurrentValue();
		
		PrimaryKeyAttributeNameSet alternativePrimaryKeyAttributeNameSetValue = (PrimaryKeyAttributeNameSet) this.getChildrenNodeBuilderNameMap().get(alternativePrimaryKeyAttributeNameSet).getCurrentValue();
		boolean toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTableValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTable).getCurrentValue();
		@SuppressWarnings("unchecked")
		Set<SimpleName> mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTableValue = (Set<SimpleName>) this.getChildrenNodeBuilderNameMap().get(mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTable).getCurrentValue();
		boolean toIncludeAllDiscoverdTagAttriubteInResultedDataTableValue = (boolean) this.getChildrenNodeBuilderNameMap().get(toIncludeAllDiscoverdTagAttriubteInResultedDataTable).getCurrentValue();
		@SuppressWarnings("unchecked")
		Set<SimpleName> discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTableValue = (Set<SimpleName>) this.getChildrenNodeBuilderNameMap().get(discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTable).getCurrentValue();
		
		return new RecordDataImporter(
				notesValue,
				dataSourcePathValue,
				fileFormatIDValue,
				mainImportedMetadataNameValue,
				
				alternativePrimaryKeyAttributeNameSetValue,
				toIncludeAllMandatoryPrimitiveRecordAttributesInResultedDataTableValue,
				mandatorySimpleRecordAttributeNameSetIncludedInResultedDataTableValue,
				toIncludeAllDiscoverdTagAttriubteInResultedDataTableValue,
				discoveredTagSimpleRecordAttributeNameSetIncludedInResultedDataTableValue
				);
	}
	
}
