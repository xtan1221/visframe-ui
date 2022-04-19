package session.toolpanel.process.visinstance;

import core.pipeline.visinstance.VisInstanceProcessMainManager;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;
import visinstance.VisInstance;

public class VisInstanceToolPanelManager extends AbstractProcessToolPanelManager<VisInstance,VisInstanceToolPanelController,VisInstanceProcessMainManager>{
	
	public VisInstanceToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, VisInstanceToolPanelController.FXML_FILE_DIR, new VisInstanceProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
