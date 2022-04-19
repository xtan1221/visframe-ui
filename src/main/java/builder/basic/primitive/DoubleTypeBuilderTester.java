package builder.basic.primitive;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoubleTypeBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
//        Button button2 = new Button("print current status");
//        root.getChildren().add(button2);
        
//        String name, String description, boolean canBeNull,
//		NonLeafNodeBuilder<?, ?, ?> parentNodeBuilder
        DoubleTypeBuilder builder = new DoubleTypeBuilder(
        		"test", "test", true, null, 
        		e-> {return e>=-100.2 && e<25.232;},
        		"value must be in [-100.2,25.232]" );
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
			
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
