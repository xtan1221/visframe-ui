package session.toolpanel.process;

import java.io.IOException;

import basic.process.ProcessType;
import core.pipeline.ProcessPipelineMainManager;
import javafx.scene.Parent;
import session.VFSessionManager;

public interface ProcessToolPanelManager<T extends ProcessType, C extends ProcessToolPanelController<T>, P extends ProcessPipelineMainManager<T>> {
	VFSessionManager getSessionManager();
	
	C getController() throws IOException;
	
	Parent getRootNode() throws IOException;
	
	P getProcessMainManager();
	
	/**
	 * return the FXML file dir string for the tool pane root node;
	 * @return
	 */
	String getFXMLFileDirString();
}
