package builder.visframe.context.scheme.applier.reproduceandinserter.cf;

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

public class CFReproducingAndInsertinoController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/cf/CFReproducingAndInsertion.fxml";
	
	///////////////////////////////////
	private CFReproducingAndInsertionManager manager;
	
	void setManager(CFReproducingAndInsertionManager manager) {
		this.manager = manager;
	}

	/**
	 * @return the manager
	 */
	public CFReproducingAndInsertionManager getManager() {
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
	
	VBox getReproducedAndInsertedCFContainerHBox() {
		return this.reproducedAndInsertedCFContainerHBox;
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
	@FXML
	Button startButton;
	@FXML
	Button rollbackAllButton;
	@FXML
	private VBox reproducedAndInsertedCFContainerHBox;


	@FXML
	public void startButtonOnAction(ActionEvent event) throws SQLException {
		this.getManager().insertAll(true);
	}
	
	@FXML
	public void rollbackAllButtonOnAction(ActionEvent event) throws SQLException {
		Optional<ButtonType> result = 
				AlertUtils.popConfirmationDialog(
						"Warning", 
						"confiramtion is needed", 
						"perform this will roll back all CFs that have been inserted so far!");
		if (result.get() == ButtonType.OK){
			this.getManager().rollbackAll(true);
		}
	}
}
