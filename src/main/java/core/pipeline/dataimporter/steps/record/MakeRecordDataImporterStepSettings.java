package core.pipeline.dataimporter.steps.record;

import core.pipeline.ProcessStepSettings;
import importer.record.RecordDataImporter;

public class MakeRecordDataImporterStepSettings implements ProcessStepSettings{
	private final RecordDataImporter recordDataImporter;
	
	MakeRecordDataImporterStepSettings(){
		this.recordDataImporter = null;
	}
	
	MakeRecordDataImporterStepSettings(RecordDataImporter recordDataImporter){
		this.recordDataImporter = recordDataImporter;
	}
	
	/**
	 * @return the recordDataImporter
	 */
	public RecordDataImporter getRecordDataImporter() {
		return recordDataImporter;
	}
}
