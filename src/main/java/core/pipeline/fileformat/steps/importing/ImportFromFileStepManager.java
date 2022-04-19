package core.pipeline.fileformat.steps.importing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import context.project.process.simple.FileFormatInserter;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.fileformat.steps.SelectImportModeStepManager;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import progressmanager.SingleSimpleProcessProgressManager;
import session.serialization.SerializationUtils;
import utils.AlertUtils;

/**
 * step to import an existing serialized FileFormat file from local file system;
 * 
 * previous step is {@link SelectImportModeStepManager}
 * 
 * final step of {@link FileFormatProcessMainManager};
 * 
 * @author tanxu
 *
 */
public class ImportFromFileStepManager extends AbstractProcessStepManager<FileFormat, ImportFromFileStepSettings,ImportFromFileStepController> {
	private final static String DESCRIPTION = "Import from an existing FileFormat file.";
	private final static ImportFromFileStepSettings DEFAULT_SETTINGS = new ImportFromFileStepSettings();	
	
	private static ImportFromFileStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this step;
	 * if not built yet;
	 * 
	 * 1. initialize the SINGLETON instance;
	 * 2. add next step (if any) by invoke their singleton() methods which will trigger the initialization chain starting from the root step to every final step;
	 * 3. add previous step (if any) with the SINGLETON field to avoid circular reference
	 * @return
	 */
	public static ImportFromFileStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ImportFromFileStepManager();
			
			SINGLETON.addPreviousStep(SelectImportModeStepManager.SINGLETON);
			
			SINGLETON.finishInitialization();
		}
		
		return SINGLETON;
	}
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ImportFromFileStepManager() {
		super(FileFormat.class, ImportFromFileStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	//
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("no next step for "+this.getClass().getSimpleName());
	}
	
	/**
	 * first check if the given file format file exists or not;
	 * then try to de-serialize it to a FileFormat object;
	 * validate the FileFormat object and cast it to a specific FileFormat subtype;
	 * 
	 * try to insert it into the management table;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public boolean finish() throws IOException, SQLException {
//		System.out.println("importing existing FileFormat file");
		
		//check if FileFormatID already exist
		
		String fileFormatFileDirString = this.getController().getStepSettings().getFileFormatFileDirString();
		
		if(fileFormatFileDirString.isEmpty()) {
			AlertUtils.popAlert("Error", "No file format file is selected!");
			return false;
		}else {
			Path path = Paths.get(fileFormatFileDirString);
			if(Files.isRegularFile(path)) {
				Object o = SerializationUtils.deserializeFromFile(path);
				
				if(o==null) {
					AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
					return false;
				}else {
					//check if FileFormatID already exist
					if(this.doesFileFormatIDExist(this.getController().getImportedFileFormatID())) {
						AlertUtils.popAlert("Error", "Given imported file format name already exist in the project, change to another one!");
						return false;
					}
					
					//
					FileFormat original = (FileFormat)o;
					
					FileFormat imported = original.rename(this.getController().getImportedFileFormatID().getName());
					
					FileFormatInserter inserter = 
							new FileFormatInserter(
									this.getProcessMainManager().getHostVisProjectDBContext(), 
									imported);
					
					SingleSimpleProcessProgressManager progressManager = 
							new SingleSimpleProcessProgressManager(
									inserter, 
									this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
									true,
									null,
									null);
					
					//close the process window
					this.getProcessMainManager().closeWindow();
					
					progressManager.start(false);
					
					return true;
				}
			}else {
				AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
				return false;
			}
		}
		//if yes, 
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public boolean doesFileFormatIDExist(FileFormatID id) throws SQLException {
		return this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getFileFormatManager().lookup(id)!=null;
	}
	
	
}
