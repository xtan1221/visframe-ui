package core.pipeline.visinstance.run.steps;

import core.pipeline.ProcessStepSettings;
import visinstance.VisInstance;

public class ASelectVisInstanceStepSettings implements ProcessStepSettings {
	private VisInstance selectedVisInstance;
	
	ASelectVisInstanceStepSettings(VisInstance selectedVisInstance){
		this.selectedVisInstance = selectedVisInstance;
	}
	
	public VisInstance getSelectedVisInstance() {
		return selectedVisInstance;
	}
	
	public void setSelectedVisInstance(VisInstance selectedVisInstance) {
		this.selectedVisInstance = selectedVisInstance;
	}

}
