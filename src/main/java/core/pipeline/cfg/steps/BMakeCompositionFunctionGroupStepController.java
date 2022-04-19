package core.pipeline.cfg.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import function.group.CompositionFunctionGroup;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BMakeCompositionFunctionGroupStepController extends AbstractProcessStepController<CompositionFunctionGroup,BMakeCompositionFunctionGroupStepManagerSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/cfg/steps/BMakeCompositionFunctionGroupStep.fxml";
	
	@Override
	public BMakeCompositionFunctionGroupStepManager getManager() {
		return(BMakeCompositionFunctionGroupStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeCompositionFunctionGroupStepManagerSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getCfg(), settings.getCfg()==null?true:false);
	}
	
	
	@Override
	public BMakeCompositionFunctionGroupStepManagerSettings getStepSettings() throws SQLException, IOException {
		return new BMakeCompositionFunctionGroupStepManagerSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
