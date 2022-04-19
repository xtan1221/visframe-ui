package dependencygraph.solutionset;

import java.util.Optional;
import java.util.Set;

import com.google.common.base.Objects;

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

public class DAGSolutionSetSelectorController<V,E> {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/solutionset/DAGSolutionSetSelector.fxml";
	
	///////////////////////////////////
	private DAGSolutionSetSelectorManager<V, E> manager;
	
	void setManager(DAGSolutionSetSelectorManager<V, E> manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public DAGSolutionSetSelectorManager<V, E> getManager() {
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
	 * clear all selected nodes and set to default status (restart), cannot restore to previous status afterwards;
	 * 
	 * need to trigger the {@link DAGSolutionSetSelectorManager#getAfterChangeMadeAction()} IF {@link DAGSolutionSetSelectorManager#getMostRecentlySavedSelectedSetFinished()} is true;
	 * 
	 * @param event
	 */
	@FXML
	public void clearAllButtonOnAction(ActionEvent event) {
		boolean toTriggerAfterChangeAction = false;
		if(this.getManager().getMostRecentlySavedSelectedSetFinished()!=null && this.getManager().getMostRecentlySavedSelectedSetFinished()) {
			//change from a saved finished selection to default status with empty set;
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
		
		
		////clear all selection
		this.getManager().clearAllSelection();
		
		//save the resulted empty set
		this.getManager().setMostRecentlySavedSelectedSet(this.getManager().getCurrentlySelectedSet());
		this.getManager().setMostRecentlySavedSelectedSetFinished(false);
		
		//update button status
		this.disableFinishAndCancelButton();
		
		//take action
		if(toTriggerAfterChangeAction)
			if(this.getManager().getAfterChangeMadeAction()!=null)
				this.getManager().getAfterChangeMadeAction().run();
			
		
	}
	
	/**
	 * cancel any changes since the start of current session, restore all selection to the previous one saved at the beginning of this session;
	 * 
	 * @param event
	 */
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		
		this.getManager().setSelectedSet(this.getManager().getMostRecentlySavedSelectedSet());
		
		this.disableFinishAndCancelButton();
	}
	
	/**
	 * try to save the currently selected set as if it is a finished solution set;
	 * 
	 * will only succeed if the selected set is a finished one, show alert otherwise;
	 * 
	 * @param event
	 */
	@FXML
	public void finishButtonOnAction(ActionEvent event) {
		if(!this.getManager().getDAGSolutionSetSelector().allRootPathsHaveOneNodeSelected()) {
			AlertUtils.popAlert("ERROR", "Solution set selection is not finishable!");
			return;
		}else {//
			Set<V> currentSelectedSet = this.getManager().getCurrentlySelectedSet();
			if(!Objects.equal(currentSelectedSet,this.getManager().getMostRecentlySavedSelectedSet())) {
				//currently selected solution set is different from most recently saved one;
				//pop up warning and react accordingly to the response
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Warning!");
				alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//save the currently finished assignment to override the previously saved one;
					this.getManager().setMostRecentlySavedSelectedSet(currentSelectedSet);
					this.getManager().setMostRecentlySavedSelectedSetFinished(true);
					
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
		}
		
		this.disableFinishAndCancelButton();
	}
	
//	
}
