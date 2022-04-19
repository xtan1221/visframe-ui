package core.pipeline.visscheme.steps;

import java.io.IOException;
import java.sql.SQLException;

import context.scheme.VisScheme;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visscheme.steps.create.MakeVisSchemeStepManager;
import core.pipeline.visscheme.steps.importing.ImportFromFileStepManager;


/**
 * step to select whether import a new VisScheme from an existing serialized file or create from scratch;
 * 
 * first step of {@link VisInstanceRunLayoutConfigurationProcessMainManager};
 * 
 * next steps are
 * 1. if imported from an existing serialized file of a pre-defined file format, {@link ImportFromFileStepManager}
 * 2. if create a new one, {@link MakeVisSchemeStepManager}
 * @author tanxu
 *
 */
public class SelectImportModeStepManager extends AbstractProcessStepManager<VisScheme, SelectImportModeStepSettings, SelectImportModeStepController> {
	private final static String DESCRIPTION = "SELECT how to import the new VisScheme;";
	private final static SelectImportModeStepSettings DEFAULT_SETTINGS = new SelectImportModeStepSettings(SelectImportModeStepSettings.ImportModeType.FROM_EXISTING_FILE);
	
	public static SelectImportModeStepManager SINGLETON;
	
	
	/**
	 * static factory method to get a singleton object of this step;
	 * if not built yet;
	 * 
	 * 1. initialize the SINGLETON instance;
	 * 2. add next step (if any) by invoke their singleton() methods which will trigger the initialization chain starting from the root step to every final step;
	 * 3. add previous step (if any) with the SINGLETON field to avoid circular reference
	 * @return
	 * @throws SQLException 
	 */
	public static SelectImportModeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new SelectImportModeStepManager();
			
			SINGLETON.addNextStep(ImportFromFileStepManager.singleton());
			
			SINGLETON.addNextStep(MakeVisSchemeStepManager.singleton());
			
			SINGLETON.finishInitialization();
		}
		
		
		return SINGLETON;
	}
	
	////////////////////////////////////////
	
	
	/**
	 * constructor
	 * @param hostProcessManager
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	private SelectImportModeStepManager() {
		super(VisScheme.class, SelectImportModeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		Integer index = null;
		if(this.getController().getStepSettings().getType().equals(SelectImportModeStepSettings.ImportModeType.FROM_EXISTING_FILE)){
			index = this.getPossibleNextStepList().indexOf(ImportFromFileStepManager.singleton());
		}else if(this.getController().getStepSettings().getType().equals(SelectImportModeStepSettings.ImportModeType.FROM_SCRATCH)) {
			index = this.getPossibleNextStepList().indexOf(MakeVisSchemeStepManager.singleton());
		}else {
			
		}
		
		return index;
	}
	
	
	@Override
	public boolean finish() {
		//
		throw new UnsupportedOperationException();
	}

	
}
