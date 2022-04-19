package core.pipeline.dataimporter.steps.vftree;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.dataimporter.steps.SelectDataTypeStepManager;
import fileformat.vftree.VfTreeDataFileFormatType;
import importer.DataImporter;

/**
 * step to select a {@link VfTreeDataFileFormatType} for the vftree data file to be imported;
 * 
 * @author tanxu
 *
 */
public class ASelectVfTreeFileFormatTypeStepManager extends AbstractProcessStepManager<DataImporter, ASelectVfTreeFileFormatTypeStepSettings, ASelectVfTreeFileFormatTypeStepController>{
	private final static String DESCRIPTION = "Select VfTreeDataFileFormatType of the data file to import;";
	private final static ASelectVfTreeFileFormatTypeStepSettings DEFAULT_SETTINGS = new ASelectVfTreeFileFormatTypeStepSettings(VfTreeDataFileFormatType.SIMPLE_NEWICK_1);	
	
	
	protected static ASelectVfTreeFileFormatTypeStepManager SINGLETON;
	
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
	public static ASelectVfTreeFileFormatTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectVfTreeFileFormatTypeStepManager();
			
			SINGLETON.addPreviousStep(SelectDataTypeStepManager.singleton());
			
			SINGLETON.addNextStep(BMakeVfTreeDataImporterStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////////
	/**
	 * constructor
	 */
	protected ASelectVfTreeFileFormatTypeStepManager() {
		super(DataImporter.class, ASelectVfTreeFileFormatTypeStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
	}
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeVfTreeDataImporterStepManager.singleton());
	}

	@Override
	public boolean finish() throws SQLException, IOException {
		throw new UnsupportedOperationException();
	}

}
