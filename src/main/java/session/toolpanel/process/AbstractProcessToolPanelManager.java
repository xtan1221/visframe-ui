package session.toolpanel.process;

import java.io.IOException;

import basic.process.ProcessType;
import core.pipeline.ProcessPipelineMainManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import session.VFSessionManager;

public abstract class AbstractProcessToolPanelManager<T extends ProcessType, C extends ProcessToolPanelController<T>, P extends ProcessPipelineMainManager<T>> 
implements ProcessToolPanelManager<T,C,P> {
	private final VFSessionManager sessionManager;
	private final String FXMLFileDirString;
	private final P processPipelineMainManager;
	
	private C controller;
	
	
	/**
	 * 
	 * @param sessionManager
	 * @param FXMLFileDirString
	 * @param processPipelineMainManager
	 */
	protected AbstractProcessToolPanelManager(VFSessionManager sessionManager, String FXMLFileDirString, P processPipelineMainManager){
		this.sessionManager = sessionManager;
		this.FXMLFileDirString = FXMLFileDirString;
		this.processPipelineMainManager = processPipelineMainManager;
		
		//set the tool panel manager
		processPipelineMainManager.setProcessToolPanelManager(this);
	}
	
	
	@Override
	public VFSessionManager getSessionManager() {
		return sessionManager;
	}
	@Override
	public String getFXMLFileDirString() {
		return this.FXMLFileDirString;
	}
	@Override
	public P getProcessMainManager(){
		return processPipelineMainManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public C getController() throws IOException {
		if(this.controller == null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(this.getFXMLFileDirString()));
			
			loader.load();
			
			this.controller = (C)loader.getController();
			this.controller.setManager(this);
		}
		
		return this.controller;
	}
	
	
	@Override
	public 
	Parent getRootNode() throws IOException {
		return this.getController().getRootContainerPane();
	}
	
	
	
	
	
}
