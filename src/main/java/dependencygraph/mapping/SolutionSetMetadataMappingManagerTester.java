package dependencygraph.mapping;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import dependency.TestDAGFactory;
import dependency.TestDAGFactory.SimpleEdge;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SolutionSetMetadataMappingManagerTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Button btn = new Button();
        btn.setText("start");
        Button btn1 = new Button();
        btn1.setText("print current value");
        Button btn2 = new Button();
        btn2.setText("print previous value");
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);
        
        /////////////////////////////
        Comparator<Integer> nodeOrderComparator = (a,b)->{
        	return a.compareTo(b);
        };
		
        Function<Integer,String> nodeInforStringFunction = (n)->{
        	return Integer.toString(n);
        };
        
        Map<Integer, Integer> nodeCopyNumberMap = new HashMap<>();
        nodeCopyNumberMap.put(1, 2);
        nodeCopyNumberMap.put(2, 2);
        nodeCopyNumberMap.put(3, 2);
        nodeCopyNumberMap.put(4, 2);
//        nodeCopyNumberMap.put(5, 1);
//        nodeCopyNumberMap.put(6, 1);
//        nodeCopyNumberMap.put(7, 1);
        
        
		double distBetweenLevels = 200;
		double distBetweenNodesOnSameLevel = 200;
		
//		CopyLinkAssignerManager<Integer, SimpleEdge> builder = new CopyLinkAssignerManager<>(
//				TestDAGFactory.make4NodeDAG2(),
//				SimpleEdge.class,
//				nodeCopyNumberMap,
//				nodeOrderComparator,
//				distBetweenLevels,
//				distBetweenNodesOnSameLevel,
////				nodeRootNodeHeight,
////				nodeRootNodeWidth,
//				nodeInforStringFunction
////        		distBetweenLevels,
////        		distBetweenNodesOnSameLevel,
////        		nodeRootNodeHeight,
////        		nodeRootNodeWidth
//        		);
//        
//		
//		builder.setChangeMadeInforTextAndAction(()->{System.out.println("changes successfully made!");}, "change is made!");
//		
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                
//				root.getChildren().add(builder.getController().getRootNodeContainer());
//				
//            }
//        });
//        
//        btn1.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////            	builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingCopy, dependedCopy)->{
//////            		
////            	});
//				
//				System.out.println(builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().toString());
//            }
//        });
//        
//        btn2.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////            	builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingCopy, dependedCopy)->{
//////            		
////            	});
//				
//				System.out.println(
//						builder.getPreviousDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap()==null?
//								builder.getPreviousDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap():
//									builder.getPreviousDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().toString());
//            }
//        });
////        builder.calculateNodeCopyRootFXNodeLayoutCoordAndRealHeightAndWidth();
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
