package builder.visframe.function.evaluator.sqlbased.utils;

import java.io.IOException;
import java.sql.SQLException;

import function.evaluator.sqlbased.utils.SqlString;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WhereConditionProcessorBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        WhereConditionProcessorBuilder builder = new WhereConditionProcessorBuilder(
        		"test", "test", true, null);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//            	builder.getEmbeddedUIRootContainerNodeController();
//                System.out.println(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        
        Button button2 = new Button("set to a predefined value");
        
        root.getChildren().add(button2);
        button2.setOnAction(e->{
        	
			try {
				builder.setValue(new SqlString("testsetsetseretes"), false);
			} catch (SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
        });
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
