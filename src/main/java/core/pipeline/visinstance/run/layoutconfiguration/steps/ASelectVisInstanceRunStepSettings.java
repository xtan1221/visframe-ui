package core.pipeline.visinstance.run.layoutconfiguration.steps;

import core.pipeline.ProcessStepSettings;
import visinstance.run.VisInstanceRun;

public class ASelectVisInstanceRunStepSettings implements ProcessStepSettings {
	private VisInstanceRun selectedVisInstanceRun;
	
	ASelectVisInstanceRunStepSettings(VisInstanceRun selectedVisInstanceRun){
		this.selectedVisInstanceRun = selectedVisInstanceRun;
	}
	
	public VisInstanceRun getSelectedVisInstanceRun() {
		return selectedVisInstanceRun;
	}
	
	public void setSelectedVisInstanceRun(VisInstanceRun selectedVisInstanceRun) {
		this.selectedVisInstanceRun = selectedVisInstanceRun;
	}

}
