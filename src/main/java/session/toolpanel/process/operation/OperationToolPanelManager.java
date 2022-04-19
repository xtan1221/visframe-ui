package session.toolpanel.process.operation;

import core.pipeline.operation.OperationProcessMainManager;
import operation.Operation;
import session.VFSessionManager;
import session.toolpanel.process.AbstractProcessToolPanelManager;

public class OperationToolPanelManager extends AbstractProcessToolPanelManager<Operation,OperationToolPanelController,OperationProcessMainManager>{
	
	public OperationToolPanelManager(VFSessionManager sessionManager) {
		super(sessionManager, OperationToolPanelController.FXML_FILE_DIR, new OperationProcessMainManager());
		// TODO Auto-generated constructor stub
	}
	
}
