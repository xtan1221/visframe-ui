package core.pipeline.visinstance.run.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import visinstance.run.VisInstanceRun;

public class BMakeVisInstanceRunStepController extends AbstractProcessStepController<VisInstanceRun, BMakeVisInstanceRunStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/run/steps/BMakeVisInstanceRunStep.fxml";
	
	@Override
	public BMakeVisInstanceRunStepManager getManager() {
		return(BMakeVisInstanceRunStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeVisInstanceRunStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getVisInstanceRun(), settings.getVisInstanceRun()==null?true:false);
	}
	
	
	@Override
	public BMakeVisInstanceRunStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeVisInstanceRunStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
