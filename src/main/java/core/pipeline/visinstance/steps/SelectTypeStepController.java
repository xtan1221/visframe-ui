package core.pipeline.visinstance.steps;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import visinstance.VisInstance;

public class SelectTypeStepController extends AbstractProcessStepController<VisInstance,SelectTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/steps/SelectTypeStep.fxml";
	
	@Override
	public SelectTypeStepManager getManager() {
		return (SelectTypeStepManager)this.manager;
	}

	
	@Override
	public void setStepSettings(SelectTypeStepSettings settings) {
		this.nativeVisInstanceRadioButton.setSelected(settings.getType().equals(SelectTypeStepSettings.VisInstanceType.NATIVE));
		this.visSchemeBasedVisInstanceRadioButton.setSelected(settings.getType().equals(SelectTypeStepSettings.VisInstanceType.VISSCHEME_BASED));
	}
	
	
	@Override
	public SelectTypeStepSettings getStepSettings() {
		SelectTypeStepSettings.VisInstanceType type;
		if(this.nativeVisInstanceRadioButton.isSelected()) {
			type = SelectTypeStepSettings.VisInstanceType.NATIVE;
		}else if(visSchemeBasedVisInstanceRadioButton.isSelected()) {
			type = SelectTypeStepSettings.VisInstanceType.VISSCHEME_BASED;
		}else {
			type = null;
		}
		
		return new SelectTypeStepSettings(type);
	}
	

	
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getType()==null) {
			AlertUtils.popAlert("Error", "Type is not selected!");
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
//		System.out.println("initialize "+this.getClass().getSimpleName());
		ToggleGroup group = new ToggleGroup();
		
		group.getToggles().add(this.nativeVisInstanceRadioButton);
		group.getToggles().add(this.visSchemeBasedVisInstanceRadioButton);
		
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private RadioButton nativeVisInstanceRadioButton;
	@FXML
	private RadioButton visSchemeBasedVisInstanceRadioButton;


	
}
