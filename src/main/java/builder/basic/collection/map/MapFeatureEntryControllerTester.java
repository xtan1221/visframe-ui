package builder.basic.collection.map;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MapFeatureEntryControllerTester extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("add");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(MapFeatureEntryController.FXML_FILE_DIR));
        		
            	//this must be invoked before the getController() method!!!!!!!
            	Node entry;
				try {
					entry = loader.load();
					root.getChildren().add(entry);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
