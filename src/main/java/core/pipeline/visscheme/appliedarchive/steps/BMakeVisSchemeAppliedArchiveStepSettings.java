package core.pipeline.visscheme.appliedarchive.steps;

import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.ProcessStepSettings;

public class BMakeVisSchemeAppliedArchiveStepSettings implements ProcessStepSettings{
	
	private VisSchemeAppliedArchive visSchemeAppliedArchive;
	
	
	////
	BMakeVisSchemeAppliedArchiveStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	BMakeVisSchemeAppliedArchiveStepSettings(VisSchemeAppliedArchive visSchemeAppliedArchive){
		this.visSchemeAppliedArchive = visSchemeAppliedArchive;
	}
	
	
	
	public VisSchemeAppliedArchive getVisSchemeAppliedArchive() {
		return visSchemeAppliedArchive;
	}
	
}
