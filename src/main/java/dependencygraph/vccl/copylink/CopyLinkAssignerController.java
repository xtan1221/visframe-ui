package dependencygraph.vccl.copylink;

import java.util.Map;
import java.util.Optional;

import com.google.common.base.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import utils.FXUtils;

public class CopyLinkAssignerController<V,E> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/copylink/CopyLinkAssigner.fxml";
	
	///////////////////////////////////
	private CopyLinkAssignerManager<V, E> manager;
	
	void setManager(CopyLinkAssignerManager<V, E> manager) {
		this.manager = manager;
		
		//if there is only one node in the DAG, no copy link needs to be created;
		if(this.getManager().getDag().vertexSet().size()==1) {
			FXUtils.set2Disable(this.clearAllButton, true);
			FXUtils.set2Disable(this.cancelButton, true);
			
			FXUtils.set2Disable(this.finishButton, false);
		}
	}
	
	/**
	 * @return the manager
	 */
	public CopyLinkAssignerManager<V, E> getManager() {
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
	
	/**
	 * simply activate the save and cancel button;
	 * 
	 * invoked whenever a copy number is to be changed;
	 */
	public void enableFinishAndCancelButton() {
		FXUtils.set2Disable(this.finishButton, false);
		FXUtils.set2Disable(this.cancelButton, false);
	}
	
	public void disableFinishAndCancelButton() {
		FXUtils.set2Disable(this.finishButton, true);
		FXUtils.set2Disable(this.cancelButton, true);
	}
	
	
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.controlButtonHBox, !modifiable);
	}
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
		this.disableFinishAndCancelButton();
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private AnchorPane layoutAnchorPane;
	@FXML
	private HBox controlButtonHBox;
	@FXML
	private Button clearAllButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button finishButton;

	
	/**
	 * clear all links and save to default status (no link created yet), cannot restore to previous status afterwards;
	 * 
	 * need to trigger the {@link CopyLinkAssignerManager#getAfterChangeMadeAction()} IF {@link CopyLinkAssignerManager#getMostRecentlySavedCopyLinkAssignmentFinished()} is true;
	 * 
	 * @param event
	 */
	@FXML
	public void clearAllButtonOnAction(ActionEvent event) {
		boolean toTriggerAfterChangeAction = false;
		if(this.getManager().getMostRecentlySavedCopyLinkAssignmentFinished()!=null && this.getManager().getMostRecentlySavedCopyLinkAssignmentFinished()) {//current link assignment is finished, need to invoke the ...
			//change from a saved finished assignment to default status with no link assigned;
			//pop up warning and react accordingly
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Warning!");
			alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				toTriggerAfterChangeAction = true;
			} else {//action denied, do nothing and return;
			    return;
			}
		}
		
		//remove all copy links
		this.getManager().clearAllCopyLinks();
		
		//save the resulted empty assignment
		this.getManager().setMostRecentlySavedCopyLinkAssignment(this.getManager().getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap());
		this.getManager().setMostRecentlySavedCopyLinkAssignmentFinished(false);
		
		//update button status
		this.disableFinishAndCancelButton();
		
		//take action
		if(toTriggerAfterChangeAction)
			if(this.getManager().getAfterChangeMadeAction()!=null)
				this.getManager().getAfterChangeMadeAction().run();
		
		
		////////////////////////
		//set all node copy UI to SELECTABLE
		this.getManager().getDAGNodeManagerMap().forEach((node, manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, copyManager)->{
				copyManager.setStatus(DAGNodeCopyManager.Status.SELECTABLE);
			});
		});
		
	}
	
	/**
	 * cancel any changes since last time saved, and restore all links to the previous one saved;
	 * 
	 * @param event
	 */
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		
		//
		this.getManager().setLinks(this.getManager().getMostRecentlySavedCopyLinkAssignment());
		
		this.disableFinishAndCancelButton();
		
		//set all node copy UI to SELECTABLE
		this.getManager().getDAGNodeManagerMap().forEach((node, manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, copyManager)->{
				copyManager.setStatus(DAGNodeCopyManager.Status.SELECTABLE);
			});
		});
				
	}
	
	/**
	 * try to save the current links as if it is a finished assignment
	 * 
	 * will only succeed if current copy link assignment is finished, show alert otherwise;
	 * 
	 * @param event
	 */
	@FXML
	public void finishButtonOnAction(ActionEvent event) {
		if(!this.getManager().getDAGNodeCopyLinkAssigner().copyLinkGraphIsFullyBuilt()) {
			AlertUtils.popAlert("ERROR", "Copy links assignment is not finishable!");
			return;
		}else {//
			Map<V,Map<Integer,Map<V,Integer>>> currentLinkAssignment = this.getManager().getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap();
			if(!Objects.equal(currentLinkAssignment,this.getManager().getMostRecentlySavedCopyLinkAssignment())) {
				//currently finished link assignment is different from most recently saved one;
				//pop up warning and react accordingly to the response
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Warning!");
				alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//save the currently finished assignment to override the previously saved one;
					this.getManager().setMostRecentlySavedCopyLinkAssignment(currentLinkAssignment);
					this.getManager().setMostRecentlySavedCopyLinkAssignmentFinished(true);
					
					//invoke after change made action
					if(this.getManager().getAfterChangeMadeAction()!=null)
						this.getManager().getAfterChangeMadeAction().run();
					
				} else {//action denied, do nothing and return;
				    return;
				}
			}else {
				if(this.getManager().getDag().vertexSet().size()==1) {//there is only one node, no copy link need to be created
					this.getManager().setMostRecentlySavedCopyLinkAssignment(currentLinkAssignment);
					this.getManager().setMostRecentlySavedCopyLinkAssignmentFinished(true);
					
					//invoke after change made action
					if(this.getManager().getAfterChangeMadeAction()!=null)
						this.getManager().getAfterChangeMadeAction().run();
				}else {
					AlertUtils.popAlert("Warning", "no change has been made since last time saved!");
				}
					
			}
		}
		
		this.disableFinishAndCancelButton();
		
		//////////////////////////////
		//set all node copy UI to SELECTABLE
		this.getManager().getDAGNodeManagerMap().forEach((node, manager)->{
			manager.getCopyIndexNodeCopyManagerMap().forEach((copyIndex, copyManager)->{
				copyManager.setStatus(DAGNodeCopyManager.Status.SELECTABLE);
			});
		});
	}
//	
}
