package session.toolpanel.process;

import basic.process.ProcessType;

public abstract class AbstractProcessToolPanelController<T extends ProcessType> implements ProcessToolPanelController<T> {
	private ProcessToolPanelManager<T,?,?> manager;
	
	@Override
	public void setManager(ProcessToolPanelManager<T,?,?> manager) {
		this.manager = manager;
	}
	
	public ProcessToolPanelManager<T,?,?> getManager(){
		return this.manager;
	}
}
