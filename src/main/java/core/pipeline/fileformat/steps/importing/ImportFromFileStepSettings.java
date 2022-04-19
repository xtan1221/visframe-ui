package core.pipeline.fileformat.steps.importing;

import core.pipeline.ProcessStepSettings;

/**
 * 
 * @author tanxu
 *
 */
public class ImportFromFileStepSettings implements ProcessStepSettings {
	private String fileFormatFileDirString;
	
	
	ImportFromFileStepSettings(){
		this.fileFormatFileDirString = "";
	}
	
	ImportFromFileStepSettings(String fileFormatFileDirString){
		this.setFileFormatFileDirString(fileFormatFileDirString);
	}
	
	
	//////////////////////////
	String getFileFormatFileDirString() {
		return fileFormatFileDirString;
	}
	
	void setFileFormatFileDirString(String fileFormatFileDirString) {
		this.fileFormatFileDirString = fileFormatFileDirString;
	}
	
}
