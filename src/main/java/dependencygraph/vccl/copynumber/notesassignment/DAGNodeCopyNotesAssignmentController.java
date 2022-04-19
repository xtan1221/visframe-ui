package dependencygraph.vccl.copynumber.notesassignment;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DAGNodeCopyNotesAssignmentController<V,E>{
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/copynumber/notesassignment/DAGNodeCopyNotesAssignment.fxml";
	
	/////////////////////////////
	private DAGNodeCopyNotesAssignmentManager<V,E> manager;

	void setManager(DAGNodeCopyNotesAssignmentManager<V,E> manager) {
		this.manager = manager;
		
		this.copyIndexTextField.setText(Integer.toString(this.getManager().getCopyIndex()));
		
		//whenever the notes text is changed, activate the save and cancel button
		this.nodeCopyNotesTextArea.textProperty().addListener((obs,ov,nv)->{
			this.getManager().getHostDAGNodeNotesAssignmentManager().getHostDAGNodeManager().getHostDAGNodeCopyNumberAssignmentManager().getController().enableFinishAndCancelButton();
		});
	}
	
	/**
	 * @return the manager
	 */
	public DAGNodeCopyNotesAssignmentManager<V, E> getManager() {
		return manager;
	}
	
	public Pane getRootNodeContainer() {
		return this.rootContainerVBox;
	}
	
	public TextArea getNodeCopyNotesTextArea() {
		return this.nodeCopyNotesTextArea;
	}
	
	
	void setModifiable(boolean modifiable) {
		this.nodeCopyNotesTextArea.setEditable(!modifiable);
	}
	
	////////////////////
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField copyIndexTextField;
	@FXML
	private TextArea nodeCopyNotesTextArea;
}
