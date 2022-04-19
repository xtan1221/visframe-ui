package builder.basic.misc;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class FilePathChooserEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<Path> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/misc/FilePathChooserEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		
		//any specific initialization based on the owner builder
		this.dataFileLocationTextField.textProperty().addListener((o,oldValue,newValue)->{
//			if(this.dataFileLocationTextField.getText().isEmpty()) {//default empty
//				//
//			}else {
//				try{
//					Path path = this.build();
//					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(path);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update!");
//				}
//			}
//			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public FilePathChooser getOwnerNodeBuilder() {
		return (FilePathChooser) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	@Override
	public Path build() {
		Path path = Paths.get(this.dataFileLocationTextField.getText());
		
		if(!path.toFile().exists()) {
			throw new VisframeException("selected file path does not exist!");
		}
		
		return Paths.get(this.dataFileLocationTextField.getText());
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.dataFileLocationTextField.setText("");
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public void setUIToNonNullValue(Path value) {
		this.dataFileLocationTextField.setText(value.toString());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		//TODO
		
	}
	
	@FXML
	private VBox contentVBox;
	@FXML
	private TextField dataFileLocationTextField;
	@FXML
	private Button browseDataFileButton;

	// Event Listener on Button[#browseDataFileButton].onAction
	@FXML
	public void browseDataFileButtonOnAction(ActionEvent event) {
		try {
			FileChooser fileChooser = new FileChooser();
			
			File selectedFile = fileChooser.showOpenDialog(this.getStage());
			
			if(selectedFile!=null) {
				this.dataFileLocationTextField.setText(selectedFile.toString());
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show2(this.getClass().getSimpleName().concat(".browseDataFileButtonOnAction"), e);
		}
	}
	
}
