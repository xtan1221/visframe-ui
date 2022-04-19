package core.pipeline.cf.steps;

import core.pipeline.ProcessStepSettings;
import function.group.CompositionFunctionGroup;

public class ASelectOwnerCFGStepSettings implements ProcessStepSettings{
	
	private CompositionFunctionGroup ownerCFG;
	
	
	////
	ASelectOwnerCFGStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	ASelectOwnerCFGStepSettings(CompositionFunctionGroup ownerCFG){
		this.ownerCFG = ownerCFG;
	}
	
	
	
	public CompositionFunctionGroup getOwnerCompositionFunctionGroup() {
		return ownerCFG;
	}
	
}
