package session.serialization.visscheme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import context.project.VisProjectDBContext;
import context.scheme.VisScheme;
import context.scheme.VisSchemeID;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ExportVisSchemeManager {
	public static final String VISSCHEME_SERIALIZED_FILE_EXTENSTION = ".VS";
	
	///////////////////////
	private final VisProjectDBContext visProjectDBContext;
	
	private VisframeUDTTypeTableViewerManager<VisScheme,VisSchemeID> visSchemeTableViewManager;
	
	private ExportVisSchemeController controller;
	
	
	private Stage stage;
	private Scene scene;
	
	/**
	 * constructor
	 * @param visProjectDBContext
	 */
	public ExportVisSchemeManager(VisProjectDBContext visProjectDBContext){
		this.visProjectDBContext = visProjectDBContext;
	}
	
	public VisframeUDTTypeTableViewerManager<VisScheme,VisSchemeID> getVisSchemeTableViewManager(){
		if(this.visSchemeTableViewManager==null) {
			Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
			invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(VisSchemeID.UID_COLUMN.getName(), this.getController().getSelectedVisSchemeUIDTextField());
			
			try {
				this.visSchemeTableViewManager = new VisframeUDTTypeTableViewerManager<>(
						this.visProjectDBContext.getHasIDTypeManagerController().getVisSchemeManager(),
						VisSchemeBuilderFactory.singleton(this.visProjectDBContext),
						null,
						null,
						TableViewerMode.SELECTION,
						invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
						);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return visSchemeTableViewManager;
	}
	
	public ExportVisSchemeController getController(){
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ExportVisSchemeController.FXML_FILE_DIR_STRING));
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
		            	  ExportVisSchemeManager.this.getController().cancelButtonOnAction(null);
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
