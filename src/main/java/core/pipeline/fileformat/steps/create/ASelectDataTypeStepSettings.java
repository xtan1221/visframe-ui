package core.pipeline.fileformat.steps.create;

import core.pipeline.ProcessStepSettings;
import metadata.DataType;

public class ASelectDataTypeStepSettings implements ProcessStepSettings {
	private DataType selectedDataType;
	
	
	ASelectDataTypeStepSettings(DataType selectedDataType){
		this.setSelectedDataType(selectedDataType);
	}

	
	public DataType getSelectedDataType() {
		return selectedDataType;
	}
	
	
	public void setSelectedDataType(DataType selectedDataType) {
		this.selectedDataType = selectedDataType;
	}
	
	
}
