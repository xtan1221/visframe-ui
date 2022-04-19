package core.pipeline.fileformat.steps.create;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FXUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.fileformat.FileFormatBuilderFactory;
import builder.visframe.fileformat.record.RecordDataFileFormatBuilder;
import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import javafx.event.ActionEvent;

public class C2SelectRecordFileFormatTemplateStepController extends AbstractProcessStepController<FileFormat,C2SelectRecordFileFormatTemplateStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/create/C2SelectRecordFileFormatTemplateStep.fxml";
	
	@Override
	public C2SelectRecordFileFormatTemplateStepManager getManager() {
		return (C2SelectRecordFileFormatTemplateStepManager)manager;
	}
	
	@Override
	public void setStepSettings(C2SelectRecordFileFormatTemplateStepSettings settings) {
		if(settings.getSelectedRecordDataFileFormat()==null) {
			this.selectedTemplateFileFormatNameTextField.setText("");
			FXUtils.set2Disable(this.viewSelectedFileFormatTemplateDetailButton, true);
		}else {
			this.selectedTemplateFileFormatNameTextField.setText(settings.getSelectedRecordDataFileFormat().getName().getStringValue());
			FXUtils.set2Disable(this.viewSelectedFileFormatTemplateDetailButton, false);
		}
	}
	
	@Override
	public C2SelectRecordFileFormatTemplateStepSettings getStepSettings() throws SQLException, IOException {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException {
		try {
			if(this.getStepSettings().getSelectedRecordDataFileFormat()==null) {
				AlertUtils.popAlert("Error", "Template Record FileFormat is not selected!");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException("");
	}
	
	@Override
	public Pane getRootNode() {
		return rootVBox;
	}
	
	////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		this.selectedTemplateFileFormatNameTextField.setText("");
		FXUtils.set2Disable(this.viewSelectedFileFormatTemplateDetailButton, true);
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private TextField selectedTemplateFileFormatNameTextField;
	@FXML
	private Button browseFileformatButton;
	@FXML
	private Button viewSelectedFileFormatTemplateDetailButton;

	// Event Listener on Button[#browseFileformatButton].onAction
	@FXML
	public void browseFileformatTemplateButtonOnAction(ActionEvent event) throws IOException, SQLException {
		try {
			this.getManager().getFileFormatTableViewManager().refresh();
			this.getManager().getFileFormatTableViewManager().showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SelectRecordFileFormatTemplateStepController.browseFileformatTemplateButtonOnAction", e, this.getStage());
		}
	}
	
	// Event Listener on Button[#viewSelectedFileFormatTemplateDetailButton].onAction
	@FXML
	public void viewSelectedFileFormatTemplateDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		RecordDataFileFormatBuilder builder = 
				(RecordDataFileFormatBuilder) FileFormatBuilderFactory.singleton()
				.build(this.getManager().getCurrentAssignedSettings().getSelectedRecordDataFileFormat());
		
		builder.setModifiable(false);
		
		Scene scene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
		//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getSelectedRecordDataFileFormat().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
	
}
