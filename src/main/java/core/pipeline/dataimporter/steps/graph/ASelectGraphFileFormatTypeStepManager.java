package core.pipeline.dataimporter.steps.graph;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepManager;
import core.pipeline.dataimporter.steps.SelectDataTypeStepManager;
import fileformat.graph.GraphDataFileFormatType;
import importer.DataImporter;

/**
 * step to choose a {@link GraphDataFileFormatType} supported by visframe
 * 
 * @author tanxu
 *
 */
public class ASelectGraphFileFormatTypeStepManager extends AbstractProcessStepManager<DataImporter, ASelectGraphFileFormatTypeStepSettings, ASelectGraphFileFormatTypeStepController>{
	private final static String DESCRIPTION = "Select graph data file format type of the data file to import;";
	private final static ASelectGraphFileFormatTypeStepSettings DEFAULT_SETTINGS = new ASelectGraphFileFormatTypeStepSettings(GraphDataFileFormatType.SIMPLE_GEXF);	
	
	
	protected static ASelectGraphFileFormatTypeStepManager SINGLETON;
	
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
	public static ASelectGraphFileFormatTypeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new ASelectGraphFileFormatTypeStepManager();
			
			SINGLETON.addPreviousStep(SelectDataTypeStepManager.singleton());
			
			SINGLETON.addNextStep(BMakeGraphDataImporterStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	
	/**
	 * constructor
	 */
	protected ASelectGraphFileFormatTypeStepManager() {
		super(DataImporter.class, ASelectGraphFileFormatTypeStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		return this.getPossibleNextStepList().indexOf(BMakeGraphDataImporterStepManager.singleton());
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		throw new UnsupportedOperationException();
	}

}
