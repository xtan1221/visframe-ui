package builder.visframe.fileformat;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import test.VisProjectDBContextTest;

public class FileFormatIDSelectorTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		

        FileFormatIDSelector builder = new FileFormatIDSelector(
        		"test","test", true,  
        		null,//parent node builder
        		VisProjectDBContextTest.getConnectedProject1().getHasIDTypeManagerController().getFileFormatManager(), 
        		DataType.RECORD,
        		null);
        
        VBox root = new VBox();
        
        
        Button btn = new Button();
        btn.setText("start"); 
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
