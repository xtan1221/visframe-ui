package builder.visframe.context.scheme.applier.reproduceandinserter.cfg.utils;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author tanxu
 *
 */
public class InsertedCFGController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/cfg/utils/InsertedCFG.fxml";
	
	///////////////////////////////////
	private InsertedCFGManager manager;
	
	void setManager(InsertedCFGManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public InsertedCFGManager getManager() {
		return manager;
	}
	
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerHBox;
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

	// Event Listener on Button[#showOnGraphButton].onAction
	@FXML
	public void showOnGraphButtonOnAction(ActionEvent event) {
		//TODO
//		this.getManager().getHostCFGReproducingAndInsertionManager().highlightIntegratedDOSGraphEdges(this.getManager().getInsertedCompositionFunctionGroup().getID(), true);
	}
	
	//////////////////////
	private Scene scene;
	private Stage stage;
	/**
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage == null) {
			//set builder modifiable 
			this.manager.getCompositionFunctionGroupBuilder().setModifiable(false);
			
			scene = new Scene(this.manager.getCompositionFunctionGroupBuilder().getIntegrativeUIController().getRootNode());
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getInsertedCompositionFunctionGroup().getID().toString();
			stage.setTitle(title);
		}
		
		stage.showAndWait();
	}
	
}
