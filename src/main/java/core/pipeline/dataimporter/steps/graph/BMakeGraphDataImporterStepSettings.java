package core.pipeline.dataimporter.steps.graph;

import core.pipeline.ProcessStepSettings;
import importer.graph.GraphDataImporterBase;


public class BMakeGraphDataImporterStepSettings implements ProcessStepSettings{
	private final GraphDataImporterBase graphDataImporterBase;
	
	
	BMakeGraphDataImporterStepSettings(GraphDataImporterBase graphDataImporterBase){
		this.graphDataImporterBase = graphDataImporterBase;
	}
	

	public GraphDataImporterBase getGraphDataImporterBase() {
		return graphDataImporterBase;
	}
}
