package core.pipeline.dataimporter.steps.vftree;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


public class BMakeVfTreeDataImporterStepController extends AbstractProcessStepController<DataImporter,BMakeVfTreeDataImporterStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/vftree/BMakeVfTreeDataImporterStep.fxml";
	
	@Override
	public BMakeVfTreeDataImporterStepManager getManager() {
		return(BMakeVfTreeDataImporterStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVfTreeDataImporterStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVfTreeDataImporterBase(),settings.getVfTreeDataImporterBase()==null?true:false);
	}
	
	@Override
	public BMakeVfTreeDataImporterStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVfTreeDataImporterStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
