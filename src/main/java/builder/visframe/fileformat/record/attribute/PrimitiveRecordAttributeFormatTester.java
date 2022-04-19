package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import fileformat.record.attribute.PrimitiveRecordAttributeFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rdb.sqltype.SQLDataTypeFactory;

public class PrimitiveRecordAttributeFormatTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        PrimitiveRecordAttributeFormatBuilder builder = new PrimitiveRecordAttributeFormatBuilder(
        		"test", "test", true, null);
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

            }
        });
        
        
        PrimitiveRecordAttributeFormat graphTypeEnforcer = new PrimitiveRecordAttributeFormat(
        		new SimpleName("attribute1"), VfNotes.makeVisframeDefinedVfNotes(), SQLDataTypeFactory.booleanType());
        
        Button button2 = new Button("set to a pre-defined value");
        root.getChildren().add(button2);
        button2.setOnAction(e->{
        	
				try {
					builder.setValue(graphTypeEnforcer, false);
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
