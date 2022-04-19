package builder.visframe.operation.sql.generic.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import metadata.MetadataID;
import metadata.MetadataName;
import test.VisProjectDBContextTest;

public class TableAndColumnsMappingBuilderTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		String tableAliasName = "table_1";
		
		Set<String> columnAliasNameSet = new LinkedHashSet<>();
		columnAliasNameSet.add("c1");
		columnAliasNameSet.add("c2");
		columnAliasNameSet.add("c3");
		
		TableAndColumnsMappingBuilder builder = new TableAndColumnsMappingBuilder(
        		"test","test", true,  
        		null,//parent node builder
        		VisProjectDBContextTest.getConnectedProject1());
        
		builder.setTableAndColumnNameSet(tableAliasName, columnAliasNameSet);
		
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
        
        
        MetadataID id = new MetadataID(new MetadataName("gff3_23"), DataType.RECORD);
        
        
        Button btn2 = new Button();
        btn2.setText("set to non-null value");
        root.getChildren().add(btn2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
				try {
					builder.setValue(id, false);
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            }
        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
