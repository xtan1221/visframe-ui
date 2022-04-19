package core.pipeline.dataimporter.steps.graph;

import core.pipeline.ProcessStepSettings;
import fileformat.graph.GraphDataFileFormatType;

public class ASelectGraphFileFormatTypeStepSettings implements ProcessStepSettings{
	private final GraphDataFileFormatType selectedType;
	
	ASelectGraphFileFormatTypeStepSettings(GraphDataFileFormatType selectedType){
		this.selectedType = selectedType;
	}

	GraphDataFileFormatType getSelectedType() {
		return selectedType;
	}
	
}
