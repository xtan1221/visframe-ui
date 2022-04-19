package session.toolpanel.nonprocess.indepenentfreeinputvariabletype;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import session.VFSessionManager;

public class IndependentFreeInputVariableTypeToolPanelManager {
	private final VFSessionManager sessionManager;
	
	////
	private IndependentFreeInputVariableTypeToolPanelController controller;
	
	private Parent rootNode;
	/**
	 * constructor
	 * @param sessionManager
	 */
	public IndependentFreeInputVariableTypeToolPanelManager(VFSessionManager sessionManager){
		this.sessionManager = sessionManager;
	}
	
	public Parent getRootNode() throws IOException {
		if(this.rootNode==null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(IndependentFreeInputVariableTypeToolPanelController.FXML_FILE_DIR_STRING));
			
			this.rootNode = loader.load();
			
			this.controller = (IndependentFreeInputVariableTypeToolPanelController)loader.getController();
			this.controller.setManager(this);
		}
		
		return this.rootNode;
	}
	
	public IndependentFreeInputVariableTypeToolPanelController getController() throws IOException {
		if(this.controller == null) {
			this.getRootNode();
		}
		
		return this.controller;
	}

	
	public VFSessionManager getSessionManager() {
		return sessionManager;
	}
	
	
}
