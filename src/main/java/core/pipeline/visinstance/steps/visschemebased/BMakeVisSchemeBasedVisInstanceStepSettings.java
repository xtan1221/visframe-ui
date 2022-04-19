package core.pipeline.visinstance.steps.visschemebased;

import core.pipeline.ProcessStepSettings;
import visinstance.VisSchemeBasedVisInstance;

public class BMakeVisSchemeBasedVisInstanceStepSettings implements ProcessStepSettings{
	
	private VisSchemeBasedVisInstance visSchemeBasedVisInstance;
	
	
	////
	BMakeVisSchemeBasedVisInstanceStepSettings(){
		
	}
	
	BMakeVisSchemeBasedVisInstanceStepSettings(VisSchemeBasedVisInstance visSchemeBasedVisInstance){
		this.visSchemeBasedVisInstance = visSchemeBasedVisInstance;
	}

	public VisSchemeBasedVisInstance getVisSchemeBasedVisInstance() {
		return visSchemeBasedVisInstance;
	}
	
	
}
