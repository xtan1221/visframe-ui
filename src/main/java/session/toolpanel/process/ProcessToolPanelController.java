package session.toolpanel.process;

import basic.process.ProcessType;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Stage;

public interface ProcessToolPanelController<T extends ProcessType>{
	
	void setManager(ProcessToolPanelManager<T,?,?> manager);
	
	ProcessToolPanelManager<T,?,?> getManager();
	
	
	default Stage getStage() {
		return (Stage) this.getRootContainerPane().getScene().getWindow();
	}
 
	Parent getRootContainerPane();
	
	
	@FXML
	void initialize();
}
