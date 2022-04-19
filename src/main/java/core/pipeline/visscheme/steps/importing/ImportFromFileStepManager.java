package core.pipeline.visscheme.steps.importing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import context.project.process.simple.VisSchemeInserter;
import context.scheme.VisScheme;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visscheme.steps.SelectImportModeStepManager;
import progressmanager.SingleSimpleProcessProgressManager;
import session.serialization.SerializationUtils;
import utils.AlertUtils;

/**
 * step to import an existing serialized VisScheme file from local file system;
 * 
 * previous step is {@link SelectImportModeStepManager}
 * 
 * final step of {@link VisInstanceRunLayoutConfigurationProcessMainManager};
 * 
 * @author tanxu
 *
 */
public class ImportFromFileStepManager extends AbstractProcessStepManager<VisScheme, ImportFromFileStepSettings,ImportFromFileStepController> {
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
		super(VisScheme.class, ImportFromFileStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	//
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
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
		
		String fileFormatFileDirString = this.getController().getStepSettings().getVisSchemeFileDirString();
		
		if(fileFormatFileDirString.isEmpty()) {
			AlertUtils.popAlert("Error", "No VisScheme file is selected!");
			return false;
		}else {
			Path path = Paths.get(fileFormatFileDirString);
			if(Files.isRegularFile(path)) {
				Object o = SerializationUtils.deserializeFromFile(path);
				
				if(o==null) {
					AlertUtils.popAlert("Error", "Selected VisScheme file is not recognized!");
					return false;
				}else {
					VisScheme original = (VisScheme)o;
					
					original.setImported(true);
					original.setUID(this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().findNextAvaiableUID());
					
					VisSchemeInserter inserter = new VisSchemeInserter(this.getProcessMainManager().getHostVisProjectDBContext(), original);
					
					//set the init window of the progress bar to the stage of the visframe session;
					SingleSimpleProcessProgressManager progressManager = 
							new SingleSimpleProcessProgressManager(
									inserter, 
									this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
									true,
									null,
									null);
					
					//close the process window
					this.getProcessMainManager().closeWindow();
					
					//
					progressManager.start(false);
					
					return true;
				}
			}else {
				AlertUtils.popAlert("Error", "Selected VisScheme file is not recognized!");
				return false;
			}
		}
	}
	
	
}
