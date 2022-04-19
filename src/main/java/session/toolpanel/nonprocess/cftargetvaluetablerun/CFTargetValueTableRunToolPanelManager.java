package session.toolpanel.nonprocess.cftargetvaluetablerun;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import session.VFSessionManager;

public class CFTargetValueTableRunToolPanelManager {
	private final VFSessionManager sessionManager;
	
	////
	private CFTargetValueTableRunToolPanelController controller;
	private Parent rootNode;
	/**
	 * constructor
	 * @param sessionManager
	 */
	public CFTargetValueTableRunToolPanelManager(VFSessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	public Parent getRootNode() throws IOException {
		if(this.rootNode==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(CFTargetValueTableRunToolPanelController.FXML_FILE_DIR_STRING));
			
			this.rootNode = loader.load();
			
			this.controller = (CFTargetValueTableRunToolPanelController)loader.getController();
			this.controller.setManager(this);
		}
		
		return this.rootNode;
	}
	
	public CFTargetValueTableRunToolPanelController getController() throws IOException {
		if(this.controller == null) {
			this.getRootNode();
		}
		
		return this.controller;
	}

	
	public VFSessionManager getSessionManager() {
		return sessionManager;
	}
	
	
}
