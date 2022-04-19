package dependencygraph.vccl.copynumber.notesassignment;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class DAGNodeNotesAssignmentController<V,E>{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/copynumber/notesassignment/DAGNodeNotesAssignment.fxml";
	
	/////////////////////////////
	private DAGNodeNotesAssignmentManager<V,E> manager;

	void setManager(DAGNodeNotesAssignmentManager<V,E> manager) {
		this.manager = manager;
		
		this.rootContainerTitledPane.setText("node:".concat(this.getManager().getHostDAGNodeManager().getNodeInforString()));
	}
	
	/**
	 * @return the manager
	 */
	public DAGNodeNotesAssignmentManager<V, E> getManager() {
		return manager;
	}
	
	public Parent getRootNodeContainer() {
		return this.rootContainerTitledPane;
	}
	
	
	void setModifiable(boolean modifiable) {
		//do nothing
	}
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private TitledPane rootContainerTitledPane;
	@FXML
	VBox nodeCopyNotesAssignmentVBox;
}
