package core.table.process;

import java.io.IOException;
import java.sql.SQLException;

import context.project.process.logtable.ProcessLogTableAndProcessPerformerManager;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ProcessLogTableViewerManager {
	private final ProcessLogTableAndProcessPerformerManager processLogTableAndProcessPerformerManager;
	
	///////////////////////////////
	private ProcessLogTableViewDelegate tableViewDelegate;
	
	private ProcessLogTableViewerController controller;
	private Stage stage;
	private Scene scene;
	private Parent rootNode;
	
	/**
	 * 
	 * @param processLogTableAndProcessPerformerManager
	 * @throws SQLException
	 */
	public ProcessLogTableViewerManager(ProcessLogTableAndProcessPerformerManager processLogTableAndProcessPerformerManager) throws SQLException{
		this.processLogTableAndProcessPerformerManager = processLogTableAndProcessPerformerManager;
		this.initializeTableViewDelegate();
	}
	
	private void initializeTableViewDelegate() throws SQLException {
		this.tableViewDelegate = new ProcessLogTableViewDelegate(this);
	}

	
	public ProcessLogTableAndProcessPerformerManager getProcessLogTableAndProcessPerformerManager() {
		return processLogTableAndProcessPerformerManager;
	}

	public ProcessLogTableViewDelegate getTableViewDelegate() {
		return tableViewDelegate;
	}
	
	
	String getFXMLFileDirString() {
		return ProcessLogTableViewerController.FXML_FILE_DIR_STRING;
	}
	
	public ProcessLogTableViewerController getController() throws IOException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(this.getFXMLFileDirString()));
			
			this.rootNode = loader.load();
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
			
		}
		
		return this.controller;
	}
	
	Parent getRootNode() throws IOException {
		if(this.rootNode == null) {
			this.getController();
		}
		return this.rootNode;
	}
	
	/**
	 * display a pop up window containing the TableView;
	 * @param primaryStage
	 * @throws IOException
	 */
	public void showWindow(Stage primaryStage) throws IOException {
		
		if(this.stage == null) {
			this.scene = new Scene(this.getRootNode());
			
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(primaryStage);
			
			//when window is directly closed by upper-right X, clear the currently selected row;
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
//		              System.out.println("Stage is closing");
		              try {
		            	  ProcessLogTableViewerManager.this.getController().closeWindowButtonOnAction(null);
						} catch (IOException e1) {
							e1.printStackTrace();
							System.exit(1);
						}
		          }
		      });        
		}
		
		this.stage.show();
	}
	
	void closeWindow() {
		this.stage.close();
	}
}
