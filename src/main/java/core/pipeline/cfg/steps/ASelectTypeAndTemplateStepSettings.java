package core.pipeline.cfg.steps;

import core.pipeline.ProcessStepSettings;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;

public class ASelectTypeAndTemplateStepSettings implements ProcessStepSettings{
	
	private Class<? extends CompositionFunctionGroup> selectedCompositionFunctionGroupType;
	private CompositionFunctionGroupID selectedTemplateCompositionFunctionGroupID;
	
	
	////
	ASelectTypeAndTemplateStepSettings(){
		
	}
	/**
	 * 
	 * @param selectedOperationType
	 * @param selectedTemplateOperationID
	 */
	ASelectTypeAndTemplateStepSettings(Class<? extends CompositionFunctionGroup> selectedOperationType, CompositionFunctionGroupID selectedTemplateOperationID){
		this.selectedCompositionFunctionGroupType = selectedOperationType;
		this.selectedTemplateCompositionFunctionGroupID = selectedTemplateOperationID;
	}
	
	/**
	 * @return the selectedCompositionFunctionGroupType
	 */
	public Class<? extends CompositionFunctionGroup> getSelectedCompositionFunctionGroupType() {
		return selectedCompositionFunctionGroupType;
	}
	/**
	 * @return the selectedTemplateCompositionFunctionGroupID
	 */
	public CompositionFunctionGroupID getSelectedTemplateCompositionFunctionGroupID() {
		return selectedTemplateCompositionFunctionGroupID;
	}
	
	
}
