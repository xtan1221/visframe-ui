package core.pipeline;

import basic.process.ProcessType;

public abstract class AbstractProcessStepController<P extends ProcessType, T extends ProcessStepSettings> implements ProcessStepController<P, T> {
	protected ProcessStepManager<P, T, ?> manager;
	
	@Override
	public void setManager(ProcessStepManager<P, T, ?> manager) {
		this.manager = manager;
	}
	
	@Override
	public ProcessStepManager<P, T, ?> getManager() {
		return this.manager;
	}
}
