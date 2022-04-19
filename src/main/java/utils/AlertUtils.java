package utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

public class AlertUtils {
	
	
	public static void popAlert(String title, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		
		Text content = new Text(contentText);
		content.setWrappingWidth(800);
		alert.getDialogPane().setContent(content);
		
		alert.showAndWait();
	}
	
	/**
	 * build and pop up a confirmation dialog;
	 * then return the feed back from user;
	 * @param title
	 * @param header
	 * @param context
	 * @return
	 */
	public static Optional<ButtonType> popConfirmationDialog(String title, String header, String context) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		
		Text content = new Text(context);
		content.setWrappingWidth(800);
		alert.getDialogPane().setContent(content);
		
		return alert.showAndWait();
	}
}
