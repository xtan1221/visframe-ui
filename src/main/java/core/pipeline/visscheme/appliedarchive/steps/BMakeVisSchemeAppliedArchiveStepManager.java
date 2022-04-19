package core.pipeline.visscheme.appliedarchive.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilder;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.VisSchemeAppliedArchiveInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisSchemeAppliedArchiveStepManager extends AbstractProcessStepManager<VisSchemeAppliedArchive, BMakeVisSchemeAppliedArchiveStepSettings, BMakeVisSchemeAppliedArchiveStepController> {
	private final static String DESCRIPTION = "Build the VisSchemeAppliedArchive;";
	private final static BMakeVisSchemeAppliedArchiveStepSettings DEFAULT_SETTINGS = new BMakeVisSchemeAppliedArchiveStepSettings();	
	
	protected static BMakeVisSchemeAppliedArchiveStepManager SINGLETON;
	
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
	public static BMakeVisSchemeAppliedArchiveStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVisSchemeAppliedArchiveStepManager();
			
			SINGLETON.addPreviousStep(ASelectAppliedVisSchemeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeVisSchemeAppliedArchiveStepManager() {
		super(VisSchemeAppliedArchive.class, BMakeVisSchemeAppliedArchiveStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
		
	}
	
	/////////////////
	private VisSchemeAppliedArchiveBuilder visSchemeAppliedArchiveBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectAppliedVisSchemeStepManager prestep = (ASelectAppliedVisSchemeStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		
    	//initialize the node builder and add it to the step content UI
		VisScheme appliedVisScheme = prestep.getCurrentAssignedSettings().getAppliedVisScheme();
		
		int indexID = this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveManager().findNextAvailableUID();
		
		this.visSchemeAppliedArchiveBuilder = new VisSchemeAppliedArchiveBuilder(
				this.getProcessMainManager().getHostVisProjectDBContext(),
				appliedVisScheme,
				indexID
				);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.visSchemeAppliedArchiveBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
	}
	
	
	@Override
	public NodeBuilder<VisSchemeAppliedArchive, ?> getNodeBuilder() {
		return (NodeBuilder<VisSchemeAppliedArchive, ?>) visSchemeAppliedArchiveBuilder;
	}
	
	
	@Override
	public boolean finish() throws Throwable {
		if(!this.visSchemeAppliedArchiveBuilder.isFinishable()) {
			AlertUtils.popAlert("Error", "CompositionFunction is not finishable!");
			return false;
		}
		
		VisSchemeAppliedArchiveInserter inserter = 
				new VisSchemeAppliedArchiveInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						this.visSchemeAppliedArchiveBuilder.getEmbeddedUIContentController().build()
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
