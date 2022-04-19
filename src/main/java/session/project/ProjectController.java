package session.project;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class ProjectController {
	public static final String FXML_FILE_DIR_STRING = "/session/project/Project.fxml";
	
	private ProjectManager manager;
	
	void setManager(ProjectManager manager) throws IOException {
		this.manager = manager;
		
		this.processLogTableVBox.getChildren().add(this.getManager().getProcessLogTableViewerManager().getController().getRootContainerNode());
		
		this.CFDGraphVBox.getChildren().add(this.getManager().getSimpleCFDGraphViewerManager().getController().getRootContainerNode());
		
		this.DOSGraphVBox.getChildren().add(this.getManager().getSimpleDOSGraphViewerManager().getController().getRootContainerNode());
	}
	
	void updateCFDGraph() {
		this.CFDGraphVBox.getChildren().clear();
		this.CFDGraphVBox.getChildren().add(this.getManager().getSimpleCFDGraphViewerManager().getController().getRootContainerNode());
	}
	
	void updateDOSGraph() {
		this.DOSGraphVBox.getChildren().clear();
		this.DOSGraphVBox.getChildren().add(this.getManager().getSimpleDOSGraphViewerManager().getController().getRootContainerNode());
	}
	
	ProjectManager getManager() {
		return this.manager;
	}
	
	public Parent getRootNodePane() {
		return rootContainerTabPane;
	}
	
	///////////////////////////////////////////////////
	@FXML
	public void initialize() {
		//
	}
	
	@FXML
	private TabPane rootContainerTabPane;
	@FXML
	private Tab processLogTableTab;
	@FXML
	private VBox processLogTableVBox;
	@FXML
	private Tab CFDGraphTab;
	@FXML
	private VBox CFDGraphVBox;
	@FXML
	private Tab DOSGraphTab;
	@FXML
	private VBox DOSGraphVBox;
	
	
}
