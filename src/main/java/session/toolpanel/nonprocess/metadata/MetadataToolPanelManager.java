package session.toolpanel.nonprocess.metadata;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import session.VFSessionManager;

public class MetadataToolPanelManager {
	private final VFSessionManager sessionManager;
	
	////
	private MetadataToolPanelController controller;
	private Parent rootNode;
	/**
	 * constructor
	 * @param sessionManager
	 */
	public MetadataToolPanelManager(VFSessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	public Parent getRootNode() throws IOException {
		if(this.rootNode==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(MetadataToolPanelController.FXML_FILE_DIR_STRING));
			
			this.rootNode = loader.load();
			
			this.controller = (MetadataToolPanelController)loader.getController();
			this.controller.setManager(this);
		}
		
		return this.rootNode;
	}
	
	public MetadataToolPanelController getController() throws IOException {
		if(this.controller == null) {
			this.getRootNode();
		}
		
		return this.controller;
	}

	
	public VFSessionManager getSessionManager() {
		return sessionManager;
	}
	
	
}
