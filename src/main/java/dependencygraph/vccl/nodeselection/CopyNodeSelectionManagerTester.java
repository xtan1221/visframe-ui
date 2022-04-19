package dependencygraph.vccl.nodeselection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

public class CopyNodeSelectionManagerTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Button btn = new Button();
        btn.setText("start");
        Button btn1 = new Button();
        btn1.setText("print selected copies");
        
        VBox root = new VBox();
        root.getChildren().add(btn);
        root.getChildren().add(btn1);
        
        /////////////////////////////
        Comparator<Integer> nodeOrderComparator = (a,b)->{
        	return a.compareTo(b);
        };
		
        Function<Integer,String> nodeInforStringFunction = (n)->{
        	return Integer.toString(n);
        };
        

        Map<Integer, Integer> DAGNodeCopyIndexMap = new HashMap<>();
        DAGNodeCopyIndexMap.put(1, 2);
        DAGNodeCopyIndexMap.put(2, 2);
        DAGNodeCopyIndexMap.put(3, 2);
        DAGNodeCopyIndexMap.put(4, 2);
        
        
        Set<CopyLink<Integer>> copyLinkSet = new HashSet<>();
        copyLinkSet.add(new CopyLink<>(2,1,1,2));
        copyLinkSet.add(new CopyLink<>(4,2,3,1));
        
        
		double distBetweenLevels = 200;
		double distBetweenNodesOnSameLevel = 200;
		
		CopyNodeSelectionManager<Integer, SimpleEdge> builder = new CopyNodeSelectionManager<>(
				TestDAGFactory.make4NodeDAG2(),
				DAGNodeCopyIndexMap,
				copyLinkSet,
				nodeOrderComparator,
				distBetweenLevels,
				distBetweenNodesOnSameLevel,
				nodeInforStringFunction,
				()->{System.out.println("changed");}
        		);
        
		
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getController().getRootNodeContainer());
				
            }
        });
        
       
        
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	builder.getSelectedDAGNodeCopyIndexPairSet().forEach(pair->{
            		System.out.println(pair);
            	});
            }
        });
        
        
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.show();
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
