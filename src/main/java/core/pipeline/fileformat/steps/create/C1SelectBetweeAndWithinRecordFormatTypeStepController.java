package core.pipeline.fileformat.steps.create;

import core.pipeline.AbstractProcessStepController;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.BetweenRecordFormatType;
import core.pipeline.fileformat.steps.create.C1SelectBetweeAndWithinRecordFormatTypeStepSettings.WithinRecordFormatType;
import fileformat.FileFormat;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.AlertUtils;

public class C1SelectBetweeAndWithinRecordFormatTypeStepController extends AbstractProcessStepController<FileFormat,C1SelectBetweeAndWithinRecordFormatTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/create/C1SelectBetweeAndWithinRecordFormatTypeStep.fxml";
	
	@Override
	public C1SelectBetweeAndWithinRecordFormatTypeStepManager getManager() {
		// TODO Auto-generated method stub
		return (C1SelectBetweeAndWithinRecordFormatTypeStepManager)this.manager;
	}
	
	@Override
	public void setStepSettings(C1SelectBetweeAndWithinRecordFormatTypeStepSettings settings) {
		// TODO Auto-generated method stub
		this.betweenRecordFormatTypeChoiceBox.setValue(settings.getBetweenRecordFormatType());
		this.withinRecordFormatTypeChoiceBox.setValue(settings.getWithinRecordFormatType());
	}
	
	@Override
	public C1SelectBetweeAndWithinRecordFormatTypeStepSettings getStepSettings() {
		// TODO Auto-generated method stub
		return new C1SelectBetweeAndWithinRecordFormatTypeStepSettings(this.betweenRecordFormatTypeChoiceBox.getValue(), this.withinRecordFormatTypeChoiceBox.getValue());
	}

	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getBetweenRecordFormatType()==null) {
			AlertUtils.popAlert("Error", "BetweenRecordFormatType is not set!");
			
			return false;
		}else if(this.getStepSettings().getWithinRecordFormatType()==null) {
			AlertUtils.popAlert("Error", "WithinRecordFormatType is not set!");
			
			return false;
		}
		
		return true;
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
	///////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		for(BetweenRecordFormatType type:BetweenRecordFormatType.values()) {
			betweenRecordFormatTypeChoiceBox.getItems().add(type);
		}
		
		for(WithinRecordFormatType type:WithinRecordFormatType.values()) {
			withinRecordFormatTypeChoiceBox.getItems().add(type);
		}
	}
	@FXML
	private VBox rootVBox;
	@FXML
	private ChoiceBox<BetweenRecordFormatType> betweenRecordFormatTypeChoiceBox;
	@FXML
	private ChoiceBox<WithinRecordFormatType> withinRecordFormatTypeChoiceBox;
	
	
	
}
