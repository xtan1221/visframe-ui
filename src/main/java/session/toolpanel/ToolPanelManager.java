package session.toolpanel;

import java.io.IOException;

import javafx.scene.Parent;
import session.VFSessionManager;

public interface ToolPanelManager<T, C extends ToolPanelController<T>> {
	VFSessionManager getSessionManager();
	
	C getController() throws IOException;
	
	Parent getRootNode() throws IOException;
	
	/**
	 * return the FXML file dir string for the tool pane root node;
	 * @return
	 */
	String getFXMLFileDirString();
}
