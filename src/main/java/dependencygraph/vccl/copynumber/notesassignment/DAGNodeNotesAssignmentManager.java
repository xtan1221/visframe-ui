package dependencygraph.vccl.copynumber.notesassignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import dependency.vccl.utils.NodeCopy;
import dependencygraph.vccl.copynumber.node.DAGNodeManager;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author tanxu
 *
 * @param <V>
 * @param <E>
 */
public class DAGNodeNotesAssignmentManager<V,E>{
	private final DAGNodeManager<V,E> hostDAGNodeManager;
	
	///////////////////////////
	private DAGNodeNotesAssignmentController<V,E> controller;
	
	////////////////////
	/**
	 * the current assigned copy number to the corresponding node;
	 */
	private int copyNumber;
	/**
	 * copy index from 1 to n
	 */
	private Map<Integer, DAGNodeCopyNotesAssignmentManager<V,E>> copyIndexDAGNodeCopyNotesAssignmentManagerMap;

	/**
	 * 
	 * @param hostDAGNodeCopyNumberAssignmentManager
	 * @param node
	 */
	public DAGNodeNotesAssignmentManager(
			DAGNodeManager<V,E> hostDAGNodeManager){
		
		
		this.hostDAGNodeManager = hostDAGNodeManager;
		this.copyNumber = 0;
		this.copyIndexDAGNodeCopyNotesAssignmentManagerMap = new HashMap<>();
	}
	
	/**
	 * set the {@link #copyIndexDAGNodeCopyNotesAssignmentManagerMap} according to the given copyNumber
	 * @param copyNumber
	 */
	public void updateCopyNumber(int copyNumber) {
		if(this.copyNumber == copyNumber)
			return;
		
		if(copyNumber<this.copyNumber) {
			Set<Integer> indexSet = new LinkedHashSet<>();
			indexSet.addAll(this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.keySet());
			
			indexSet.forEach(index->{
				if(index>copyNumber) {
					//remove from UI
					this.getController().nodeCopyNotesAssignmentVBox.getChildren().remove(this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.get(index).getController().getRootNodeContainer());
					//remove from ...
					this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.remove(index);
				}
			});
			
			this.copyNumber = copyNumber;
			return;
		}
		
		if(copyNumber>this.copyNumber) {
			for(int i=1;i<=copyNumber;i++) {
				if(!this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.containsKey(i)) {
					DAGNodeCopyNotesAssignmentManager<V,E> manager = new DAGNodeCopyNotesAssignmentManager<>(this,i);
					this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.put(i, manager);
					
					this.getController().nodeCopyNotesAssignmentVBox.getChildren().add(manager.getController().getRootNodeContainer());
				}
			}
			
			this.copyNumber = copyNumber;
		}
		
	}
	/**
	 * 
	 * @param map
	 */
	public void setCopyIndexNodeCopyMap(Map<Integer,NodeCopy<V>> map) {
		this.getCopyIndexDAGNodeCopyNotesAssignmentManagerMap().forEach((index, manager)->{
			manager.setNotesText(map.get(index).getNotes().getNotesString());
		});
	}
	
	//////////////////////////
	/**
	 * set modifiable
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		this.getController().setModifiable(modifiable);
		this.copyIndexDAGNodeCopyNotesAssignmentManagerMap.forEach((index, manager)->{
			manager.setModifiable(modifiable);
		});
	}
	
	/////////////////////////////
	public DAGNodeNotesAssignmentController<V,E> getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(DAGNodeNotesAssignmentController.FXML_FILE_DIR_STRING));
			
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
	 * @return the hostDAGNodeManager
	 */
	public DAGNodeManager<V,E> getHostDAGNodeManager() {
		return hostDAGNodeManager;
	}
	
	/**
	 * @return the copyIndexDAGNodeCopyNotesAssignmentManagerMap
	 */
	public Map<Integer, DAGNodeCopyNotesAssignmentManager<V, E>> getCopyIndexDAGNodeCopyNotesAssignmentManagerMap() {
		return copyIndexDAGNodeCopyNotesAssignmentManagerMap;
	}

}
