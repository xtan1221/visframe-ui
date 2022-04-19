package session.toolpanel.nonprocess.cftargetvaluetablerun;

import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;
import viewer.visframe.visinstance.run.calculation.function.composition.CFTargetValueTableRunViewerFactory;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;
import visinstance.run.calculation.function.composition.CFTargetValueTableRunID;

public class CFTargetValueTableRunToolPanelController {
	public static final String FXML_FILE_DIR_STRING ="/session/toolpanel/nonprocess/cftargetvaluetablerun/CFTargetValueTableRunToolPanel.fxml";
	
	////////////////////////
	private CFTargetValueTableRunToolPanelManager manager;
	
	public void setManager(CFTargetValueTableRunToolPanelManager manager) {
		this.manager = manager;
	}
	
	CFTargetValueTableRunToolPanelManager getManager() {
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
			
			CFTargetValueTableRunViewerFactory viewerFactory = 
					new CFTargetValueTableRunViewerFactory(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
			
			VisframeUDTTypeTableViewerManager<CFTargetValueTableRun,CFTargetValueTableRunID> CFTargetValueTableRunViewerManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getCFTargetValueTableRunManager(),
							viewerFactory, //viewerFactory
							null, //sqlFilterConditionString
							null//entityFilteringCondition
							);
			CFTargetValueTableRunViewerManager.showWindow(this.getStage());
			
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
}
