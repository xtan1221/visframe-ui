package core.pipeline.operation.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.operation.AbstractOperationBuilder;
import builder.visframe.operation.OperationBuilderFactory;
import context.project.process.simple.OperationPerformer;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import javafx.scene.Parent;
import operation.AbstractOperation;
import operation.Operation;
import progressmanager.SingleSimpleProcessProgressManager;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeOperationStepManager extends AbstractProcessStepManager<Operation, BMakeOperationStepManagerSettings, BMakeOperationStepController>{
	private final static String DESCRIPTION = "Build the Operation instance;";
	private final static BMakeOperationStepManagerSettings DEFAULT_SETTINGS = new BMakeOperationStepManagerSettings(null);	
	
	protected static BMakeOperationStepManager SINGLETON;
	
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
	public static BMakeOperationStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeOperationStepManager();
			
			SINGLETON.addPreviousStep(ASelectTypeAndTemplateStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeOperationStepManager() {
		super(Operation.class, BMakeOperationStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS);
	}
	
	/////////////////
	private AbstractOperationBuilder<? extends AbstractOperation> operationBuilder;
	
	
	/**
	 * @throws SQLException 
	 * 
	 */
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		ASelectTypeAndTemplateStepManager prestep = (ASelectTypeAndTemplateStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
    	//clear any current node builder if added previously
		this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
		
		//initialize the node builder
		//if a template is selected, set the template as the current value of the node builder;
    	if(prestep.getCurrentAssignedSettings().getSelectedTemplateOperationID()!=null) {
    		//there is a selected existing Operation instance as template
			Operation template = this.getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getOperationManager().lookup(prestep.getCurrentAssignedSettings().getSelectedTemplateOperationID());
			
			//build the operation builder with the template and reproduced==false no matter the selected template Operation is reproduced or not;
			this.operationBuilder = 
					OperationBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext())
					.build(template, false);
    	}else {
    		//there is no selected template Operation instance
    		//simply initialize an empty operation builder with reproduced == false
    		this.operationBuilder = 
    				OperationBuilderFactory.singleton(this.getProcessMainManager().getHostVisProjectDBContext())
    				.initializeOperationBuilder(prestep.getCurrentAssignedSettings().getSelectedOperationType(), false);
    	}
    	
    	//add the newly created node builder to the step main UI;
    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().add(this.operationBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

    	////////////
		return this.getController().getRootNode();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public NodeBuilder<Operation, ?> getNodeBuilder() {
		return (NodeBuilder<Operation, ?>) operationBuilder;
	}
	
	
	@Override
	public boolean finish() throws SQLException, IOException {
		OperationPerformer performer = new OperationPerformer(this.getProcessMainManager().getHostVisProjectDBContext(), this.getNodeBuilder().getCurrentValue());
		
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
		
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
}
