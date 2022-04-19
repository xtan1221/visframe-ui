package core.builder.ui.integrative;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import core.builder.NonLeafNodeBuilder;
import core.builder.utils.ConstraintsViolotionWarningAlertManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * class for FXML controller for the UI of a nonleaf node builder;
 * 
 * @author tanxu
 *
 */
public class NonLeafNodeBuilderIntegrativeUIController {
	public static final String FXML_FILE_DIR = "/core/builder/ui/integrative/NonLeafNodeBuilderIntegrativeUI.fxml";
	
	
	//////////////////////
	protected NonLeafNodeBuilder<?> ownerNonLeafNodeBuilder;
	
	
	/**
	 * set the owner and perform any initialization based on the owner;
	 * 
	 * @param owner
	 * @throws IOException 
	 */
	public void setOwnerNonLeafNodeBuilder(NonLeafNodeBuilder<?> owner) throws IOException {
		this.ownerNonLeafNodeBuilder = owner;
		
		//
		this.nodeNameLabel.setText(this.getOwnerNodeBuilder().getName());
		this.nodeDescriptionLabel.setText(this.getOwnerNodeBuilder().getDescription());
		//add children {@link NodeBuilder}s 
//		for(String name:this.getOwnerNodeBuilder().getChildrenNodeBuilderNameMap().keySet()) {
//			this.getChildrenEmbeddedNodeContainerPane().getChildren().add(this.ownerNonLeafNodeBuilder.getChildrenNodeBuilderNameMap().get(name).getEmbeddedUIRootContainerNodeController().getRootContainerPane());
//		}
	}
	
	
	/**
	 * @return the ownerNonLeafNodeBuilder
	 */
	public NonLeafNodeBuilder<?> getOwnerNodeBuilder() {
		return ownerNonLeafNodeBuilder;
	}

	
	///////////////////////////////////////////////////
	
	/**
	 * set whether this UI is modifiable or not;
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.operationButtonContainerHBox.setVisible(modifiable);
	}
	
	///////////////////////////////////////////////
	public Parent getRootNode() {
		return this.containerVBox;
	}
	
	/**
	 * return the Parent node to which all children {@link NodeBuilder}'s root node should be added;
	 * 
	 * @return
	 */
	public Pane getChildrenEmbeddedNodeContainerPane() {
		return childrenEmbeddedNodeVBox;
	}
	
	/**
	 * @return the finishButton
	 */
	public Button getFinishButton() {
		return finishButton;
	}


	/**
	 * @return the setToDefaultEmptyButton
	 */
	public Button getSetToDefaultEmptyButton() {
		return setToDefaultEmptyButton;
	}


	/**
	 * @return the cancelButton
	 */
	public Button getCancelButton() {
		return cancelButton;
	}

	/**
	 * return the stage
	 * @return
	 */
	protected Stage getStage() {
		return this.stage;
	}
	
	private Stage stage;
	private Scene scene;
	////////////////////////////////////////
	/**
	 * 
	 * @param ownerStage
	 * @param title
	 */
	public void showAndWait(Window ownerStage, String title) {
		
		if(this.stage==null) {
			this.stage = new Stage();
			//close request
			stage.setOnCloseRequest(e->{
//				System.out.println("close requested");
				try {
					this.cancelButtonOnAction(null);
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			this.scene = new Scene(this.getRootNode());
			
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			
			if(ownerStage!=null) {
				stage.initOwner(ownerStage);
				stage.initModality(Modality.WINDOW_MODAL);
			}else {
				stage.initModality(Modality.NONE);
			}
			
			
			stage.setTitle(title);
			
		}
		
		//whenever the Integrative UI is shown in a window, store the current status in previousStatus in the owner NonLeafNodeBuilder!
		this.getOwnerNodeBuilder().storePreviousStatus();
		
		this.stage.showAndWait();
	}
	
	
	
	/**
	 * any initialization not based on {@link #ownerNonLeafNodeBuilder}
	 */
	@FXML
	public void initialize() {
		//
	}
	
	
	@FXML
	private VBox containerVBox;
	@FXML
	private Label nodeNameLabel;
	@FXML
	private Label nodeDescriptionLabel;
	@FXML
	private ScrollPane childrenEmbeddedRootContainerNodeScrollPane;
	@FXML
	private VBox childrenEmbeddedNodeVBox;
	@FXML
	private HBox operationButtonContainerHBox;
	@FXML
	private Button finishButton;

	@FXML
	private Button setToDefaultEmptyButton;
	@FXML
	private Button cancelButton;

	
	
	/**
	 * 
	 * 1. first check if all constraints are obeyed
	 * 		1. inter-children sibling nodes constraints
	 * 		2. constraints in the constructor of the target type of the owner {@link NonLeafNodeBuilder}
	 * 
	 * 2. if not, display a popup window to choose what to do next:
	 * 		1. continue editing to correct the violated constraints;
	 * 		2. cancel current editing session and reset to previous status and exit
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	// Event Listener on Button[#finishButton].onAction
	@FXML
	public void finishButtonOnAction(ActionEvent event) throws SQLException, IOException {
		
		List<String> violatedConstraintsDescriptionSet = this.getOwnerNodeBuilder().findConstraintViolation();
		if(!violatedConstraintsDescriptionSet.isEmpty()) {
//			System.out.println("cannot finish because of violated constraints!");
//			violatedConstraintsDescriptionSet.forEach(e->{
//				System.out.println(e);
//			});
			
			ConstraintsViolotionWarningAlertManager manager = new ConstraintsViolotionWarningAlertManager(this.getRootNode().getScene().getWindow(), violatedConstraintsDescriptionSet);
			
			manager.showAndWait();
			
//			System.out.println("continue");
			if(manager.isToContinueEditing()) {
				//do nothing
				
			}else {//rollback and exit
				cancelButtonOnAction(event);
			}
			
		}else {//valid
//			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			this.getOwnerNodeBuilder().saveNewValue();
			
			this.stage.hide();
		}
	}
	
	
	// Event Listener on Button[#setToDefaultEmptyButton].onAction
	@FXML
	public void setToDefaultEmptyButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getOwnerNodeBuilder().setToDefaultEmpty();
	}
	
	
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) throws SQLException, IOException {
//		System.out.println("cancel");
		this.getOwnerNodeBuilder().restorePreviousValue();
		
		Stage stage = (Stage)this.cancelButton.getScene().getWindow();
		stage.close();
	}
}
