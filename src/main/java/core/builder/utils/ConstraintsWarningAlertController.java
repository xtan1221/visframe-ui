package core.builder.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ConstraintsWarningAlertController {
	static final String FXML_FILE_DIR_STRING = "/core/builder/utils/ConstraintsWarningAlert.fxml";
	
	
	private ConstraintsViolotionWarningAlertManager manager;
	
	/**
	 * set manager and initialize
	 * @param manager
	 */
	void setManager(ConstraintsViolotionWarningAlertManager manager) {
		this.manager = manager;
		
		
		this.mainMessageLabel.setText(this.manager.getMainMessageString());
		this.detailedMessageTextArea.setText(this.manager.buildDetailedMessageString());
		
		
	}
	
	Parent getRootNode() {
		return this.rootVBox;
	}

	////////////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	
	@FXML
	private VBox rootVBox;
	@FXML
	private Label mainMessageLabel;
	@FXML
	private TextArea detailedMessageTextArea;
	@FXML
	private Button continueEditingButton;
	@FXML
	private Button rollbackAndExitButton;

	// Event Listener on Button[#continueEditingButton].onAction
	@FXML
	public void continueEditingButtonOnAction(ActionEvent event) {
		this.manager.setToContinueEditing();
		
		this.manager.getPopupWindow().close();
	}
	// Event Listener on Button[#rollbackAndExitButton].onAction
	@FXML
	public void rollbackAndExitButtonOnAction(ActionEvent event) {
		this.manager.setToRollbackAndExit();
		
		this.manager.getPopupWindow().close();
	}
}
