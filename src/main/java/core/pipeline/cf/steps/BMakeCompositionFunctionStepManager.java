package core.pipeline.cf.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.composition.CompositionFunctionBuilder;
import context.project.process.simple.CompositionFunctionInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import function.composition.CompositionFunction;
import function.group.CompositionFunctionGroup;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;


public class BMakeCompositionFunctionStepManager extends AbstractProcessStepManager<CompositionFunction, BMakeCompositionFunctionStepSettings, BMakeCompositionFunctionStepController> {
	private final static String DESCRIPTION = "Build the CompositionFunction instance;";
	private final static BMakeCompositionFunctionStepSettings DEFAULT_SETTINGS = new BMakeCompositionFunctionStepSettings();	
	
	protected static BMakeCompositionFunctionStepManager SINGLETON;
	
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
	public static BMakeCompositionFunctionStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeCompositionFunctionStepManager();
			
			SINGLETON.addPreviousStep(ASelectOwnerCFGStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeCompositionFunctionStepManager() {
		super(CompositionFunction.class, BMakeCompositionFunctionStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1500d);
	}
	
	/////////////////
	private CompositionFunctionBuilder cfBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException {
		ASelectOwnerCFGStepManager prestep = (ASelectOwnerCFGStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		
    	//initialize the node builder and add it to the step content UI
		CompositionFunctionGroup ownerCFG = prestep.getCurrentAssignedSettings().getOwnerCompositionFunctionGroup();
		
		try {
			int indexID = this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionManager().findNextAvailableCompositionFunctionIndexID(ownerCFG.getID());
			
			this.cfBuilder = new CompositionFunctionBuilder(
					this.getProcessMainManager().getHostVisProjectDBContext(),
					ownerCFG,
					indexID
					);
			
			this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
			
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
	    		.add(this.cfBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	    	
	    	
	    	////////////
			return this.getController().getRootNode();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		//this should never be reached!
		return null;
	}
	
	
	@Override
	public NodeBuilder<CompositionFunction, ?> getNodeBuilder() {
		return (NodeBuilder<CompositionFunction, ?>) cfBuilder;
	}
	
	
	@Override
	public boolean finish() throws Throwable {
		if(!this.cfBuilder.isFinishable()) {
			AlertUtils.popAlert("Error", "CompositionFunction is not finishable!");
			return false;
		}
		
		CompositionFunctionInserter inserter = 
				new CompositionFunctionInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						this.cfBuilder.getEmbeddedUIContentController().build()
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
