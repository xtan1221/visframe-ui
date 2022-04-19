package core.pipeline.fileformat.steps.create;

import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BSelectRecordBuildingModeStepController extends AbstractProcessStepController<FileFormat,BSelectRecordBuildingModeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/create/BSelectRecordBuildingModeStep.fxml";

	@Override
	public BSelectRecordBuildingModeStepManager getManager() {
		// TODO Auto-generated method stub
		return (BSelectRecordBuildingModeStepManager)this.manager;
	}

	@Override
	public void setStepSettings(BSelectRecordBuildingModeStepSettings settings) {
		this.basedOnTemplateCheckBox.setSelected(settings.isBasedOnTemplate());
	}

	@Override
	public BSelectRecordBuildingModeStepSettings getStepSettings() {
		return new BSelectRecordBuildingModeStepSettings(this.basedOnTemplateCheckBox.isSelected());
	}


	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Pane getRootNode() {
		return rootVBox;
	}
	////////////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	

	@FXML
	private VBox rootVBox;
	@FXML
	private CheckBox basedOnTemplateCheckBox;

	@Override
	public boolean validateSettingsToGoToNextStep() {
		
		return true;
	}

}
