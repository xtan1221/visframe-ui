package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducerAndInserter;
import core.builder.NodeBuilder;
import core.pipeline.AbstractProcessStepManager;
import core.pipeline.ProcessPipelineMainManager;
import core.pipeline.ProcessStepManager;
import core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import utils.AlertUtils;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager 
	extends AbstractProcessStepManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance, BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings, BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepController> {
	
	private final static String DESCRIPTION = "Build the VisSchemeAppliedArchiveReproducedAndInsertedInstance;";
	private final static BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings DEFAULT_SETTINGS = new BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings();	
	
	protected static BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager SINGLETON;
	
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
	public static BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager singleton() {
		if(SINGLETON==null) {
			SINGLETON = new BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager();
			
			SINGLETON.addPreviousStep(ASelectVisSchemeAppliedArchiveStepManager.singleton());
			
		}
		
		return SINGLETON;
	}
	
	//////////////////////////////////
	
	/**
	 * constructor
	 */
	protected BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager() {
		super(VisSchemeAppliedArchiveReproducedAndInsertedInstance.class, BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepController.FXML_FILE_DIR_STRING, DESCRIPTION, DEFAULT_SETTINGS, 800d, 1200d);
	}
	
	/////////////////
	private VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	
	/**
	 * build and return the root node of the VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder built with 
	 * the VisSchemeAppliedArchiveReproducerAndInserter of the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager
	 */
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
//		ASelectVisSchemeAppliedArchiveStepManager prestep = (ASelectVisSchemeAppliedArchiveStepManager)this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		try {
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager mainManager = 
					(VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager)this.getProcessMainManager();
			//
			this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder = 
					new VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder(
							mainManager.getVisSchemeAppliedArchiveReproducerAndInserter());
			
			this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.initialize();//
			
			//
			this.getController().getBuilderEmbeddedRootNodeContainer().getChildren().clear();
	    	this.getController().getBuilderEmbeddedRootNodeContainer().getChildren()
	    		.add(this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	    	
	    	
	    	////////////
			return this.getController().getRootNode();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);//debug
		}
		
		//this should never be reached!
		return null;
	}
	
	
	@Override
	public NodeBuilder<VisSchemeAppliedArchiveReproducedAndInsertedInstance, ?> getNodeBuilder() {
		return (NodeBuilder<VisSchemeAppliedArchiveReproducedAndInsertedInstance, ?>) visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	}
	
	/**
	 * consistent with {@link #cancel()} method
	 */
	@Override
	public Runnable getPipelineMainWindowCloseRequestEventHandler() {
		return ()->{
			//abort and roll back the currently running VisSchemeAppliedArchiveReproducerAndInserter;
			try {
				this.getProcessMainManager().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
				.getVSAArchiveReproducerAndInserterManager().abortAndRollbackCurrentlyRunningVSAArchiveReproducerAndInserter();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);//debug
			}
		};
	}
	
	
	/**
	 * always pop up a window to ask for confirmation before take any action
	 * 		whether to abandon any settings that have been done so far with the current VisSchemeAppliedArchiveReproducerAndInserter;
	 * 
	 * need to 
	 * 1. abort and roll back the current VisSchemeAppliedArchiveReproducerAndInserter in the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager
	 * 2. set the VisSchemeAppliedArchiveReproducerAndInserter in the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager to null;
	 */
	@Override
	public void back() throws IOException, SQLException {
		
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will abandon any settings that have been done so far!");
		
		if (result.get() == ButtonType.OK){
			super.back();
			
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager mainManager = 
					(VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager)this.getProcessMainManager();
			
			//abort and roll back the currently running VisSchemeAppliedArchiveReproducerAndInserter;
			mainManager.getVisSchemeAppliedArchiveReproducerAndInserter().abortAndRollback();
			
			//set the VisSchemeAppliedArchiveReproducerAndInserter to null
			mainManager.setVisSchemeAppliedArchiveReproducerAndInserter(null);
		}
	}
	
	/**
	 * always pop up a window to ask for confirmation before take any action
	 * 		whether to abandon any settings that have been done so far with the current VisSchemeAppliedArchiveReproducerAndInserter and restart it;
	 * 
	 * need to 
	 * 1. abort and roll back currently running VisSchemeAppliedArchiveReproducerAndInserter
	 * 2. re-initialize the VisSchemeAppliedArchiveReproducerAndInserter in VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager
	 * 3. re-start the VisSchemeAppliedArchiveReproducerAndInserter
	 * 4. reset the UI by invoke {@link ProcessPipelineMainManager#setCurrentlyOpenedStep(ProcessStepManager)} method
	 */
	@Override
	public void reset() throws IOException, SQLException {
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will abandon any settings that have been done so far!");
		
		if (result.get() == ButtonType.OK){
			super.reset();
			
			//first abort and roll back the currently running VisSchemeAppliedArchiveReproducerAndInserter
			VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager mainManager = 
					(VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager)this.getProcessMainManager();
			
			mainManager.getVisSchemeAppliedArchiveReproducerAndInserter().abortAndRollback();
			
			//////
			//initialize a new VisSchemeAppliedArchiveReproducerAndInserter with the selected VisSchemeAppliedArchive
			VisSchemeAppliedArchiveReproducerAndInserter VisSchemeAppliedArchiveReproducerAndInserter = 
					new VisSchemeAppliedArchiveReproducerAndInserter(
							this.getProcessMainManager().getHostVisProjectDBContext(),
							mainManager.getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive()
							);
			
			//start the VisSchemeAppliedArchiveReproducerAndInserter process
			this.getProcessMainManager().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
			.getVSAArchiveReproducerAndInserterManager().startNewVSAArchiveReproducerAndInserter(VisSchemeAppliedArchiveReproducerAndInserter);
			
			//save the new VisSchemeAppliedArchiveReproducerAndInserter to the VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager
			mainManager.setVisSchemeAppliedArchiveReproducerAndInserter(VisSchemeAppliedArchiveReproducerAndInserter);
			
			
			//reset the UI to the re-initialized VisSchemeAppliedArchiveReproducerAndInserter
			this.getProcessMainManager().setCurrentlyOpenedStep(this);
		}
	}
	
	
	/**
	 * 
	 * always pop up a window to ask for confirmation before take any action
	 * 		whether to abandon any settings that have been done so far with the current VisSchemeAppliedArchiveReproducerAndInserter;
	 * 
	 * need to 
	 * 1. close the process main window
	 * 2. abort and roll back currently running VisSchemeAppliedArchiveReproducerAndInserter
	 * 
	 * @throws SQLException 
	 */
	@Override
	public void cancel() throws IOException, SQLException {
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will abandon any settings that have been done so far!");
		
		if (result.get() == ButtonType.OK){
			super.cancel();
			
			//abort and roll back the currently running VisSchemeAppliedArchiveReproducerAndInserter;
			this.getProcessMainManager().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager()
			.getVSAArchiveReproducerAndInserterManager().abortAndRollbackCurrentlyRunningVSAArchiveReproducerAndInserter();
		}
	}
	
	/**
	 * need to 
	 * 
	 * 1. 
	 */
	@Override
	public boolean finish() throws Throwable {
		if(!this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.isFinishable()) {
			AlertUtils.popAlert("Error", "VisSchemeAppliedArchiveReproducedAndInsertedInstance is not finishable!");
			return false;
		}
		
		///////
		VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager mainManager = 
				(VisSchemeAppliedArchiveReproducedAndInsertedInstanceProcessMainManager)this.getProcessMainManager();
		
		VisSchemeAppliedArchiveReproducerAndInserter inserter = mainManager.getVisSchemeAppliedArchiveReproducerAndInserter();
		
		try {
			//
			inserter.finish(this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.getEmbeddedUIContentController().build());
			
			//close the process window
			this.getProcessMainManager().closeWindow();
			
			//pop up window
			AlertUtils.popAlert("Message", "VisSchemeAppliedArchiveReproducedAndInsertedInstance have been successfully inserted!");
			
		}catch(Exception e) {//exception thrown, need to debug?
			e.printStackTrace();
			//roll back?????
			this.getProcessMainManager().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager().rollbackProcess(inserter.getID());
			
			System.exit(1);//debug
		}
		
		return true;
	}
	
	///////////////////////////////////////
	@Override
	public Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException {
		throw new UnsupportedOperationException();
	}
}
