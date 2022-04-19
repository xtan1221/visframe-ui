package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import core.pipeline.ProcessStepSettings;

public class BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings implements ProcessStepSettings{
	
	private VisSchemeAppliedArchiveReproducedAndInsertedInstance visSchemeAppliedArchiveReproducedAndInsertedInstance;
	
	
	////
	BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings(VisSchemeAppliedArchiveReproducedAndInsertedInstance visSchemeAppliedArchiveReproducedAndInsertedInstance){
		this.visSchemeAppliedArchiveReproducedAndInsertedInstance = visSchemeAppliedArchiveReproducedAndInsertedInstance;
	}
	
	
	
	public VisSchemeAppliedArchiveReproducedAndInsertedInstance getVisSchemeAppliedArchiveReproducedAndInsertedInstance() {
		return visSchemeAppliedArchiveReproducedAndInsertedInstance;
	}
	
}
