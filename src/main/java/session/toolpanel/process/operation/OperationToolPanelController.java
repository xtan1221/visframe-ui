package session.toolpanel.process.operation;

import builder.visframe.operation.OperationBuilderForViewOnlyFactory;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import operation.Operation;
import operation.OperationID;
import session.toolpanel.process.AbstractProcessToolPanelController;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class OperationToolPanelController extends AbstractProcessToolPanelController<Operation>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/operation/OperationToolPanel.fxml";
	


	@Override
	public Parent getRootContainerPane() {
		return rootContainerVBox;
	}
	
	////////////////////////////////////////////////////
	@FXML
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button newOperationButton;
	@FXML
	private Button viewAllButton;

	// Event Listener on Button[#newOperationButton].onAction
	@FXML
	public void newOperationButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			this.getManager().getProcessMainManager().start(this.getStage(),this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("newOperationButtonOnAction"), e, this.getStage());
		}
	}
	
	// Event Listener on Button[#viewAllButton].onAction
	@FXML
	public void viewAllButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			VisframeUDTTypeTableViewerManager<Operation,OperationID> operationTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getOperationManager(),
							OperationBuilderForViewOnlyFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),
							null,
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			operationTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}

}
