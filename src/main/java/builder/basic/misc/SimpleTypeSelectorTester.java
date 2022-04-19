package builder.basic.misc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Function;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleTypeSelectorTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        
        VBox root = new VBox();
        
        
        Collection<Integer> pool1 = new HashSet<>();
        for(int i=0;i<20;i++) {
        	pool1.add(i);
        }
        
        Collection<Integer> pool2 = new HashSet<>();
        for(int i=0;i<10;i++) {
        	pool2.add(i);
        }
        ///////
        Function<Integer,String> typeToStringRepresentationFunction = e->{return Integer.toString(e);};
        Function<Integer,String> typeToDescriptionFunction = e->{return Integer.toString(e);};
        
        SimpleTypeSelector<Integer> builder = new SimpleTypeSelector<>(
        		"test", "test", true, null, 
        		typeToStringRepresentationFunction, typeToDescriptionFunction);
        
        ///
        
        ///////////////////////////////////
        Button btn = new Button();
        btn.setText("set to poo1");
        root.getChildren().add(btn);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					builder.setPool(pool1);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
            }
        });
        
        
        //////////////////////
        
        Button btn2 = new Button();
        btn2.setText("set to another pool");
        root.getChildren().add(btn2);
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
					builder.setPool(pool2);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        
        ///////////////
        root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
        
        
        
        //////////////////////
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
