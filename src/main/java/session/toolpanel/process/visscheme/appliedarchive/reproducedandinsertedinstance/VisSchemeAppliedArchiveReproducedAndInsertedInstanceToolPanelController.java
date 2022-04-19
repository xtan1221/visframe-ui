package session.toolpanel.process.visscheme.appliedarchive.reproducedandinsertedinstance;

import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceID;
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

public class VisSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelController extends AbstractProcessToolPanelController<VisSchemeAppliedArchiveReproducedAndInsertedInstance>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/visscheme/appliedarchive/reproducedandinsertedinstance/VisSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanel.fxml";
	
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
	private Button newVisSchemeAppliedArchiveReproducedAndInsertedInstanceButton;
	@FXML
	private Button viewAllButton;

	
	// Event Listener on Button[#newFileFormatButton].onAction
	@FXML
	public void newVisSchemeAppliedArchiveReproducedAndInsertedInstanceButtonOnAction(ActionEvent event) {
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
			VisframeUDTTypeTableViewerManager<VisSchemeAppliedArchiveReproducedAndInsertedInstance,VisSchemeAppliedArchiveReproducedAndInsertedInstanceID> visSchemeAppliedArchiveReproducedAndInsertedInstanceTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceManager(),
							VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),
//							TableContentSQLStringFactory.buildColumnValueEquityCondition(FileFormatID.TYPE_COLUMN.getName().getStringValue(), DataType.RECORD.toString(), true),
							null,//no filter, show all including visframe defined graph and vftree file formats
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			visSchemeAppliedArchiveReproducedAndInsertedInstanceTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
	
}
