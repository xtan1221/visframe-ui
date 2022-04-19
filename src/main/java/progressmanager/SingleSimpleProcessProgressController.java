package progressmanager;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SingleSimpleProcessProgressController {
	public static final String FXML_FILE_DIR_STRING = "/progressmanager/SingleSimpleProcessProgress.fxml";
	
	/////////////////////
	private SingleSimpleProcessProgressManager manager;
	
	/**
	 * 
	 * @param manager
	 */
	void setManager(SingleSimpleProcessProgressManager manager) {
		this.manager = manager;
		
		this.cancelAndRollbackButton.setVisible(this.manager.isAllowingCancelAndRollback());
	}
	
	public Parent getRootNode() {
		return this.rootContainerVBox;
	}
	
	//////////////////////////////////////
	@FXML
	public void initialize() {
		this.statusLabel.setText("Running...");
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Label statusLabel;
	@FXML
	private Button cancelAndRollbackButton;

	// Event Listener on Button[#cancelAndRollbackButton].onAction
	@FXML
	public void cancelAndRollbackButtonOnAction(ActionEvent event) throws SQLException {
		this.statusLabel.setText("Canceling...");
		
		this.manager.abortAndRollback();
	}
}
