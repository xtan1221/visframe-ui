package dependencygraph.vccl.copylink;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import basic.VfNotes;
import dependency.TestDAGFactory;
import dependency.TestDAGFactory.SimpleEdge;
import dependency.vccl.utils.NodeCopy;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CopyLinkAssignerManagerTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Button btn = new Button();
        btn.setText("start");
        Button btn1 = new Button();
        btn1.setText("print current value");
        Button btn2 = new Button();
        btn2.setText("print most recently saved value");
        
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
        
//        Map<Integer, Integer> nodeCopyNumberMap = new HashMap<>();
//        nodeCopyNumberMap.put(1, 2);
//        nodeCopyNumberMap.put(2, 2);
//        nodeCopyNumberMap.put(3, 2);
//        nodeCopyNumberMap.put(4, 2);
////        nodeCopyNumberMap.put(5, 1);
////        nodeCopyNumberMap.put(6, 1);
////        nodeCopyNumberMap.put(7, 1);
        
        Map<Integer, Map<Integer, NodeCopy<Integer>>> nodeCopyIndexNodeCopyMapMap = new HashMap<>();
        nodeCopyIndexNodeCopyMapMap.put(1, new HashMap<>());
        nodeCopyIndexNodeCopyMapMap.get(1).put(1, new NodeCopy<>(1, 1, new VfNotes("1-1")));
        nodeCopyIndexNodeCopyMapMap.get(1).put(2, new NodeCopy<>(1, 2, new VfNotes("1-2")));
        
        nodeCopyIndexNodeCopyMapMap.put(2, new HashMap<>());
        nodeCopyIndexNodeCopyMapMap.get(2).put(1, new NodeCopy<>(2, 1, new VfNotes("2-1")));
        nodeCopyIndexNodeCopyMapMap.get(2).put(2, new NodeCopy<>(2, 2, new VfNotes("2-2")));
        
        nodeCopyIndexNodeCopyMapMap.put(3, new HashMap<>());
        nodeCopyIndexNodeCopyMapMap.get(3).put(1, new NodeCopy<>(3, 1, new VfNotes("3-1")));
        nodeCopyIndexNodeCopyMapMap.get(3).put(2, new NodeCopy<>(3, 2, new VfNotes("3-2")));
        
        nodeCopyIndexNodeCopyMapMap.put(4, new HashMap<>());
        nodeCopyIndexNodeCopyMapMap.get(4).put(1, new NodeCopy<>(4, 1, new VfNotes("4-1")));
        nodeCopyIndexNodeCopyMapMap.get(4).put(2, new NodeCopy<>(4, 2, new VfNotes("4-2")));
        
        
        
		double distBetweenLevels = 200;
		double distBetweenNodesOnSameLevel = 200;
		
		CopyLinkAssignerManager<Integer, SimpleEdge> builder = new CopyLinkAssignerManager<>(
				TestDAGFactory.make4NodeDAG2(),
				SimpleEdge.class,
				nodeCopyIndexNodeCopyMapMap,
				nodeOrderComparator,
				distBetweenLevels,
				distBetweenNodesOnSameLevel,
//				nodeRootNodeHeight,
//				nodeRootNodeWidth,
				nodeInforStringFunction
//        		distBetweenLevels,
//        		distBetweenNodesOnSameLevel,
//        		nodeRootNodeHeight,
//        		nodeRootNodeWidth
        		);
        
		
		builder.setBeforeChangeMadeWarningInforTextAndAfterChangeMadeAction(()->{System.out.println("changes successfully made!");}, "change is made!");
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getController().getRootNodeContainer());
				
            }
        });
        
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//            	builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingCopy, dependedCopy)->{
////            		
//            	});
				
				System.out.println(builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().toString());
            }
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//            	builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingCopy, dependedCopy)->{
////            		
//            	});
				
				System.out.println(
						builder.getMostRecentlySavedCopyLinkAssignment()==null?
								builder.getMostRecentlySavedCopyLinkAssignment():
									builder.getMostRecentlySavedCopyLinkAssignment().toString());
            }
        });
//        builder.calculateNodeCopyRootFXNodeLayoutCoordAndRealHeightAndWidth();
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
