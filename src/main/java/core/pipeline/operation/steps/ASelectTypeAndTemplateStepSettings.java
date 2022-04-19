package core.pipeline.operation.steps;

import core.pipeline.ProcessStepSettings;
import operation.Operation;
import operation.OperationID;

public class ASelectTypeAndTemplateStepSettings implements ProcessStepSettings{
	
	private Class<? extends Operation> selectedOperationType;
	private OperationID selectedTemplateOperationID;
	
	
	////
	ASelectTypeAndTemplateStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	ASelectTypeAndTemplateStepSettings(Class<? extends Operation> selectedOperationType, OperationID selectedTemplateOperationID){
		this.selectedOperationType = selectedOperationType;
		this.selectedTemplateOperationID = selectedTemplateOperationID;
	}

	public Class<? extends Operation> getSelectedOperationType() {
		return selectedOperationType;
	}

	public OperationID getSelectedTemplateOperationID() {
		return selectedTemplateOperationID;
	}
	
}
