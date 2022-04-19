package session.project.open;

import java.nio.file.Path;

import basic.SimpleName;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javafx.scene.control.RadioButton;

public class ExistingProjectEntryController {
	public static final String FXML_FILE_DIR = "/session/project/open/ExistingProjectEntry.fxml";
	
	/**
	 * 
	 * @param projectName
	 * @param projectFullPath
	 */
	public void setAll(SimpleName projectName, Path projectFullPath) {
		this.projectNameTextField.setText(projectName.getStringValue());
		this.projectLocationTextField.setText(projectFullPath.toString());
	}

	public RadioButton getSelectRadioButton() {
		return this.selectRadioButton;
	}
	
	public TextField getProjectNameTextField() {
		return projectNameTextField;
	}


	public void setProjectNameTextField(TextField projectNameTextField) {
		this.projectNameTextField = projectNameTextField;
	}


	public TextField getProjectLocationTextField() {
		return projectLocationTextField;
	}


	public void setProjectLocationTextField(TextField projectLocationTextField) {
		this.projectLocationTextField = projectLocationTextField;
	}
	
	//////////////////////////////
	
	@FXML
	public void initialize() {
		
	}
	

	@FXML
	private RadioButton selectRadioButton;
	@FXML
	private TextField projectNameTextField;
	@FXML
	private TextField projectLocationTextField;

}
