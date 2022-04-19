package utils.exceptionhandler;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

public class ExceptionHandlerController {
	public static final String FXML_FILE_DIR = "/utils/exceptionhandler/ExceptionHandler.fxml";
	
	
	void setAll(String title, String briefMessage, String detailsInfor) {
		this.titleLabel.setText(title);
		
		this.briefMessageTextArea.setText(briefMessage);
		
		this.detailsTextArea.setText(detailsInfor);
	}
	
	
	
	
	@FXML
	private Label titleLabel;
	@FXML
	private TextArea briefMessageTextArea;
	@FXML
	private TextArea detailsTextArea;
	
}
