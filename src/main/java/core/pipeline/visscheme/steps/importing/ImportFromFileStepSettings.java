package core.pipeline.visscheme.steps.importing;

import core.pipeline.ProcessStepSettings;

/**
 * 
 * @author tanxu
 *
 */
public class ImportFromFileStepSettings implements ProcessStepSettings {
	private String visSchemeFileDirString;
	
	
	ImportFromFileStepSettings(){
		this.setVisSchemeFileDirString("");
	}
	
	ImportFromFileStepSettings(String visSchemeFileDirString){
		this.setVisSchemeFileDirString(visSchemeFileDirString);
	}

	public String getVisSchemeFileDirString() {
		return visSchemeFileDirString;
	}

	public void setVisSchemeFileDirString(String visSchemeFileDirString) {
		this.visSchemeFileDirString = visSchemeFileDirString;
	}
	
	
}
