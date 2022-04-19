package core.pipeline.visinstance.run.layoutconfiguration.steps;

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
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.run.VisInstanceRunBuilder;
import builder.visframe.visinstance.run.VisInstanceRunBuilderFactory;
import core.pipeline.AbstractProcessStepController;
import javafx.event.ActionEvent;

/**
 * 
 * @author tanxu
 *
 */
public class ASelectVisInstanceRunStepController extends AbstractProcessStepController<VisInstanceRunLayoutConfiguration,ASelectVisInstanceRunStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/run/layoutconfiguration/steps/ASelectVisInstanceRunStep.fxml";
	//////////////
	@Override
	public ASelectVisInstanceRunStepManager getManager() {
		return (ASelectVisInstanceRunStepManager)manager;
	}
	
	@Override
	public void setStepSettings(ASelectVisInstanceRunStepSettings settings) {
		if(settings.getSelectedVisInstanceRun()==null) {
			FXUtils.set2Disable(this.viewVisInstanceRunDetailButton, true);
			this.visInstanceRunUIDTextField.setText("");
		}else {
			FXUtils.set2Disable(this.viewVisInstanceRunDetailButton, false);
			this.visInstanceRunUIDTextField.setText(Integer.toString(this.getManager().getCurrentAssignedSettings().getSelectedVisInstanceRun().getRunUID()));
		}
	}
	
	@Override
	public ASelectVisInstanceRunStepSettings getStepSettings() throws SQLException, IOException {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException, SQLException {
		if(this.getStepSettings().getSelectedVisInstanceRun()==null) {
			AlertUtils.popAlert("Error", "VisInstanceRun is not selected!");
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
		FXUtils.set2Disable(this.viewVisInstanceRunDetailButton, true);
		this.visInstanceRunUIDTextField.setText("");
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button browseButton;
	@FXML
	private HBox selectedItemHBox;
	@FXML
	private TextField visInstanceRunUIDTextField;
	@FXML
	private Button viewVisInstanceRunDetailButton;

	// Event Listener on Button[#browseButton].onAction
	@FXML
	public void browseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.getManager().getVisInstanceRunTableViewManager().refresh();
		this.getManager().getVisInstanceRunTableViewManager().showWindow(this.getStage());
	}
	
	// Event Listener on Button[#viewVisInstanceDetailButton].onAction
	@FXML
	public void viewVisInstanceRunDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		VisInstanceRunBuilder builder = 
				VisInstanceRunBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext())
				.build(this.getManager().getCurrentAssignedSettings().getSelectedVisInstanceRun());
		
		builder.setModifiable(false);
		
		Scene scene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
		//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getSelectedVisInstanceRun().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
	
}
