package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.fileformat.steps.SelectImportModeStepManager;
import fileformat.FileFormat;
import metadata.DataType;

/**
 * step to select data type of the new file format to build
 * 
 * previous step is {@link SelectImportModeStepManager};
 * next step is {@link SelectBuildingModeStep};
 * @author tanxu
 *
 */
public class ASelectDataTypeStepManager extends AbstractProcessStepManager<FileFormat, ASelectDataTypeStepSettings, ASelectDataTypeStepController> {
	private final static String DESCRIPTION = "Select data type of the new File Format to be built from scratch;";
	private final static ASelectDataTypeStepSettings DEFAULT_SETTINGS = new ASelectDataTypeStepSettings(DataType.RECORD);	
	
	
	protected static ASelectDataTypeStepManager SINGLETON;
	
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
	public static ASelectDataTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectDataTypeStepManager();
			
			SINGLETON.addNextStep(BSelectRecordBuildingModeStepManager.singleton());
			
			SINGLETON.addPreviousStep(SelectImportModeStepManager.SINGLETON);
		}
		
		return SINGLETON;
	}
	
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected ASelectDataTypeStepManager() {
		super(FileFormat.class, ASelectDataTypeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		if(this.getController().getStepSettings().getSelectedDataType().equals(DataType.RECORD)) {
			return this.getPossibleNextStepList().indexOf(BSelectRecordBuildingModeStepManager.singleton());
		}else {
			throw new UnsupportedOperationException();
		}
	}


	@Override
	public boolean finish() {
		//
		throw new UnsupportedOperationException();
	}
	
}
