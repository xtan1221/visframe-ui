package core.pipeline.visinstance.run.layoutconfiguration.steps;

import core.pipeline.ProcessStepSettings;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

public class BMakeVisInstanceRunLayoutConfigurationStepSettings implements ProcessStepSettings{
	
	private VisInstanceRunLayoutConfiguration visInstanceRunLayout;
	
	
	////
	BMakeVisInstanceRunLayoutConfigurationStepSettings(){
		
	}
	
	BMakeVisInstanceRunLayoutConfigurationStepSettings(VisInstanceRunLayoutConfiguration visInstanceRunLayout){
		this.visInstanceRunLayout = visInstanceRunLayout;
	}

	/**
	 * @return the visInstanceRun
	 */
	public VisInstanceRunLayoutConfiguration getVisInstanceRunLayout() {
		return visInstanceRunLayout;
	}

	
}
