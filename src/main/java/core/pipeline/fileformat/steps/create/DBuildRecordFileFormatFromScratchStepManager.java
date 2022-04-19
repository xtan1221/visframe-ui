package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.fileformat.record.RecordDataFileFormatBuilder;
import context.project.process.simple.FileFormatInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.BetweenRecordFormatType;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.WithinRecordFormatType;
import fileformat.FileFormat;
import fileformat.record.RecordDataFileFormat;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;


/**
 * step to build the record FileFormat
 * 
 * previous steps are:
 * 1. based on template, {@link C2SelectRecordFileFormatTemplateStepManager}
 * 2. based on scratch, {@link C1SelectBetweeAndWithinRecordFormatTypeStepManager}
 * 
 * @author tanxu
 * 
 */
public class DBuildRecordFileFormatFromScratchStepManager extends AbstractProcessStepManager<FileFormat, DBuildRecordFileFormatStepSettings, DBuildRecordFileFormatStepController> {
	
	private final static String DESCRIPTION = "Builder new Record File Format from scratch;";
	private final static DBuildRecordFileFormatStepSettings DEFAULT_SETTINGS = new DBuildRecordFileFormatStepSettings();	
	
	private static DBuildRecordFileFormatFromScratchStepManager SINGLETON;
	
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 */
	public static DBuildRecordFileFormatFromScratchStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new DBuildRecordFileFormatFromScratchStepManager();
			
			SINGLETON.addPreviousStep(C1SelectBetweeAndWithinRecordFormatTypeStepManager.SINGLETON);
			
			SINGLETON.finishInitialization();
		}
		
		return SINGLETON;
	}
	
	
	///////////////////////////////////
	
	
	private RecordDataFileFormatBuilder builder;
	
	private BetweenRecordFormatType previousSelectedBetweenRecordFormatType;
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 */
	protected DBuildRecordFileFormatFromScratchStepManager() {
		super(FileFormat.class, DBuildRecordFileFormatStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS);
	}
	
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		C1SelectBetweeAndWithinRecordFormatTypeStepManager prestep = (C1SelectBetweeAndWithinRecordFormatTypeStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
//		if(this.controller==null) {//first time invoked
////			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
////	    	//this must be invoked before the getController() method!!!!!!!
////	    	this.stepRootNode = loader.load();
////	    	
////	    	this.controller = (DBuildRecordFileFormatStepController)loader.getController();
////	    	
//	    	//
//	    	this.builder = new RecordDataFileFormatBuilder(
//	    			prestep.getCurrentAssignedSettings().getBetweenRecordFormatType()==BetweenRecordFormatType.SEQUENTIAL,
//	    			prestep.getCurrentAssignedSettings().getWithinRecordFormatType()==WithinRecordFormatType.DELIMITED_BY_STRING);//TODO check previous step's 
//	    	
//	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
//	    	this.getController().setManager(this);
//		}else if(this.previousSelectedBetweenRecordFormatType!=prestep.getCurrentAssignedSettings().getBetweenRecordFormatType()) {//
//			//BetweenRecordFormatType changed since last time
//			
//			this.builder = new RecordDataFileFormatBuilder(
//					prestep.getCurrentAssignedSettings().getBetweenRecordFormatType()==BetweenRecordFormatType.SEQUENTIAL,
//					prestep.getCurrentAssignedSettings().getWithinRecordFormatType()==WithinRecordFormatType.DELIMITED_BY_STRING);//TODO check previous step's 
//			
//			this.previousSelectedBetweenRecordFormatType = prestep.getCurrentAssignedSettings().getBetweenRecordFormatType();
//			
//		    this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
//		    this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
////		    
//		}
		
		
		
		this.builder = new RecordDataFileFormatBuilder(
    			prestep.getCurrentAssignedSettings().getBetweenRecordFormatType()==BetweenRecordFormatType.SEQUENTIAL,
    			prestep.getCurrentAssignedSettings().getWithinRecordFormatType()==WithinRecordFormatType.DELIMITED_BY_STRING);//TODO check previous step's 
		
		//
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
    	if(this.previousSelectedBetweenRecordFormatType!=prestep.getCurrentAssignedSettings().getBetweenRecordFormatType()) {
			this.previousSelectedBetweenRecordFormatType = prestep.getCurrentAssignedSettings().getBetweenRecordFormatType();
    	}
		
		////////////////////
//		this.controller.setStepSettings(this.getCurrentAssignedSettings());
		
		
		
		/////////////////////
		return this.getController().getRootNode();
		
	}
	
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public NodeBuilder<RecordDataFileFormat, ?> getNodeBuilder() {
		return builder;
	}

	
	/**
	 * build the FileFormat and insert it into the host VisProjectDBContext with a FileFormatInserter;
	 * @throws IOException 
	 */
	@Override
	public boolean finish() throws SQLException, IOException {
//		System.out.println("built RecordFileFormat:"+this.getNodeBuilder().build());
		
		FileFormatInserter inserter = new FileFormatInserter(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						inserter, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
				
		progressManager.start(false);
		
		return true;
	}
	
}
