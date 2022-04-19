package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import builder.basic.primitive.BooleanTypeBuilderFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FixedKeySetMapValueBuilderTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        //String name, String description, boolean canBeNull
//        StringTypeBuilderFactory stringTypeBuilderFactory = new StringTypeBuilderFactory("key","key",false);
        BooleanTypeBuilderFactory booleanTypeBuilderFactory = new BooleanTypeBuilderFactory("value","value", false);
        
        FixedKeySetMapValueBuilder<String, Boolean> builder = new FixedKeySetMapValueBuilder<>(
        		"test", "test", true, null,
//        		stringTypeBuilderFactory,
        		booleanTypeBuilderFactory,
        		e->{return e;},
        		e->{return e;},
        		null,
        		false,
        		false
        		);
        
        
        Set<String> keySet = new LinkedHashSet<>();
        keySet.add("a");
        keySet.add("b");
        keySet.add("c");
        keySet.add("d");
        keySet.add("e");
        keySet.add("f");
        
        builder.setKeySet(keySet);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
        
        HashMap<String, Boolean> map = new HashMap<>();
        map.put("a", true);
        map.put("b", true);
        map.put("c", false);
        map.put("d", false);
        map.put("e", true);
        map.put("f", false);
        
        Button button2 = new Button("set to a pre-defined value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
					try {
						builder.setValue(map, false);
					} catch (SQLException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        });
        
//        builder.setModifiable(false);
        
//        Button button3 = new Button("set to a pre-defined value");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//        	
//        	try {
//				builder.setValue(graphTypeEnforcer, false);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//        	
//        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
