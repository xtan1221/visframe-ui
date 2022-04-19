package builder.visframe.basic;

import basic.SimpleName;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VfNameStringBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        Button button2 = new Button("print current status");
        root.getChildren().add(button2);
        
//        String name, String description, boolean canBeNull,
//		NonLeafNodeBuilder<?, ?, ?> parentNodeBuilder
        VfNameStringBuilder<SimpleName> builder = new VfNameStringBuilder<>(
        		"test", "test", true, null, SimpleName.class);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        button2.setOnAction(e->{
        	
        	System.out.println(builder.getCurrentStatus());
        	
        });
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
