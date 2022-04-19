package session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import context.project.VisProjectDBContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import session.project.ProjectManager;
import session.project.open.OpenProjectManager;
import session.toolpanel.nonprocess.cftargetvaluetablerun.CFTargetValueTableRunToolPanelManager;
import session.toolpanel.nonprocess.indepenentfreeinputvariabletype.IndependentFreeInputVariableTypeToolPanelManager;
import session.toolpanel.nonprocess.metadata.MetadataToolPanelManager;
import session.toolpanel.process.cf.CompositionFunctionToolPanelManager;
import session.toolpanel.process.cfg.CompositionFunctionGroupToolPanelManager;
import session.toolpanel.process.dataimporter.DataImporterToolPanelManager;
import session.toolpanel.process.fileformat.FileFormatToolPanelManager;
import session.toolpanel.process.operation.OperationToolPanelManager;
import session.toolpanel.process.visinstance.VisInstanceToolPanelManager;
import session.toolpanel.process.visinstance.run.VisInstanceRunToolPanelManager;
import session.toolpanel.process.visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfigurationToolPanelManager;
import session.toolpanel.process.visscheme.VisSchemeToolPanelManager;
import session.toolpanel.process.visscheme.appliedarchive.VisSchemeAppliedArchiveToolPanelManager;
import session.toolpanel.process.visscheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager;

/**
 * 
 * @author tanxu
 * 
 */
public class VFSessionManager {
	
	private Map<SimpleName, ProjectManager> openedProjectNameManagerMap;
	private ProjectManager currentlyFocusedProjectManager;
	
	///////////////////////////
//	private Map<SimpleName, VisProjectDBContext> openedProjectNameVisProjectDBContextMap;
//	private VisProjectDBContext currentFocusedProject;
	
	///////
	private boolean stageShown = false;
	private Stage sessionStage;
	private Scene sessionScene;
	private Parent sessionRootNode;
	private VFSessionController sessionController;
	
	
	private OpenProjectManager openProjectManager;
	
	
	////////////////
	private FileFormatToolPanelManager fileFormatToolPanelManager;
	
	private DataImporterToolPanelManager dataImporterToolPanelManager;
	
	private MetadataToolPanelManager metadataToolPanelManager;
	
	private OperationToolPanelManager operationToolPanelManager;
	
	private CompositionFunctionGroupToolPanelManager compositionFunctionGroupToolPanelManager;
	
	private CompositionFunctionToolPanelManager compositionFunctionToolPanelManager;
	
	private IndependentFreeInputVariableTypeToolPanelManager independentFreeInputVariableTypeToolPanelManager;
	
	private VisSchemeToolPanelManager visSchemeToolPanelManager;
	
	private VisSchemeAppliedArchiveToolPanelManager visSchemeAppliedArchiveToolPanelManager;
	
	private VisSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager visSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager;
	
	private VisInstanceToolPanelManager visInstanceToolPanelManager;
	
	private VisInstanceRunToolPanelManager visInstanceRunToolPanelManager;
	
	private CFTargetValueTableRunToolPanelManager CFTargetValueTableRunToolPanelManager;
	
	private VisInstanceRunLayoutConfigurationToolPanelManager visInstanceRunLayoutConfigurationToolPanelManager;
	
