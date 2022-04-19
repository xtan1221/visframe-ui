package core.pipeline.visscheme.steps.create;

import context.scheme.VisScheme;
import core.pipeline.ProcessStepSettings;

public class MakeVisSchemeStepSettings implements ProcessStepSettings{
	
	private VisScheme visScheme;
	
	
	////
	MakeVisSchemeStepSettings(){
		
	}
	
	MakeVisSchemeStepSettings(VisScheme visScheme){
		this.visScheme = visScheme;
	}
	
	
	
	public VisScheme getVisScheme() {
		return visScheme;
	}
	
}
