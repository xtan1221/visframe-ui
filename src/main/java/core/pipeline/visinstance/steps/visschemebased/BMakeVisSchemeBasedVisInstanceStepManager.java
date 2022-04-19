package core.pipeline.visinstance.steps.visschemebased;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.visschemebased.VisSchemeBasedVisInstanceBuilder;
import context.project.process.simple.VisInstanceInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import visinstance.VisInstance;
import visinstance.VisSchemeBasedVisInstance;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisSchemeBasedVisInstanceStepManager extends AbstractProcessStepManager<VisInstance, BMakeVisSchemeBasedVisInstanceStepSettings, BMakeVisSchemeBasedVisInstanceStepController> {
	private final static String DESCRIPTION = "Build a VisSchemeBasedVisInstance;";
	private final static BMakeVisSchemeBasedVisInstanceStepSettings DEFAULT_SETTINGS = new BMakeVisSchemeBasedVisInstanceStepSettings();	
	
	protected static BMakeVisSchemeBasedVisInstanceStepManager SINGLETON;
	
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
	public static BMakeVisSchemeBasedVisInstanceStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVisSchemeBasedVisInstanceStepManager();
			
			SINGLETON.addPreviousStep(ASelectVSAAReproducedAndInsertedInstanceStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeVisSchemeBasedVisInstanceStepManager() {
		super(VisInstance.class, BMakeVisSchemeBasedVisInstanceStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
	}
	
	/////////////////
	private VisSchemeBasedVisInstanceBuilder visSchemeBasedVisInstanceBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectVSAAReproducedAndInsertedInstanceStepManager prestep = (ASelectVSAAReproducedAndInsertedInstanceStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		//
		this.visSchemeBasedVisInstanceBuilder = new VisSchemeBasedVisInstanceBuilder(
				this.getProcessMainManager().getHostVisProjectDBContext(),
				this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceManager().findNextAvailableUID(),
				prestep.getCurrentAssignedSettings().getSelectedVSAAReproducedAndInsertedInstance().getAppliedVisSchemeID(),
				prestep.getCurrentAssignedSettings().getSelectedVSAAReproducedAndInsertedInstance().getApplierArchiveID(),
				prestep.getCurrentAssignedSettings().getSelectedVSAAReproducedAndInsertedInstance().getID()
				);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.visSchemeBasedVisInstanceBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
		
	}
	
	
	@Override
	public NodeBuilder<VisSchemeBasedVisInstance, ?> getNodeBuilder() {
		return (NodeBuilder<VisSchemeBasedVisInstance, ?>) visSchemeBasedVisInstanceBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {

		VisSchemeBasedVisInstance nativeVisInstance = this.visSchemeBasedVisInstanceBuilder.getCurrentValue();
		
		VisInstanceInserter inserter = 
				new VisInstanceInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						nativeVisInstance
						);
		
		//set the init window of the progress bar to the stage of the visframe session;
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						inserter, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
		
		//
		progressManager.start(false);
		
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
}
