package core.builder.utils;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConstraintsWarningAlertManagerTester extends Application{
	
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
            	ConstraintsViolotionWarningAlertManager alert;

				alert = new ConstraintsViolotionWarningAlertManager(primaryStage, new ArrayList<>());
				alert.showAndWait();
            	
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}