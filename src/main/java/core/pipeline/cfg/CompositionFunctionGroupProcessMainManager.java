package core.pipeline.cfg;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.cfg.steps.ASelectTypeAndTemplateStepManager;
import function.group.CompositionFunctionGroup;

public class CompositionFunctionGroupProcessMainManager extends AbstractProcessMainManager<CompositionFunctionGroup>{
	private final static String TITLE = "Make new CompositionFunctionGroup";
	
	public CompositionFunctionGroupProcessMainManager() {
		super(CompositionFunctionGroup.class, ASelectTypeAndTemplateStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
