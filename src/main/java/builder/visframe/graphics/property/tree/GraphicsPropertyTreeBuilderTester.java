package builder.visframe.graphics.property.tree;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GraphicsPropertyTreeBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        GraphicsPropertyTreeBuilder builder = new GraphicsPropertyTreeBuilder(
        		"test", "test", true, null);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//            	builder.getEmbeddedUIRootContainerNodeController();
//                System.out.println(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        
//        Button button2 = new Button("set to a predefined value");
//        
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//        	
//			builder.setValue("testsetsetseretes", false);
//        	
//        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
