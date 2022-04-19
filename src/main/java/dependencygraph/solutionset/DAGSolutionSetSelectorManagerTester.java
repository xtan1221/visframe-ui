package dependencygraph.solutionset;

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

public class DAGSolutionSetSelectorManagerTester extends Application{
	
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
        
        
		double distBetweenLevels = 150;
		double distBetweenNodesOnSameLevel = 200;
		
		DAGSolutionSetSelectorManager<Integer, SimpleEdge> builder = new DAGSolutionSetSelectorManager<>(
				TestDAGFactory.makeSolutionSetSelectionDAG(),
//				SimpleEdge.class,
//				nodeCopyNumberMap,
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
				
				System.out.println(builder.getCurrentlySelectedSet().toString());
            }
        });
        
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//            	builder.getCurrentDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap().forEach((dependingCopy, dependedCopy)->{
////            		
//            	});
				
				System.out.println(
						builder.getMostRecentlySavedSelectedSet()==null?
								builder.getMostRecentlySavedSelectedSet():
									builder.getMostRecentlySavedSelectedSet().toString());
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
