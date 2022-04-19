package dependencygraph.dos.integrated;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class IntegratedDOSGraphViewerController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/dos/integrated/IntegratedDOSGraphViewer.fxml";
	
	///////////////////////////////////
	private IntegratedDOSGraphViewerManager manager;
	
	void setManager(IntegratedDOSGraphViewerManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public IntegratedDOSGraphViewerManager getManager() {
		return manager;
	}
	
	
	public Parent getRootNodeContainer() {
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
