package core.table.hasIDTypeRelationalTableSchema;

import java.nio.file.Path;
import java.nio.file.Paths;
import basic.SimpleName;
import context.project.VisProjectDBContext;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.MetadataID;
import metadata.MetadataName;
import metadata.record.RecordDataMetadata;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;
import visinstance.run.calculation.function.composition.CFTargetValueTableRunID;

public class HasIDTypeRelationalTableContentViewerTester extends Application{
	
	public static VisProjectDBContext TEST_PROJECT_4;
	static Path project4ParentDir = Paths.get("C:\\visframeUI-test");
	static SimpleName project4Name = new SimpleName("project8");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("start");
        
        
        VBox root = new VBox();
        
        root.getChildren().add(btn);
        
        TEST_PROJECT_4 = new VisProjectDBContext(project4Name,project4ParentDir);
        TEST_PROJECT_4.connect();
        
        
//        CFTargetValueTableRunID id = new CFTargetValueTableRunID(1);
//        CFTargetValueTableRun run = TEST_PROJECT_4.getHasIDTypeManagerController().getCFTargetValueTableRunManager().lookup(id);
//        HasIDTypeRelationalTableContentViewer manager = new HasIDTypeRelationalTableContentViewer(TEST_PROJECT_4, run, run.getValueTableSchema());
        
        
        MetadataID id = MetadataID.record(new MetadataName("domtbl_2")); //hmm_domtbl_54; domtbl_2
        RecordDataMetadata rdm =  (RecordDataMetadata)TEST_PROJECT_4.getHasIDTypeManagerController().getMetadataManager().lookup(id);
        HasIDTypeRelationalTableContentViewer manager = 
        		new HasIDTypeRelationalTableContentViewer(TEST_PROJECT_4, rdm, rdm.getDataTableSchema());
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	manager.showWindow(primaryStage);
            }
        });
        
        
        Button btn2 = new Button();
        btn2.setText("show sql type string");
        root.getChildren().add(btn2);
        btn2.setOnAction(e->{
        	//TODO
        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
