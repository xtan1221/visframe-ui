package core.pipeline.visinstance.steps.nativevi;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import visinstance.VisInstance;

public class MakeNativeVisInstanceStepController extends AbstractProcessStepController<VisInstance, MakeNativeVisInstanceStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/visinstance/steps/nativevi/MakeNativeVisInstanceStep.fxml";
	
	@Override
	public MakeNativeVisInstanceStepManager getManager() {
		return(MakeNativeVisInstanceStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(MakeNativeVisInstanceStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getNativeVisInstance(), settings.getNativeVisInstance()==null?true:false);
	}
	
	
	@Override
	public MakeNativeVisInstanceStepSettings getStepSettings() throws SQLException, IOException {
		return new MakeNativeVisInstanceStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
