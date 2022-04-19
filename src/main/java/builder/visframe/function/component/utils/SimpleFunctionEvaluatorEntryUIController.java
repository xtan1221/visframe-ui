package builder.visframe.function.component.utils;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import utils.FXUtils;


public class SimpleFunctionEvaluatorEntryUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/component/utils/SimpleFunctionEvaluatorEntryUI.fxml";
	
	/////////////////////////
	private SimpleFunctionEvaluatorEntryBuilder ownerBuilder;
//	
	
	public void setOwnerBuilder(SimpleFunctionEvaluatorEntryBuilder ownerBuilder) {
		this.ownerBuilder = ownerBuilder;
		
		
		//initialization based on owner builder
		
	}
	
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.deleteButton, !modifiable);
		FXUtils.set2Disable(this.editButton, !modifiable);
	}
	
	
	/**
	 * @return the ownerBuilder
	 */
	public SimpleFunctionEvaluatorEntryBuilder getOwnerBuilder() {
		return ownerBuilder;
	}


	public Parent getRootContainerNode() {
		return this.containerHBox;
	}
	
	/**
	 * set UI visual effect to indicate whether a valid evaluator has been created or not for this entry
	 */
	public void setUIEffect() {
		if(this.getOwnerBuilder().getSimpleFunctionEvaluatorBuilderDelegate().getCurrentStatus().hasValidValue()) {
			this.containerHBox.setStyle("-fx-background-color:#1FDE5E;");
		}else {
			this.containerHBox.setStyle("-fx-background-color:red;");
		}
	}
	
	void setDeleteDisable(boolean disable) {
		FXUtils.set2Disable(this.deleteButton, disable);
	}
	
	void setEditDisable(boolean disable) {
		FXUtils.set2Disable(this.editButton, disable);
	}
	/////////////////////////////////////////////
	@FXML
	public void initialize() {
		
		
	}
	
	@FXML
	private HBox containerHBox;
	@FXML
	private Button deleteButton;
	@FXML
	private Button editButton;
	@FXML
	private Button viewButton;
	
	
	// Event Listener on Button[#deleteButton].onAction
	@FXML
	public void deleteButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerBuilder().getHostSimpleFunctionBuilder().deleteEvaluator(this.getOwnerBuilder());
		
		//
		this.getOwnerBuilder().getHostSimpleFunctionBuilder().getHostCompositionFunctionBuilder().update();
	}
	
	// Event Listener on Button[#editButton].onAction
	@FXML
	public void editButtonOnAction(ActionEvent event) throws SQLException, IOException {
		//set modifiable
		this.getOwnerBuilder().getSimpleFunctionEvaluatorBuilderDelegate().setModifiable(true);
		
		
		this.getOwnerBuilder().getSimpleFunctionEvaluatorBuilderDelegate().getIntegrativeUIController().showAndWait(this.getRootContainerNode().getScene().getWindow(), "SimpleFunctionEvaluatorBuilder");
		
		System.out.println();
	}
	
	
	@FXML
	public void viewButtonOnAction(ActionEvent event) throws SQLException, IOException {
		//set modifiable to false
		this.getOwnerBuilder().getSimpleFunctionEvaluatorBuilderDelegate().setModifiable(false);;
		
		
		this.getOwnerBuilder().getSimpleFunctionEvaluatorBuilderDelegate().getIntegrativeUIController().showAndWait(this.getRootContainerNode().getScene().getWindow(), "SimpleFunctionEvaluatorBuilder");
	}
}
