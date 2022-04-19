package dependencygraph.vcd.pre.utils;

import java.io.IOException;

import dependency.vcd.VCDNodeImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VCDNodeAssignedEntitiesManager {
	private final VCDNodeImpl node;
	
	/////////////
	private VCDNodeAssignedEntitiesController controller;
	
	/**
	 * 
	 * @param node
	 */
	public VCDNodeAssignedEntitiesManager(VCDNodeImpl node){
		this.node = node;
	}


	public VCDNodeImpl getNode() {
		return node;
	}

	
	VCDNodeAssignedEntitiesController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(VCDNodeAssignedEntitiesController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		
		return this.controller;
	}
	
	
	/**
	 * show the assigned entity in a new window;
	 * @param initStage
	 */
	public void showAndWait(Stage initStage) {
		
		Scene scene = new Scene(this.getController().getRootNodePane());
		
		
		Stage stage = new Stage();
		stage.initOwner(initStage);
		
		
		stage.setScene(scene);
		
		stage.showAndWait();
	}
	
}
