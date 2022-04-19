package core.pipeline.cf;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.cf.steps.ASelectOwnerCFGStepManager;
import function.composition.CompositionFunction;

public class CompositionFunctionProcessMainManager extends AbstractProcessMainManager<CompositionFunction>{
	private final static String TITLE = "Make new CompositionFunction";
	
	public CompositionFunctionProcessMainManager() {
		super(CompositionFunction.class, ASelectOwnerCFGStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}
	
}
