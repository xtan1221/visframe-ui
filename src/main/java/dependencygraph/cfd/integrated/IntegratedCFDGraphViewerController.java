package dependencygraph.cfd.integrated;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class IntegratedCFDGraphViewerController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/cfd/integrated/IntegratedCFDGraphViewer.fxml";
	
	///////////////////////////////////
	private IntegratedCFDGraphViewerManager manager;
	
	void setManager(IntegratedCFDGraphViewerManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public IntegratedCFDGraphViewerManager getManager() {
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
