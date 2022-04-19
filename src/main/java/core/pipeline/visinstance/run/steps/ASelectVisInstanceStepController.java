package core.pipeline.visinstance.run.steps;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FXUtils;
import visinstance.run.VisInstanceRun;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.VisInstanceBuilderBase;
import builder.visframe.visinstance.VisInstanceBuilderFactory;
import core.pipeline.AbstractProcessStepController;
import javafx.event.ActionEvent;

/**
 * 
 * @author tanxu
 *
 */
public class ASelectVisInstanceStepController extends AbstractProcessStepController<VisInstanceRun,ASelectVisInstanceStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/run/steps/ASelectVisInstanceStep.fxml";
	//////////////
	@Override
	public ASelectVisInstanceStepManager getManager() {
		return (ASelectVisInstanceStepManager)manager;
	}
	
	@Override
	public void setStepSettings(ASelectVisInstanceStepSettings settings) {
		if(settings.getSelectedVisInstance()==null) {
			FXUtils.set2Disable(this.viewVisInstanceDetailButton, true);
			this.visInstanceUIDTextField.setText("");
			this.visInstanceNameTextField.setText("");
		}else {
			FXUtils.set2Disable(this.viewVisInstanceDetailButton, false);
			this.visInstanceUIDTextField.setText(Integer.toString(settings.getSelectedVisInstance().getUID()));
			this.visInstanceNameTextField.setText(settings.getSelectedVisInstance().getName().getStringValue());
		}
	}
	
	@Override
	public ASelectVisInstanceStepSettings getStepSettings() throws SQLException, IOException {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException, SQLException {
		if(this.getStepSettings().getSelectedVisInstance()==null) {
			AlertUtils.popAlert("Error", "VisInstance is not selected!");
			return false;
		}
		return true;
	}
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException("only implemented by final steps of a pipeline!");
	}
	
	@Override
	public Pane getRootNode() {
		return this.rootContainerVBox;
	}
	
	
	////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		FXUtils.set2Disable(this.viewVisInstanceDetailButton, true);
		this.visInstanceUIDTextField.setText("");
		this.visInstanceNameTextField.setText("");
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button browseButton;
	@FXML
	private HBox selectedItemHBox;
	@FXML
	private TextField visInstanceUIDTextField;
	@FXML
	private TextField visInstanceNameTextField;
	@FXML
	private Button viewVisInstanceDetailButton;

	
	// Event Listener on Button[#browseFileformatButton].onAction
	@FXML
	public void browseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.getManager().getVisInstanceTableViewManager().refresh();
		this.getManager().getVisInstanceTableViewManager().showWindow(this.getStage());
	}
	

	// Event Listener on Button[#viewVisInstanceDetailButton].onAction
	@FXML
	public void viewVisInstanceDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		VisInstanceBuilderBase<?,?> builder = 
				VisInstanceBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext())
				.build(this.getManager().getCurrentAssignedSettings().getSelectedVisInstance());
		
		builder.setModifiable(false);
		
		Scene scene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
			//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getSelectedVisInstance().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
	
}
