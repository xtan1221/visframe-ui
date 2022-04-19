package core.pipeline.visscheme.steps.importing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import basic.SimpleName;
import context.scheme.VisScheme;
import context.scheme.VisSchemeID;
import core.pipeline.AbstractProcessStepController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import session.serialization.SerializationUtils;
import session.serialization.visscheme.ExportVisSchemeManager;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class ImportFromFileStepController extends AbstractProcessStepController<VisScheme,ImportFromFileStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/steps/importing/ImportFromFileStep.fxml";
	
	@Override
	public ImportFromFileStepManager getManager() {
		// TODO Auto-generated method stub
		return (ImportFromFileStepManager)super.getManager();
	}
	
	
	@Override
	public void setStepSettings(ImportFromFileStepSettings settings) {
		if(settings.getVisSchemeFileDirString()==null) {
			this.importedFileLocationTextField.clear();
		}else {
			this.importedFileLocationTextField.setText(settings.getVisSchemeFileDirString());
		}
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		throw new UnsupportedOperationException("");
	}
	
	@Override
	public ImportFromFileStepSettings getStepSettings() {
		return new ImportFromFileStepSettings(this.importedFileLocationTextField.getText());
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public VisSchemeID getImportedVisSchemeID() throws SQLException {
		return new VisSchemeID(this.getManager().getProcessMainManager().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().findNextAvaiableUID());
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
		this.importedFileLocationTextField.textProperty().addListener(e->{
			if(this.importedFileLocationTextField.getText().isEmpty()) {
				this.originalNameTextField.setText("");
				this.importedNameTextField.setText("");
				
			}else {
				Path path = Paths.get(this.importedFileLocationTextField.getText());
				if(Files.isRegularFile(path)) {
					Object o = SerializationUtils.deserializeFromFile(path);
					
					if(o==null) {
						AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
					}else {
						VisScheme ff = (VisScheme)o;
						this.originalNameTextField.setText(ff.getName().getStringValue());
						this.importedNameTextField.setText(ff.getName().getStringValue());
					}
				}else {
					AlertUtils.popAlert("Error", "Selected File Format file is not recognized!");
				}
			}
		});
		
		
		/////////////////
		this.importedNameTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.importedNameTextField.focusedProperty().get()) {//lose focus
				try {
					new SimpleName(this.importedNameTextField.getText());
					
				}catch(Exception ex) {
					AlertUtils.popAlert("Error found", ex.getMessage());
					this.importedNameTextField.setText("");
				}
			}
		});
		
		this.importedNameTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.importedNameTextField.getParent().requestFocus();
			}
		});
		
		
	}
	
	
	
	@FXML
	private VBox rootVBox;
	@FXML
	private TextField importedFileLocationTextField;
	@FXML
	private Button browseButton;
	@FXML
	private TextField originalNameTextField;
	@FXML
	private TextField importedNameTextField;

	// Event Listener on Button[#browseButton].onAction
	@FXML
	public void browseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Visframe File Format File","*".concat(ExportVisSchemeManager.VISSCHEME_SERIALIZED_FILE_EXTENSTION)));
			File selectedFile = fileChooser.showOpenDialog(this.getStage());
			
			if(selectedFile!=null) {
				this.importedFileLocationTextField.setText(selectedFile.toString());
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("ImportFromFileStepController.browseButtonOnAction", e, this.getStage());
		}
	}
	
}
