package core.pipeline.visscheme;

import context.scheme.VisScheme;
import core.pipeline.AbstractProcessMainManager;
import core.pipeline.visscheme.steps.SelectImportModeStepManager;

public class VisSchemeProcessMainManager extends AbstractProcessMainManager<VisScheme>{
	private final static String TITLE = "Make new VisScheme";
	
	public VisSchemeProcessMainManager() {
		super(VisScheme.class, SelectImportModeStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
