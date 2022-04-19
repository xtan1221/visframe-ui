package core.pipeline.fileformat.steps.create;

import core.pipeline.ProcessStepSettings;

public class BSelectRecordBuildingModeStepSettings implements ProcessStepSettings {
	private boolean basedOnTemplate;
	
	BSelectRecordBuildingModeStepSettings(boolean basedOnTemplate){
		this.setBasedOnTemplate(basedOnTemplate);
	}
	
	public boolean isBasedOnTemplate() {
		return basedOnTemplate;
	}

	public void setBasedOnTemplate(boolean basedOnTemplate) {
		this.basedOnTemplate = basedOnTemplate;
	}
}
