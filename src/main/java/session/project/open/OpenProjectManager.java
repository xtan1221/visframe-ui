package session.project.open;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import basic.SimpleName;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import session.VFSessionManager;
import session.log.ProjectLogFileProcessor;

public class OpenProjectManager {
	private final VFSessionManager sessionManager;
	
	////////////////////////////////////////
	private ProjectLogFileProcessor projectLogFileProcessor;
	
	private OpenProjectController openProjectController;
	private Scene openProjectScene;
	private Stage openProjectStage;
	
	public OpenProjectManager(VFSessionManager sessionManager){
		this.sessionManager = sessionManager;
		this.initialize();
	}
	
	
	private void initialize() {
		this.projectLogFileProcessor = new ProjectLogFileProcessor();
	}
	
	
	public void createNew() throws IOException {
		if(this.openProjectController==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(OpenProjectController.FXML_FILE_DIR));
			Parent openProjectRootNode = loader.load();
			
			openProjectController = (OpenProjectController)loader.getController();
			
			openProjectScene = new Scene(openProjectRootNode);
			//
			openProjectStage = new Stage();
			
			openProjectStage.setScene(openProjectScene);
			
			//!!!!this must be invoked after openProjectStage is initialized since it is depended by the setManager() method;
			openProjectController.setManager(this);
			
			openProjectStage.initModality(Modality.WINDOW_MODAL);
			
			openProjectStage.initOwner(this.sessionManager.getSessionStage());
			
		}else {
			openProjectController.addExistingProjects();
		}
		//
		openProjectController.resetButtonOnAction(null);
		//
		
		openProjectStage.show();
		
	}
	
	
	public void clear() {
		this.openProjectController = null;
		this.openProjectScene = null;
		this.openProjectStage = null;
	}
	
	
	public void finish(SimpleName projectName, Path parentDirPath,Boolean newProject) throws IOException, SQLException {
		this.openProjectStage.close();
		
		this.getSessionManager().openProject(projectName, parentDirPath,newProject);
	}
	

	public VFSessionManager getSessionManager() {
		return sessionManager;
	}


	public ProjectLogFileProcessor getProjectLogFileProcessor() {
		return projectLogFileProcessor;
	}
	
}
