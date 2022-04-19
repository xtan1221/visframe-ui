package dependencygraph;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DAGMainController<V,E> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/DAGMain.fxml";
	
	///////////////////////////////////////////////
	private DAGMainManager<V,E> manager;
	
	/**
	 * @return the manager
	 */
	public DAGMainManager<V, E> getManager() {
		return manager;
	}

	void setManager(DAGMainManager<V,E> manager) {
		this.manager = manager;
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
