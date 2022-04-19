package viewer.visframe.graphics.property.node;

import java.nio.file.Path;
import java.nio.file.Paths;
import basic.SimpleName;
import context.project.VisProjectDBContext;
import graphics.property.shape2D.factory.VfBasicGraphicsPropertyNodeFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GraphicsPropertyNonLeafNodeViewerTester extends Application{
	
	public static VisProjectDBContext TEST_PROJECT_4;
	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName project4Name = new SimpleName("project8");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        GraphicsPropertyNonLeafNodeViewer viewer = new GraphicsPropertyNonLeafNodeViewer(VfBasicGraphicsPropertyNodeFactory.LAYOUT_CART_2D);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                root.getChildren().add(viewer.getController().getRootContainerPane());
            }
        });
        
        
//        Button btn2 = new Button();
//        btn2.setText("show sql type string");
//        root.getChildren().add(btn2);
//        btn2.setOnAction(e->{
//        	//TODO
//        });
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
