package core.pipeline.operation.steps;

import java.io.IOException;
import java.sql.SQLException;
import core.pipeline.AbstractProcessStepManager;
import operation.Operation;

public class ASelectTypeAndTemplateStepManager extends AbstractProcessStepManager<Operation, ASelectTypeAndTemplateStepSettings, ASelectTypeAndTemplateStepController> {
	private final static String DESCRIPTION = "Select the type and template for the operation to be created";
	private final static ASelectTypeAndTemplateStepSettings DEFAULT_SETTINGS = new ASelectTypeAndTemplateStepSettings();
	
	
	////////////////////
	public static ASelectTypeAndTemplateStepManager SINGLETON;
	
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
	public static ASelectTypeAndTemplateStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectTypeAndTemplateStepManager();
			
			SINGLETON.addNextStep(BMakeOperationStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ASelectTypeAndTemplateStepManager() {
		super(Operation.class, ASelectTypeAndTemplateStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeOperationStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() {
		//
		throw new UnsupportedOperationException();
	}
	
	
}
