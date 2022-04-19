package builder.basic.collection.list2.element;

import java.io.IOException;
import java.sql.SQLException;

import builder.basic.collection.list2.FixedSizeArrayListNonLeafNodeBuilder;
import core.builder.NodeBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.exceptionhandler.ExceptionHandlerUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public final class FixedSizeArrayListElementController<E> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/list2/element/FixedSizeArrayListElement.fxml";
	
	private NodeBuilder<E,?> elementBuilder;
	
	private FixedSizeArrayListNonLeafNodeBuilder<E> ownerListFeatureBuilder;
	
	/**
	 * set the builder for key and value;
	 * 
	 * must be invoked upon the controller is loaded;
	 * 
	 * @param keyBuilder
	 * @param valueBuilder
	 * @throws IOException
	 */
	public void setBuilder(NodeBuilder<E,?> elementBuilder, FixedSizeArrayListNonLeafNodeBuilder<E> ownerListFeatureBuilder) throws IOException {
		this.elementBuilder = elementBuilder;
		this.ownerListFeatureBuilder = ownerListFeatureBuilder;
		//
		this.elementHBox.getChildren().add(this.getElementBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane()); //
	}
	
	
	public NodeBuilder<E,?> getElementBuilder() {
		return elementBuilder;
	}
	
	public FixedSizeArrayListNonLeafNodeBuilder<E> getOwnerListFeatureBuilder() {
		return ownerListFeatureBuilder;
	}
	
	public void setIndex(int index) {
		this.indexLabel.setText(Integer.toString(index));
	}

	/**
	 * set the 
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.elementOperationHBox.setVisible(modifiable);
		
		this.elementBuilder.setModifiable(modifiable);
	}
	
	public Pane getRootPane() {
		return this.rootHBox;
	}
	
	/////////////////////////////////
	@FXML
	public void initialize(){
		
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private HBox rootHBox;
	@FXML
	private HBox elementOperationHBox;
	@FXML
	private HBox operationButtonHBox;
	@FXML
	private Button moveDownButton;
	@FXML
	private Button moveUpButton;
	@FXML
	private Label indexLabel;
	@FXML
	private HBox elementHBox;

	// Event Listener on Button[#moveDownButton].onAction
	@FXML
	public void moveDownButtonOnAction(ActionEvent event) {
		try {
			this.getOwnerListFeatureBuilder().getIntegrativeUIController().moveDown(this);
		} catch (Exception e) {
			ExceptionHandlerUtils.show2("ListFeatureElementController.moveDownButtonOnAction", e);
		}
	}
	// Event Listener on Button[#moveUpButton].onAction
	@FXML
	public void moveUpButtonOnAction(ActionEvent event) {
		try {
			this.getOwnerListFeatureBuilder().getIntegrativeUIController().moveUp(this);
		} catch (Exception e) {
			ExceptionHandlerUtils.show2("ListFeatureElementController.moveUpButtonOnAction", e);
		}
	}
	
	
//
//	// Event Listener on Button[#moveDownButton].onAction
//	@FXML
//	public void moveDownButtonOnAction(ActionEvent event) {
//		try {
//			this.getOwnerListFeatureBuilder().getIntegrativeUIController().moveDown(this);
//		} catch (Exception e) {
//			ExceptionHandlerUtils.show2("ListFeatureElementController.moveDownButtonOnAction", e);
//		}
//	}
//	
//	// Event Listener on Button[#moveUpButton].onAction
//	@FXML
//	public void moveUpButtonOnAction(ActionEvent event) {
//		try {
//			this.getOwnerListFeatureBuilder().getIntegrativeUIController().moveUp(this);
//		} catch (Exception e) {
//			ExceptionHandlerUtils.show2("ListFeatureElementController.moveUpButtonOnAction", e);
//		}
//	}
//	// Event Listener on Button[#removeButton].onAction
//	@FXML
//	public void removeButtonOnAction(ActionEvent event) {
//		try {
//			this.getOwnerListFeatureBuilder().getIntegrativeUIController().remove(this);
//		} catch (Exception e) {
//			ExceptionHandlerUtils.show2("ListFeatureElementController.removeButtonOnAction", e);
//		}
//	}

	
}
