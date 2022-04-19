package core.pipeline.fileformat.steps;

import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import javafx.fxml.FXML;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.AlertUtils;

public class SelectImportModeStepController extends AbstractProcessStepController<FileFormat,SelectImportModeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/SelectImportModeStep.fxml";

	@Override
	public SelectImportModeStepManager getManager() {
		return (SelectImportModeStepManager)this.manager;
	}

	
	@Override
	public void setStepSettings(SelectImportModeStepSettings settings) {
		this.importFromExistingFileRadioButton.setSelected(settings.getType().equals(SelectImportModeStepSettings.ImportModeType.FROM_EXISTING_FILE));
		this.buildFromScratchRadioButton.setSelected(settings.getType().equals(SelectImportModeStepSettings.ImportModeType.FROM_SCRATCH));
	}
	
	
	@Override
	public SelectImportModeStepSettings getStepSettings() {
		SelectImportModeStepSettings.ImportModeType type;
		if(this.importFromExistingFileRadioButton.isSelected()) {
			type = SelectImportModeStepSettings.ImportModeType.FROM_EXISTING_FILE;
		}else if(buildFromScratchRadioButton.isSelected()) {
			type = SelectImportModeStepSettings.ImportModeType.FROM_SCRATCH;
		}else {
			type = null;
		}
		
		return new SelectImportModeStepSettings(type);
	}
	

	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getType()==null) {
			AlertUtils.popAlert("Error", "Import Mode is not selected!");
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
		return rootVBox;
	}

	
	
	//////////////////////////////////////////
	@FXML
	public void initialize() {
		System.out.println("initialize "+this.getClass().getSimpleName());
		ToggleGroup group = new ToggleGroup();
		
		group.getToggles().add(this.importFromExistingFileRadioButton);
		group.getToggles().add(this.buildFromScratchRadioButton);
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private RadioButton importFromExistingFileRadioButton;
	@FXML
	private RadioButton buildFromScratchRadioButton;

	
}
