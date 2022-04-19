package core.pipeline;

import java.io.IOException;
import java.sql.SQLException;

import basic.process.ProcessType;
import context.project.VisProjectDBContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.toolpanel.process.AbstractProcessToolPanelManager;


public abstract class AbstractProcessMainManager<P extends ProcessType> implements ProcessPipelineMainManager<P> {
	///////////////////////////////////////
	private final Class<P> type;
	private final ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> rootStep;
	private final String title;
	
	////////////////////
	private VisProjectDBContext hostVisProjectDBContext;
	private AbstractProcessToolPanelManager<P,?,?> processToolPanelManager;
	
	
	private ProcessPipelineMainController processMainController;
	private ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> currentlyOpenedStep;
	
	
	/**
	 * constructor
	 * @param hostVisProjectDBContext
	 * @param rootStep
	 * @param description
	 */
	protected AbstractProcessMainManager(
			Class<P> type,
			ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> rootStep, 
			String title
			){
		this.type = type;
		this.rootStep = rootStep;
		this.title = title;
		
		//
		this.getRootStep().setHostProcessManager(this);
		
	}
	//////////////////////////////////////
	
	@Override
	public void start(Stage primaryStage, VisProjectDBContext project) throws IOException, SQLException {
		this.hostVisProjectDBContext = project;
		
	
		this.createWindow(primaryStage, this.getController().getProcessMainRootVBox());
		
		/////////////////////////
		this.setCurrentlyOpenedStep(this.getRootStep());
		
		
		//invoke the reset button to clear any previous settings
		this.getController().resetButtonOnAction(null);
	}
	
	////////////////
	private Stage processStage;
	private Scene processScene;
	
   /**
	* 
	* @param primaryStage
	* @param root
	*/
	private void createWindow(Stage primaryStage, Pane root) {
		if(this.processStage==null) {
			processStage = new Stage();
			processScene = new Scene(root);
			processStage.setTitle(this.getTitle());
			processStage.setScene(processScene);
			
			// Specifies the modality for new window.
			processStage.initModality(Modality.WINDOW_MODAL);
			
			// Specifies the owner Window (parent) for new window
			processStage.initOwner(primaryStage);
		}
		processStage.show();
	}
	
	////////////////////////////////////
	@Override
	public Class<P> getType(){
		return this.type;
	}
	
	
	@Override
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}
	
	
	@Override
	public ProcessPipelineMainController getController() throws IOException {
		if(this.processMainController==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ProcessPipelineMainController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
	    	loader.load();
	    	
	    	this.processMainController = (ProcessPipelineMainController)loader.getController();
	    	
	    	this.processMainController.setProcessManager(this);
			
		}
		return this.processMainController;
	}
	
	@Override
	public void closeWindow() throws IOException {
		this.processStage.close();
	}
	
	
	@Override
	public ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> getRootStep() {
		return rootStep;
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	
	@Override
	public void setCurrentlyOpenedStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step) throws IOException, SQLException {
		if(step.getType() != this.getType()) {
			throw new IllegalArgumentException("given step is not of valid type");
		}
		
		this.currentlyOpenedStep = step;
		
		this.getController().setStep(this.currentlyOpenedStep);
	}
	
	
	/**
	 * return the instance of the step that is currently set as the scene of this main controller;
	 * @return
	 */
	@Override
	public ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> getCurrentlyOpenedStepManager(){
		return this.currentlyOpenedStep;
	}
	
	
	@Override
	public void setProcessToolPanelManager(AbstractProcessToolPanelManager<P,?,?> manager) {
		this.processToolPanelManager = manager;
	}
	
	@Override
	public AbstractProcessToolPanelManager<P,?,?> getProcessToolPanelManager(){
		return this.processToolPanelManager;
	}
	
}
