package core.pipeline.dataimporter.steps.vftree;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.importer.vftree.VfTreeDataImporterBaseBuilder;
import builder.visframe.importer.vftree.VfTreeDataImporterBaseBuilderFactory;
import context.project.process.simple.DataImporterPerformer;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import fileformat.vftree.VfTreeDataFileFormatType;
import importer.DataImporter;
import importer.vftree.VfTreeDataImporterBase;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;


/**
 * step to build a sub-type instance of {@link VfTreeDataImporterBase} based on the selected {@link VfTreeDataFileFormatType} in previous step;
 * 
 * @author tanxu
 *
 */
public class BMakeVfTreeDataImporterStepManager extends AbstractProcessStepManager<DataImporter, BMakeVfTreeDataImporterStepSettings, BMakeVfTreeDataImporterStepController>{
	private final static String DESCRIPTION = "build the VfTreeDataImporterBase";
	private final static BMakeVfTreeDataImporterStepSettings DEFAULT_SETTINGS = new BMakeVfTreeDataImporterStepSettings(null);	
	
	protected static BMakeVfTreeDataImporterStepManager SINGLETON;
	
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
	public static BMakeVfTreeDataImporterStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVfTreeDataImporterStepManager();
			
			SINGLETON.addPreviousStep(ASelectVfTreeFileFormatTypeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}

	
	
	//////////////////////////////////
	/**
	 * constructor
	 */
	protected BMakeVfTreeDataImporterStepManager() {
		super(DataImporter.class, BMakeVfTreeDataImporterStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
	}
	
	/////////////////////GraphDataImporterBaseBuilder specific
	private VfTreeDataImporterBaseBuilder<? extends VfTreeDataImporterBase> vfTreeDataImporterBaseBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		
//		if(this.stepRootNode==null) {//first time invoked
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
//	    	//this must be invoked before the getController() method!!!!!!!
//	    	this.stepRootNode = loader.load();
//	    	
//	    	this.controller = (BMakeVfTreeDataImporterStepController)loader.getController();
//	    	
//	    	//
//	    	this.vfTreeDataImporterBaseBuilder = VfTreeDataImporterBaseBuilderFactory.make(
//	    			((ASelectVfTreeFileFormatTypeStepSettings)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex()).getCurrentAssignedSettings()).getSelectedType(), 
//	    			this.getProcessMainManager().getHostVisProject());
//			
//	    	this.controller.getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
//	    	
//	    	this.controller.setManager(this);
//		}
		
		
		//first time invoked
		if(this.controller==null) {
			this.vfTreeDataImporterBaseBuilder = VfTreeDataImporterBaseBuilderFactory.make(
	    			((ASelectVfTreeFileFormatTypeStepSettings)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex()).getCurrentAssignedSettings()).getSelectedType(), 
	    			this.getProcessMainManager().getHostVisProjectDBContext());
			
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.getNodeBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	    	
		}
		
		
		this.getController().setStepSettings(this.getCurrentAssignedSettings());
		
		return this.getController().getRootNode();
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public NodeBuilder<VfTreeDataImporterBase, ?> getNodeBuilder() {
		return (NodeBuilder<VfTreeDataImporterBase, ?>) vfTreeDataImporterBaseBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		DataImporterPerformer performer = 
				new DataImporterPerformer(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						performer, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
				
		progressManager.start(true);
//		
		return true;
	}
	
	
	////////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
	
}
