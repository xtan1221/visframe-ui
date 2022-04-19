package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilder;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilderFactory;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
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

public class ASelectVisSchemeAppliedArchiveStepController extends AbstractProcessStepController<VisSchemeAppliedArchiveReproducedAndInsertedInstance, ASelectVisSchemeAppliedArchiveStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/appliedarchive/reproducedandinsertedinstance/steps/ASelectVisSchemeAppliedArchiveStep.fxml";
	
	@Override
	public ASelectVisSchemeAppliedArchiveStepManager getManager() {
		return(ASelectVisSchemeAppliedArchiveStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectVisSchemeAppliedArchiveStepSettings settings) throws SQLException {
		if(settings.getVisSchemeAppliedArchive()==null) {
			FXUtils.set2Disable(this.viewDetailButton, true);
			this.visSchemeAppliedArchiveUIDTextField.setText("");
		}else {
			FXUtils.set2Disable(this.viewDetailButton, false);
			this.visSchemeAppliedArchiveUIDTextField.setText(Integer.toString(settings.getVisSchemeAppliedArchive().getUID()));
		}
	}
	
	@Override
	public ASelectVisSchemeAppliedArchiveStepSettings getStepSettings() {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getVisSchemeAppliedArchive()==null) {
			AlertUtils.popAlert("Error", "VisSchemeAppliedArchive is not selected!");
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
		FXUtils.set2Disable(this.viewDetailButton, true);
		this.visSchemeAppliedArchiveUIDTextField.setText("");
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField visSchemeAppliedArchiveUIDTextField;
	@FXML
	private Button viewDetailButton;
	@FXML
	private Button chooseVisSchemeAppliedArchiveButton;
//	@FXML
//	private Button clearSelectionButton;
	
	// Event Listener on Button[#chooseOwnerCFGButton].onAction
	@FXML
	public void chooseVisSchemeAppliedArchiveButtonOnAction(ActionEvent event) throws SQLException, IOException {
		this.getManager().getVisSchemeAppliedArchiveTableViewManager().refresh();
		this.getManager().getVisSchemeAppliedArchiveTableViewManager().showWindow(this.getStage());
	}
	
	

	// Event Listener on Button[#viewDetailButton].onAction
	@FXML
	public void viewDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		VisSchemeAppliedArchiveBuilder visSchemeAppliedArchiveBuilder = 
				VisSchemeAppliedArchiveBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext())
				.build(this.getManager().getCurrentAssignedSettings().getVisSchemeAppliedArchive());
		visSchemeAppliedArchiveBuilder.setModifiable(false);
		
		Scene visSchemeAppliedArchiveScene = new Scene(visSchemeAppliedArchiveBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
	
		Stage visSchemeAppliedArchiveStage = new Stage();
		
		visSchemeAppliedArchiveStage.setScene(visSchemeAppliedArchiveScene);
		
		visSchemeAppliedArchiveStage.setWidth(1200);
		visSchemeAppliedArchiveStage.setHeight(800);
		visSchemeAppliedArchiveStage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getVisSchemeAppliedArchive().getID().toString();
		visSchemeAppliedArchiveStage.setTitle(title);
		
		visSchemeAppliedArchiveStage.showAndWait();
	}

}
