package builder.visframe.fileformat.record;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import basic.SimpleName;
import fileformat.record.RecordDataFileFormat.PrimaryKeyAttributeNameSet;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimaryKeyAttributeNameSetBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        PrimaryKeyAttributeNameSetBuilder builder = new PrimaryKeyAttributeNameSetBuilder(
        		"test", "test", true, null
        		);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

            }
        });
        
        
        Set<SimpleName> s1 = new HashSet<>();
        s1.add(new SimpleName("s1sdfd"));
        s1.add(new SimpleName("dfdffdfdf"));
        s1.add(new SimpleName("s1sd232_32sdfdf"));
        Set<SimpleName> s2 = new HashSet<>();
        s2.add(new SimpleName("dfasfdfdfsdfdf"));
        PrimaryKeyAttributeNameSet set = new PrimaryKeyAttributeNameSet(s1,s2);
        		
        
        Button button2 = new Button("set to a pre-defined value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
				try {
					builder.setValue(set, false);
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
