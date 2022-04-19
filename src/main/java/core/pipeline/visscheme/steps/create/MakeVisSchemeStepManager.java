package core.pipeline.visscheme.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.VisSchemeBuilder;
import context.project.process.simple.VisSchemeInserter;
import context.scheme.VisScheme;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.visscheme.steps.SelectImportModeStepManager;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;

public class MakeVisSchemeStepManager extends AbstractProcessStepManager<VisScheme, MakeVisSchemeStepSettings, MakeVisSchemeStepController> {
	private final static String DESCRIPTION = "Build the CompositionFunction instance;";
	private final static MakeVisSchemeStepSettings DEFAULT_SETTINGS = new MakeVisSchemeStepSettings();	
	
	protected static MakeVisSchemeStepManager SINGLETON;
	
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
	public static MakeVisSchemeStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new MakeVisSchemeStepManager();
			
			SINGLETON.addPreviousStep(SelectImportModeStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected MakeVisSchemeStepManager() {
		super(VisScheme.class, MakeVisSchemeStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1500d);
	}
	
	/////////////////
	private VisSchemeBuilder visSchemeBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
//		ASelectOwnerCFGStepManager prestep = (ASelectOwnerCFGStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		
    	//initialize the node builder and add it to the step content UI
//		CompositionFunctionGroup ownerCFG = prestep.getCurrentAssignedSettings().getOwnerCompositionFunctionGroup();
		
//			int indexID = this.getProcessMainManager().getHostVisProject().getHasIDTypeManagerController().getCompositionFunctionManager().findNextAvailableCompositionFunctionIndexID(ownerCFG.getID());
			
		this.visSchemeBuilder = new VisSchemeBuilder(
				this.getProcessMainManager().getHostVisProjectDBContext()
				);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.visSchemeBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
		
	}
	
	
	@Override
	public NodeBuilder<VisScheme, ?> getNodeBuilder() {
		return (NodeBuilder<VisScheme, ?>) visSchemeBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		if(!this.visSchemeBuilder.getCurrentStatus().hasValidValue()) {
			AlertUtils.popAlert("Error", "VisScheme is not finishable!");
			return false;
		}
		
		VisScheme visScheme = this.visSchemeBuilder.getCurrentValue();
		visScheme.setImported(false);
		visScheme.setUID(this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().findNextAvaiableUID());
		
		VisSchemeInserter inserter = 
				new VisSchemeInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						visScheme
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
