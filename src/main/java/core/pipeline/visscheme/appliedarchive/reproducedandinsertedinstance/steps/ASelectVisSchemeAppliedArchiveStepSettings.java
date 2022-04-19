package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.ProcessStepSettings;

public class ASelectVisSchemeAppliedArchiveStepSettings implements ProcessStepSettings{
	
	private VisSchemeAppliedArchive visSchemeAppliedArchive;
	
	
	////
	ASelectVisSchemeAppliedArchiveStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	ASelectVisSchemeAppliedArchiveStepSettings(VisSchemeAppliedArchive visSchemeAppliedArchive){
		this.visSchemeAppliedArchive = visSchemeAppliedArchive;
	}
	
	
	
	public VisSchemeAppliedArchive getVisSchemeAppliedArchive() {
		return visSchemeAppliedArchive;
	}
	
}
