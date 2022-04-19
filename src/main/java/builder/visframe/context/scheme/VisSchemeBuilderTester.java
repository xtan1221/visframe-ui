package builder.visframe.context.scheme;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class VisSchemeBuilderTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		VisSchemeBuilder builder = new VisSchemeBuilder(
        		VisProjectDBContextTest.getConnectedProject2());
        
        VBox root = new VBox();
        
        Button btn = new Button();
        btn.setText("start"); 
        root.getChildren().add(btn);
        
        ScrollPane sp = new ScrollPane();
        root.getChildren().add(sp);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	sp.setContent(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
