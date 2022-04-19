package viewer.visframe.graphics.shape;

import graphics.shape.shape2D.type.VfLine;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VfShapeTypeViewerTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        VfShapeTypeViewer viewer = new VfShapeTypeViewer(VfLine.SINGLETON);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Scene scene = new Scene(viewer.getController().getRootContainerPane());
            	
            	Stage stage = new Stage();
            	stage.setScene(scene);
            	stage.initModality(Modality.WINDOW_MODAL);
            	stage.show();
            }
        });
        
        
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
