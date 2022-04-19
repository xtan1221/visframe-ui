package core.pipeline.visinstance.run.steps;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import builder.visframe.visinstance.run.VisInstanceRunBuilder;
import context.project.process.simple.VisInstanceRunInserterAndCalculator;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import progressmanager.SingleSimpleProcessProgressManager;
import utils.AlertUtils;
import visinstance.run.VisInstanceRun;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisInstanceRunStepManager extends AbstractProcessStepManager<VisInstanceRun, BMakeVisInstanceRunStepSettings, BMakeVisInstanceRunStepController> {
	private final static String DESCRIPTION = "Build a VisInstanceRun";
	private final static BMakeVisInstanceRunStepSettings DEFAULT_SETTINGS = new BMakeVisInstanceRunStepSettings();	
	
	protected static BMakeVisInstanceRunStepManager SINGLETON;
	
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
	public static BMakeVisInstanceRunStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVisInstanceRunStepManager();
			
			SINGLETON.addPreviousStep(ASelectVisInstanceStepManager.singleton());
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeVisInstanceRunStepManager() {
		super(VisInstanceRun.class, BMakeVisInstanceRunStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
	}
	
	/////////////////
	private VisInstanceRunBuilder visInstanceRunBuilder;
	
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectVisInstanceStepManager prestep = (ASelectVisInstanceStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		this.visInstanceRunBuilder = 
				new VisInstanceRunBuilder(
						this.getProcessMainManager().getHostVisProjectDBContext(),
						prestep.getCurrentAssignedSettings().getSelectedVisInstance().getID(),
						this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunManager().findNextAvailableRunUID()
						);
		
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
    		.add(this.visInstanceRunBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
    	
    	
    	////////////
		return this.getController().getRootNode();
		
	}
	
	
	@Override
	public NodeBuilder<VisInstanceRun, ?> getNodeBuilder() {
		return (NodeBuilder<VisInstanceRun, ?>) visInstanceRunBuilder;
	}
	
	/**
	 * first need to check if there is any existing VisInstanceRun such that
	 * 1. with the same VisInstance
	 * 2. with the same IndependetFIVTypeIDStringValueMap
	 * if yes, show message that the VisInstanceRun cannot be inserted;
	 */
	@Override
	public boolean finish() throws SQLException, IOException {
		VisInstanceRun visInstanceRun = this.visInstanceRunBuilder.getCurrentValue();
		
		if(this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController()
				.getVisInstanceRunManager().isVisInstanceRunAlreadyCalculated(visInstanceRun.getVisInstanceID(), visInstanceRun.getCFDGraphIndependetFIVStringValueMap())) {
			
			Optional<ButtonType> result = AlertUtils.popConfirmationDialog(
					"Warning", 
					"Confirmation is needed",
					"An existing VisInstanceRun with same VisInstance and IndependetFIVType values found! \n"
					+ "Disard and close the VisInstanceRun window?");
			
			if (result.get() == ButtonType.OK){
				//discard the VisInstanceRun and close the process window
				this.getProcessMainManager().closeWindow();
				return true;
			}else {
				//do nothing, so that user can continue to set different IndependetFIVType values or discard the VisInstanceRun
				return false;
			}
		}
		
		
		VisInstanceRunInserterAndCalculator inserterAndCalculator = 
				new VisInstanceRunInserterAndCalculator(
						this.getProcessMainManager().getHostVisProjectDBContext(), 
						visInstanceRun
						);
		
		//set the init window of the progress bar to the stage of the visframe session;
		SingleSimpleProcessProgressManager progressManager = 
				new SingleSimpleProcessProgressManager(
						inserterAndCalculator, 
						this.getProcessMainManager().getProcessToolPanelManager().getSessionManager().getSessionStage(),
						true,
						null,
						null);
		
		//close the process window
		this.getProcessMainManager().closeWindow();
		
		//
		progressManager.start(true);
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
}
