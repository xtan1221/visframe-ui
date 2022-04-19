package dependencygraph.vccl.nodeselection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.FXUtils;

public class CopyNodeSelectionController<V,E> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/nodeselection/CopyNodeSelection.fxml";
	
	///////////////////////////////////
	private CopyNodeSelectionManager<V, E> manager;
	
	void setManager(CopyNodeSelectionManager<V, E> manager) {
		this.manager = manager;
		
	}
	
	/**
	 * @return the manager
	 */
	public CopyNodeSelectionManager<V, E> getManager() {
		return manager;
	}
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerVBox;
	}
	
	/**
	 * return the AnchorPane on which the graph is laid out
	 * @return
	 */
	public AnchorPane getGraphLayoutAnchorPane() {
		return this.layoutAnchorPane;
	}
	

	/**
	 * 
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.controlButtonHBox, !modifiable);
	}
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private HBox controlButtonHBox;
	@FXML
	private Button clearAllButton;
	@FXML
	private Button selectAllButton;
	@FXML
	private AnchorPane layoutAnchorPane;

	// Event Listener on Button[#clearAllButton].onAction
	@FXML
	public void clearAllButtonOnAction(ActionEvent event) {
		this.getManager().clearAll();
	}
	
	// Event Listener on Button[#selectAllButton].onAction
	@FXML
	public void selectAllButtonOnAction(ActionEvent event) {
		this.getManager().selectAll();
	}
}
