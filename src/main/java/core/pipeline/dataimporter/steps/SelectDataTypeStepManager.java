package core.pipeline.dataimporter.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.dataimporter.steps.graph.ASelectGraphFileFormatTypeStepManager;
import core.pipeline.dataimporter.steps.record.MakeRecordDataImporterStepManager;
import core.pipeline.dataimporter.steps.vftree.ASelectVfTreeFileFormatTypeStepManager;
import importer.DataImporter;
import metadata.DataType;

/**
 * step to select data type of the data file to import
 * 
 * root step of DataImporter process
 * 
 * @author tanxu
 *
 */
public class SelectDataTypeStepManager extends AbstractProcessStepManager<DataImporter, SelectDataTypeStepSettings, SelectDataTypeStepController> {
	private final static String DESCRIPTION = "Select data type of the data file to import;";
	private final static SelectDataTypeStepSettings DEFAULT_SETTINGS = new SelectDataTypeStepSettings(DataType.RECORD);	
	
	
	protected static SelectDataTypeStepManager SINGLETON;
	
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
	public static SelectDataTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new SelectDataTypeStepManager();
			
			SINGLETON.addNextStep(MakeRecordDataImporterStepManager.singleton());
			SINGLETON.addNextStep(ASelectGraphFileFormatTypeStepManager.singleton());
			SINGLETON.addNextStep(ASelectVfTreeFileFormatTypeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	////////////////////////////////////
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected SelectDataTypeStepManager() {
		super(DataImporter.class, SelectDataTypeStepController.FXML_FILE_DIR_STRING,DESCRIPTION,DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		if(this.getController().getStepSettings().getSelectedDataType().equals(DataType.RECORD)) {
			return this.getPossibleNextStepList().indexOf(MakeRecordDataImporterStepManager.singleton());
		}else if(this.getController().getStepSettings().getSelectedDataType().equals(DataType.GRAPH)) {
			return this.getPossibleNextStepList().indexOf(ASelectGraphFileFormatTypeStepManager.singleton());
		}else if(this.getController().getStepSettings().getSelectedDataType().equals(DataType.vfTREE)) {
			return this.getPossibleNextStepList().indexOf(ASelectVfTreeFileFormatTypeStepManager.singleton());
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
