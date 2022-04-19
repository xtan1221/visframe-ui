package core.pipeline.fileformat.steps.importing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import basic.SimpleName;
import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import metadata.DataType;
import session.serialization.SerializationUtils;
import session.serialization.fileformat.ExportFileFormatManager;
import utils.AlertUtils;
import utils.FXUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class ImportFromFileStepController extends AbstractProcessStepController<FileFormat,ImportFromFileStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/importing/ImportFromFileStep.fxml";
	
	@Override
	public ImportFromFileStepManager getManager() {
		// TODO Auto-generated method stub
		return (ImportFromFileStepManager)super.getManager();
	}
	
	
	@Override
	public void setStepSettings(ImportFromFileStepSettings settings) {
		if(settings.getFileFormatFileDirString()==null) {
			this.fileFormatFileLocationTextField.clear();
		}else {
			this.fileFormatFileLocationTextField.setText(settings.getFileFormatFileDirString());
		}
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		throw new UnsupportedOperationException("");
	}
	
	@Override
	public ImportFromFileStepSettings getStepSettings() {
		return new ImportFromFileStepSettings(this.fileFormatFileLocationTextField.getText());
	}
	
	public FileFormatID getImportedFileFormatID() {
		if(this.fileFormatImportedNameTextField.getText().isEmpty()||this.fileFormatTypeTextField.getText().isEmpty()) {
			return null;
		}else {
			return new FileFormatID(new SimpleName(this.fileFormatImportedNameTextField.getText()),DataType.valueOf(this.fileFormatTypeTextField.getText()));
		}
	}


	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public Pane getRootNode() {
		return rootVBox;
	}
	///////////////////////////
	
	@FXML
	@Override
	public void initialize() {
		this.fileFormatFileLocationTextField.textProperty().addListener(e->{
			if(this.fileFormatFileLocationTextField.getText().isEmpty()) {
				this.fileFormatTypeTextField.setText("");
				this.fileFormatOriginalNameTextField.setText("");
				this.fileFormatImportedNameTextField.setText("");
				FXUtils.set2Disable(this.checkFileFormatIDExistenceButton, true);
				this.fileFormatIDAlreadyExistLabel.setText("");
				
			}else {
				Path path = Paths.get(this.fileFormatFileLocationTextField.getText());
				if(Files.isRegularFile(path)) {
					Object o = SerializationUtils.deserializeFromFile(path);
					
					if(o==null) {
						AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
					}else {
						FileFormat ff = (FileFormat)o;
						this.fileFormatTypeTextField.setText(ff.getDataType().toString());
						this.fileFormatOriginalNameTextField.setText(ff.getName().getStringValue());
						this.fileFormatImportedNameTextField.setText(ff.getName().getStringValue());
						FXUtils.set2Disable(this.checkFileFormatIDExistenceButton, false);
					}
				}else {
					AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
				}
			}
		});
		
		/////
		this.fileFormatImportedNameTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.fileFormatImportedNameTextField.focusedProperty().get()) {//lose focus
				try {
					new SimpleName(this.fileFormatImportedNameTextField.getText());
					
				}catch(Exception ex) {
					AlertUtils.popAlert("Error found", ex.getMessage());
					this.fileFormatImportedNameTextField.setText("");
				}
			}
		});
		
		this.fileFormatImportedNameTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.fileFormatImportedNameTextField.getParent().requestFocus();
			}
		});
	}
	@FXML
	private VBox rootVBox;
	@FXML
	private TextField fileFormatFileLocationTextField;
	@FXML
	private Button browseButton;
	@FXML
	private TextField fileFormatTypeTextField;
	@FXML
	private TextField fileFormatOriginalNameTextField;
	@FXML
	private TextField fileFormatImportedNameTextField;
	@FXML
	private Button checkFileFormatIDExistenceButton;
	@FXML
	private Label fileFormatIDAlreadyExistLabel;
	
	// Event Listener on Button[#browseButton].onAction
	@FXML
	public void browseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Visframe File Format File","*".concat(ExportFileFormatManager.FILE_FORMAT_SERIALIZED_FILE_EXTENSTION)));
			File selectedFile = fileChooser.showOpenDialog(this.getStage());
			
			if(selectedFile!=null) {
				this.fileFormatFileLocationTextField.setText(selectedFile.toString());
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("ImportFromFileStepController.browseButtonOnAction", e, this.getStage());
		}
	}
	
	
	// Event Listener on Button[#checkFileFormatIDExistenceButton].onAction
	@FXML
	public void checkFileFormatIDExistenceButtonOnAction(ActionEvent event) throws IOException, SQLException {
		try {
			FileFormatID importedID = getImportedFileFormatID();
			if(importedID==null) {
				AlertUtils.popAlert("Error", "File format type and/or imported name is empty");
			}else {
				if(this.getManager().doesFileFormatIDExist(importedID)) {
					AlertUtils.popAlert("Error", "Given imported file format name already exist in the project, change to another one!");
					this.fileFormatIDAlreadyExistLabel.setText("duplicate!");
				}else {
					this.fileFormatIDAlreadyExistLabel.setText("passed");
				}
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("ImportFromFileStepController.checkFileFormatIDExistenceButtonOnAction", e, this.getStage());
		}
	}



}
