package core.pipeline.visscheme.steps.create;

import java.io.IOException;
import java.sql.SQLException;

import context.scheme.VisScheme;
import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MakeVisSchemeStepController extends AbstractProcessStepController<VisScheme, MakeVisSchemeStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visscheme/steps/create/MakeVisSchemeStep.fxml";
	
	@Override
	public MakeVisSchemeStepManager getManager() {
		return(MakeVisSchemeStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(MakeVisSchemeStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisScheme(), settings.getVisScheme()==null?true:false);
	}
	
	
	@Override
	public MakeVisSchemeStepSettings getStepSettings() throws SQLException, IOException {
		return new MakeVisSchemeStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
	private ScrollPane cfBuilderEmbeddedRootNodeContainerScrollPane;
	@FXML
	private VBox builderEmbeddedRootNodeVBox;

}
