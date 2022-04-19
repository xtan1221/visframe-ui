package core.pipeline.dataimporter.steps.vftree;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import fileformat.vftree.VfTreeDataFileFormatType;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.AlertUtils;

public class ASelectVfTreeFileFormatTypeStepController extends AbstractProcessStepController<DataImporter,ASelectVfTreeFileFormatTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/vftree/ASelectVfTreeFileFormatTypeStep.fxml";
	
	@Override
	public ASelectVfTreeFileFormatTypeStepManager getManager() {
		return (ASelectVfTreeFileFormatTypeStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectVfTreeFileFormatTypeStepSettings settings) {
		this.vftreeDataFileFormatTypeChoiceBox.setValue(settings.getSelectedType());
	}
	
	@Override
	public ASelectVfTreeFileFormatTypeStepSettings getStepSettings() throws SQLException, IOException {
		return new ASelectVfTreeFileFormatTypeStepSettings(this.vftreeDataFileFormatTypeChoiceBox.getValue());
	}

	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException, SQLException {
		if(this.getStepSettings().getSelectedType()==null) {
			AlertUtils.popAlert("Error", "Graph Data File Format type is not selected!");
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
		return rootHBox;
	}
	////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		for(VfTreeDataFileFormatType type:VfTreeDataFileFormatType.values()) {
			this.vftreeDataFileFormatTypeChoiceBox.getItems().add(type);
		}
		this.vftreeDataFileFormatTypeChoiceBox.setValue(VfTreeDataFileFormatType.SIMPLE_NEWICK_1);
	}

	@FXML
	private HBox rootHBox;
	@FXML
	private ChoiceBox<VfTreeDataFileFormatType> vftreeDataFileFormatTypeChoiceBox;

}
