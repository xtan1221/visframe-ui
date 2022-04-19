package core.pipeline.dataimporter.steps.record;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import importer.DataImporter;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MakeRecordDataImporterStepController extends AbstractProcessStepController<DataImporter,MakeRecordDataImporterStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/dataimporter/steps/record/MakeRecordDataImporterStep.fxml";
	
	@Override
	public MakeRecordDataImporterStepManager getManager() {
		return(MakeRecordDataImporterStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(MakeRecordDataImporterStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getRecordDataImporter(),settings.getRecordDataImporter()==null?true:false);
	}
	
	@Override
	public MakeRecordDataImporterStepSettings getStepSettings() throws SQLException, IOException {
		return new MakeRecordDataImporterStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
		// TODO Auto-generated method stub
		
	}
	@FXML
	private VBox rootVBox;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;
}
