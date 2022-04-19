package core.pipeline.dataimporter;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.dataimporter.steps.SelectDataTypeStepManager;
import importer.DataImporter;

public class DataImporterProcessMainManager extends AbstractProcessMainManager<DataImporter>{
	private final static String TITLE = "Make new Data Importer";
	
	public DataImporterProcessMainManager() {
		super(DataImporter.class, SelectDataTypeStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}
	
}
