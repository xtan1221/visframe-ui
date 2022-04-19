package core.pipeline.visscheme.appliedarchive.steps;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.VisSchemeBuilder;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.AbstractProcessStepController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FXUtils;

public class ASelectAppliedVisSchemeStepController extends AbstractProcessStepController<VisSchemeAppliedArchive, ASelectAppliedVisSchemeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/appliedarchive/steps/ASelectAppliedVisSchemeStep.fxml";
	
	@Override
	public ASelectAppliedVisSchemeStepManager getManager() {
		return(ASelectAppliedVisSchemeStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectAppliedVisSchemeStepSettings settings) {
		if(settings.getAppliedVisScheme()==null) {
			this.appliedVisSchemeUIDTextField.setText("");
			this.appliedVisSchemeNameTextField.setText("");
			FXUtils.set2Disable(this.viewDetailButton, true);
		}else {
			this.appliedVisSchemeUIDTextField.setText(Integer.toString(settings.getAppliedVisScheme().getUID()));
			this.appliedVisSchemeNameTextField.setText(settings.getAppliedVisScheme().getName().getStringValue());
			FXUtils.set2Disable(this.viewDetailButton, false);
		}
	}
	
	@Override
	public ASelectAppliedVisSchemeStepSettings getStepSettings() {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getAppliedVisScheme()==null) {
			AlertUtils.popAlert("Error", "applied VisScheme is not selected!");
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	public Pane getRootNode() {
		return rootContainerHBox;
	}
	
	
	
	///////////////////////////////////////////////////
	
	@FXML
	@Override
	public void initialize() {
		this.appliedVisSchemeUIDTextField.setText("");
		this.appliedVisSchemeNameTextField.setText("");
		FXUtils.set2Disable(this.viewDetailButton, true);
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField appliedVisSchemeUIDTextField;
	@FXML
	private TextField appliedVisSchemeNameTextField;
	@FXML
	private Button viewDetailButton;
	@FXML
	private Button chooseAppliedVisSchemeButton;
	
	// Event Listener on Button[#chooseOwnerCFGButton].onAction
	@FXML
	public void chooseAppliedVisSchemeButtonOnAction(ActionEvent event) throws SQLException, IOException {
		/////////
		this.getManager().getVisSchemeTableViewManager().refresh();
		this.getManager().getVisSchemeTableViewManager().showWindow(this.getStage());
	}
	
	
	// Event Listener on Button[#viewDetailButton].onAction
	@FXML
	public void viewDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		VisSchemeBuilder visSchemeBuilder = 
				VisSchemeBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext())
				.build(this.getManager().getCurrentAssignedSettings().getAppliedVisScheme());
		visSchemeBuilder.setModifiable(false);
		
		Scene scene = new Scene(visSchemeBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
		//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getAppliedVisScheme().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
	
}
