package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FixedKeySetFixedValueSetMapBuilderTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
		Function<Integer,String> mapKeyToStringRepresentationFunction = (i)->{return Integer.toString(i);};
		Function<Integer,String> mapKeyToDescriptionFunction = (i)->{return Integer.toString(i);};
		Function<Integer,String> mapValueToStringRepresentationFunction = (i)->{return Integer.toString(i);};
		Function<Integer,String> mapValueToDescriptionFunction = (i)->{return Integer.toString(i);};
		
		BiPredicate<Integer,Integer> keyValuePairBiPredicate = (k,v)->{return k<v;};
		
		
		boolean allowingNullMapValue = false;
		boolean allowingDuplicateMapValue = true;
		
        FixedKeySetFixedValueSetMapBuilder<Integer, Integer> builder = new FixedKeySetFixedValueSetMapBuilder<>(
        		"test", "test", true, null,
        		mapKeyToStringRepresentationFunction,
        		mapKeyToDescriptionFunction,
        		mapValueToStringRepresentationFunction,
        		mapValueToDescriptionFunction,
        		keyValuePairBiPredicate,
        		allowingNullMapValue,
        		allowingDuplicateMapValue
        		);
        
        Set<Integer> keySet = new LinkedHashSet<>();
        keySet.add(1);
        keySet.add(2);
        keySet.add(3);
        keySet.add(4);
        keySet.add(5);
        
        Set<Integer> valueSet = new LinkedHashSet<>();
        valueSet.add(3);
        valueSet.add(4);
        valueSet.add(5);
        valueSet.add(6);
        valueSet.add(7);
        valueSet.add(11);
        valueSet.add(232);
        valueSet.add(5522);
        valueSet.add(111111);
        
        
        builder.setMapKeySet(keySet);
        builder.setMapValueSet(valueSet);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        /////////////////
//        Set<Integer> valueSet2 = new LinkedHashSet<>();
//        valueSet2.add(10);
//        valueSet2.add(11);
//        valueSet2.add(12);
//        valueSet2.add(13);
//        valueSet2.add(16);
//        
//        Button button2 = new Button("set value set to another set");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//						builder.setValueSet(valueSet2);
////        	builder.setValue(valueSet2, false);
//        });
        
        
        
        ///////////////////
        Map<Integer, Integer> mapValue = new HashMap<>();
        mapValue.put(1, 3);
        mapValue.put(2, 4);
        mapValue.put(3, 5);
        mapValue.put(4, 6);
        mapValue.put(5, 7);
        
        
        Button button3 = new Button("set map value");
        root.getChildren().add(button3);
        button3.setOnAction(e->{
				try {
					builder.setValue(mapValue, false);
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.setHeight(800);
        primaryStage.setWidth(1200);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
