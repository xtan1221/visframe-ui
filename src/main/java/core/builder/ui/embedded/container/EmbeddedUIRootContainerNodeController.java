package core.builder.ui.embedded.container;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.NodeBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.FXUtils;

/**
 * fxml controller for the header part of embedded UI of a {@link NodeBuilder};
 * which is the same for all types of {@link NodeBuilder}; 
 * 
 * responsible for making the value of the target property of the owner {@link NodeBuilder} to null/non-null;
 * 
 * no constructor
 * @author tanxu
 * 
 */
public final class EmbeddedUIRootContainerNodeController {
	public static final String FXML_FILE_DIR = "/core/builder/ui/embedded/container/EmbeddedUIRootContainerNode.fxml";
	
	//////////////////////////////////////////
	/**
	 * 
	 */
	private NodeBuilder<?,?> ownerNodeBuilder;
	
	
	
	//////////////////////////////////////////////////
	/**
	 * 
	 * set owner;
	 * 
	 * then perform any initialization depending on the owner;
	 * 
	 * @param ownerNodeBuilder
	 * @throws IOException 
	 */
	public void setOwnerNodeBuilder(NodeBuilder<?,?> ownerNodeBuilder) throws IOException {
		if(this.ownerNodeBuilder!=null)
			throw new UnsupportedOperationException("cannot set owner Node Builder when it is not null!");
		
		this.ownerNodeBuilder = ownerNodeBuilder;
		
		
		
		////////////////////////////////////////
		//initializations that are depending on the owner NodeBuilder
		this.setToNullCheckBox.selectedProperty().addListener(e->{
			try {
				if(this.setToNullCheckBox.isSelected()) {//set to null
					//first set to default empty to clear all
					
					this.getOwnerNodeBuilder().setToDefaultEmpty();
					
					//then set to null
					this.getOwnerNodeBuilder().setToNull();
					
					FXUtils.set2Disable(this.getSetToDefaultEmptyButton(), true);
				}else {//set to non-null
					
					this.getOwnerNodeBuilder().setToNonNull();
					
					FXUtils.set2Disable(this.getSetToDefaultEmptyButton(), false);
				}
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		
		//set name label
		this.nodeNameLabel.setText(this.getOwnerNodeBuilder().getName());
		
		//set the setToNull checkbox
		if(!this.getOwnerNodeBuilder().canBeNull()) {
			this.disableSetToNullCheckBox();
		}
		//
		
		
		
		/////////////////////////////////////
		//debug further TODO
		
		//invoking this.getOwnerNodeBuilder().getEmbeddedUIContentController() 
		//will add the root node of EmbeddedUIContent to the getContentPane; which is not expected!!!!
		Node node = this.getOwnerNodeBuilder().getEmbeddedUIContentController().getRootParentNode();
		//clear it to avoid duplicate child
		this.getContentPane().getChildren().clear();
		
		this.getContentPane().getChildren().add(node);
		
		
	}
	
	/**
	 * do not show the set to null checkbox
	 */
	private void disableSetToNullCheckBox() {
//		this.getRootContainerNode().
		FXUtils.set2Disable(this.getSetToNullCheckBox(), true);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public NodeBuilder<?,?> getOwnerNodeBuilder(){
		return this.ownerNodeBuilder;
	}
	/////////////////////////////////////
	
	/**
	 * set whether the root UI is modifiable or not
	 * @param modifiable
	 */
	public void setModifiable(boolean modifiable) {
		if(modifiable) {
			if(!this.controlHBox.getChildren().contains(this.setToEmptyButton)) {
//				this.controlHBox.getChildren().add(this.setToNullCheckBox);
				this.controlHBox.getChildren().add(this.setToEmptyButton);
			}
			this.setToNullCheckBox.setMouseTransparent(false);
		}else {
//			this.controlHBox.getChildren().remove(this.setToNullCheckBox);
			this.controlHBox.getChildren().remove(this.setToEmptyButton);
			
			this.setToNullCheckBox.setMouseTransparent(true);
		}
	}
	
	
	
	
	
	//////////////////////////////////////
	/**
	 * return the root container Node
	 * @return
	 */
	public Pane getRootContainerPane() {
		return this.containerVBox;
	}
	
	
	/////////////////////////////////////
	///methods invoked by owner NodeBuilder to directly change the UI nodes of the root container for specific purpose
	/**
	 * simply set the embedded UI from non-null to the null status;
	 * 
	 * 1. select the set to null checkbox
	 * 2. hide the EmbeddedUIContent node
	 * 
	 * 
	 */
	public void setUIToNullStatus() {
		//?? not needed?
		this.getSetToNullCheckBox().setSelected(true);
		
		//
		this.getContentPane().getChildren().clear();
		
	}
	
	/**
	 * simply set the embedded UI from null to the non-null status;
	 * 
	 * 1. un-select the set to null checkbox
	 * 2. show the EmbeddedUIContent node
	 * @throws IOException 
	 */
	public void setUIToNonNullStatus() {
		//?
		this.getSetToNullCheckBox().setSelected(false);
		
		if(!this.getContentPane().getChildren().contains(this.getOwnerNodeBuilder().getEmbeddedUIContentController().getRootParentNode()))
			this.getContentPane().getChildren().add(this.getOwnerNodeBuilder().getEmbeddedUIContentController().getRootParentNode());
	}
	
	/**
	 * set the visual effect of the root container based on the current status of the owner NodeBuilder
	 */
	public void setStatusVisualEffect() {
		if(this.getOwnerNodeBuilder().getCurrentStatus().hasValidValue()) {
			this.getRootContainerPane().setStyle("-fx-border-color:#1FDE5E;" + "-fx-border-size:5;");

		}else {
			this.getRootContainerPane().setStyle("-fx-border-color:red;" + "-fx-border-size:5;");
		}
	}
//	/**
//	 * simply set the UI to default empty status;
//	 * 
//	 * 1. set the 
//	 * 
//	 * 2. set the content UI
//	 * 
//	 * not responsible for any downstream invocation;
//	 * 
//	 */
//	public void setUIToDefaultEmptyStatus() {
////		this.isEmptyCheckBox.setSelected(true);
////		this.getOwnerNodeBuilder().getEmbeddedUIContentController().setUIToDefaultEmptyStatus();
//	}
	
	
	
	////////////////////////////////
	//key {@link Node}s
	
	
	/**
	 * return the Parent Node that hold the content Node;
	 * @return
	 */
	Pane getContentPane() {
		return this.contentVBox;
	}
	
	public CheckBox getSetToNullCheckBox() {
		return this.setToNullCheckBox;
	}
	
	Button getSetToDefaultEmptyButton() {
		return this.setToEmptyButton;
	}
//	Pane getDefaultEmptyPane() {
//		return this.defaultEmptyValueHBox;
//	}
	
	////////////////////////////////
	
	@FXML
	private VBox containerVBox;
	@FXML
	private Label nodeNameLabel;
	@FXML
	private HBox controlHBox;
	@FXML
	private CheckBox setToNullCheckBox;
	@FXML
	private Button setToEmptyButton;
	@FXML
	private Button printCurrentStatusButton;
	@FXML
	private HBox contentHBox;
	@FXML
	private VBox contentVBox;
	
	@FXML
	public void initialize() {
		//set property change event listeners

		//
//		this.isEmptyCheckBox.setMouseTransparent(true);
	}
	
	
	// Event Listener on Button[#setToEmptyButton].onAction
	@FXML
	public void setToEmptyButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.getOwnerNodeBuilder().setToDefaultEmpty();
	}
	
	
	// Event Listener on Button[#printCurrentStatusButton].onAction
	@FXML
	public void printCurrentStatusButtonOnAction(ActionEvent event) {
		System.out.println(this.getOwnerNodeBuilder().getCurrentStatus());
	}
}
