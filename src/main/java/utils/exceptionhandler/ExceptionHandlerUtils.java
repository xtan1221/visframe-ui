package utils.exceptionhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExceptionHandlerUtils {
	
	/**
	 * 
	 * @param title
	 * @param throwable
	 * @param ownerStage
	 */
	public static void show(String title, Throwable throwable, Stage ownerStage) {
		show2(title, throwable);
//		FXMLLoader loader = new FXMLLoader(ExceptionHandlerUtils.class.getResource(ExceptionHandlerController.FXML_FILE_DIR));
//		Parent root;
//		try {
//			root = loader.load();
//			ExceptionHandlerController controller = (ExceptionHandlerController)loader.getController();
//			
//			controller.setAll(title, throwable.getMessage(), getFullStackTraceString(throwable));
//			
//			Scene scene = new Scene(root);
//			
//			Stage stage = new Stage();
//			
//			stage.setScene(scene);
//			stage.initModality(Modality.WINDOW_MODAL);
//			
//			stage.initOwner(ownerStage);
//			
//			stage.show();
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	
	static String getFullStackTraceString(Throwable throwable) {
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		String fullStackTrace = sw.toString();
		return fullStackTrace;
	}
	
	/////////////////////
	/**
	 * 
	 * @param title
	 * @param throwable
	 * @param ownerStage
	 */
	public static void show2(String title, Throwable throwable) {
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle("Errors found!");
		alert.setWidth(800);
		ScrollPane scroll = new ScrollPane();
//		scroll.setPrefWidth(800);
		TextArea textArea = new TextArea(getFullStackTraceString(throwable));
		textArea.setEditable(false);
		textArea.setPrefWidth(800);
		scroll.setContent(textArea);
		
		alert.getDialogPane().setContent(scroll);
		
		alert.show();
	}
	
}
