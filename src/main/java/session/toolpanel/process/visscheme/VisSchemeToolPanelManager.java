package session.toolpanel.process.visscheme;

import context.scheme.VisScheme;
import core.pipeline.visscheme.VisSchemeProcessMainManager;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class VisSchemeToolPanelManager extends AbstractProcessToolPanelManager<VisScheme,VisSchemeToolPanelController,VisSchemeProcessMainManager>{
	
	public VisSchemeToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, VisSchemeToolPanelController.FXML_FILE_DIR, new VisSchemeProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
