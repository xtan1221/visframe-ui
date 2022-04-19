package misc;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SelectTypePopupBuilderTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
//     
        
        Set<String> pool = new HashSet<>();
        pool.add("a");
        pool.add("b");
        pool.add("c");
        pool.add("d");
        
        
        SelectTypePopupBuilder<String> builder = new SelectTypePopupBuilder<>(
        		primaryStage, e->{return e;}, pool);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("start");
            	String ret = builder.showAndWait();
            	System.out.println("end");
            	
            	System.out.println(ret==null?"null":ret);
            }
        });
        
//        button2.setOnAction(e->{
//        	
//        	System.out.println(builder.getCurrentStatus());
//        	
//        });
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
