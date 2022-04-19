package builder.visframe.function.group;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import test.VisProjectDBContextTest;

public class CompositionFunctionGroupIDSelectorTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Set<DataType> set = new HashSet<>();
		set.add(DataType.RECORD);
		
		CompositionFunctionGroupIDSelector builder = new CompositionFunctionGroupIDSelector(
        		"test","test", true,  
        		null,//parent node builder
        		VisProjectDBContextTest.getConnectedProject1().getHasIDTypeManagerController().getCompositionFunctionGroupManager(), 
        		null,
        		null);
        
        VBox root = new VBox();
        
        
        Button btn = new Button();
        btn.setText("start"); 
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

            }
        });
        
        
//        MetadataID id = new MetadataID(new MetadataName("gff3_23"), DataType.RECORD);
//        
//        
//        Button btn2 = new Button();
//        btn2.setText("set to non-null value");
//        root.getChildren().add(btn2);
//        btn2.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                
//				builder.setValue(id, false);
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
