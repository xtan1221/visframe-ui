package image.save;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import utils.AlertUtils;
import utils.FileIOUtils;

public final class FXNodeSaveAsImageController {
	public static final String FXML_FILE_DIR_STRING = "/image/save/FXNodeSaveAsImage.fxml";
	
	///////////////////////
	private FXNodeSaveAsImageManager manager;
	
	/**
	 * 
	 * @param manager
	 * @throws SQLException
	 */
	void setManager(FXNodeSaveAsImageManager manager) throws SQLException {
		this.manager = manager;
	}
	
	
	/////////////////////////
	FXNodeSaveAsImageManager getManager() {
		return this.manager;
	}
	
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	///////////////////////////////////////////////////	
	@FXML
	public void initialize() {
		//see https://stackoverflow.com/questions/59089118/javax-imageio-imageio-file-format-constants
//		this.fileFormatChoiceBox.getItems().add("BMP"); //NOT WORKING?
		this.fileFormatChoiceBox.getItems().add("GIF");
		this.fileFormatChoiceBox.getItems().add("JPEG");
		this.fileFormatChoiceBox.getItems().add("PNG");
		this.fileFormatChoiceBox.getItems().add("TIF");
//		this.fileFormatChoiceBox.getItems().add("WBMP"); //NOT WORKING?
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField selectedDirectoryPathStringTextField;
	@FXML
	private Button chooseDirButton;
	@FXML
	private TextField fileNameTextField;
	@FXML
	private ChoiceBox<String> fileFormatChoiceBox;
	@FXML
	private Button clearButton;
	@FXML
	private Button saveButton;

	// Event Listener on Button[#chooseDirButton].onAction
	@FXML
	public void chooseDirButtonOnAction(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedPath = directoryChooser.showDialog(this.rootContainerVBox.getScene().getWindow());
		
		if(selectedPath!=null)
			this.selectedDirectoryPathStringTextField.setText(selectedPath.toString());
		else
			this.selectedDirectoryPathStringTextField.setText("");
	}
	
	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.selectedDirectoryPathStringTextField.setText("");
		this.fileNameTextField.setText("");
	}
	
	// Event Listener on Button[#saveButton].onAction
	@FXML
	public void saveButtonOnAction(ActionEvent event) throws IOException {
		if(this.fileNameTextField.getText().isEmpty()) {
			AlertUtils.popAlert("Error", "file name is not given!");
			return;
		}
		if(this.selectedDirectoryPathStringTextField.getText().isEmpty()) {
			AlertUtils.popAlert("Error", "directory path is not selected!");
			return;
		}
		if(this.fileFormatChoiceBox.getSelectionModel().getSelectedItem()==null) {
			AlertUtils.popAlert("Error", "Image file format is not selected!");
			return;
		}
		
		//////////////
		String fileFormatType = this.fileFormatChoiceBox.getSelectionModel().getSelectedItem();
		
//		Path projectParentPath = Paths.get(this.selectedDirectoryPathStringTextField.getText());
//		
//		Path imageFilePath = Paths.get(projectParentPath.toString(), this.fileNameTextField.getText().concat(".").concat(fileFormatType));
//		
//		if(Files.exists(imageFilePath)) {//the target file path is already existing
//			if(Files.isDirectory(imageFilePath)) {//the target file path is an existing directory, cannot proceed
//				AlertUtils.popAlert("Error", "An existing folder is found to locate at the given path: \n\t"+imageFilePath.toString()+"\nCannot save image file!");
//				return;
//			}else {//the target file path is an existing file, need to ask for confirmation
//				Optional<ButtonType> result = 
//						AlertUtils.popConfirmationDialog(
//								"Confirmation is needed!", 
//								"Overwrite file?", 
//								"An existing file with the same name at the give location is found! Overwrite it?");
//				if (result.get() == ButtonType.CANCEL){
//					return;
//				}
//			}
//		}
		
		//first check if output file name is valid or not
		try {
			FileIOUtils.validateFileName(this.fileNameTextField.getText());
		}catch(InvalidPathException e){
			AlertUtils.popAlert("invalid file name", e.getMessage());
			
			return;
		}
		
		//try to build the full path of the target image file - UN-tested TODO
		Path imageFilePath = 
				FileIOUtils.buildFilePath(
						this.selectedDirectoryPathStringTextField.getText(), 
						this.fileNameTextField.getText(), 
						fileFormatType);
		if(imageFilePath==null)
			return;
		
		FXNodeTiledSnapShot.exportPng(this.getManager().getFXNode(), imageFilePath.toString(), fileFormatType);
//		Group group = new Group();
//		Scene scene = new Scene(group, this.getManager().getNodeWidth(), this.getManager().getNodeHeight());
//		
//		group.getChildren().add(this.getManager().getFXNode());
//		
//		//Saving the scene as image
//		//Exception thrown from snapshot if dimensions are larger than max texture size
//		//see https://bugs.openjdk.java.net/browse/JDK-8088198 and https://github.com/openjdk/jfx/pull/68
//		WritableImage image = scene.snapshot(null);
//		
//		RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
//		
//		ImageIO.write(renderedImage, fileFormatType, imageFilePath.toFile());
//		
//		//
//		group.getChildren().clear();
		
		this.popupSuccessfullyRolledbackMessageWindow(imageFilePath);
	}
	
	/**
	 * 
	 * @param imageFile
	 */
	private void popupSuccessfullyRolledbackMessageWindow(Path imageFile) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Image is successfully saved!");
		alert.setContentText("view saved image in ".concat(imageFile.toString()));
		
		alert.showAndWait();
	}
	
	
}
