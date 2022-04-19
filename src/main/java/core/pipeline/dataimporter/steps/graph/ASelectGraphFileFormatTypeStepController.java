package core.pipeline.dataimporter.steps.graph;

import java.io.IOException;
import core.pipeline.AbstractProcessStepController;
import fileformat.graph.GraphDataFileFormatType;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import utils.AlertUtils;

public class ASelectGraphFileFormatTypeStepController extends AbstractProcessStepController<DataImporter,ASelectGraphFileFormatTypeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/graph/ASelectGraphFileFormatTypeStep.fxml";
	
	@Override
	public ASelectGraphFileFormatTypeStepManager getManager() {
		return(ASelectGraphFileFormatTypeStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(ASelectGraphFileFormatTypeStepSettings settings) {
		this.graphDataFileFormatTypeChoiceBox.setValue(settings.getSelectedType());
	}
	
	@Override
	public ASelectGraphFileFormatTypeStepSettings getStepSettings() {
		return new ASelectGraphFileFormatTypeStepSettings(this.graphDataFileFormatTypeChoiceBox.getValue());
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException {
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
		for(GraphDataFileFormatType type:GraphDataFileFormatType.values()) {
			this.graphDataFileFormatTypeChoiceBox.getItems().add(type);
		}
		this.graphDataFileFormatTypeChoiceBox.setValue(GraphDataFileFormatType.SIMPLE_GEXF);
	}
	
	@FXML
	private HBox rootHBox;
	@FXML
	private ChoiceBox<GraphDataFileFormatType> graphDataFileFormatTypeChoiceBox;

}
