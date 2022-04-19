package core.pipeline.visinstance.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visinstance.steps.nativevi.MakeNativeVisInstanceStepManager;
import core.pipeline.visinstance.steps.visschemebased.ASelectVSAAReproducedAndInsertedInstanceStepManager;
import visinstance.VisInstance;


/**
 * step to select whether import a new FileFormat from an existing serialized file or create from scratch;
 * 
 * first step of {@link FileFormatProcessMainManager};
 * 
 * next steps are
 * 1. if imported from an existing serialized file of a pre-defined file format, {@link ImportFromFileStepManager}
 * 2. if create a new one, {@link ASelectDataTypeStepManager}
 * @author tanxu
 *
 */
public class SelectTypeStepManager extends AbstractProcessStepManager<VisInstance, SelectTypeStepSettings, SelectTypeStepController> {
	private final static String DESCRIPTION = "SELECT how to create the VisInstance;";
	private final static SelectTypeStepSettings DEFAULT_SETTINGS = new SelectTypeStepSettings(SelectTypeStepSettings.VisInstanceType.NATIVE);	
	public static SelectTypeStepManager SINGLETON;
	
	
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
	public static SelectTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new SelectTypeStepManager();
			
			//native
			SINGLETON.addNextStep(MakeNativeVisInstanceStepManager.singleton());
			
			// visscheme based
			SINGLETON.addNextStep(ASelectVSAAReproducedAndInsertedInstanceStepManager.singleton());
			
			
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
	private SelectTypeStepManager() {
		super(VisInstance.class, SelectTypeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		Integer index = null;
		if(this.getController().getStepSettings().getType().equals(SelectTypeStepSettings.VisInstanceType.NATIVE)){
			index = this.getPossibleNextStepList().indexOf(MakeNativeVisInstanceStepManager.singleton());
		}else if(this.getController().getStepSettings().getType().equals(SelectTypeStepSettings.VisInstanceType.VISSCHEME_BASED)) {
			index = this.getPossibleNextStepList().indexOf(ASelectVSAAReproducedAndInsertedInstanceStepManager.singleton());
		}else {
			throw new UnsupportedOperationException();
		}
		
		return index;
	}
	
	
	@Override
	public boolean finish() {
		//
		throw new UnsupportedOperationException();
	}

	
}
