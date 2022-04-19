package core.pipeline.visinstance.run.layoutconfiguration.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfigurationBuilder;
import context.project.process.simple.VisInstanceRunLayoutConfigurationInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import javafx.scene.Parent;
import progressmanager.SingleSimpleProcessProgressManager;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisInstanceRunLayoutConfigurationStepManager extends AbstractProcessStepManager<VisInstanceRunLayoutConfiguration, BMakeVisInstanceRunLayoutConfigurationStepSettings, BMakeVisInstanceRunLayoutConfigurationStepController> {
	private final static String DESCRIPTION = "Build a VisInstanceRunLayout";
	private final static BMakeVisInstanceRunLayoutConfigurationStepSettings DEFAULT_SETTINGS = new BMakeVisInstanceRunLayoutConfigurationStepSettings();	
	
	protected static BMakeVisInstanceRunLayoutConfigurationStepManager SINGLETON;
	
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
	public static BMakeVisInstanceRunLayoutConfigurationStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVisInstanceRunLayoutConfigurationStepManager();
			
			SINGLETON.addPreviousStep(ASelectVisInstanceRunStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeVisInstanceRunLayoutConfigurationStepManager() {
		super(VisInstanceRunLayoutConfiguration.class, BMakeVisInstanceRunLayoutConfigurationStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
	}
	
	/////////////////
	private VisInstanceRunLayoutConfigurationBuilder visInstanceRunLayoutConfigurationBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectVisInstanceRunStepManager prestep = (ASelectVisInstanceRunStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		this.visInstanceRunLayoutConfigurationBuilder = 
				new VisInstanceRunLayoutConfigurationBuilder(
						this.getProcessMainManager().getHostVisProjectDBContext(),
						prestep.getCurrentAssignedSettings().getSelectedVisInstanceRun().getID(),
						this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunLayoutConfigurationManager().findNextAvailableUID()
						);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.visInstanceRunLayoutConfigurationBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
	}
	
	
	@Override
	public NodeBuilder<VisInstanceRunLayoutConfiguration, ?> getNodeBuilder() {
		return (NodeBuilder<VisInstanceRunLayoutConfiguration, ?>) visInstanceRunLayoutConfigurationBuilder;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean finish() throws SQLException, IOException {
		VisInstanceRunLayoutConfiguration visInstanceRunLayout = this.visInstanceRunLayoutConfigurationBuilder.getCurrentValue();
		
		
		VisInstanceRunLayoutConfigurationInserter inserter = 
				new VisInstanceRunLayoutConfigurationInserter(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						visInstanceRunLayout
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
