package builder.basic.primitive;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntegerTypeBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
 
        
//        String name, String description, boolean canBeNull,
//		NonLeafNodeBuilder<?, ?, ?> parentNodeBuilder
        IntegerTypeBuilder builder = new IntegerTypeBuilder(
        		"test", "test", true, null, 
        		e-> {return e>=0 && e<Integer.MAX_VALUE;},
        		"value must be in [0,".concat(Integer.toString(Integer.MAX_VALUE)).concat("]!") );
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        
        Button button2 = new Button("set to null value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
        	
//        	try {
			try {
				builder.setValue(null, false);
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
        	
        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
