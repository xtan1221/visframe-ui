package core.pipeline.visinstance;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visinstance.steps.SelectTypeStepManager;
import visinstance.VisInstance;

public class VisInstanceProcessMainManager extends AbstractProcessMainManager<VisInstance>{
	private final static String TITLE = "Make new VisInstance";
	
	public VisInstanceProcessMainManager() {
		super(VisInstance.class, SelectTypeStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
