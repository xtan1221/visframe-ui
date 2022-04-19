package viewer.visframe.metadata.graph;

import java.nio.file.Path;
import java.nio.file.Paths;
import basic.SimpleName;
import context.project.VisProjectDBContext;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import metadata.graph.GraphDataMetadata;

public class GraphDataMetadataViewerTester extends Application{
	
	public static VisProjectDBContext TEST_PROJECT_4;
	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName project4Name = new SimpleName("project3");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        TEST_PROJECT_4 = new VisProjectDBContext(project4Name,project4ParentDir);
        TEST_PROJECT_4.connect();
        
        
        GraphDataMetadata graphMetadata = 
        		(GraphDataMetadata) TEST_PROJECT_4.getHasIDTypeManagerController().getMetadataManager()
        		.lookup(new MetadataID(new MetadataName("graph_from_vftree"), DataType.GRAPH)); //mono1 (vftree); graph_from_vftree
        
        
        GraphDataMetadataViewer viewer = new GraphDataMetadataViewer(graphMetadata, TEST_PROJECT_4);
        
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
