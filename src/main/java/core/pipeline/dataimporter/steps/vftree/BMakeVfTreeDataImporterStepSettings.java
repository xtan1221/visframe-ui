package core.pipeline.dataimporter.steps.vftree;

import core.pipeline.ProcessStepSettings;
import importer.vftree.VfTreeDataImporterBase;

public class BMakeVfTreeDataImporterStepSettings implements ProcessStepSettings{
	private final VfTreeDataImporterBase vfTreeDataImporterBase;
	
	
	BMakeVfTreeDataImporterStepSettings(VfTreeDataImporterBase vfTreeDataImporterBase){
		this.vfTreeDataImporterBase = vfTreeDataImporterBase;
	}
	

	public VfTreeDataImporterBase getVfTreeDataImporterBase() {
		return vfTreeDataImporterBase;
	}
}
