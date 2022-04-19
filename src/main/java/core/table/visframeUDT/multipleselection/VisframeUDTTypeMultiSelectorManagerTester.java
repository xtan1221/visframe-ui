package core.table.visframeUDT.multipleselection;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import basic.SimpleName;
import context.project.VisProjectDBContext;
import fileformat.FileFormatID;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.Metadata;
import metadata.MetadataID;

public class VisframeUDTTypeMultiSelectorManagerTester extends Application{
	
	public static VisProjectDBContext TEST_PROJECT;
	static Path projectParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName projectName = new SimpleName("project2");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
//        TextField tf1 = new TextField();
//        TextField tf2 = new TextField();
//        root.getChildren().add(tf1);
//        root.getChildren().add(tf2);
//        root.getChildren().add(btn2);
        
        TEST_PROJECT = new VisProjectDBContext(projectName,projectParentDir);
        TEST_PROJECT.connect();
        
        
        
        VisframeUDTTypeMultiSelectorManager<Metadata, MetadataID> manager = 
        		new VisframeUDTTypeMultiSelectorManager<>(
        				TEST_PROJECT.getHasIDTypeManagerController().getMetadataManager(),
        				null,
        				null,
        				null
        				);
//        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
                try {
					manager.showAndWait(primaryStage);
					
					System.out.println("selected items:");
					manager.getTableViewDelegate().getCurrentlySelectedRowSet().forEach(e->{
						System.out.println(e);
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
//        
//        Button btn2 = new Button();
//        btn2.setText("show sql type string");
//        root.getChildren().add(btn2);
//        btn2.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//            	System.out.println(manager.getTableViewDelegate().getCurrentlySelectedRow().getTableRow().getEntity().getID());
//            	
//            }
//        });
////        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
