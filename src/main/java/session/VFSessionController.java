package session;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import basic.SimpleName;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import session.project.ProjectManager;
import utils.AlertUtils;
import utils.DraggableTabPaneSupport;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class VFSessionController {
	public static final String FXML_FILE_DIR = "/session/VFSession.fxml";
	
	private VFSessionManager manager;
	
	private Map<Tab,ProjectManager> openedProjectTabManagerMap;
	
	private SingleSelectionModel<Tab> projectTablePaneSelectionModel;
	
	public void setManager(VFSessionManager manager) {
		this.manager = manager;
		
		this.openedProjectTabManagerMap = new HashMap<>();
		
		this.projectTablePaneSelectionModel = this.openedProjectsTabPane.getSelectionModel();
		
		this.projectTablePaneSelectionModel.selectedItemProperty().addListener(e->{
			System.out.println("changed to:"+(VFSessionController.this.openedProjectsTabPane.getSelectionModel().getSelectedItem()==null?"null":VFSessionController.this.openedProjectTabManagerMap.get(
	    			VFSessionController.this.openedProjectsTabPane.getSelectionModel().getSelectedItem()).getVisProjectDBContext().getName().getStringValue()));
			
			if(!this.openedProjectTabManagerMap.isEmpty()) {
				this.manager.setCurrentlyFocusedProjectManager(this.openedProjectTabManagerMap.get(this.projectTablePaneSelectionModel.getSelectedItem()));
			}else {
				//no project tab exist, do not set the currently focused project from here; (will be set elsewhere)
			}
		});
		
//		this.projectTablePaneSelectionModel.selectedItemProperty().addListener(new ChangeListener<Tab>() {
//
//		    @Override
//		    public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
//		    	System.out.println("tabpane selection changed to:"+(VFSessionController.this.openedProjectsTabPane.getSelectionModel().getSelectedItem()==null?"null":VFSessionController.this.openedProjectTabManagerMap.get(
//		    			VFSessionController.this.openedProjectsTabPane.getSelectionModel().getSelectedItem()).getVisProjectDBContext().getName().getStringValue()));
//
////		    	System.out.println(newTab);
//		    	if(!VFSessionController.this.openedProjectTabManagerMap.isEmpty()) {
//		    		VFSessionController.this.manager.setCurrentlyFocusedProjectManager(VFSessionController.this.openedProjectTabManagerMap.get(VFSessionController.this.projectTablePaneSelectionModel.getSelectedItem()));
//				}else {
//					//no project tab exist, do not set the currently focused project from here; (will be set elsewhere)
//				}
//		    }
//		});
		
	}
	
	public void addProjectTab(ProjectManager manager) throws IOException {
		Tab tab = new Tab(manager.getVisProjectDBContext().getName().getStringValue());
		tab.setContent(manager.getController().getRootNodePane());
		manager.setProjectTab(tab);
		
		this.openedProjectTabManagerMap.put(tab,manager);
		this.openedProjectsTabPane.getTabs().add(tab);
	}
	
	public void setFocusedProjectTab(ProjectManager manager) throws IOException {
//		SingleSelectionModel<Tab> selectionModel = this.openedProjectsTabPane.getSelectionModel();
		this.projectTablePaneSelectionModel.select(manager.getProjectTab());
	}
	
	public void removeProjectTab(ProjectManager manager) throws IOException {
		this.openedProjectsTabPane.getTabs().remove(manager.getProjectTab());
		this.openedProjectTabManagerMap.remove(manager.getProjectTab());
	}
	
	////////////////////////////////////////
	public void addToolPanel(Parent processToolPanelNode) {
		this.getProcessTypeToolPanelPane().getChildren().add(processToolPanelNode);
		this.getProcessTypeToolPanelPane().getChildren().add(new Line(0,0,0,100));
	}
	
	Pane getProcessTypeToolPanelPane() {
		return this.processToolHBox;
	}
	
	Stage getStage() {
		return (Stage) this.getProcessTypeToolPanelPane().getScene().getWindow();
	}
	
	///////////////////////////////////////////////
	@FXML
	public void initialize() {
		DraggableTabPaneSupport support = new DraggableTabPaneSupport();
		support.addSupport(this.openedProjectsTabPane);
	}
	
	
	@FXML
	private MenuItem openProjectMenuItem;
	@FXML
	private MenuItem closeCurrentProjectMenuItem;
	@FXML
	private Button printProjectButton;
	@FXML
	private HBox processToolHBox;
	@FXML
	private TabPane openedProjectsTabPane;

	
	
	
	// Event Listener on MenuItem[#openProjectMenuItem].onAction
	@FXML
	public void openProjectMenuItemOnAction(ActionEvent event) {
		try {
			this.manager.getOpenProjectManager().createNew();
		}catch(Exception e) {
			ExceptionHandlerUtils.show("VFSessionController.openProjectMenuItemOnAction", e, this.getStage());
		}
	}

	// Event Listener on MenuItem[#closeCurrentProjectMenuItem].onAction
	@FXML
	public void closeCurrentProjectMenuItemOnAction(ActionEvent event) {
		try {
			if(this.manager.getCurrentlyFocusedProjectManager()==null) {
				AlertUtils.popAlert("Warning", "No currently focused project!");
				return;
			}
			
			this.manager.closeCurrentlyFocusedProject();
			
		}catch(Exception e) {
			ExceptionHandlerUtils.show("VFSessionController.openProjectMenuItemOnAction", e, this.getStage());
		}
		
	}
	
	// Event Listener on Button[#printProjectButton].onAction
	@FXML
	public void printProjectButtonOnAction(ActionEvent event) {
		System.out.println("currently focused project:"+
					(this.manager.getCurrentlyFocusedProjectManager()==null?"null":this.manager.getCurrentlyFocusedProjectManager().getVisProjectDBContext().getName().getStringValue()));
		
		System.out.println("opened projects...");
		for(SimpleName openedProjectName:this.manager.getOpenedProjectNameManagerMap().keySet()) {
			System.out.println(openedProjectName.getStringValue());
		}
		
	}
}
