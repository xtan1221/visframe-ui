package session.toolpanel.process.visinstance.run;

import core.pipeline.visinstance.run.VisInstanceRunProcessMainManager;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;
import visinstance.run.VisInstanceRun;

public class VisInstanceRunToolPanelManager extends AbstractProcessToolPanelManager<VisInstanceRun,VisInstanceRunToolPanelController,VisInstanceRunProcessMainManager>{
	
	public VisInstanceRunToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, VisInstanceRunToolPanelController.FXML_FILE_DIR, new VisInstanceRunProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