	/**
	 * 
	 * @param primaryStage
	 * @throws IOException
	 */
	VFSessionManager(Stage primaryStage) throws IOException{
		this.sessionStage = primaryStage;
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void init() throws IOException {
//		this.openedProjectNameVisProjectDBContextMap = new LinkedHashMap<>();
		this.openedProjectNameManagerMap = new LinkedHashMap<>();
		
		this.sessionStage.setOnCloseRequest(e->{
			try {
				this.getOpenProjectManager().getProjectLogFileProcessor().writeAll();
				
				for(SimpleName projectName:this.getOpenedProjectNameManagerMap().keySet()) {
					System.out.println("Closing project "+projectName.getStringValue()+"...");
					this.getSessionController().removeProjectTab(this.getOpenedProjectNameManagerMap().get(projectName));
					this.getOpenedProjectNameManagerMap().get(projectName).closeDBConnection();
				}
				
				//Terminates the currently running Java Virtual Machine. 
				System.exit(0);
				
				
			} catch (IOException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
		});
		
		this.initSessionScene();
		
		this.openProjectManager = new OpenProjectManager(this);
		
		this.openProjectManager.createNew();
	}
	
	private void initSessionScene() throws IOException {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(VFSessionController.FXML_FILE_DIR));
		this.sessionRootNode = loader.load();
		
		this.sessionController = (VFSessionController)loader.getController();
		this.sessionController.setManager(this);
		
		this.sessionScene = new Scene(this.sessionRootNode, Screen.getPrimary().getBounds().getWidth()*0.9, Screen.getPrimary().getBounds().getHeight()*0.9);
		
		this.sessionStage.setScene(this.sessionScene);
		
//		this.sessionStage.show();
		
		this.initProcessTooPanel();
	}
	
	
	private void initProcessTooPanel() throws IOException {
		//
		this.fileFormatToolPanelManager = new FileFormatToolPanelManager(this);
		this.getSessionController().addToolPanel(this.fileFormatToolPanelManager.getRootNode());
		
		this.dataImporterToolPanelManager = new DataImporterToolPanelManager(this);
		this.getSessionController().addToolPanel(this.dataImporterToolPanelManager.getRootNode());
		
		this.metadataToolPanelManager = new MetadataToolPanelManager(this);
		this.getSessionController().addToolPanel(this.metadataToolPanelManager.getRootNode());
		
		this.operationToolPanelManager = new OperationToolPanelManager(this);
		this.getSessionController().addToolPanel(this.operationToolPanelManager.getRootNode());
		
		this.compositionFunctionGroupToolPanelManager = new CompositionFunctionGroupToolPanelManager(this);
		this.getSessionController().addToolPanel(this.compositionFunctionGroupToolPanelManager.getRootNode());
		
		this.compositionFunctionToolPanelManager = new CompositionFunctionToolPanelManager(this);
		this.getSessionController().addToolPanel(this.compositionFunctionToolPanelManager.getRootNode());
		
		this.independentFreeInputVariableTypeToolPanelManager = new IndependentFreeInputVariableTypeToolPanelManager(this);
		this.getSessionController().addToolPanel(this.independentFreeInputVariableTypeToolPanelManager.getRootNode());
		
		this.visSchemeToolPanelManager = new VisSchemeToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visSchemeToolPanelManager.getRootNode());
		
		this.visSchemeAppliedArchiveToolPanelManager = new VisSchemeAppliedArchiveToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visSchemeAppliedArchiveToolPanelManager.getRootNode());
		
