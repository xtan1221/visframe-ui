package core.pipeline.dataimporter.steps;

import core.pipeline.ProcessStepSettings;
import metadata.DataType;

public class SelectDataTypeStepSettings implements ProcessStepSettings {
	private DataType selectedDataType;
	
	
	SelectDataTypeStepSettings(DataType selectedDataType){
		this.setSelectedDataType(selectedDataType);
	}

	
	public DataType getSelectedDataType() {
		return selectedDataType;
	}
	
	
	public void setSelectedDataType(DataType selectedDataType) {
		this.selectedDataType = selectedDataType;
	}
	
	
}
