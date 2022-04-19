package core.pipeline;

import java.io.IOException;
import java.sql.SQLException;

import basic.process.ProcessType;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * controller class of each specific step class should implemented this one;
 * 
 * @author tanxu
 * 
 * @param <T>
 */
public interface ProcessStepController<P extends ProcessType, T extends ProcessStepSettings> {
	
	/**
	 * set the settings as the given one on the UI;
	 * @param settings
	 * @throws IOException 
	 * @throws SQLException 
	 */
	void setStepSettings(T settings) throws IOException, SQLException;
	
	void setManager(ProcessStepManager<P,T,?> manager);
	
	ProcessStepManager<P,T,?> getManager();
	
	Pane getRootNode();
	
	
	default Stage getStage() throws IOException, SQLException {
		System.out.println("1");
		
		this.getManager();
		System.out.println("2");
		this.getManager().getStepRootNode();
		System.out.println("3");
		this.getManager().getStepRootNode().getScene();
		System.out.println("4");
		this.getManager().getStepRootNode().getScene().getWindow();
		System.out.println("5");
		
		return (Stage) this.getManager().getStepRootNode().getScene().getWindow();
	}
	
	/**
	 * return the pane to hold the embedded root node of the NodeBuilder of this pipeline;
	 * must be implemented by controller of final steps to build a new entity with a NodeBuilder;
	 * @return
	 */
	Pane getBuilderEmbeddedRootNodeContainer();
	
	/**
	 * build and return the currently assigned settings on the UI
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	T getStepSettings() throws SQLException, IOException;
	
	/**
	 * check if currently assigned settings are valid so that can go to next step;
	 * 
	 * popup Alert to show information if invalid;
	 * 
	 * only applicable for non-final steps;
	 * @return
	 * @throws IOException 
	 * @throws SQLException 
	 */
	boolean validateSettingsToGoToNextStep() throws IOException, SQLException;
	
	@FXML
	public void initialize();
	
	
}
