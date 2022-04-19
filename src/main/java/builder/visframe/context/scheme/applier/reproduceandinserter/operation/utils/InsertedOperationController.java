package builder.visframe.context.scheme.applier.reproduceandinserter.operation.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.AlertUtils;

/**
 * 
 * @author tanxu
 *
 */
public class InsertedOperationController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/operation/utils/InsertedOperation.fxml";
	
	///////////////////////////////////
	private InsertedOperationManager manager;
	
	void setManager(InsertedOperationManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public InsertedOperationManager getManager() {
		return manager;
	}
	
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerHBox;
	}
	
	Button getRollbackButton() {
		return this.rollbackButton;
	}
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
		
		//
	}
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private Button showOnGraphButton;
	@FXML
	private Button viewDetailsButton;
	@FXML
	Button rollbackButton;

	// Event Listener on Button[#showOnGraphButton].onAction
	@FXML
	public void showOnGraphButtonOnAction(ActionEvent event) {
		this.getManager().getHostOperationReproducingAndInsertionManager().highlightIntegratedDOSGraphEdges(this.getManager().getInsertedOperation().getID(), true);
	}
	
	
	private Stage stage;
	private Scene scene;
	// Event Listener on Button[#viewDetailsButton].onAction
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage == null) {
			//set builder modifiable 
			this.manager.getOperationBuilder().setModifiable(false);
			
			scene = new Scene(this.manager.getOperationBuilder().getIntegrativeUIController().getRootNode());
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getInsertedOperation().getID().toString();
			stage.setTitle(title);
		}
		stage.showAndWait();
	}
	
	/**
	 * @param event
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@FXML
	public void rollbackButtonOnAction(ActionEvent event) throws SQLException, IOException {
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will also roll back all CFs and CFGs that have been inserted so far (if any)!");
		
		if (result.get() == ButtonType.OK){
			this.getManager().getHostOperationReproducingAndInsertionManager().rollbackMostRecentlyInsertedOperation();
		}
		
//		System.out.println(this.getClass()+": test");
	}
	
}
