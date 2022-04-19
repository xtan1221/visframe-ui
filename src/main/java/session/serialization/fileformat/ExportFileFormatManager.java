package session.serialization.fileformat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.fileformat.FileFormatBuilderFactory;
import context.project.VisProjectDBContext;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ExportFileFormatManager {
	public static final String FILE_FORMAT_SERIALIZED_FILE_EXTENSTION = ".VFF";
	private final VisProjectDBContext visProjectDBContext;
	
	private VisframeUDTTypeTableViewerManager<FileFormat,FileFormatID> fileFormatTableViewManager;
	
	private ExportFileFormatController controller;
	
	
	private Stage stage;
	private Scene scene;
	
	/**
	 * constructor
	 * @param visProjectDBContext
	 */
	public ExportFileFormatManager(VisProjectDBContext visProjectDBContext){
		this.visProjectDBContext = visProjectDBContext;
	}
	
	public VisframeUDTTypeTableViewerManager<FileFormat,FileFormatID> getFileFormatTableViewManager() throws SQLException, IOException {
		if(this.fileFormatTableViewManager==null) {
			Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
			invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.TYPE_COLUMN.getName(), this.getController().getSelectedFileFormatDataTypeTextField());
			invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.NAME_COLUMN.getName(), this.getController().getSelectedFileFormatNameTextField());
			this.fileFormatTableViewManager = new VisframeUDTTypeTableViewerManager<>(
					this.visProjectDBContext.getHasIDTypeManagerController().getFileFormatManager(),
					FileFormatBuilderFactory.singleton(),
					null,
					null,
					TableViewerMode.SELECTION,
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
					);
		}
		return fileFormatTableViewManager;
	}
	
	
	public ExportFileFormatController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ExportFileFormatController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.controller = loader.getController();
			this.controller.setManager(this);
		}
		return controller;
	}
	
	public void showWindow(Stage primaryStage) throws IOException {
		if(this.stage == null) {
			this.scene = new Scene(this.getController().getRootNode());
			
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(primaryStage);
			
			//when window is directly closed by upper-right X, clear the currently selected row;
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		              System.out.println("Stage is closing");
		              try {
		            	  ExportFileFormatManager.this.getController().cancelButtonOnAction(null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
					}
		          }
		      });        
		}
		
		this.stage.show();
		
	}
	
	
}
