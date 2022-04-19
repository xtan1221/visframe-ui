package builder.visframe.basic;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import basic.SimpleName;
import context.project.VisProjectDBContext;
import core.builder.NonLeafNodeBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.DataType;
import rdb.sqltype.SQLDataTypeFactory;

public class VfNameStringBuilderTester2 extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
        
        
        VBox root = new VBox();
        
        
        HBox hbox = new HBox();
        TextField tf = new TextField();
        CheckBox cb = new CheckBox();
        Button b = new Button("hello");
        hbox.getChildren().add(tf);
        hbox.getChildren().add(cb);
        hbox.getChildren().add(b);
        root.getChildren().add(hbox);
        
        Button btn = new Button();
        btn.setText("transparent");
        root.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	hbox.setMouseTransparent(true);
            }
        });
        
        Button btn2 = new Button();
        btn2.setText("not transparent");
        root.getChildren().add(btn2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	hbox.setMouseTransparent(false);
            }
        });
        
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
