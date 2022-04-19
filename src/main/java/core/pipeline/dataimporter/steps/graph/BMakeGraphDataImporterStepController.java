package core.pipeline.dataimporter.steps.graph;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BMakeGraphDataImporterStepController extends AbstractProcessStepController<DataImporter,BMakeGraphDataImporterStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/graph/BMakeGraphDataImporterStep.fxml";

	@Override
	public BMakeGraphDataImporterStepManager getManager() {
		return(BMakeGraphDataImporterStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeGraphDataImporterStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getGraphDataImporterBase(), settings.getGraphDataImporterBase()==null?true:false);
	}
	
	@Override
	public BMakeGraphDataImporterStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeGraphDataImporterStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
	}
	
	@Override
	public boolean validateSettingsToGoToNextStep() throws IOException {
		throw new UnsupportedOperationException("");
	}
	
	@Override
	public Pane getBuilderEmbeddedRootNodeContainer() {
		return builderEmbeddedRootNodeVBox;
	}
	
	@Override
	public Pane getRootNode() {
		return rootVBox;
	}

	////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		
	}
	@FXML
	private VBox rootVBox;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;

}
