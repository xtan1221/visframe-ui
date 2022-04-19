package core.pipeline.fileformat.steps.create;

import core.pipeline.ProcessStepSettings;
import fileformat.record.RecordDataFileFormat;

public class DBuildRecordFileFormatStepSettings implements ProcessStepSettings {
	private final RecordDataFileFormat recordDataFileFormat;
	
	
	DBuildRecordFileFormatStepSettings(){
		this.recordDataFileFormat = null;
	}
	
	DBuildRecordFileFormatStepSettings(RecordDataFileFormat recordDataFileFormat){
		this.recordDataFileFormat = recordDataFileFormat;
	}

	public RecordDataFileFormat getRecordDataFileFormat() {
		return recordDataFileFormat;
	}
	
}
