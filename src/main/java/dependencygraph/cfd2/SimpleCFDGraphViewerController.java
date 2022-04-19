package dependencygraph.cfd2;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SimpleCFDGraphViewerController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/cfd2/SimpleCFDGraphViewer.fxml";
	
	///////////////////////////////////
	private SimpleCFDGraphViewerManager manager;
	
	void setManager(SimpleCFDGraphViewerManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public SimpleCFDGraphViewerManager getManager() {
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
