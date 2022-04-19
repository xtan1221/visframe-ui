package session.toolpanel;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public interface ToolPanelController<T> {
	
	void setManager(ToolPanelManager<T,?> manager);
	
	ToolPanelManager<T,?> getManager();
	
	Stage getStage();
	@FXML
	void initialize();
}
