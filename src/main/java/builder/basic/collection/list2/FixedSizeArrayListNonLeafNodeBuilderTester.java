package builder.basic.collection.list2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import basic.SimpleName;
import builder.basic.primitive.BooleanTypeBuilderFactory;
import builder.basic.primitive.StringTypeBuilderFactory;
import builder.visframe.basic.VfNameStringBuilderFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FixedSizeArrayListNonLeafNodeBuilderTester extends Application{
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        //String name, String description, boolean canBeNull
        StringTypeBuilderFactory stringTypeBuilderFactory = new StringTypeBuilderFactory("key","key",false);
        BooleanTypeBuilderFactory booleanTypeBuilderFactory = new BooleanTypeBuilderFactory("value","value", true);
        
        VfNameStringBuilderFactory<SimpleName> simpleNameBuilderFactory = new VfNameStringBuilderFactory<>("element","element", false, SimpleName.class);
        
        FixedSizeArrayListNonLeafNodeBuilder<SimpleName> builder = new FixedSizeArrayListNonLeafNodeBuilder<>(
        		"test", "test", true, null,
        		simpleNameBuilderFactory,
        		false//not allow duplicate
        		);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
            }
        });
        
//        HashSet<String> set = new HashSet<>();
//        set.add("safdf");
//        set.add("df");
//        set.add("2222");
//        set.add("@!!@");
        
        ArrayList<SimpleName> list = new ArrayList<>();
        list.add(new SimpleName("safdf"));
        list.add(new SimpleName("dfdf_d1111"));
        list.add(new SimpleName("df232321_df"));
        list.add(new SimpleName("fdf12121"));
        
        Button button2 = new Button("set size to 10");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
				try {
					builder.setSize(10);
				} catch (SQLException | IOException e1) {
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
