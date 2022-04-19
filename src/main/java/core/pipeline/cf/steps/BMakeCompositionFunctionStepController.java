package core.pipeline.cf.steps;

import java.io.IOException;
import java.sql.SQLException;
import core.pipeline.AbstractProcessStepController;
import function.composition.CompositionFunction;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class BMakeCompositionFunctionStepController extends AbstractProcessStepController<CompositionFunction, BMakeCompositionFunctionStepSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/cf/steps/BMakeCompositionFunctionStep.fxml";
	
	@Override
	public BMakeCompositionFunctionStepManager getManager() {
		return(BMakeCompositionFunctionStepManager)super.getManager();
	}
	
	@Override
	public void setStepSettings(BMakeCompositionFunctionStepSettings settings) throws IOException, SQLException {
		this.getManager().getNodeBuilder().setValue(settings.getCompositionFunction(), settings.getCompositionFunction()==null?true:false);
	}
	
	
	@Override
	public BMakeCompositionFunctionStepSettings getStepSettings() throws SQLException, IOException {
		return new BMakeCompositionFunctionStepSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
