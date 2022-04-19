package core.pipeline.visinstance.steps.visschemebased;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import visinstance.VisInstance;

public class BMakeVisSchemeBasedVisInstanceStepController extends AbstractProcessStepController<VisInstance, BMakeVisSchemeBasedVisInstanceStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/steps/visschemebased/BMakeVisSchemeBasedVisInstanceStep.fxml";
	
	@Override
	public BMakeVisSchemeBasedVisInstanceStepManager getManager() {
		return(BMakeVisSchemeBasedVisInstanceStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVisSchemeBasedVisInstanceStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisSchemeBasedVisInstance(), settings.getVisSchemeBasedVisInstance()==null?true:false);
	}
	
	
	@Override
	public BMakeVisSchemeBasedVisInstanceStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVisSchemeBasedVisInstanceStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
