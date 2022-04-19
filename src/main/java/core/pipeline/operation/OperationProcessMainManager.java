package core.pipeline.operation;

import core.pipeline.AbstractProcessMainManager;
import core.pipeline.operation.steps.ASelectTypeAndTemplateStepManager;
import operation.Operation;

public class OperationProcessMainManager extends AbstractProcessMainManager<Operation>{
	private final static String TITLE = "Make new Operation";
	
	public OperationProcessMainManager() {
		super(Operation.class, ASelectTypeAndTemplateStepManager.singleton(), TITLE);
		// TODO Auto-generated constructor stub
	}

}
