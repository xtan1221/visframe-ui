package session.toolpanel.nonprocess.indepenentfreeinputvariabletype;

import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;
import viewer.visframe.function.variable.independent.IndependentFreeInputVariableTypeViewerFactory;

public class IndependentFreeInputVariableTypeToolPanelController {
	public static final String FXML_FILE_DIR_STRING ="/session/toolpanel/nonprocess/indepenentfreeinputvariabletype/IndependentFreeInputVariableTypeToolPanel.fxml";
	
	private IndependentFreeInputVariableTypeToolPanelManager manager;
	
	public void setManager(IndependentFreeInputVariableTypeToolPanelManager manager) {
		this.manager = manager;
	}
	
	IndependentFreeInputVariableTypeToolPanelManager getManager() {
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
			
			IndependentFreeInputVariableTypeViewerFactory viewerFactory = 
					new IndependentFreeInputVariableTypeViewerFactory(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
			
			VisframeUDTTypeTableViewerManager<IndependentFreeInputVariableType,IndependentFreeInputVariableTypeID> indieFIVViewerManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getIndependentFreeInputVariableTypeManager(),
							viewerFactory, //viewerFactory
							null, //sqlFilterConditionString
							null//entityFilteringCondition
							);
			indieFIVViewerManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
}
