package session.toolpanel.nonprocess.metadata;

import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import metadata.Metadata;
import metadata.MetadataID;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;
import viewer.visframe.metadata.MetadataViewerFactory;

public class MetadataToolPanelController {
	public static final String FXML_FILE_DIR_STRING ="/session/toolpanel/nonprocess/metadata/MetadataToolPanel.fxml";
	
	private MetadataToolPanelManager manager;
	
	public void setManager(MetadataToolPanelManager manager) {
		this.manager = manager;
	}
	
	MetadataToolPanelManager getManager() {
		return this.manager;
	}
	
	
	public Stage getStage() {
		return (Stage) this.viewAllButton.getScene().getWindow();
	}
	
	//////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private Button viewAllButton;

	// Event Listener on Button[#viewAllButton].onAction
	@FXML
	public void viewAllButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			
			MetadataViewerFactory viewerFactory = 
					new MetadataViewerFactory(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
			
			VisframeUDTTypeTableViewerManager<Metadata,MetadataID> metadataTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getMetadataManager(),
							viewerFactory, //viewerFactory
							null, //sqlFilterConditionString
							null//entityFilteringCondition
							);
			
			metadataTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
}
