package core.pipeline.operation;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class OperationProcessMainManagerTester extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("new FileFormat");
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
//                manager.initialize();
                OperationProcessMainManager manager = new OperationProcessMainManager();
            	
                try {
					manager.start(primaryStage,VisProjectDBContextTest.getConnectedProject3());
				} catch (IOException | SQLException e) {
//					 TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}