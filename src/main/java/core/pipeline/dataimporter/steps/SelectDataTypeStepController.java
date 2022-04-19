package core.pipeline.dataimporter.steps;

import core.pipeline.AbstractProcessStepController;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.DataType;
import utils.AlertUtils;

public class SelectDataTypeStepController extends AbstractProcessStepController<DataImporter,SelectDataTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/SelectDataTypeStep.fxml";

	
	@Override
	public SelectDataTypeStepManager getManager() {
		return(SelectDataTypeStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(SelectDataTypeStepSettings settings) {
		this.recordDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.RECORD));
		this.graphDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.GRAPH));
		this.treeDataTypeRadioButton.setSelected(settings.getSelectedDataType().equals(DataType.vfTREE));
	}
	
	@Override
	public SelectDataTypeStepSettings getStepSettings() {
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
		
		return new SelectDataTypeStepSettings(selectedType);
	}

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
		throw new UnsupportedOperationException();
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
