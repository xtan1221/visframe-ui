package dependencygraph.vccl.copynumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Objects;

import dependency.vccl.utils.NodeCopy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import utils.FXUtils;

public class DAGNodeCopyNumberAssignmentController<V,E> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vccl/copynumber/DAGNodeCopyNumberAssignment.fxml";
	
	///////////////////////////////////
	private DAGNodeCopyNumberAssignmentManager<V,E> manager;
	
	void setManager(DAGNodeCopyNumberAssignmentManager<V,E> manager) {
		this.manager = manager;
	}
	
	
	/**
	 * @return the manager
	 */
	public DAGNodeCopyNumberAssignmentManager<V, E> getManager() {
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
	
	public VBox getNodeNotesAssignmentVBox() {
		return this.nodeNotesAssignmentVBox;
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
	
	/**
	 * set the UI modifiable
	 * @param modifiable
	 */
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
	
	@FXML
	private VBox nodeNotesAssignmentVBox;
	
	/**
	 * set all copy number default status (0) and save; cannot restore to previous status afterwards;
	 * 
	 * need to trigger the {@link DAGNodeCopyNumberAssignmentManager#getAfterChangeMadeAction()} IF {@link DAGNodeCopyNumberAssignmentManager#getMostRecentlySavedAssignmentFinished()} is true;
	 * 
	 * ==============
	 * @param event
	 */
	// Event Listener on Button[#resetButton].onAction
	@FXML
	public void clearAllButtonOnAction(ActionEvent event) {
		boolean toTriggerAfterChangeAction = false;
		
		if(this.getManager().getMostRecentlySavedAssignmentFinished()!=null && this.getManager().getMostRecentlySavedAssignmentFinished()) {
			//change from a saved finished assignment to default assignment with all copy number being 0;
			//pop up warning and react accordingly
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Warning!");
			alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK){
				toTriggerAfterChangeAction = true;
			}else {//action denied, do nothing and return;
				return;
			}
		}
		
		//set all copy number to 0
		Map<V, Map<Integer, NodeCopy<V>>> nodeCopyIndexNodeCopyMap = new HashMap<>();
		
		this.getManager().getNodeManagerMap().keySet().forEach(v->{
			nodeCopyIndexNodeCopyMap.put(v, new HashMap<>());
		});
		
		this.getManager().setNodeCopyIndexNodeCopyMap(nodeCopyIndexNodeCopyMap);//set copy number to 0
		
		//save the resulted default assignment
		this.getManager().setMostRecentlySavedNodeCopyIndexNodeCopyMap(this.getManager().getCurrentNodeCopyIndexNodeCopyMap());
		this.getManager().setMostRecentlySavedAssignmentFinished(false);
		
		//update button status
		this.disableFinishAndCancelButton();
		
		
		//invoke the {@link DAGNodeCopyNumberAssignmentManager#getAfterChangeMadeAction()} if toTriggerAfterChangeAction is true
		if(toTriggerAfterChangeAction)
			if(this.getManager().getAfterChangeMadeAction()!=null)
				this.getManager().getAfterChangeMadeAction().run();
		
	}
	
	
	/**
	 * cancel any changes since the start of current session, restore all copy number to the previous one saved at the beginning of this session;
	 * 
	 * @param event
	 */
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		this.getManager().setNodeCopyIndexNodeCopyMap(this.getManager().getMostRecentlySavedNodeCopyIndexNodeCopyMap());
		//
		this.disableFinishAndCancelButton();
	}
	
	
	/**
	 * try to save the current copy numbers as if it is a finished assignment
	 * will only succeed if {@link DAGNodeCopyNumberAssignmentManager#currentCopyNumberAssignmentIsFinishable()} returns true, show alert otherwise;
	 * 
	 * @param event
	 */
	@FXML
	public void finishButtonOnAction(ActionEvent event) {
		if(!this.getManager().currentCopyNumberAssignmentIsFinishable()) {
			AlertUtils.popAlert("ERROR", "copy number assignment is not finishable!");
			return;
		}else {
			Map<V,Map<Integer,NodeCopy<V>>>  currentNodeCopyIndexNodeCopyMap = this.getManager().getCurrentNodeCopyIndexNodeCopyMap();
			if(!Objects.equal(currentNodeCopyIndexNodeCopyMap,this.getManager().getMostRecentlySavedNodeCopyIndexNodeCopyMap())) {
				//current copy number assignment is different from most recently saved one;
				//pop up warning and react accordingly to the response
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Warning!");
				alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//save the currently finished assignment to override the previously saved one;
					this.getManager().setMostRecentlySavedNodeCopyIndexNodeCopyMap(currentNodeCopyIndexNodeCopyMap);
					this.getManager().setMostRecentlySavedAssignmentFinished(true);
					
					//invoke after change made action
					if(this.getManager().getAfterChangeMadeAction()!=null)
						this.getManager().getAfterChangeMadeAction().run();
					
				} else {//action denied, do nothing and return;
				    return;
				}
			}else {
				//
				AlertUtils.popAlert("Warning", "no change has been made since last time saved!");
			}
			
			
			this.disableFinishAndCancelButton();
		}
	}
}
