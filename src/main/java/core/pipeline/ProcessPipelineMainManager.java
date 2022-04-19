package core.pipeline;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import basic.process.ProcessType;
import context.project.VisProjectDBContext;
import javafx.stage.Stage;
import session.toolpanel.process.AbstractProcessToolPanelManager;

/**
 * start a pipeline to build and perform an instance of a specific ProcessType;
 * 
 * @author tanxu
 *
 * @param <P>
 */
public interface ProcessPipelineMainManager<P extends ProcessType> {
	Class<P> getType();
	/**
	 * return the host VisProjectDBContext
	 * @return
	 */
	VisProjectDBContext getHostVisProjectDBContext();
	
	/**
	 * 
	 * @param manager
	 */
	void setProcessToolPanelManager(AbstractProcessToolPanelManager<P,?,?> manager);
	
	AbstractProcessToolPanelManager<P,?,?> getProcessToolPanelManager();
	
	
	ProcessPipelineMainController getController() throws IOException;
	
	/**
	 * show the window
	 * @param primaryStage
	 * @param project
	 * @throws IOException
	 * @throws SQLException 
	 */
	void start(Stage primaryStage, VisProjectDBContext project) throws IOException, SQLException;
	
	void closeWindow() throws IOException;
	
	/**
	 * 
	 * @return
	 */
	String getTitle();
	
	
	
	
	/////////////////////////////////////////
	/**
	 * return the ProcessStepControllerFactory for root step controller
	 * @return
	 */
	ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> getRootStep();
	
	
	/**
	 * return the instance of the step that is currently set as the scene of this main controller;
	 * @return
	 */
	ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> getCurrentlyOpenedStepManager();
	
	/**
	 * set the current opened step as the given one
	 * @param step
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void setCurrentlyOpenedStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> processStep) throws IOException, SQLException;
//	     setCurrentlyOpenedStep(ProcessStep<P, ? extends ProcessStepSettings, ? extends ProcessStepController<?>> stepController) throws IOException;
	
	

	
}
