package session.toolpanel.process.cfg;

import core.pipeline.cfg.CompositionFunctionGroupProcessMainManager;
import function.group.CompositionFunctionGroup;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class CompositionFunctionGroupToolPanelManager extends AbstractProcessToolPanelManager<CompositionFunctionGroup,CompositionFunctionGroupToolPanelController,CompositionFunctionGroupProcessMainManager>{
	
	public CompositionFunctionGroupToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, CompositionFunctionGroupToolPanelController.FXML_FILE_DIR, new CompositionFunctionGroupProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
