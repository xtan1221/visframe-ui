package dependencygraph.vccl.copynumber.notesassignment;

import java.io.IOException;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 */
public class DAGNodeCopyNotesAssignmentManager<V,E>{
	private final DAGNodeNotesAssignmentManager<V,E> hostDAGNodeNotesAssignmentManager;
	private final int copyIndex;
	///////////////////////////
	private DAGNodeCopyNotesAssignmentController<V,E> controller;
	
	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public DAGNodeCopyNotesAssignmentManager(
			DAGNodeNotesAssignmentManager<V,E> hostDAGNodeNotesAssignmentManager,
			int copyIndex
			){
		//
		this.hostDAGNodeNotesAssignmentManager = hostDAGNodeNotesAssignmentManager;
		this.copyIndex = copyIndex;
	}
	
	public void setNotesText(String notesText) {
		this.getController().getNodeCopyNotesTextArea().setText(notesText);
	}
	
	/**
	 * set modifiable
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
	}
	
	/////////////////////////////
	public DAGNodeCopyNotesAssignmentController<V,E> getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeCopyNotesAssignmentController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}


	/**
	 * @return the hostDAGNodeNotesAssignmentManager
	 */
	public DAGNodeNotesAssignmentManager<V,E> getHostDAGNodeNotesAssignmentManager() {
		return hostDAGNodeNotesAssignmentManager;
	}


	/**
	 * @return the copyIndex
	 */
	public int getCopyIndex() {
		return copyIndex;
	}
	
}
