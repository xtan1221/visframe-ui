package core.pipeline.cfg.utils;

import function.group.CompositionFunctionGroup;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CompositionFunctionGroupTypeTreeViewTester extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("tester!");

        VBox root = new VBox();
        
        
        TextField tf = new TextField();
        root.getChildren().add(tf);
        TreeView<Class<? extends CompositionFunctionGroup>> treeView = CompositionFunctionGroupTypeTreeViewUtils.makeTreeView(tf);
        
        root.getChildren().add(treeView);
        
        
        
        
        /////////////////////////
        Button btn2 = new Button();
        btn2.setText("print selected");
        root.getChildren().add(btn2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	TreeItem<Class<? extends CompositionFunctionGroup>> selected = treeView.getSelectionModel().getSelectedItem();
                System.out.println(selected==null?"null":selected.getValue().getSimpleName());
                
            }
        });
        
        
        Button btn3 = new Button();
        btn3.setText("clear selection");
        root.getChildren().add(btn3);
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	treeView.getSelectionModel().clearSelection();
            }
        });
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
