package utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.scene.control.ButtonType;

/**
 * 
 * @author tanxu
 *
 */
public final class FileIOUtils {
	
	/**
	 * try to build and return a Path represent a file with the given folder path and file name in the local file system;
	 * 
	 * note that javafx based Alert will be pop-up if existing folder/file with the same path is found;
	 * 		
	 * return null if
	 * 1. there is an existing folder with the same path as the built full path of the target file;
	 * 2. there is an existing file with the same path as the built full path of the target file and user choose to cancel the operation;
	 * 
	 * @param parentFolderPathString
	 * @param fileName
	 * @return
	 * 
	 * 
	 */
	public static Path buildFilePath(String parentFolderPathString, String fileName, String fileFormatType){
		
		Path projectParentPath = Paths.get(parentFolderPathString);
		
		Path fullFilePath = Paths.get(projectParentPath.toString(), fileName.concat(".").concat(fileFormatType));
		
		if(Files.exists(fullFilePath)) {//the target file path is already existing
			if(Files.isDirectory(fullFilePath)) {//the target file path is an existing directory, cannot proceed
				AlertUtils.popAlert("Error", "An existing folder is found to locate at the given path: \n\t"+fullFilePath.toString()+"\nCannot proceed!");
				return null;
			}else {//the target file path is an existing file, need to ask for confirmation
				Optional<ButtonType> result = 
						AlertUtils.popConfirmationDialog(
								"Confirmation is needed!", 
								"Overwrite file?", 
								"An existing file with the same name at the give location is found! Overwrite it?");
				if (result.get() == ButtonType.CANCEL){
					return null;
				}
			}
		}
		
		return fullFilePath;
	}
	
	
	/**
	 * check if the given file name contains any invalid characters;
	 * 
	 * throw InvalidPathException if any found;
	 * @param fileName
	 */
	public static void validateFileName(String fileName) {
		Paths.get(fileName);
	}
	
}