		this.visSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager = new VisSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visSchemeAppliedArchiveReproducedAndInsertedInstanceToolPanelManager.getRootNode());
		
		this.visInstanceToolPanelManager = new VisInstanceToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visInstanceToolPanelManager.getRootNode());
		
		this.visInstanceRunToolPanelManager = new VisInstanceRunToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visInstanceRunToolPanelManager.getRootNode());
		
		this.CFTargetValueTableRunToolPanelManager = new CFTargetValueTableRunToolPanelManager(this);
		this.getSessionController().addToolPanel(this.CFTargetValueTableRunToolPanelManager.getRootNode());
		
		this.visInstanceRunLayoutConfigurationToolPanelManager = new VisInstanceRunLayoutConfigurationToolPanelManager(this);
		this.getSessionController().addToolPanel(this.visInstanceRunLayoutConfigurationToolPanelManager.getRootNode());
	}
	
	/**
	 * open the project if given information are valid;
	 * 
	 * also add to the openedProjectNameVisProjectDBContextMap and set the new project as focused one;
	 * 
	 * @param name
	 * @param location
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public void openProject(SimpleName projectName, Path parentDirPath, Boolean newProject) throws IOException, SQLException {
		if(!this.stageShown) {
			this.sessionStage.show();
			this.stageShown = true;
		}
		
		if(projectName!=null&&parentDirPath!=null&&newProject!=null) {//
			if(newProject) {
//				System.out.println("new project folder:"+Paths.get(parentDirPath.toString(),projectName.getStringValue()).toString());
				Files.createDirectories(Paths.get(parentDirPath.toString(),projectName.getStringValue()));
			}
			
			VisProjectDBContext project = new VisProjectDBContext(projectName, parentDirPath);
			
			project.connect();
			
			ProjectManager manager = new ProjectManager(project);
			
//			this.openedProjectNameVisProjectDBContextMap.put(projectName, project);
			this.openedProjectNameManagerMap.put(projectName, manager);
			
			//try to add the project to the existing project in the system
//			if(newProject) {
			this.getOpenProjectManager().getProjectLogFileProcessor().addNewProject(projectName, Paths.get(parentDirPath.toString(),projectName.getStringValue()));
//			}
			
			
//			this.currentFocusedProject = project;
			this.currentlyFocusedProjectManager = manager;
			
			
			this.getSessionController().addProjectTab(this.currentlyFocusedProjectManager);
			this.getSessionController().setFocusedProjectTab(this.currentlyFocusedProjectManager);
		}else {
			//TODO
		}
	}
	
	
	public OpenProjectManager getOpenProjectManager() {
		return openProjectManager;
	}

	public Stage getSessionStage() {
		return sessionStage;
	}


	public Scene getSessionScene() {
		return sessionScene;
	}


	public Parent getSessionRootNode() {
		return sessionRootNode;
	}


	public VFSessionController getSessionController() {
		return sessionController;
	}

	/**
	 * @return the openedProjectNameManagerMap
	 */
	public Map<SimpleName, ProjectManager> getOpenedProjectNameManagerMap() {
		return openedProjectNameManagerMap;
	}
	
	/**
	 * @return the currentlyFocusedProjectManager
	 */
	public ProjectManager getCurrentlyFocusedProjectManager() {
		return currentlyFocusedProjectManager;
	}

	/**
	 * @param currentlyFocusedProjectManager the currentlyFocusedProjectManager to set
	 */
	public void setCurrentlyFocusedProjectManager(ProjectManager currentlyFocusedProjectManager) {
		this.currentlyFocusedProjectManager = currentlyFocusedProjectManager;
	}

	
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	public void closeCurrentlyFocusedProject() throws IOException, SQLException {
		if(this.getCurrentlyFocusedProjectManager()==null) {
			return;
		}
		
//		System.out.println(this.getCurrentlyFocusedProjectManager());
		this.getCurrentlyFocusedProjectManager().closeDBConnection();
		
//		System.out.println(this.getCurrentlyFocusedProjectManager());
		
		//??????
		
//		System.out.println(this.getCurrentlyFocusedProjectManager());
//		System.out.println(this.getCurrentlyFocusedProjectManager().getVisProjectDBContext());
//		System.out.println(this.getCurrentlyFocusedProjectManager().getVisProjectDBContext().getName().getStringValue());
		
//		for(SimpleName projectName:this.getOpenedProjectNameManagerMap().keySet()) {
//			System.out.println(projectName.getStringValue());
//		}
		
		this.getOpenedProjectNameManagerMap().remove(this.getCurrentlyFocusedProjectManager().getVisProjectDBContext().getName());
		
		this.getSessionController().removeProjectTab(this.getCurrentlyFocusedProjectManager());
		
		if(!this.getOpenedProjectNameManagerMap().isEmpty()) {
			this.currentlyFocusedProjectManager = this.getOpenedProjectNameManagerMap().values().iterator().next();
			
//			this.getSessionController().addProjectTab(this.currentlyFocusedProjectManager);
			this.getSessionController().setFocusedProjectTab(this.currentlyFocusedProjectManager);
		}else {
			this.currentlyFocusedProjectManager = null;
		}
	}
	
	
//	public Map<SimpleName, VisProjectDBContext> getOpenedProjectNameVisProjectDBContextMap() {
//		return openedProjectNameVisProjectDBContextMap;
//	}
	
//	public VisProjectDBContext getCurrentFocusedProject() {
//		return this.currentFocusedProject;
//	}
}
