package builder.basic.collection.misc;

import java.util.LinkedHashSet;
import java.util.Set;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoubleListSelectorTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		
		DoubleListSelector<Integer, Integer> builder = new DoubleListSelector<>(
        		"test", "test", true, null, 
        		
        		e->{return Integer.toString(e);},//Function<K1,K2> mapKeyFunction,
        		e->{return Integer.toString(e);}//Function<V1,V2> mapValueFunction,
        		
				);
        
        
        Set<Integer> keyPool = new LinkedHashSet<>();
        for(int i=10;i<13;i++) {
        	keyPool.add(i);
        }
        Set<Integer> valuePool = new LinkedHashSet<>();
        for(int i=0;i<20;i++) {
        	valuePool.add(i);
        }
        
        builder.setPools(keyPool, valuePool);
        
        Button btn = new Button();
        btn.setText("start");
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
					root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
            }
        });
        
        
//        /////////////////////
//        LinkedHashMap<String, String> first = new LinkedHashMap<>();
//        first.put("10", "2");
//        first.put("11", "10");
//        first.put("12", "4");
//        LinkedHashMap<String, String> second = new LinkedHashMap<>();
//        second.put("10", "1");
//        second.put("11", "0");
//        second.put("12", "11");
//        
//        
//        Button button2 = new Button();
//        button2.setText("set to non-null value");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//        	builder.getEmbeddedUIContentController().setUIToNonNullValue(new Pair<>(first, second));
//        });
        
       
//        Button button3 = new Button();
//        button3.setText("pring current status");
//        root.getChildren().add(button3);
//        button3.setOnAction(e->{
//        	
//        	System.out.println(builder.getCurrentStatus());
//        	
//        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
