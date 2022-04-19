package core.pipeline.visinstance.run.layoutconfiguration.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;

/**
 * 
 * @author tanxu
 *
 */
public class BMakeVisInstanceRunLayoutConfigurationStepController extends AbstractProcessStepController<VisInstanceRunLayoutConfiguration, BMakeVisInstanceRunLayoutConfigurationStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/run/layoutconfiguration/steps/BMakeVisInstanceRunLayoutConfigurationStep.fxml";
	
	@Override
	public BMakeVisInstanceRunLayoutConfigurationStepManager getManager() {
		return(BMakeVisInstanceRunLayoutConfigurationStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVisInstanceRunLayoutConfigurationStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisInstanceRunLayout(), settings.getVisInstanceRunLayout()==null?true:false);
	}
	
	
	@Override
	public BMakeVisInstanceRunLayoutConfigurationStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVisInstanceRunLayoutConfigurationStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
