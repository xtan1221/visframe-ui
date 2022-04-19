package core.pipeline.fileformat.steps.create;

import core.pipeline.AbstractProcessStepController;
import fileformat.FileFormat;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.DataType;
import utils.AlertUtils;
import utils.FXUtils;

public class ASelectDataTypeStepController extends AbstractProcessStepController<FileFormat,ASelectDataTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/fileformat/steps/create/ASelectDataTypeStep.fxml";
	
	@Override
	public boolean validateSettingsToGoToNextStep() {
		if(this.getStepSettings().getSelectedDataType()==null) {
			AlertUtils.popAlert("Error", "Data Type is not selected!");
			return false;
		}
		return true;
	}
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		throw new UnsupportedOperationException("only can be implemented by final steps");
	}
	
	@Override
	public ASelectDataTypeStepManager getManager() {
		return (ASelectDataTypeStepManager)this.manager;
	}
	
	@Override
	public void setStepSettings(ASelectDataTypeStepSettings settings) {
		this.recordDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.RECORD));
		this.graphDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.GRAPH));
		this.treeDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.vfTREE));
	}
	
	@Override
	public ASelectDataTypeStepSettings getStepSettings() {
		DataType selectedType;
		if(this.recordDataTypeRadioButton.isSelected()) {
			selectedType = DataType.RECORD;
		}else if(this.graphDataTypeRadioButton.isSelected()) {
			selectedType = DataType.GRAPH;
		}else if(this.treeDataTypeRadioButton.isSelected()) {
			selectedType = DataType.vfTREE;
		}else{
			selectedType = null;
		}
		
		return new ASelectDataTypeStepSettings(selectedType);
	}
	


	@Override
	public Pane getRootNode() {
		return rootVBox;
	}

	////////////////////////////////////////
	@FXML
	public void initialize() {
//		System.out.println("initialize "+this.getClass().getSimpleName());
		ToggleGroup group = new ToggleGroup();
		
		group.getToggles().add(this.recordDataTypeRadioButton);
		group.getToggles().add(this.graphDataTypeRadioButton);
		group.getToggles().add(this.treeDataTypeRadioButton);
		
		
		FXUtils.set2Disable(this.graphDataTypeRadioButton, true);
		FXUtils.set2Disable(this.treeDataTypeRadioButton, true);
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private RadioButton recordDataTypeRadioButton;
	@FXML
	private RadioButton graphDataTypeRadioButton;
	@FXML
	private RadioButton treeDataTypeRadioButton;


	
}
