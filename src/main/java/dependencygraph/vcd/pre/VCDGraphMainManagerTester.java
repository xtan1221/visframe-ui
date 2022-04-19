package dependencygraph.vcd.pre;

import dependency.vcd.VCDGraph;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class VCDGraphMainManagerTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        
        /////////////////////////////
//        VCDGraph visProjectDOSGraph = VisProjectDBContextTest.getConnectedProject2().getDOSGraph();
//		
//		double distBetweenLevels = 200;
//		double distBetweenNodesOnSameLevel = 200;
//		double nodeRootNodeHeight = 30;
//		double nodeRootNodeWidth = 30;
//		
//        VCDGraphMainManager builder = new VCDGraphMainManager(
//        		visProjectDOSGraph,
//        		VisProjectDBContextTest.getConnectedProject2()
////        		distBetweenLevels,
////        		distBetweenNodesOnSameLevel,
////        		nodeRootNodeHeight,
////        		nodeRootNodeWidth
//        		);
//        
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                
//				root.getChildren().add(builder.getController().getRootContainerNode());
//				
//            }
//        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
