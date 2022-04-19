package dependencygraph.cfd2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;

import org.jgrapht.graph.SimpleDirectedGraph;

import basic.SimpleName;
import context.project.VisProjectDBContext;
import dependency.cfd.CFDEdgeImpl;
import dependency.cfd.CFDNodeImpl;
import dependencygraph.cfd.CFDNodeOrderingComparatorFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleCFDGraphViewerManagerTester extends Application{
	public static VisProjectDBContext TEST_PROJECT_4;
	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName project4Name = new SimpleName("project7");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        
        /////////////////////////////
        TEST_PROJECT_4 = new VisProjectDBContext(project4Name,project4ParentDir);
        TEST_PROJECT_4.connect();
		SimpleDirectedGraph<CFDNodeImpl, CFDEdgeImpl> underlyingSimpleCFDGraph = TEST_PROJECT_4.getCFDGraph().getUnderlyingGraph();
		
		Comparator<CFDNodeImpl> nodeOrderComparator = CFDNodeOrderingComparatorFactory.getComparator();
		double distBetweenLevels = 200;
		double distBetweenNodesOnSameLevel = 200;
		Function<CFDNodeImpl,String> dagNodeInforStringFunction = (a)->{return a.getCFID().toString();};
        
		SimpleCFDGraphViewerManager manager = new SimpleCFDGraphViewerManager(
				TEST_PROJECT_4,
				underlyingSimpleCFDGraph,
				nodeOrderComparator,
				distBetweenLevels,
				distBetweenNodesOnSameLevel,
				dagNodeInforStringFunction
				);
		
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	root.getChildren().add(manager.getController().getRootContainerNode());
            }
        });
        
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
