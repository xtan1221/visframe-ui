package core.pipeline.cf.steps;

import java.io.IOException;
import java.sql.SQLException;
import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import core.pipeline.AbstractProcessStepController;
import function.composition.CompositionFunction;
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

public class ASelectOwnerCFGStepController extends AbstractProcessStepController<CompositionFunction, ASelectOwnerCFGStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/cf/steps/ASelectOwnerCFGStep.fxml";
	
	@Override
	public ASelectOwnerCFGStepManager getManager() {
		return(ASelectOwnerCFGStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectOwnerCFGStepSettings settings) {
		if(settings.getOwnerCompositionFunctionGroup()==null) {
			this.ownerCFGNameTextField.setText("");
			FXUtils.set2Disable(this.viewDetailButton, true);
		}else {
			this.ownerCFGNameTextField.setText(settings.getOwnerCompositionFunctionGroup().getName().getStringValue());
			FXUtils.set2Disable(this.viewDetailButton, false);
		}
	}
	
	@Override
	public ASelectOwnerCFGStepSettings getStepSettings() {
		return this.getManager().getCurrentAssignedSettings();
	}
	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getOwnerCompositionFunctionGroup()==null) {
			AlertUtils.popAlert("Error", "CompositionFunctionGroup Type is not selected!");
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
		this.ownerCFGNameTextField.setText("");
		FXUtils.set2Disable(this.viewDetailButton, true);
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField ownerCFGNameTextField;
	@FXML
	private Button viewDetailButton;
	@FXML
	private Button chooseOwnerCFGButton;

	// Event Listener on Button[#chooseOwnerCFGButton].onAction
	@FXML
	public void chooseOwnerCFGButtonOnAction(ActionEvent event) throws SQLException, IOException {
		/////////
		this.getManager().getCFGTableViewManager().refresh();
		this.getManager().getCFGTableViewManager().showWindow(this.getStage());
	}
	
	
	// Event Listener on Button[#viewDetailButton].onAction
	@FXML
	public void viewDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		AbstractCompositionFunctionGroupBuilder<?> builder = 
				CompositionFunctionGroupBuilderFactory.singleton(this.getManager().getProcessMainManager().getHostVisProjectDBContext())
				.build(this.getManager().getCurrentAssignedSettings().getOwnerCompositionFunctionGroup().getClass());
		
		builder.setValue(this.getManager().getCurrentAssignedSettings().getOwnerCompositionFunctionGroup(), false);
		
		builder.setModifiable(false);
		Scene scene = new Scene(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
		
		//////////////
		Stage stage = new Stage();
		
		stage.setScene(scene);
		
		stage.setWidth(1200);
		stage.setHeight(800);
		stage.initModality(Modality.NONE);
		String title = this.getManager().getCurrentAssignedSettings().getOwnerCompositionFunctionGroup().getID().toString();
		stage.setTitle(title);
		
		stage.showAndWait();
	}
}
