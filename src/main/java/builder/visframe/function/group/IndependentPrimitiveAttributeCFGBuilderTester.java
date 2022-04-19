package builder.visframe.function.group;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class IndependentPrimitiveAttributeCFGBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("start");
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        IndependentPrimitiveAttributeCFGBuilder builder = new IndependentPrimitiveAttributeCFGBuilder(
        		VisProjectDBContextTest.getConnectedProject1().getHasIDTypeManagerController().getMetadataManager());
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				root.getChildren().add(builder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());

            }
        });
        
        
//        TagFormat tagFormat = new TagFormat(
//        		false, null, 
//        		SQLDataTypeFactory.booleanType(),
//        		new PlainStringMarker("=", false),
//        		0, 1, null);
//        		
//        
//        Button button2 = new Button("set to null");
//        root.getChildren().add(button2);
//        button2.setOnAction(e->{
//				try {
//					builder.setValue(null, false);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//        });
        
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
