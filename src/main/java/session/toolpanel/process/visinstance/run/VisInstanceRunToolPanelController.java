package session.toolpanel.process.visinstance.run;

import builder.visframe.visinstance.run.VisInstanceRunBuilderFactory;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import session.toolpanel.process.AbstractProcessToolPanelController;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;
import visinstance.run.VisInstanceRun;
import visinstance.run.VisInstanceRunID;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunToolPanelController extends AbstractProcessToolPanelController<VisInstanceRun>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/visinstance/run/VisInstanceRunToolPanel.fxml";
	
	@Override
	public Pane getRootContainerPane() {
		return rootContainerVBox;
	}
	
	////////////////////////
	@FXML
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button newVisInstanceRunButton;
	@FXML
	private Button viewAllButton;

	// Event Listener on Button[#newVisInstanceButton].onAction
	@FXML
	public void newVisInstanceRunButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			this.getManager().getProcessMainManager().start(this.getStage(),this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("newFileFormatButtonOnAction"), e, this.getStage());
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
			VisframeUDTTypeTableViewerManager<VisInstanceRun,VisInstanceRunID> visInstanceRunTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunManager(),
							VisInstanceRunBuilderFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),
//							TableContentSQLStringFactory.buildColumnValueEquityCondition(FileFormatID.TYPE_COLUMN.getName().getStringValue(), DataType.RECORD.toString(), true),
							null,//no filter, show all including visframe defined graph and vftree file formats
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			visInstanceRunTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
	
}
