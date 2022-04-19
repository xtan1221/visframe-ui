package builder.visframe.context.scheme.applier.reproduceandinserter.cfg;

import java.sql.SQLException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import utils.FXUtils;

public class CFGReproducingAndInsertinoController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/cfg/CFGReproducingAndInsertion.fxml";
	
	///////////////////////////////////
	private CFGReproducingAndInsertionManager manager;
	
	void setManager(CFGReproducingAndInsertionManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public CFGReproducingAndInsertionManager getManager() {
		return manager;
	}
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerHBox;
	}
	
	/**
	 * return the AnchorPane on which the graph is laid out
	 * @return
	 */
	AnchorPane getGraphLayoutAnchorPane() {
		return this.trimmedIntegratedCFDGraphAnchorPane;
	}
	
	VBox getReproducedAndInsertedCFGContainerHBox() {
		return this.reproducedAndInsertedCFGContainerHBox;
	}
	/////////////////////////////////////
	@FXML
	public void initialize() {
		FXUtils.set2Disable(this.startButton, false);
		FXUtils.set2Disable(this.rollbackAllButton, true);
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private AnchorPane trimmedIntegratedCFDGraphAnchorPane;
	@FXML
	HBox controlHBox;
	@FXML Button startButton;
	@FXML
	Button rollbackAllButton;
	@FXML
	private VBox reproducedAndInsertedCFGContainerHBox;

	/**
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	public void startButtonOnAction(ActionEvent event) throws SQLException {
		this.getManager().insertAll(true);
	}
	
	/**
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	public void rollbackAllButtonOnAction(ActionEvent event) throws SQLException {
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will roll back all CFs and CFGs that have been inserted so far!");
		
		if (result.get() == ButtonType.OK){
			this.getManager().rollbackAll(true);
		}
		
	}

	
}
