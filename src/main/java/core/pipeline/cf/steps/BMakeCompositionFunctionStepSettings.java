package core.pipeline.cf.steps;

import core.pipeline.ProcessStepSettings;
import function.composition.CompositionFunction;

public class BMakeCompositionFunctionStepSettings implements ProcessStepSettings{
	
	private CompositionFunction compositionFunction;
	
	
	////
	BMakeCompositionFunctionStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	BMakeCompositionFunctionStepSettings(CompositionFunction compositionFunction){
		this.compositionFunction = compositionFunction;
	}
	
	
	
	public CompositionFunction getCompositionFunction() {
		return compositionFunction;
	}
	
}
