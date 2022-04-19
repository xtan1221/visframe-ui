package dependencygraph.mapping;

import java.util.Map;
import java.util.Optional;

import com.google.common.base.Objects;

import context.scheme.appliedarchive.mapping.MetadataMapping;
import dependency.dos.integrated.IntegratedDOSGraphNode;
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

public class SolutionSetMetadataMappingController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/mapping/SolutionSetMetadataMapping.fxml";
	
	///////////////////////////////////
	private SolutionSetMetadataMappingManager manager;
	
	void setManager(SolutionSetMetadataMappingManager manager) {
		this.manager = manager;
	}
	
	/**
	 * @return the manager
	 */
	public SolutionSetMetadataMappingManager getManager() {
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
	public void enableFinishButton() {
		FXUtils.set2Disable(this.finishButton, false);
	}
	
	public void disableFinishButton() {
		FXUtils.set2Disable(this.finishButton, true);
	}
	
	
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.controlButtonHBox, !modifiable);
	}
	/////////////////////////////////////
	@FXML
	public void initialize() {
//		this.saveButton.setDisable(true);
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
	private Button finishButton;

	
	/**
	 * clear all created mappings and set to default status (restart), cannot restore to previous status afterwards;
	 * 
	 * need to trigger the {@link SolutionSetMetadataMappingManager#getAfterChangeMadeAction()} IF {@link SolutionSetMetadataMappingManager#getMostRecentlySavedMetadataMappingFinished()} is true;
	 * 
	 * @param event
	 */
	// Event Listener on Button[#clearAllButton].onAction
	@FXML
	public void clearAllButtonOnAction(ActionEvent event) {
		boolean toTriggerAfterChangeAction = false;
		if(this.getManager().getMostRecentlySavedMetadataMappingFinished()!=null && this.getManager().getMostRecentlySavedMetadataMappingFinished()) {
			//change from a saved finished mapping for all solution set nodes to default status with empty value for all mappings;
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
		
		////////////////////////
		//
		this.getManager().clearAllMappings();
		
		//save the resulted map whose values are all null
		this.getManager().setMostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap(this.getManager().getCurrentIntegratedDOSGraphNodeMetadataMappingMap());
		this.getManager().setMostRecentlySavedMetadataMappingFinished(false);
		
		//update button status
		this.disableFinishButton();
		
		if(toTriggerAfterChangeAction)
			if(this.getManager().getAfterChangeMadeAction()!=null)
				this.getManager().getAfterChangeMadeAction().run();
		
	}
	
	/**
	 * try to save the current mappings as if all nodes in solution set has a valid mapping created
	 *  
	 * will only succeed if all mappings are created, show alert otherwise;
	 * 
	 * @param event
	 */
	// Event Listener on Button[#saveButton].onAction
	@FXML
	public void finishButtonOnAction(ActionEvent event) {
		if(!this.getManager().allSolutionSetNodeMappingFinished()) {
			AlertUtils.popAlert("ERROR", "Metadata mapping is not finishable!");
			return;
		}else {
			Map<IntegratedDOSGraphNode, MetadataMapping>  currentMappingMap = this.getManager().getCurrentIntegratedDOSGraphNodeMetadataMappingMap();
			
			if(!Objects.equal(currentMappingMap,this.getManager().getMostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap())) {
				//currently created Mappings map is different from most recently saved one;
				//pop up warning and react accordingly to the response
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText("Warning!");
				alert.setContentText(this.getManager().getBeforeChangeMadeWarningInforText());
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					//save the currently finished assignment to override the previously saved one;
					this.getManager().setMostRecentlySavedIntegratedDOSGraphNodeMetadataMappingMap(currentMappingMap);
					this.getManager().setMostRecentlySavedMetadataMappingFinished(true);
					
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
		
		
		this.disableFinishButton();
		
	}
	
}
