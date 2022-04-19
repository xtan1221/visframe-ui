package core.table.visframeUDT;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import basic.SimpleName;
import builder.visframe.fileformat.FileFormatBuilderFactory;
import context.project.VisProjectDBContext;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import fileformat.FileFormat;
import fileformat.FileFormatID;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import sql.derby.TableContentSQLStringFactory;

public class VisframeUDTTypeTableViewerManagerTester extends Application{
	
	public static VisProjectDBContext TEST_PROJECT_4;
	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName project4Name = new SimpleName("project2");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        TextField tf1 = new TextField();
        TextField tf2 = new TextField();
        root.getChildren().add(tf1);
        root.getChildren().add(tf2);
//        root.getChildren().add(btn2);
        
        TEST_PROJECT_4 = new VisProjectDBContext(project4Name,project4ParentDir);
        TEST_PROJECT_4.connect();
        
        
        
        Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new HashMap<>();
        invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.NAME_COLUMN.getName(), tf1);
        invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(FileFormatID.TYPE_COLUMN.getName(), tf2);
        
        VisframeUDTTypeTableViewerManager<FileFormat, FileFormatID> manager = 
        		new VisframeUDTTypeTableViewerManager<>(
        				TEST_PROJECT_4.getHasIDTypeManagerController().getFileFormatManager(),
        				FileFormatBuilderFactory.singleton(),
        				TableContentSQLStringFactory.buildColumnValueEquityCondition(FileFormatID.TYPE_COLUMN.getName().getStringValue(), DataType.RECORD.toString(), true, true),
        				null,
        				TableViewerMode.SELECTION,invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap);
//        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                
                try {
					manager.showWindow(primaryStage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
//        
        Button btn2 = new Button();
        btn2.setText("show sql type string");
        root.getChildren().add(btn2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println(manager.getTableViewDelegate().getCurrentlySelectedRow().getTableRow().getEntity().getID());
            	
            }
        });
//        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
