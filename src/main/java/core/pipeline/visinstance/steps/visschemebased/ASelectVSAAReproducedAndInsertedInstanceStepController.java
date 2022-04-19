package core.pipeline.visinstance.steps.visschemebased;

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
import visinstance.VisInstance;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.context.scheme.VisSchemeBuilder;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilder;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilderFactory;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory;
import core.pipeline.AbstractProcessStepController;
import javafx.event.ActionEvent;

public class ASelectVSAAReproducedAndInsertedInstanceStepController extends AbstractProcessStepController<VisInstance,ASelectVSAAReproducedAndInsertedInstanceStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/steps/visschemebased/ASelectVSAAReproducedAndInsertedInstanceStep.fxml";
	
	//////////////////////////////
	@Override
	public ASelectVSAAReproducedAndInsertedInstanceStepManager getManager() {
		return (ASelectVSAAReproducedAndInsertedInstanceStepManager)manager;
	}
	
	@Override
	public void setStepSettings(ASelectVSAAReproducedAndInsertedInstanceStepSettings settings) {
		if(settings.getSelectedVSAAReproducedAndInsertedInstance()==null) {
			FXUtils.set2Disable(this.viewAppliedVisSchemeDetailButton, true);
			FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveDetailButton, true);
			FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton, true);
			
			this.appliedVisSchemeNameTextField.setText("");
			this.appliedVisSchemeUIDTextField.setText("");
			this.visSchemeAppliedArchiveUIDTextField.setText("");
			this.visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField.setText("");
		}else {
			FXUtils.set2Disable(this.viewAppliedVisSchemeDetailButton, false);
			FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveDetailButton, false);
			FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton, false);
			
			this.appliedVisSchemeNameTextField.setText(this.getManager().getVisScheme().getName().getStringValue());
			this.appliedVisSchemeUIDTextField.setText(Integer.toString(this.getManager().getVisScheme().getUID()));
			this.visSchemeAppliedArchiveUIDTextField.setText(Integer.toString(this.getManager().getVisSchemeAppliedArchive().getUID()));
			this.visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField.setText(Integer.toString(this.getManager().getCurrentAssignedSettings().getSelectedVSAAReproducedAndInsertedInstance().getUID()));
		}
	}
	
	@Override
	public ASelectVSAAReproducedAndInsertedInstanceStepSettings getStepSettings() throws SQLException, IOException {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException, SQLException {
		if(this.getStepSettings().getSelectedVSAAReproducedAndInsertedInstance()==null) {
			AlertUtils.popAlert("Error", "VisSchemeAppliedArchiveReproducedAndInsertedInstance is not selected!");
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
		FXUtils.set2Disable(this.viewAppliedVisSchemeDetailButton, true);
		FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveDetailButton, true);
		FXUtils.set2Disable(this.viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton, true);
		
		this.appliedVisSchemeNameTextField.setText("");
		this.appliedVisSchemeUIDTextField.setText("");
		this.visSchemeAppliedArchiveUIDTextField.setText("");
		this.visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField.setText("");
	}
	
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button browseButton;
	@FXML
	private VBox selectedItemVBox;
	@FXML
	private TextField appliedVisSchemeUIDTextField;
	@FXML
	private TextField appliedVisSchemeNameTextField;
	@FXML
	private Button viewAppliedVisSchemeDetailButton;
	@FXML
	private TextField visSchemeAppliedArchiveUIDTextField;
	@FXML
	private Button viewVisSchemeAppliedArchiveDetailButton;
	@FXML
	private TextField visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField;
	@FXML
	private Button viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton;

	// Event Listener on Button[#browseFileformatButton].onAction
	@FXML
	public void browseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		this.getManager().getVSAAReproducedAndInsertedInstanceTableViewManager().refresh();
		this.getManager().getVSAAReproducedAndInsertedInstanceTableViewManager().showWindow(this.getStage());
	}
	
	
	@FXML
	public void viewAppliedVisSchemeDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		VisSchemeBuilder builder = VisSchemeBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext()).build(this.getManager().getVisScheme());
		
		builder.setModifiable(false);
		
		Scene visSchemeScene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
			
			//////////////
		Stage visSchemeStage = new Stage();
		
		visSchemeStage.setScene(visSchemeScene);
		
		visSchemeStage.setWidth(1200);
		visSchemeStage.setHeight(800);
		visSchemeStage.initModality(Modality.NONE);
		String title = this.getManager().getVisScheme().getID().toString();
		visSchemeStage.setTitle(title);
		
		visSchemeStage.showAndWait();
	}
	
	
	@FXML
	public void viewVisSchemeAppliedArchiveDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		VisSchemeAppliedArchiveBuilder visSchemeAppliedArchiveBuilder =
				VisSchemeAppliedArchiveBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext()).build(
						this.getManager().getVisSchemeAppliedArchive());
		visSchemeAppliedArchiveBuilder.setModifiable(false);
		
		Scene visSchemeAppliedArchiveScene = new Scene(visSchemeAppliedArchiveBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
			
			//////////////
		Stage visSchemeAppliedArchiveStage = new Stage();
		
		visSchemeAppliedArchiveStage.setScene(visSchemeAppliedArchiveScene);
		
		visSchemeAppliedArchiveStage.setWidth(1200);
		visSchemeAppliedArchiveStage.setHeight(800);
		visSchemeAppliedArchiveStage.initModality(Modality.NONE);
		String title = this.getManager().getVisSchemeAppliedArchive().getID().toString();
		visSchemeAppliedArchiveStage.setTitle(title);
		
		visSchemeAppliedArchiveStage.showAndWait();
	}
	
	
	// Event Listener on Button[#viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton].onAction
	@FXML
	public void viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder builder =
				VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext()).build(
						this.getManager().getCurrentAssignedSettings().getSelectedVSAAReproducedAndInsertedInstance());
		
		builder.setModifiable(false);
		
		Scene scene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
			
			//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getVisSchemeAppliedArchive().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
}
