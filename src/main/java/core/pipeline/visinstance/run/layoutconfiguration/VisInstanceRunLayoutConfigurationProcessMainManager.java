package core.pipeline.visinstance.run.layoutconfiguration;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visinstance.run.layoutconfiguration.steps.ASelectVisInstanceRunStepManager;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

public class VisInstanceRunLayoutConfigurationProcessMainManager extends AbstractProcessMainManager<VisInstanceRunLayoutConfiguration>{
	private final static String TITLE = "Make new VisInstanceRunLayoutConfiguration";
	
	public VisInstanceRunLayoutConfigurationProcessMainManager() {
		super(VisInstanceRunLayoutConfiguration.class, ASelectVisInstanceRunStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
