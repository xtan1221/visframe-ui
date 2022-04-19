package dependencygraph.dos;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SimpleDOSGraphViewerController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/dos/SimpleDOSGraphViewer.fxml";
	
	///////////////////////////////////
	private SimpleDOSGraphViewerManager manager;
	
	void setManager(SimpleDOSGraphViewerManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public SimpleDOSGraphViewerManager getManager() {
		return manager;
	}
	
	
	public Parent getRootContainerNode() {
		return this.rootContainerVBox;
	}
	
	/**
	 * return the AnchorPane on which the graph is laid out
	 * @return
	 */
	AnchorPane getGraphLayoutAnchorPane() {
		return this.layoutAnchorPane;
	}
	
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private AnchorPane layoutAnchorPane;

	
}
