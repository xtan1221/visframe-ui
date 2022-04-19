package builder.basic.collection.set.leaf;

import java.util.LinkedHashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FixedPoolLinkedHashSetSelectorTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		VBox root = new VBox();
		
        FixedPoolLinkedHashSetSelector<String> builder = new FixedPoolLinkedHashSetSelector<>(
        		"test", "test", true, null, e->{return e;});
        
        
        Set<String> set = new LinkedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");
        set.add("d");
        builder.setPool(set);
        
        Button btn = new Button();
        btn.setText("start");
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
					root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        
        Set<String> set2 = new LinkedHashSet<>();
        set2.add("1");
        set2.add("2");
        set2.add("3");
        set2.add("4");
        Button button2 = new Button();
        button2.setText("set to another pool");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
        	
        	builder.setPool(set2);
        	
        });
        
       
//        Button button3 = new Button();
//        button3.setText("pring current status");
//        root.getChildren().add(button3);
//        button3.setOnAction(e->{
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
