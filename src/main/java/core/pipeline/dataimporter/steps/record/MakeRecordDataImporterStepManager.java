package core.pipeline.dataimporter.steps.record;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.record.RecordDataImporterBuilder;
import context.project.process.simple.DataImporterPerformer;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.dataimporter.steps.SelectDataTypeStepManager;
import importer.DataImporter;
import importer.record.RecordDataImporter;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;

public class MakeRecordDataImporterStepManager extends AbstractProcessStepManager<DataImporter, MakeRecordDataImporterStepSettings, MakeRecordDataImporterStepController>{
	private final static String DESCRIPTION = "Build the Record Data Importer;";
	private final static MakeRecordDataImporterStepSettings DEFAULT_SETTINGS = new MakeRecordDataImporterStepSettings();	
	
	
	/////////////
	protected static MakeRecordDataImporterStepManager SINGLETON;
	
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
	public static MakeRecordDataImporterStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new MakeRecordDataImporterStepManager();
			
			SINGLETON.addPreviousStep(SelectDataTypeStepManager.singleton());
		}
		
		return SINGLETON;
	}

	
	
	//////////////////////////////////
	private RecordDataImporterBuilder recordDataImporterBuilder;
	
	
	/**
	 * constructor
	 */
	protected MakeRecordDataImporterStepManager() {
		super(DataImporter.class, MakeRecordDataImporterStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		//first time invoked
		if(this.controller==null) {
			this.recordDataImporterBuilder = new RecordDataImporterBuilder(this.getProcessMainManager().getHostVisProjectDBContext()); 
			
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		}
		
		
		//
		this.getController().setStepSettings(this.getCurrentAssignedSettings());
		
		return this.getController().getRootNode();
	}
	
	@Override
	public NodeBuilder<RecordDataImporter, ?> getNodeBuilder() {
		return recordDataImporterBuilder;
	}
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean finish() throws SQLException, IOException {
		
		DataImporterPerformer performer = new DataImporterPerformer(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		
		//set the init window of the progress bar to the stage of the visframe session;
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						performer, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
		
		//
		progressManager.start(true);
		
		
		/////debug the abortAndRollbackCurrentRunningSimpleProcess method of SimpleProcessManager
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		return true;
	}
}
