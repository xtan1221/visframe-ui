package core.pipeline;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import basic.process.ProcessType;
import core.builder.NodeBuilder;
import core.pipeline.visscheme.appliedarchive.steps.BMakeVisSchemeAppliedArchiveStepManager;
import exception.VisframeException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class AbstractProcessStepManager<P extends ProcessType, T extends ProcessStepSettings, C extends ProcessStepController<P,T>> implements ProcessStepManager<P, T, C> {
	private final String FXMLFileDirString;
	private final String description;
	private final T defaultSettings;
	private final Class<P> type;
	
	/**
	 *  height of the process main window when showing this step;
	 * applicable for steps with large size UI such as the 
	 * {@link BMakeVisSchemeAppliedArchiveStepManager}
	 */
	private final Double prefHeight;
	private final Double prefWidth;
	////////////////////////
	private List<ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>>> possibleNextStepList;
	private List<ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>>> possiblePreviousStepList;
	protected boolean initialized;
	
	private ProcessPipelineMainManager<P> processMainManager;
	
	protected C controller; 
//	protected Parent stepRootNode;
	
	protected Integer selectedNextStepIndex;
	protected Integer selectedPreviousStepIndex;
	
	protected T currentlyAssignedSettings;
	
	
	/**
	 * constructor
	 * @param possibleNextStepControllerSet
	 * @param previousStepController
	 */
	protected AbstractProcessStepManager(
			Class<P> type,
			String FXMLFileDirString,
			String description,
			T defaultSettings,
			Double prefHeight,
			Double prefWidth
			){
		this.possibleNextStepList = new ArrayList<>();
		this.possiblePreviousStepList = new ArrayList<>();
		
		this.type = type;
		this.initialized = false;
		this.FXMLFileDirString = FXMLFileDirString;
		this.description = description;
		this.defaultSettings = defaultSettings;
		this.prefHeight = prefHeight;
		this.prefWidth = prefWidth;
	}
	
	/**
	 * overload constructor that does not set non-null pref height and width;
	 * @param type
	 * @param FXMLFileDirString
	 * @param description
	 * @param defaultSettings
	 */
	protected AbstractProcessStepManager(
			Class<P> type,
			String FXMLFileDirString,
			String description,
			T defaultSettings
			){
		this(type, FXMLFileDirString, description, defaultSettings, null, null);
	}
	
	
	@Override
	public 	Double getPrefHeight() {
		return this.prefHeight;
	}
	
	@Override
	public 	Double getPrefWidth() {
		return this.prefWidth;
	}
	
	@Override
	public Class<P> getType(){
		return this.type;
	}
	
	
	@Override
	public void addNextStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step) {
		if(this.initialized) {
			throw new VisframeException("already initialized, cannot add more");
		}
		
		if(this.possibleNextStepList.contains(step)) {
			throw new IllegalArgumentException("given step is already existing");
		}
		this.possibleNextStepList.add(step);
	}
	
	@Override
	public void addPreviousStep(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step) {
		if(this.initialized) {
			throw new VisframeException("already initialized, cannot add more");
		}
		if(this.possiblePreviousStepList.contains(step)) {
			throw new IllegalArgumentException("given step is already existing");
		}
		this.possiblePreviousStepList.add(step);
	}
	
	@Override
	public void finishInitialization() {
		this.initialized = true;
	}
	//////////////////////////////
	/**
	 * default minimal implementation;
	 * 
	 * if extra functionalities needed, override in final subtype class; see {@link DBuildRecordFileFormatFromScratchStepManager}
	 * @throws SQLException 
	 */
	@Override
	public Parent getStepRootNode() throws IOException, SQLException {
		return this.getController().getRootNode();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public C getController() throws IOException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
	    	//this must be invoked before the getController() method!!!!!!!
//		    this.stepRootNode = loader.load();
			loader.load(); //load() method must be invoked before the controller is retrieved;
		    this.controller = (C)loader.getController();
		    this.controller.setManager(this);
		}
		return this.controller;
	}
	
	
	@Override
	public String getFXMLFileDirString() {
		return this.FXMLFileDirString;
	}
	
	@Override
	public ProcessPipelineMainManager<P> getProcessMainManager() {
		return processMainManager;
	}
	
	@Override
	public List<ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>>> getPossibleNextStepList() {
		return possibleNextStepList;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	
	@Override
	public List<ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>>> getPossiblePreviousStepList() {
		return possiblePreviousStepList;
	}
	
	@Override
	public T getDefaultSettings() {
		return defaultSettings;
	}
	
	@Override
	public void setHostProcessManager(ProcessPipelineMainManager<P> manager) {
		this.processMainManager = manager;
		for(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> stepController:this.getPossibleNextStepList()){
			stepController.setHostProcessManager(manager);
		}
	}
	
	@Override
	public void updateSelectedNextStepIndex(Integer newIndex) throws IOException, SQLException {
		//changed
		if(this.selectedNextStepIndex!=null) {
			if(newIndex!=this.selectedNextStepIndex) {
				//reset the previous selected next step before set to new one;
				this.getPossibleNextStepList().get(this.selectedNextStepIndex).reset();
				this.selectedNextStepIndex = newIndex;
			}
		}else {
			this.selectedNextStepIndex = newIndex;
		}
	}
	
	
	
	@Override
	public Integer getSelectedPreviousStepIndex() {
		return this.selectedPreviousStepIndex;
	}
	
	
	@Override
	public void setSelectedPreviousStepIndex(ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> step) {
		this.selectedPreviousStepIndex = this.getPossiblePreviousStepList().indexOf(step);
	}
	
	@Override
	public T getCurrentAssignedSettings() {
		if(this.currentlyAssignedSettings==null) {
			return this.getDefaultSettings();
		}else {
			return this.currentlyAssignedSettings;
		}
	}
	
	@Override
	public void saveSettings() throws SQLException, IOException {
		this.currentlyAssignedSettings = this.controller.getStepSettings();
	}
	
	@Override
	public Runnable getPipelineMainWindowCloseRequestEventHandler() {
		return ()->{
			//do nothing
		};
	}
	
	@Override
	public void next() throws IOException, SQLException {
		if(!this.getController().validateSettingsToGoToNextStep()) {
			return;
		}
		
		this.saveSettings();
		
		Integer selectedIndex = this.getCurrentlySelectedNextStepIndex();
		
		this.updateSelectedNextStepIndex(selectedIndex);
		
		ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> nextStep = 
				this.getPossibleNextStepList().get(selectedIndex);
		
		nextStep.setSelectedPreviousStepIndex(this);
		
		this.getProcessMainManager().setCurrentlyOpenedStep(nextStep);
	}
	
	@Override
	public void back() throws IOException, SQLException {
//		this.saveSettings();
		
		ProcessStepManager<P, ? extends ProcessStepSettings, ? extends ProcessStepController<P,?>> previousStep = 
				this.getPossiblePreviousStepList().get(this.getSelectedPreviousStepIndex());
		
		this.getProcessMainManager().setCurrentlyOpenedStep(previousStep);
	}
	
	@Override
	public void reset() throws IOException, SQLException {
		this.currentlyAssignedSettings = this.getDefaultSettings();
		
		this.getController().setStepSettings(this.getCurrentAssignedSettings());
	}
	
	@Override
	public void cancel() throws IOException, SQLException {
		this.getProcessMainManager().closeWindow();
	}
	
	/**
	 * override this if last step;
	 */
	@Override
	public NodeBuilder<? extends P, ?> getNodeBuilder() {
		throw new UnsupportedOperationException("unimplemented");
	}
}
