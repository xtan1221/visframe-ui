package core.pipeline.visscheme.appliedarchive.steps;

import java.io.IOException;
import java.sql.SQLException;

import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BMakeVisSchemeAppliedArchiveStepController extends AbstractProcessStepController<VisSchemeAppliedArchive, BMakeVisSchemeAppliedArchiveStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/appliedarchive/steps/BMakeCompositionFunctionStep.fxml";
	
	@Override
	public BMakeVisSchemeAppliedArchiveStepManager getManager() {
		return(BMakeVisSchemeAppliedArchiveStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVisSchemeAppliedArchiveStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisSchemeAppliedArchive(), settings.getVisSchemeAppliedArchive()==null?true:false);
	}
	
	
	@Override
	public BMakeVisSchemeAppliedArchiveStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVisSchemeAppliedArchiveStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
	private ScrollPane builderEmbeddedRootNodeContainerScrollPane;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;

}
