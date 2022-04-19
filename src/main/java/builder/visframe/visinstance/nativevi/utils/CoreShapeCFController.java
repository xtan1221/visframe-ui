package builder.visframe.visinstance.nativevi.utils;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 
 * @author tanxu
 *
 */
public class CoreShapeCFController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/nativevi/utils/CoreShapeCF.fxml";
	
	///////////////////////////////////
	private CoreShapeCFManager manager;
	
	void setManager(CoreShapeCFManager manager) {
		this.manager = manager;
		
		//
		this.cfIndexTextField.setText(Integer.toString(this.manager.getCompositionFunction().getIndexID()));
		
		if(this.manager.isAssignedMandataryTargets()) {
			this.assignedMandatoryTargetRadioButton.setSelected(true);
			this.toIncludeInVisInstanceCheckBox.setSelected(true);
			this.toIncludeInVisInstanceCheckBox.setMouseTransparent(true); //always selected and not changeable
		}else {
			this.assignedMandatoryTargetRadioButton.setSelected(false);
			this.toIncludeInVisInstanceCheckBox.setSelected(true);
			this.toIncludeInVisInstanceCheckBox.setMouseTransparent(false);//can be selected or deselected
		}
		
		this.toIncludeInVisInstanceCheckBox.selectedProperty().addListener((obs,ov,nv)->{
			this.getManager().getHostCoreShapeCFGManager().updateSelectedCFNumber();
		});
		
		this.getManager().getHostCoreShapeCFGManager().updateSelectedCFNumber();
	}

	/**
	 * @return the manager
	 */
	public CoreShapeCFManager getManager() {
		return manager;
	}
	
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerHBox;
	}
	
	public CheckBox getToIncludeInVisInstanceCheckBox() {
		return this.toIncludeInVisInstanceCheckBox;
	}
	
	
	void setModifiable(boolean modifiable) {
		this.toIncludeInVisInstanceCheckBox.setMouseTransparent(!modifiable);
	}
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
		//
	}
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField cfIndexTextField;
	@FXML
	private Button viewDetailButton;
	@FXML
	private RadioButton assignedMandatoryTargetRadioButton;
	@FXML
	private CheckBox toIncludeInVisInstanceCheckBox;

	////////////////////
	
	@FXML
	public void toIncludeInVisInstanceCheckBoxOnAction(ActionEvent event) {
		this.getManager().getHostCoreShapeCFGManager().getHostNativeVisInstanceBuilder().checkFinishable();
	}
	
	//////////////////////////
	private Scene scene;
	private Stage stage;
	@FXML
	public void viewDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage==null) {
			//set builder modifiable 
			this.manager.getCompositionFunctionBuilder().setModifiable(false);
			
			scene = new Scene(this.manager.getCompositionFunctionBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	//		
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getCompositionFunction().getID().toString();
			stage.setTitle(title);
		}
		
		stage.showAndWait();
	}
	
}
