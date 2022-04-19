package builder.visframe.function.composition;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnchorPaneLayoutTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane ap = new AnchorPane();
        ap.setPrefHeight(400);
        ap.setPrefWidth(400);
        
        ap.setStyle("-fx-background-color:red");
        
        Button button = new Button("move");
        
        
        
        VBox vbox = new VBox();
        vbox.setLayoutX(100);
        vbox.setLayoutY(100);
        vbox.setStyle("-fx-background-color:blue");
        vbox.setPrefHeight(100);
        vbox.setPrefWidth(200);
        
        
//        vbox.getChildren().add(new Label("test"));
        
        TextField tf = new TextField("test");
        
//        tf.setLayoutX(300);
//        tf.setLayoutY(200);
        
        button.setOnAction(e->{
        	tf.setLayoutX(tf.getLayoutX()+50);
        });
        ap.getChildren().add(button);
        ap.getChildren().add(vbox);
        ap.getChildren().add(tf);
        
        System.out.println("tf layoutx:"+tf.getLayoutX());
        System.out.println("tf layouty:"+tf.getLayoutY());
        
        primaryStage.setScene(new Scene(ap, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
