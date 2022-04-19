package core.pipeline.operation.steps;

import java.io.IOException;
import java.sql.SQLException;

import core.pipeline.AbstractProcessStepController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.Operation;

public class BMakeOperationStepController extends AbstractProcessStepController<Operation,BMakeOperationStepManagerSettings>{
	public final static String FXML_FILE_DIR_STRING = "/core/pipeline/operation/steps/BMakeOperationStep.fxml";
	
	@Override
	public BMakeOperationStepManager getManager() {
		return(BMakeOperationStepManager)super.getManager();
	}
	
	/**
	 * this should never be invoked;
	 * 
	 * see {@link BMakeOperationStepManager#getStepRootNode()} for reason;
	 */
	@Override
	public void setStepSettings(BMakeOperationStepManagerSettings settings) throws IOException, SQLException {
		throw new UnsupportedOperationException("");
//		this.getManager().getNodeBuilder().setValue(settings.getOperation(), settings.getOperation()==null?true:false);
	}
	
	@Override
	public BMakeOperationStepManagerSettings getStepSettings() throws SQLException, IOException {
		return new BMakeOperationStepManagerSettings(this.getManager().getNodeBuilder().getCurrentValue());
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
		// TODO Auto-generated method stub
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
