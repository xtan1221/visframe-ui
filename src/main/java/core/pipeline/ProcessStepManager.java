package core.pipeline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import basic.process.ProcessType;
import core.builder.NodeBuilder;
import core.pipeline.visscheme.appliedarchive.steps.BMakeVisSchemeAppliedArchiveStepManager;
import javafx.scene.Parent;

/**
 * a step controller of a ProcessManager to make an instance of type P;
 * 
 * note that it is recommended to maintain a singleton object for each final subtype of {@link ProcessStepManager};
 * @author tanxu
 *
 * @param <P>
 * @param <T>
 */
public interface ProcessStepManager<P extends ProcessType, T extends ProcessStepSettings, C extends ProcessStepController<P,T>> {
	
	Class<P> getType();
	
	String getFXMLFileDirString();
	
	Parent getStepRootNode() throws IOException, SQLException;
	
	C getController() throws IOException;
	
	/**
	 * return the height of the process main window when showing this step;
	 * applicable for steps with large size UI such as the 
	 * {@link BMakeVisSchemeAppliedArchiveStepManager}
	 * 
	 * if this value is non-null, the height of the process main window when showing this step will be set to this value;
	 * otherwise, set to the {@link #getDefaultHeight()}
	 * @return
	 */
	Double getPrefHeight();
	/**
	 * return the width of the process main window when showing this step;
	 * applicable for steps with large size UI such as the 
	 * {@link BMakeVisSchemeAppliedArchiveStepManager}
	 * 
	 * if this value is non-null, the width of the process main window when showing this step will be set to this value;
	 * otherwise, set to the {@link #getDefaultWidth()}
	 * @return
	 */
	Double getPrefWidth();
	
	default double getDefaultHeight() {
		return 400;
	}
	default double getDefaultWidth() {
		return 600;
	}
	
	/**
	 * return main controller
	 * @return
	 */
	ProcessPipelineMainManager<P> getProcessMainManager();
	
	
	default boolean isFinalStep() {
		return this.getPossibleNextStepList().isEmpty();
	}
	
	default boolean isFirstStep() {
		return this.getPossiblePreviousStepList().isEmpty();
	}
	
	
	/**
	 * return the NodeBuilder for the final step;
	 * 
	 * for non-final step, this should be null;
	 * 
	 * @return
	 */
	NodeBuilder<? extends P,?> getNodeBuilder();
	
	/**
	 * set the host process manager of this {@link ProcessStepManager};
	 * also invoke all next step's {@link ProcessStepManager}'s setHostProcessManager(...) method with the same manager object;
	 * 
	 * must be invoked before this {@link ProcessStepManager} is used;
	 * 
	 * note that ProcessStepController of multiple VisProjectDBContext can shared the same singleton instance of the {@link ProcessStepManager}
	 * @param manager
	 */
	void setHostProcessManager(ProcessPipelineMainManager<P> manager);
	
	
	String getDescription();
	
	
	void addNextStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step);
	
	void addPreviousStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step);
	
	void finishInitialization();
	/**
	 * 
	 * @return
	 */
	List<ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>>> getPossibleNextStepList();
	
	/**
	 * 
	 * @return
	 */
	List<ProcessStepManager<P, ? extends ProcessStepSettings,  ? extends ProcessStepController<P,?>>> getPossiblePreviousStepList();
	
	
	/**
	 * return the index of the ProcessStepController of previous step of this one on the current run of the process manager;
	 * facilitate to trace back to upstream steps for steps with multiple possible previous steps;
	 * @return
	 */
	Integer getSelectedPreviousStepIndex();
	
	
	/**
	 * set the selected previous step index of this step with the given previous step
	 * @param index
	 */
	void setSelectedPreviousStepIndex(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> processStep);
	
	/**
	 * check the current settings in {@link #getController()} and return the index of the corresponding next step;
	 * 
	 * implemented by each final subtype;
	 * 
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	Integer getCurrentlySelectedNextStepIndex() throws IOException, SQLException;
	
	/**
	 * update and store the given new index for next step;
	 * 
	 * if the index is different from the previous one and the previous one is not null (thus, settings has changed), 
	 * 	1. need to first invoke the reset() method of the previously selected next step;
	 * 	2. then update the index;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	void updateSelectedNextStepIndex(Integer newIndex) throws IOException, SQLException;
	
	
	
	/**
	 * get the default settings of this step
	 * @return
	 */
	T getDefaultSettings();
	
	
	/**
	 * return the currently assigned {@link ProcessStepSettings} of type T of this step;
	 * if null, return {@link #getDefaultSettings()}
	 * @return
	 */
	T getCurrentAssignedSettings();

	
	
	/**
	 * save the current settings on the UI of this step;
	 * implemented by each final subtype
	 * @throws SQLException 
	 * @throws IOException 
	 */
	void saveSettings() throws SQLException, IOException;
	
	/**
	 * return the action to take when the close button of the process pipeline main window is clicked and this step is the current step shown;
	 * 
	 * 1. for most cases, only need to send pop up message that all actions have been done so far will be lost after the window is closed;
	 * 		this is implemented in {@link AbstractProcessStepManager}
	 * 
	 * 2. for cases where extra actions needs to be done, override the method in the specific subclass of that step;
	 * 
	 * note that this action should normally be consistent with the implementation of {@link #cancel()} method;
	 * 
	 * @return
	 */
	Runnable getPipelineMainWindowCloseRequestEventHandler();
	/**
	 * reset the settings of current step;
	 * 1. set the ProcessStepSettings to default;
	 * 2. invoke the controller's {@link ProcessStepController#setStepSettings(ProcessStepSettings)} with the default settings
	 * 3. if {@link #getCurrentlySelectedNextStepIndex()} is not null
	 * 		1. set the field to null
	 * 		2. invoke reset() method of the selected next step;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void reset() throws IOException, SQLException;
	
	/**
	 * finish the process;
	 * only applicable for final steps with {@link #isFinalStep()} returning true;
	 * 
	 * return true if success, false otherwise;
	 * 
	 * note that the exception thrown inside the {@link CallableStatusTracker2} after the process is submitted to the ExecutiveService are not directly caught here;
	 * 		why this is so is actually due to the basic feature of ExecutiveService!!!!!!
	 * 
	 * 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws Throwable 
	 */
	boolean finish() throws SQLException, IOException, Throwable;
	
	
	/**
	 * go to the next step based on current settings;
	 * 
	 * only applicable for non-final steps;
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	void next() throws IOException, SQLException;

	/**
	 * go back to previous step of this step;
	 * 
	 * only applicable for non-root steps;
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void back() throws IOException, SQLException;
	
	/**
	 * cancel the current process and abandon all the created data
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void cancel() throws IOException, SQLException;
	
}
