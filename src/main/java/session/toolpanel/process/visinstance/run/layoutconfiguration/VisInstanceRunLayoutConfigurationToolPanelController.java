package session.toolpanel.process.visinstance.run.layoutconfiguration;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfigurationBuilderFactory;
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
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfigurationID;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunLayoutConfigurationToolPanelController extends AbstractProcessToolPanelController<VisInstanceRunLayoutConfiguration>{
	public static final String FXML_FILE_DIR = "/session/toolpanel/process/visinstance/run/layoutconfiguration/VisInstanceRunLayoutConfigurationToolPanel.fxml";
	
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
	private Button newButton;
	@FXML
	private Button viewAllButton;
	@FXML
	private Button exportImageButton;

	// Event Listener on Button[#newVisInstanceButton].onAction
	@FXML
	public void newButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
			AlertUtils.popAlert("Warninig", "No opened project exists!");
			return;
		}
		
		this.getManager().getProcessMainManager().start(this.getStage(),this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext());
	}
	
	// Event Listener on Button[#viewAllButton].onAction
	@FXML
	public void viewAllButtonOnAction(ActionEvent event) {
		try {
			if(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warninig", "No opened project exists!");
				return;
			}
			VisframeUDTTypeTableViewerManager<VisInstanceRunLayoutConfiguration,VisInstanceRunLayoutConfigurationID> visInstanceRunTableViewManager = 
					new VisframeUDTTypeTableViewerManager<>(
							this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext().getHasIDTypeManagerController().getVisInstanceRunLayoutConfigurationManager(),
							VisInstanceRunLayoutConfigurationBuilderFactory.singleton(this.getManager().getSessionManager().getCurrentlyFocusedProjectManager().getVisProjectDBContext()),
							null,//no filter, show all including visframe defined graph and vftree file formats
							null,
							TableViewerMode.VIEW_ONLY,
							null);
			visInstanceRunTableViewManager.showWindow(this.getStage());
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat("viewAllButtonOnAction"), e, this.getStage());
		}
	}
	
	// Event Listener on Button[#exportImageButton].onAction
	@FXML
	public void exportImageButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}

}