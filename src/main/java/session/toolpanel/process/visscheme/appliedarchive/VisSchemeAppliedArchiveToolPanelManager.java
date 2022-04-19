package session.toolpanel.process.visscheme.appliedarchive;

import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.visscheme.appliedarchive.VisSchemeAppliedArchiveProcessMainManager;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class VisSchemeAppliedArchiveToolPanelManager extends AbstractProcessToolPanelManager<VisSchemeAppliedArchive, VisSchemeAppliedArchiveToolPanelController, VisSchemeAppliedArchiveProcessMainManager>{
	
	public VisSchemeAppliedArchiveToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, VisSchemeAppliedArchiveToolPanelController.FXML_FILE_DIR, new VisSchemeAppliedArchiveProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
