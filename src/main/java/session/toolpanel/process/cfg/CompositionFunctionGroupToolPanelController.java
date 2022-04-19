package session.toolpanel.process.cfg;

import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import session.toolpanel.process.AbstractProcessToolPanelController;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class CompositionFunctionGroupToolPanelController extends AbstractProcessToolPanelController<CompositionFunctionGroup>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/cfg/CompositionFunctionGroupToolPanel.fxml";
	
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
	private Button newCompositionFunctionGroupButton;
	@FXML
	private Button viewAllButton;

	// Event Listener on Button[#newOperationButton].onAction
	@FXML
	public void newCompositionFunctionGroupButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			this.getManager().getProcessMainManager().start(this.getStage(),this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("newCompositionFunctionGroupButtonOnAction"), e, this.getStage());
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
			VisframeUDTTypeTableViewerManager<CompositionFunctionGroup,CompositionFunctionGroupID> compositionFunctionGroupTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getCompositionFunctionGroupManager(),
							CompositionFunctionGroupBuilderFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),							
							null,
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			compositionFunctionGroupTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}

}
