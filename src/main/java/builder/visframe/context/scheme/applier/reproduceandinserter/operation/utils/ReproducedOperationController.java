package builder.visframe.context.scheme.applier.reproduceandinserter.operation.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.operation.AbstractOperationBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReproducedOperationController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/operation/utils/ReproducedOperation.fxml";
	
	///////////////////////////////////
	private ReproducedOperationManager manager;
	
	void setManager(ReproducedOperationManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public ReproducedOperationManager getManager() {
		return manager;
	}
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerHBox;
	}
	
	Button getEditButton() {
		return this.editButton;
	}
	
	Button getResetButton() {
		return this.resetButton;
	}
	Button getInsertButton() {
		return this.insertButton;
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
	private Button viewDetailButton;
	@FXML
	private Button editButton;
	@FXML
	private Button resetButton;
	@FXML
	Label statusLabel;
	@FXML
	private Button insertButton;

	
	/**
	 * highlight the IntegratedDOSGraphEdges on the trimmed integrated DOS graph that represents this Operation;
	 * @param event
	 */
	// Event Listener on Button[#showOnGraphButton].onAction
	@FXML
	public void showOnGraphButtonOnAction(ActionEvent event) {
		this.getManager().getHostOperationReproducingAndInsertionManager().highlightIntegratedDOSGraphEdges(this.getManager().getReproducedOperation().getID(), true);
	}
	
	
	///////////////////
	private Stage stage;
	private Scene scene;
	/**
	 * view the details of the reproduced Operation;
	 * 
	 * @param event
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@FXML
	public void viewDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage == null) {
			scene = new Scene(this.manager.getOperationBuilder().getIntegrativeUIController().getRootNode());
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getReproducedOperation().getID().toString();
			stage.setTitle(title);
		}
		
		//set builder modifiable 
		this.manager.getOperationBuilder().setModifiable(false);
		stage.showAndWait();
	}
	
	
	/**
	 * set the values for parameters of the reproduced Operation dependent on input data table content;
	 * 
	 * 
	 * see {@link AbstractOperationBuilder#checkIfForReproducing(boolean)} method for how to set up the integrative UI controller for reproduced operation for reproducing and insertion;
	 * 
	 * 
	 * once the status of the operation builder is changed, and if it has 
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void editButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage==null) {
			scene = new Scene(this.manager.getOperationBuilder().getIntegrativeUIController().getRootNode());
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getReproducedOperation().getID().toString();
			stage.setTitle(title);
		}
		
		this.manager.getOperationBuilder().setModifiable(true); 
		stage.showAndWait();
	}
	
	///////////////////
	/**
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void resetButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getManager().initialize();
	}
	
	/**
	 * 
	 * @param event
	 * @throws SQLException 
	 */
	@FXML
	public void insertButtonOnAction(ActionEvent event) throws SQLException {
		this.getManager().getHostOperationReproducingAndInsertionManager().insertCurrentOperation();
		
//		System.out.println(this.getClass()+":test");
	}
}
