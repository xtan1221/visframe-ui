package core.pipeline.operation.steps;

import core.pipeline.ProcessStepSettings;
import operation.Operation;

public class BMakeOperationStepManagerSettings implements ProcessStepSettings{
	private final Operation operation;
	
	BMakeOperationStepManagerSettings(Operation operation){
		this.operation = operation;
	}
	
	
	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

}
