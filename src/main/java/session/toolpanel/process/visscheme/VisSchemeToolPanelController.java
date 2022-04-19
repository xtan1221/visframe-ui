package session.toolpanel.process.visscheme;

import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import context.scheme.VisScheme;
import context.scheme.VisSchemeID;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import session.serialization.visscheme.ExportVisSchemeManager;
import session.toolpanel.process.AbstractProcessToolPanelController;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class VisSchemeToolPanelController extends AbstractProcessToolPanelController<VisScheme>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/visscheme/VisSchemeToolPanel.fxml";
	
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
	private Button newVisSchemeButton;
	@FXML
	private Button viewAllButton;
	@FXML
	private Button exportButton;
	
	// Event Listener on Button[#newFileFormatButton].onAction
	@FXML
	public void newVisSchemeButtonOnAction(ActionEvent event) {
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
			VisframeUDTTypeTableViewerManager<VisScheme,VisSchemeID> fileFormatTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager(),
							VisSchemeBuilderFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),
//							TableContentSQLStringFactory.buildColumnValueEquityCondition(FileFormatID.TYPE_COLUMN.getName().getStringValue(), DataType.RECORD.toString(), true),
							null,//no filter, show all including visframe defined graph and vftree file formats
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			fileFormatTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
	
	// Event Listener on Button[#exportButton].onAction
	/**
	 * export an existing FileFormat as a Serialized object file to local file system;
	 * @param event
	 */
	@FXML
	public void exportButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			
			ExportVisSchemeManager manager = new ExportVisSchemeManager(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
			
			manager.showWindow(this.getStage());
			
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}


}
