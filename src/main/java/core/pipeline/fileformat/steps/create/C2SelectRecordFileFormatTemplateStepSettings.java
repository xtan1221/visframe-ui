package core.pipeline.fileformat.steps.create;

import core.pipeline.ProcessStepSettings;
import fileformat.record.RecordDataFileFormat;

public class C2SelectRecordFileFormatTemplateStepSettings implements ProcessStepSettings {
	private RecordDataFileFormat selectedRecordDataFileFormat;
	
	C2SelectRecordFileFormatTemplateStepSettings(RecordDataFileFormat selectedRecordDataFileFormat){
		this.setSelectedRecordDataFileFormat(selectedRecordDataFileFormat);
	}
	
	public RecordDataFileFormat getSelectedRecordDataFileFormat() {
		return selectedRecordDataFileFormat;
	}
	
	public void setSelectedRecordDataFileFormat(RecordDataFileFormat selectedRecordDataFileFormat) {
		this.selectedRecordDataFileFormat = selectedRecordDataFileFormat;
	}

	@Override
	public String toString() {
		return "C2SelectRecordFileFormatTemplateStepSettings [selectedRecordDataFileFormat="
				+ selectedRecordDataFileFormat + "]";
	}
	
	
	
}
