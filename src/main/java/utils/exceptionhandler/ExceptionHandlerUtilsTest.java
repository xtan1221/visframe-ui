package utils.exceptionhandler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExceptionHandlerUtilsTest extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("tester!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	try{
            		Double d = Double.parseDouble("dfadfadf");
            	}catch(Exception e) {
            		ExceptionHandlerUtils.show2("test", e);
            	}
            	
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}