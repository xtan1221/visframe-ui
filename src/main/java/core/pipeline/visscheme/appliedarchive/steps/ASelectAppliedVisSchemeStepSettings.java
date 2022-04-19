package core.pipeline.visscheme.appliedarchive.steps;

import context.scheme.VisScheme;
import core.pipeline.ProcessStepSettings;

public class ASelectAppliedVisSchemeStepSettings implements ProcessStepSettings{
	
	private VisScheme appliedVisScheme;
	
	
	////
	ASelectAppliedVisSchemeStepSettings(){
		//
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	ASelectAppliedVisSchemeStepSettings(VisScheme appliedVisScheme){
		this.appliedVisScheme = appliedVisScheme;
	}
	
	
	
	public VisScheme getAppliedVisScheme() {
		return appliedVisScheme;
	}
	
}
