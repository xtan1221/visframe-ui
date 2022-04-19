package core.builder.utils;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConstraintsViolotionWarningAlertManager {
	final static int CONTINUE_EDITING = 1;
	final static int ROLLBACK_AND_EXIT = 0;
	
	////////////////////////////
	private final Window initWindow;
	private final List<String> violatedConstraintsDescriptionList;
	
	
	////////////////////
	private ConstraintsWarningAlertController controller;
	
	private Stage popupWindow;
	
	private Integer option = CONTINUE_EDITING; //default is continue editing
	
	/**
	 * 
	 * @param violatedConstraintsDescriptionList
	 * @throws IOException 
	 */
	public ConstraintsViolotionWarningAlertManager(Window initWindow, List<String> violatedConstraintsDescriptionList){
		
		this.initWindow = initWindow;
		this.violatedConstraintsDescriptionList = violatedConstraintsDescriptionList;
		
		this.initializeController();
	}
	
	
	private void initializeController() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(ConstraintsWarningAlertController.FXML_FILE_DIR_STRING));
		
		try {
			loader.load();
//	    	System.out.println("111111111111111111111111111111111111111111");
			this.controller = (ConstraintsWarningAlertController)loader.getController();
//	    	this.embeddedController.initialize();
    	
//	    	System.out.println("22222222222222222222222222222222222222222222");
			this.controller.setManager(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	
	String getMainMessageString() {
		return "Following constraints are violated:";
	}
	
	
	String buildDetailedMessageString() {
		StringBuilder sb = new StringBuilder();
		
//		sb.append("Violated constraints:\n");
		
		int i = 1;
		for(String description:this.violatedConstraintsDescriptionList) {
			sb.append(Integer.toString(i)).append(".").append(description).append("\n");
			i++;
		}
		
		return sb.toString();
	}
	
	
	public void showAndWait() {
		
		popupWindow = new Stage();
		//do not show window MAXIMIZE, MINIMIZE, CLOSE 
//		popupWindow.initStyle(StageStyle.UNDECORATED);
		
		
		Scene scene = new Scene(this.controller.getRootNode());
		
		popupWindow.setTitle("test");
		popupWindow.setScene(scene);
		
		popupWindow.initModality(Modality.WINDOW_MODAL);
        
        // Specifies the owner Window (parent) for new window
		popupWindow.initOwner(this.initWindow);
		
		
		//must invoke Stage.showAndWait() method instead of Stage.show() method!!!!!!
		popupWindow.showAndWait();
		
		
	}
	
	Stage getPopupWindow() {
		return this.popupWindow;
	}
	
	void setToContinueEditing() {
		this.option = CONTINUE_EDITING;
	}
	
	void setToRollbackAndExit() {
		this.option = ROLLBACK_AND_EXIT;
	}
	
	public boolean isToContinueEditing() {
		return this.option == CONTINUE_EDITING;
	}
	
}
