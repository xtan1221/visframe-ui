package core.pipeline.dataimporter.steps.vftree;

import core.pipeline.ProcessStepSettings;
import fileformat.vftree.VfTreeDataFileFormatType;

public class ASelectVfTreeFileFormatTypeStepSettings implements ProcessStepSettings{
	private final VfTreeDataFileFormatType selectedType;
	
	ASelectVfTreeFileFormatTypeStepSettings(VfTreeDataFileFormatType selectedType){
		this.selectedType = selectedType;
	}

	VfTreeDataFileFormatType getSelectedType() {
		return selectedType;
	}
}
