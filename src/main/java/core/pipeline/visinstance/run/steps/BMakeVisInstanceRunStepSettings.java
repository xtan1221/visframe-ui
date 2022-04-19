package core.pipeline.visinstance.run.steps;

import core.pipeline.ProcessStepSettings;
import visinstance.run.VisInstanceRun;

public class BMakeVisInstanceRunStepSettings implements ProcessStepSettings{
	
	private VisInstanceRun visInstanceRun;
	
	
	////
	BMakeVisInstanceRunStepSettings(){
		
	}
	
	BMakeVisInstanceRunStepSettings(VisInstanceRun visInstanceRun){
		this.visInstanceRun = visInstanceRun;
	}

	/**
	 * @return the visInstanceRun
	 */
	public VisInstanceRun getVisInstanceRun() {
		return visInstanceRun;
	}

	
}
