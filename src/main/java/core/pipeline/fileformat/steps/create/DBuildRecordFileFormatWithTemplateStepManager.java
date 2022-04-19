package core.pipeline.fileformat.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import com.google.common.base.Objects;

import builder.visframe.fileformat.record.RecordDataFileFormatBuilder;
import context.project.process.simple.FileFormatInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
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
public class DBuildRecordFileFormatWithTemplateStepManager extends AbstractProcessStepManager<FileFormat, DBuildRecordFileFormatStepSettings, DBuildRecordFileFormatStepController> {
	private final static String DESCRIPTION = "Build new Record File Format with a Template;";
	private final static DBuildRecordFileFormatStepSettings DEFAULT_SETTINGS = new DBuildRecordFileFormatStepSettings();	
	
	private static DBuildRecordFileFormatWithTemplateStepManager SINGLETON;
	
	/**
	 * static factory method to get a singleton object of this class
	 * @return
	 */
	public static DBuildRecordFileFormatWithTemplateStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new DBuildRecordFileFormatWithTemplateStepManager();
			
			SINGLETON.addPreviousStep(C2SelectRecordFileFormatTemplateStepManager.SINGLETON);
			
			SINGLETON.finishInitialization();
		}
		
		return SINGLETON;
	}
	
	
	private RecordDataFileFormatBuilder recordDataFileFormatBuilder;
	private RecordDataFileFormat templateRecordDataFileFormat;//template selected from previous step
	
	/**
	 * constructor
	 * @param possibleNextStepControllerIndexMap
	 * @param possiblePreviousStepControllerIndexMap
	 */
	protected DBuildRecordFileFormatWithTemplateStepManager() {
		super(FileFormat.class, DBuildRecordFileFormatStepController.FXML_FILE_DIR_STRING,
				DESCRIPTION,
				DEFAULT_SETTINGS);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		C2SelectRecordFileFormatTemplateStepManager prestep = (C2SelectRecordFileFormatTemplateStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		//first time invoked, initialize the controller
//		if(this.stepRootNode==null) {//first time invoked
//			System.out.println("first time invoked");
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
//	    	//this must be invoked before the getController() method!!!!!!!
//	    	this.stepRootNode = loader.load();
//	    	
//	    	this.controller = (DBuildRecordFileFormatStepController)loader.getController();
//	    	this.controller.setManager(this);
//		}
		
		//current template is null(first time this step is invoked) or has changed
		if(!Objects.equal(this.templateRecordDataFileFormat, prestep.getCurrentAssignedSettings().getSelectedRecordDataFileFormat())) {
			System.out.println(this.getClass().getSimpleName()+": !!!!!!!!!!!!!!!!!!!!!changed!!!!!!!!!!!!!!!!!!!!!!");
			this.templateRecordDataFileFormat = prestep.getCurrentAssignedSettings().getSelectedRecordDataFileFormat();
			this.currentlyAssignedSettings = new DBuildRecordFileFormatStepSettings(this.templateRecordDataFileFormat);
			
//	    	System.out.println("==============================");
//	    	System.out.println(prestep.getCurrentAssignedSettings());
//	    	System.out.println("==============================");
	    	//
	    	this.recordDataFileFormatBuilder = new RecordDataFileFormatBuilder(
	    			this.templateRecordDataFileFormat.getBetweenRecordStringFormat().isSequential(),
	    			this.templateRecordDataFileFormat.getWithinRecordAttributeStringFormat().isStringDelimited());
	    	
	    	//set the value to the selected one
	    	this.recordDataFileFormatBuilder.setValue(this.templateRecordDataFileFormat, false);
			
//	    	System.out.println("==============================");
//	    	System.out.println(this.recordDataFileFormatBuilder.getCurrentStatus());
//	    	System.out.println(this.recordDataFileFormatBuilder.getCurrentValue());
//	    	System.out.println("==============================");
	    	
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(
	    			this.recordDataFileFormatBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		}
//	    	///////////
//	    	this.templateRecordDataFileFormat = prestep.getCurrentAssignedSettings().getSelectedRecordDataFileFormat();
//			
////	    	System.out.println("==============================");
////	    	System.out.println(prestep.getCurrentAssignedSettings());
////	    	System.out.println("==============================");
//	    	//
//	    	this.recordDataFileFormatBuilder = new RecordDataFileFormatBuilder(
//	    			this.templateRecordDataFileFormat.getBetweenRecordStringFormat().isSequential(),
//	    			this.templateRecordDataFileFormat.getWithinRecordAttributeStringFormat().isStringDelimited());
//	    	
//	    	//set the value to the selected one
//	    	this.recordDataFileFormatBuilder.setValue(this.templateRecordDataFileFormat, false);
//			
////	    	System.out.println("==============================");
////	    	System.out.println(this.recordDataFileFormatBuilder.getCurrentStatus());
////	    	System.out.println(this.recordDataFileFormatBuilder.getCurrentValue());
////	    	System.out.println("==============================");
//	    	
//	    	
//	    	this.controller.getBuilderEmbeddedRootNodeContainer().getChildren().add(
//	    			this.recordDataFileFormatBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
//		
//		}else if(this.templateRecordDataFileFormat!=prestep.getCurrentAssignedSettings().getSelectedRecordDataFileFormat()) {//
//			System.out.println("first time invoked");
//			//selected template file format changed since last time
//			this.templateRecordDataFileFormat = prestep.getCurrentAssignedSettings().getSelectedRecordDataFileFormat();
//			
//			this.recordDataFileFormatBuilder = new RecordDataFileFormatBuilder(
//					this.templateRecordDataFileFormat.getBetweenRecordStringFormat().isSequential(),
//					this.templateRecordDataFileFormat.getWithinRecordAttributeStringFormat().isStringDelimited());
//			
//			this.recordDataFileFormatBuilder.setValue(this.templateRecordDataFileFormat, false);
//			
//			//
//		    this.controller.getBuilderEmbeddedRootNodeContainer().getChildren().clear();
//		    this.controller.getBuilderEmbeddedRootNodeContainer().getChildren().add(
//		    		this.recordDataFileFormatBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
//		}else {
//			System.out.println("??????????????????????????????");
//		}
		
//		this.controller.setStepSettings(this.getCurrentAssignedSettings());
		
		return this.getController().getRootNode();
		
	}

	@Override
	public NodeBuilder<? extends FileFormat, ?> getNodeBuilder() {
		return recordDataFileFormatBuilder;
	}
	
	///////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException {
		throw new UnsupportedOperationException();
	}
	
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
