package builder.visframe.function.component.utils;

import builder.visframe.function.component.PiecewiseFunctionBuilder;
import builder.visframe.function.component.SimpleFunctionBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import builder.visframe.function.component.ComponentFunctionBuilder.ComponentFunctionType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import utils.FXUtils;


public class PiecewiseFunctionConditionEntryUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/component/utils/PiecewiseFunctionConditionEntryUI.fxml";
	
	/////////////////////////
	private PiecewiseFunctionConditionEntryBuilder ownerBuilder;
	
	public void setOwnerBuilder(PiecewiseFunctionConditionEntryBuilder ownerBuilder) {
		this.ownerBuilder = ownerBuilder;
		
		
		//initialization based on owner builder
		
	}
	
	/**
	 * modify all buttons except for the view button
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.moveDownButton, !modifiable);
		FXUtils.set2Disable(this.moveUpButton, !modifiable);
		FXUtils.set2Disable(this.deleteEntryButton, !modifiable);
		FXUtils.set2Disable(this.editConditionEvaluatorButton, !modifiable);
		FXUtils.set2Disable(this.makeNextFunctionButton, !modifiable);
		FXUtils.set2Disable(this.deleteNextFunctionButton, !modifiable);
	}
	
	
	
	/**
	 * @return the ownerBuilder
	 */
	public PiecewiseFunctionConditionEntryBuilder getOwnerBuilder() {
		return ownerBuilder;
	}

	
	public Parent getRootContainerNode() {
		return this.rootContainerHBox;
	}
	
	public void updatePrecedenceIndexTextField() {
		this.precedenceIndexTextField.setText(Integer.toString(this.getOwnerBuilder().getPrecedenceIndex()));
	}
	
	
	/**
	 * set UI visual effect to indicate whether a valid evaluator has been created or not for this entry
	 */
	public void setUIEffect() {
		if(this.getOwnerBuilder().getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getCurrentStatus().hasValidValue()) {
			this.conditionHBox.setStyle("-fx-background-color:#1FDE5E;");
		}else {
			this.conditionHBox.setStyle("-fx-background-color:red;");
		}
	}
	
	void setDeleteEntryButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.deleteEntryButton, disable);
	}
	
	void setDeleteNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.deleteNextFunctionButton, disable);
		//this is a bug. visual effect of this button is not correctly when change to enabled status;
		if(disable)
			this.deleteNextFunctionButton.setOpacity(0.4);
		else
			this.deleteNextFunctionButton.setOpacity(1);
	}
	
	void setMakeNextFunctionButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.makeNextFunctionButton, disable);
		//this is a bug. visual effect of this button is not correctly when change to enabled status;
		if(disable)
			this.makeNextFunctionButton.setOpacity(0.4);
		else
			this.makeNextFunctionButton.setOpacity(1);
	}
	
	void setEditButtonDisable(boolean disable) {
		FXUtils.set2Disable(this.editConditionEvaluatorButton, disable);
	}
	
	/////////////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private HBox operationHBox;
	@FXML
	private Button moveDownButton;
	@FXML
	private Button moveUpButton;
	@FXML
	private Button deleteEntryButton;
	@FXML
	private HBox conditionHBox;
	@FXML
	private TextField precedenceIndexTextField;
	@FXML
	private Button editConditionEvaluatorButton;
	@FXML
	private Button viewConditionEvaluatorButton;
	@FXML
	private HBox nextFunctionHBox;
	@FXML
	private Button makeNextFunctionButton;
	@FXML
	private Button deleteNextFunctionButton;
	@FXML
	public Circle gotoNextFunctionCircle;
	
	// Event Listener on Button[#moveDownButton].onAction
	@FXML
	public void moveDownButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.getOwnerBuilder().getPrecedenceIndex()==this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getOrderedListOfConditionEntryBuilderByPrecedence().size()) {
			return;
		}
		
		
		Collections.swap(
				this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getOrderedListOfConditionEntryBuilderByPrecedence(), 
				this.getOwnerBuilder().getPrecedenceIndex()-1, 
				this.getOwnerBuilder().getPrecedenceIndex());
		
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().resetConditionEntries();

		
		//
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
	}
	// Event Listener on Button[#moveUpButton].onAction
	@FXML
	public void moveUpButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.getOwnerBuilder().getPrecedenceIndex()==1) {
			return;
		}
	
		Collections.swap(
				this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getOrderedListOfConditionEntryBuilderByPrecedence(), 
				this.getOwnerBuilder().getPrecedenceIndex()-1, 
				this.getOwnerBuilder().getPrecedenceIndex()-2);
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().resetConditionEntries();
		
		//
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
	}
	// Event Listener on Button[#deleteEntryButton].onAction
	@FXML
	public void deleteEntryButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().deleteCondition(this.getOwnerBuilder());
		
		//
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
	}
	// Event Listener on Button[#editConditionEvaluatorButton].onAction
	@FXML
	public void editConditionEvaluatorButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerBuilder().getPiecewiseFunctionConditionEvaluatorBuilderDelegate().setModifiable(true);
		
		
		this.getOwnerBuilder().getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getIntegrativeUIController().
		showAndWait(this.getRootContainerNode().getScene().getWindow(),"PiecewiseFunctionConditionEvaluatorBuilder");
	}
	// Event Listener on Button[#viewConditionEvaluatorButton].onAction
	@FXML
	public void viewConditionEvaluatorButtonOnAction(ActionEvent event) throws SQLException, IOException {
		//set modifiable to false
		this.getOwnerBuilder().getPiecewiseFunctionConditionEvaluatorBuilderDelegate().setModifiable(false);;
		
		this.getOwnerBuilder().getPiecewiseFunctionConditionEvaluatorBuilderDelegate().getIntegrativeUIController().
		showAndWait(this.getRootContainerNode().getScene().getWindow(),"PiecewiseFunctionConditionEvaluatorBuilder");
	}
	// Event Listener on Button[#makeNextFunctionButton].onAction
	@FXML
	public void makeNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		ComponentFunctionType selectedType = this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().getComponentFunctionTypeSelectionPopup().showAndWait();
		
		if(selectedType!=null) {
			if(selectedType.equals(ComponentFunctionType.SIMPLE)) {
				this.getOwnerBuilder().setNextComponentFunctionBuilder(
						new SimpleFunctionBuilder(
								this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder(), 
								this.getOwnerBuilder().getHostPiecewiseFunctionBuilder()));
			}else if(selectedType.equals(ComponentFunctionType.PIECEWISE)) {
				this.getOwnerBuilder().setNextComponentFunctionBuilder(
						new PiecewiseFunctionBuilder(
								this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder(), 
								this.getOwnerBuilder().getHostPiecewiseFunctionBuilder()));
			}
			
			//
			this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
			
		}else {
			//
		}
	}
	
	// Event Listener on Button[#deleteNextFunctionButton].onAction
	@FXML
	public void deleteNextFunctionButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerBuilder().setNextComponentFunctionBuilder(null);
		
		//
		this.getOwnerBuilder().getHostPiecewiseFunctionBuilder().getHostCompositionFunctionBuilder().update();
		
	}
	// Event Listener on Circle[#gotoNextFunctionCircle].onMouseClicked
	@FXML
	public void gotoNextFunctionCircleOnMouseClicked(MouseEvent event) {
		// TODO Autogenerated
	}
}
