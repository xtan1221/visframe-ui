package core.pipeline.visinstance.run;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visinstance.run.steps.ASelectVisInstanceStepManager;
import visinstance.run.VisInstanceRun;

public class VisInstanceRunProcessMainManager extends AbstractProcessMainManager<VisInstanceRun>{
	private final static String TITLE = "Make new VisInstanceRun";
	
	public VisInstanceRunProcessMainManager() {
		super(VisInstanceRun.class, ASelectVisInstanceStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
