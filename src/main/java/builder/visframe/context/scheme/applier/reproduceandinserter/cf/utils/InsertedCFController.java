package builder.visframe.context.scheme.applier.reproduceandinserter.cf.utils;

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
public class InsertedCFController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/cf/utils/InsertedCF.fxml";
	
	///////////////////////////////////
	private InsertedCFManager manager;
	
	void setManager(InsertedCFManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public InsertedCFManager getManager() {
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
		this.getManager().getHostCFReproducingAndInsertionManager().highlightIntegratedCFDGraphNode(this.getManager().getInsertedCompositionFunction().getID(), true);
	}
	
	///////////////////////////
	private Stage stage;
	private Scene scene;
	/**
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void viewDetailsButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage==null) {
			//set builder modifiable 
			this.manager.getCompositionFunctionBuilder().setModifiable(false);
			
	//		Scene scene = new Scene(this.manager.getCompositionFunctionBuilder().getEmbeddedUIContentController().getRootParentNode());
			scene = new Scene(this.manager.getCompositionFunctionBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	//		
			
			//////////////
			stage = new Stage();
	//		stage.setScene(this.manager.getCompositionFunctionBuilder().getEmbeddedUIContentController().getRootParentNode().getScene());
			stage.setScene(scene);
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getInsertedCompositionFunction().getID().toString();
			stage.setTitle(title);
		}
		
		stage.showAndWait();
	}
	
}
