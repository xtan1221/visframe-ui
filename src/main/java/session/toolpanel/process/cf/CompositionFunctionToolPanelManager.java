package session.toolpanel.process.cf;

import core.pipeline.cf.CompositionFunctionProcessMainManager;
import function.composition.CompositionFunction;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class CompositionFunctionToolPanelManager extends AbstractProcessToolPanelManager<CompositionFunction,CompositionFunctionToolPanelController,CompositionFunctionProcessMainManager>{
	
	public CompositionFunctionToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, CompositionFunctionToolPanelController.FXML_FILE_DIR, new CompositionFunctionProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
