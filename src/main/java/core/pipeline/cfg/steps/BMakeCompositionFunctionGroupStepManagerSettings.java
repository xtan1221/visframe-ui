package core.pipeline.cfg.steps;

import core.pipeline.ProcessStepSettings;
import function.group.CompositionFunctionGroup;

public class BMakeCompositionFunctionGroupStepManagerSettings implements ProcessStepSettings{
	private final CompositionFunctionGroup cfg;
	
	BMakeCompositionFunctionGroupStepManagerSettings(CompositionFunctionGroup cfg){
		this.cfg = cfg;
	}

	/**
	 * @return the cfg
	 */
	public CompositionFunctionGroup getCfg() {
		return cfg;
	}
	
}
