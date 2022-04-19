package builder.visframe.symja;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import symja.VfSymjaExpressionString;

public class VfSymjaExpressionStringBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        VfSymjaExpressionStringBuilder builder = new VfSymjaExpressionStringBuilder(
        		"test", "test", true, null
        		);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        
        VfSymjaExpressionString graphTypeEnforcer = new VfSymjaExpressionString("a+b*c/d");
        
        Button button2 = new Button("set to a pre-defined value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
				try {
					builder.setValue(graphTypeEnforcer, false);
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        });
        
//        builder.setModifiable(false);
        
//        Button button3 = new Button("set to a pre-defined value");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//        	
//        	try {
//				builder.setValue(graphTypeEnforcer, false);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//        	
//        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
