package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import fileformat.FileFormat;

/**
 * step to select whether based on template or from scratch to build the new FileFormat;
 * 
 * previous step is {@link ASelectDataTypeStepManager}
 * 
 * next steps are:
 * 1. if based on template of an existing FileFormat, {@link C2SelectRecordFileFormatTemplateStepManager}
 * 2. if from scratch, {@link C1SelectBetweeAndWithinRecordFormatTypeStepManager}
 * 
 * @author tanxu
 * 
 */
public class BSelectRecordBuildingModeStepManager extends AbstractProcessStepManager<FileFormat, BSelectRecordBuildingModeStepSettings,BSelectRecordBuildingModeStepController> {
	private final static String DESCRIPTION = "SELECT how to import the new File format;";
	private final static BSelectRecordBuildingModeStepSettings DEFAULT_SETTINGS = new BSelectRecordBuildingModeStepSettings(true);	
	
	protected static BSelectRecordBuildingModeStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 * @throws SQLException 
	 */
	public static BSelectRecordBuildingModeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BSelectRecordBuildingModeStepManager();
			
			SINGLETON.addPreviousStep(ASelectDataTypeStepManager.SINGLETON);
			
			SINGLETON.addNextStep(C2SelectRecordFileFormatTemplateStepManager.singleton());
			
			SINGLETON.addNextStep(C1SelectBetweeAndWithinRecordFormatTypeStepManager.singleton());
			
			SINGLETON.finishInitialization();
			
		}
		
		return SINGLETON;
	}
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 */
	protected BSelectRecordBuildingModeStepManager() {
		super(FileFormat.class, BSelectRecordBuildingModeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		if(this.getController().getStepSettings().isBasedOnTemplate()) {
			return this.getPossibleNextStepList().indexOf(C2SelectRecordFileFormatTemplateStepManager.singleton());
		}else {
			return this.getPossibleNextStepList().indexOf(C1SelectBetweeAndWithinRecordFormatTypeStepManager.singleton());
		}
	}

	@Override
	public boolean finish() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
