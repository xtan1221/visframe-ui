package core.builder.utils;

import java.util.function.Predicate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextField;

public class TextFieldUtils {
	
	/**
	 * add listener
	 * when enter key is pressed or lose focus, check the content;
	 * if not valid, popup an error message and re-focus the text and clear the content;
	 * @param tf
	 * @param minInclusive allowed min value (inclusive)
	 * @param maxInclusive allowed max value (inclusive)
	 */
	public static void onlyAcceptInteger(TextField tf, Predicate<Integer> domain, String domainDescription) {
		
		tf.focusedProperty().addListener(e -> {
			if(!tf.focusedProperty().get()) {//focus is lost
				System.out.println("lose focus");
//				System.out.println(minInclusive + " " +maxInclusive);
				//it's ok to leave the TextField with empty string?;
				if(tf.getText().isEmpty()) {
					return;
				}
				
				try {
					int value = Integer.parseInt(tf.getText());
					
					if(domain!=null && !domain.test(value)) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Text Content Error");
						alert.setHeaderText("Error!");
						alert.setContentText(
//								"Input text must be "+
//								(minInclusive==null?"":"larger than or equal to ".concat(Integer.toString(minInclusive))) +
//								(minInclusive!=null&&maxInclusive!=null?" and ":"") +
//								(maxInclusive==null?"":"less than or equal to ".concat(Integer.toString(maxInclusive)))
								domainDescription
								);
						
						alert.showAndWait();
						
						tf.setText("");
						tf.requestFocus();
					}
				}catch(NumberFormatException ex) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Text Content Error");
					alert.setHeaderText("Error!");
					alert.setContentText("Input text must be integer value !");
					alert.showAndWait();

					tf.setText("");
					tf.requestFocus();
				}
			}
		});
		
		tf.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {

				tf.getParent().requestFocus();

			}
		});
		
	}
	
}
