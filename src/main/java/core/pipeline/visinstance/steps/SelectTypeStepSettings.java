package core.pipeline.visinstance.steps;

import core.pipeline.ProcessStepSettings;

public class SelectTypeStepSettings implements ProcessStepSettings {
	private VisInstanceType type;
	
	/**
	 * default constructor
	 */
	SelectTypeStepSettings(){
		//
	}
	
	/**
	 * 
	 * @param type
	 */
	SelectTypeStepSettings(VisInstanceType type){
		this.type = type;
	}
	
	
	
	/**
	 * @return the type
	 */
	public VisInstanceType getType() {
		return type;
	}



	/**
	 * 
	 * @author tanxu
	 * 
	 */
	static enum VisInstanceType{
		NATIVE,
		VISSCHEME_BASED;
	}
}
