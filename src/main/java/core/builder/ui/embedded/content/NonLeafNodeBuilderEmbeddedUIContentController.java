package core.builder.ui.embedded.content;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NonLeafNodeBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.FXUtils;

public class NonLeafNodeBuilderEmbeddedUIContentController<T> extends EmbeddedUIContentControllerBase<T>{
	public static final String FXML_FILE_DIR_STRING = "/core/builder/ui/embedded/content/NonLeafNodeBuilderEmbeddedUIContent.fxml";

	
	@SuppressWarnings("unchecked")
	@Override
	public NonLeafNodeBuilder<T> getOwnerNodeBuilder() {
		return (NonLeafNodeBuilder<T>) ownerNodeBuilder;
	}
	
	/**
	 * 
	 */
	@Override
	public void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.startEditButton, !modifiable);
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootVBox;
	}
	

	////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private Button startEditButton;
	@FXML
	private Button viewOnlyButton;
	
	// Event Listener on Button[#startEditButton].onAction
	@FXML
	public void startEditButtonOnAction(ActionEvent event) throws IOException, SQLException {
		//set integrative UI modifiable modifiable to true
		this.getOwnerNodeBuilder().getIntegrativeUIController().setModifiable(true);
		//set children node builder's modifialbe to true
		this.getOwnerNodeBuilder().setChildrenModifiable(true);
		
		//first save the current status
		this.getOwnerNodeBuilder().saveCurrentStatusAsPrevious();
		
		
		//show the window of integrative UI
		this.getOwnerNodeBuilder().showMainWindow();
		
		
	}
	// Event Listener on Button[#viewOnlyButton].onAction
	@FXML
	public void viewOnlyButtonOnAction(ActionEvent event) throws IOException, SQLException {
		//set integrative UI modifiable to false;
		this.getOwnerNodeBuilder().getIntegrativeUIController().setModifiable(false);
		//also set children node builers modifiable to false
		this.getOwnerNodeBuilder().setChildrenModifiable(false);
		
		this.getOwnerNodeBuilder().showMainWindow();
	}
}
