package core.pipeline.visscheme.steps;

import core.pipeline.ProcessStepSettings;

public class SelectImportModeStepSettings implements ProcessStepSettings {
	private ImportModeType type;
	
	/**
	 * default constructor
	 */
	SelectImportModeStepSettings(){
		//
	}
	
	/**
	 * 
	 * @param type
	 */
	SelectImportModeStepSettings(ImportModeType type){
		this.type = type;
	}
	
	ImportModeType getType() {
		return type;
	}
	
	void setType(ImportModeType type) {
		this.type = type;
	}
	
	/**
	 * 
	 * @author tanxu
	 * 
	 */
	static enum ImportModeType{
		FROM_EXISTING_FILE,
		FROM_SCRATCH;
	}
}
