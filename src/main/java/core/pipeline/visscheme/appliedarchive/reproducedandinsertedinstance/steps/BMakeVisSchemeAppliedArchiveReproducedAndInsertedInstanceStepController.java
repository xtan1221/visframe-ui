package core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps;

import java.io.IOException;
import java.sql.SQLException;

import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepController extends AbstractProcessStepController<VisSchemeAppliedArchiveReproducedAndInsertedInstance, BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/appliedarchive/reproducedandinsertedinstance/steps/BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStep.fxml";
	
	@Override
	public BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager getManager() {
		return(BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisSchemeAppliedArchiveReproducedAndInsertedInstance(), settings.getVisSchemeAppliedArchiveReproducedAndInsertedInstance()==null?true:false);
	}
	
	
	@Override
	public BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
		//
	}
	
	@FXML
	private VBox rootVBox;
	@FXML
	private ScrollPane builderEmbeddedRootNodeContainerScrollPane;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;

}
