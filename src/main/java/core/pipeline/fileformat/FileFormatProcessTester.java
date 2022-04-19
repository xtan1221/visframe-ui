package core.pipeline.fileformat;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class FileFormatProcessTester extends Application {
//	public static VisProjectDBContext TEST_PROJECT_4;
//	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
//	static SimpleName project4Name = new SimpleName("project2");
//	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("new FileFormat");
        
        
//        TEST_PROJECT_4 = new VisProjectDBContext(project4Name,project4ParentDir);
//        TEST_PROJECT_4.connect();
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
//                manager.initialize();
                FileFormatProcessMainManager manager = new FileFormatProcessMainManager();
            	
                try {
					manager.start(primaryStage,VisProjectDBContextTest.getConnectedProject1());
				} catch (IOException | SQLException e) {
//					 TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}