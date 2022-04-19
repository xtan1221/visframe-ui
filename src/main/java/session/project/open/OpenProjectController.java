package session.project.open;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import basic.SimpleName;
import exception.VisframeException;
import javafx.event.ActionEvent;

import javafx.scene.control.ScrollPane;

import javafx.scene.control.RadioButton;

import javafx.scene.control.CheckBox;

import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FXUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class OpenProjectController {
	public static final String FXML_FILE_DIR = "/session/project/open/OpenProject.fxml";
	
	private OpenProjectManager manager;
	
	
	public void setManager(OpenProjectManager manager) throws IOException {
		this.manager = manager;
		
		this.addExistingProjects();
		
		this.getStage().setOnCloseRequest(e->{
			this.cancelButtonOnAction(null);
		});
		
	}
	
	public OpenProjectManager getManager() {
		return manager;
	}
	
	
	private ToggleGroup detectedExistingProjectEntryToggleGroup;
	private Map<RadioButton, ExistingProjectEntryController> detectedExistingProjectEntryRadioButtonControllerMap;
	
	/**
	 * should be invoked every time a project is to be opened;
	 * @throws IOException
	 */
	void addExistingProjects() throws IOException {
		this.detectedExistingProjectEntryVBox.getChildren().clear();
		
		detectedExistingProjectEntryToggleGroup = new ToggleGroup();
		detectedExistingProjectEntryRadioButtonControllerMap = new HashMap<>();
		
		for(SimpleName projectName:this.getManager().getProjectLogFileProcessor().getAllExistingProjectNamePathMap().keySet()) {
			//skip projects that are already opened;
			if(this.getManager().getSessionManager().getOpenedProjectNameManagerMap().keySet().contains(projectName)) {
				continue;
			}
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ExistingProjectEntryController.FXML_FILE_DIR));
			
			Parent node = loader.load();
			
			ExistingProjectEntryController controller = (ExistingProjectEntryController)loader.getController();
			
			controller.setAll(projectName, this.getManager().getProjectLogFileProcessor().getAllExistingProjectNamePathMap().get(projectName));
			
			detectedExistingProjectEntryToggleGroup.getToggles().add(controller.getSelectRadioButton());
			
			detectedExistingProjectEntryRadioButtonControllerMap.put(controller.getSelectRadioButton(), controller);
			
			this.detectedExistingProjectEntryVBox.getChildren().add(node);
		}
		
	}
	
	Stage getStage() {
		return (Stage) this.createNewProjectVBox.getScene().getWindow();
	}
	
	////////////////////////
	private DirectoryChooser directoryChooser = new DirectoryChooser();
	
	private ToggleGroup modeToggleGroup;
	
	
	@FXML
	public void initialize() {
		this.modeToggleGroup = new ToggleGroup();
		
		this.modeToggleGroup.getToggles().add(this.createNewProjectRadioButton);
		this.modeToggleGroup.getToggles().add(this.selectExistingProjectRadioButton);
		
		
		this.modeToggleGroup.selectedToggleProperty().addListener(e->{
			if(this.createNewProjectRadioButton.isSelected()) {
				FXUtils.set2Disable(this.createNewProjectVBox, false);
				FXUtils.set2Disable(this.selectExistingProjectVBox, true);
			}else {// if(this.selectExistingProjectRadioButton.isSelected()){
				FXUtils.set2Disable(this.createNewProjectVBox, true);
				FXUtils.set2Disable(this.selectExistingProjectVBox, false);
			}
			
		});
		
		
		this.createNewProjectRadioButton.setSelected(true);
		///
		this.locateExistingProjectCheckBox.setSelected(false);
		this.locateExistingProjectCheckBoxOnAction(null);
		
		
	}
	
	
	@FXML
	private RadioButton createNewProjectRadioButton;
	@FXML
	private RadioButton selectExistingProjectRadioButton;
	@FXML
	private VBox createNewProjectVBox;
	@FXML
	private TextField newProjectNameTextField;
	@FXML
	private TextField newProjectLocationTextField;
	@FXML
	private Button browseNewProjectLocationButton;
	@FXML
	private VBox selectExistingProjectVBox;
	@FXML
	private CheckBox locateExistingProjectCheckBox;
	@FXML
	private VBox locateExistingProjectVBox;
	@FXML
	private TextField locateExistingProjectLocationTextField;
	@FXML
	private Button browseExistingProjectLocationButton;
	@FXML
	private VBox selectFromDetectedExistingProjectsVBox;
	@FXML
	private ScrollPane selectExistingProjectScrollPane;
	@FXML
	private VBox detectedExistingProjectEntryVBox;
	@FXML
	private Button finishButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button resetButton;

	// Event Listener on Button[#browseNewProjectLocationButton].onAction
	@FXML
	public void browseNewProjectLocationButtonOnAction(ActionEvent event) {
		
		File selectedDirectory = directoryChooser.showDialog(this.getStage());
		
		this.newProjectLocationTextField.setText(selectedDirectory.toString());
	}
	// Event Listener on Button[#browseExistingProjectLocationButton].onAction
	@FXML
	public void browseExistingProjectLocationButtonOnAction(ActionEvent event) {
		File selectedDirectory = directoryChooser.showDialog(this.getStage());
		
		if(selectedDirectory!=null)
			this.locateExistingProjectLocationTextField.setText(selectedDirectory.toString());
	}
	
	
	// Event Listener on CheckBox[#locateExistingProjectCheckBox].onAction
	@FXML
	public void locateExistingProjectCheckBoxOnAction(ActionEvent event) {
		if(this.locateExistingProjectCheckBox.isSelected()) {
			FXUtils.set2Disable(this.locateExistingProjectVBox, false);
			FXUtils.set2Disable(this.selectFromDetectedExistingProjectsVBox, true);
		}else {
			
			FXUtils.set2Disable(this.locateExistingProjectVBox, true);
			FXUtils.set2Disable(this.selectFromDetectedExistingProjectsVBox, false);
		}
	}
	// Event Listener on Button[#finishButton].onAction
	@FXML
	public void finishButtonOnAction(ActionEvent event) throws IOException {
		SimpleName projectName = null;
		Path projectParentPath = null;
		Boolean newProject = null;
		
		try {
			if(this.createNewProjectRadioButton.isSelected()) {//create new project
				
					projectName = new SimpleName(this.newProjectNameTextField.getText());
					///note that project name should be unique in the same system;
					if(this.getManager().getProjectLogFileProcessor().getAllExistingProjectNamePathMap().containsKey(projectName)) {
						throw new VisframeException("Given project name already exists!");
					}
					
					projectParentPath = Paths.get(this.newProjectLocationTextField.getText());
					
					newProject = true;
					
					Path projectFolderPath = Paths.get(projectParentPath.toString(), projectName.getStringValue());
					
					if(Files.isDirectory(projectFolderPath)) {//only true if the path is an existing directory
						throw new VisframeException("Folder for new Project already exists!!!!");
					}
					
			}else if(this.selectExistingProjectRadioButton.isSelected()) {//existing project
				if(this.locateExistingProjectCheckBox.isSelected()) {
					Path projectPath = Paths.get(this.locateExistingProjectLocationTextField.getText());
					
					//
					projectName = new SimpleName(projectPath.getFileName().toString());
					//
					projectParentPath = projectPath.getParent();
					
//					System.out.println(projectParentPath.toString());
				}else {//select from detected existing projects
					if(this.detectedExistingProjectEntryToggleGroup.getSelectedToggle()==null) {
//						throw new VisframeException("No existing project is selected!");
						AlertUtils.popAlert("Error", "No existing project is selected!");
						return;
					}
					
					RadioButton rb = (RadioButton)this.detectedExistingProjectEntryToggleGroup.getSelectedToggle();
					
					projectName = new SimpleName(this.detectedExistingProjectEntryRadioButtonControllerMap.get(rb).getProjectNameTextField().getText());
					
					projectParentPath = Paths.get(this.detectedExistingProjectEntryRadioButtonControllerMap.get(rb).getProjectLocationTextField().getText()).getParent();
//					System.out.println(projectParentPath.toString());
				}
				
				newProject = false;
				
			}else {
				throw new VisframeException("debug");
			}
			
			
			this.getManager().finish(projectName, projectParentPath, newProject);
			
		}catch(Exception e) {
//			AlertUtils.makeAlert("Error occured", e.getMessage()).showAndWait();
			
			
			
			ExceptionHandlerUtils.show("test", e, this.getStage());
		}
		
	}
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		try {
			this.manager.finish(null, null, null);
		}catch(Exception e) {
			ExceptionHandlerUtils.show("OpenProjectController.cancelButtonOnAction", e, this.getStage());
		}
	}
	
	// Event Listener on Button[#resetButton].onAction
	@FXML
	public void resetButtonOnAction(ActionEvent event) {
		try {
			this.locateExistingProjectLocationTextField.setText("");
			this.newProjectLocationTextField.setText("");
			this.newProjectNameTextField.setText("");
			
			this.createNewProjectRadioButton.setSelected(true);
		}catch(Exception e) {
			ExceptionHandlerUtils.show("OpenProjectController.resetButtonOnAction", e, this.getStage());
		}
	}
	
	
}
