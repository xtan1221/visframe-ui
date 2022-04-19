package utils;

import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import function.group.CompositionFunctionGroupName;
import javafx.application.Application;
import javafx.beans.binding.NumberBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import test.VisProjectDBContextTest;

public class PropertyBindingUtilsTester extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		VBox container = new VBox();
		
		Button button = new Button("moveHBox");
		container.getChildren().add(button);
		
		Button button2 = new Button("moveCircle");
		container.getChildren().add(button2);
		
		///////////////////////
		AnchorPane ap = new AnchorPane();
		ap.setStyle("-fx-background-color:red");
		container.getChildren().add(ap);
		
		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color:yellow");
		Circle circle = new Circle();
        circle.setRadius(100);
        
        hbox.setOnMouseClicked(e->{
        	System.out.println(hbox.getLayoutX());
        	e.consume();
        });
        
		ap.getChildren().add(hbox);
		
		hbox.getChildren().add(circle);
		
		
		NumberBinding circleLayoutX = PropertyBindingUtils.buildInnestLayoutXPropertyBinding(ap, circle);
		
		circleLayoutX.addListener((a1,oldValue,newValue)->{
			System.out.println(newValue);
		});
		
		circle.setOnMouseClicked(e->{
	        	System.out.println("layoutx:"+circle.getLayoutX()+";radius="+circle.getRadius());
	        	e.consume();
	     });
		
		

		button.setOnAction(e->{
			hbox.setLayoutX(hbox.getLayoutX()+10);
			
		});
		
		button2.setOnAction(e->{
			circle.setLayoutX(circle.getLayoutX()+10);
			
		});
		
        primaryStage.setScene(new Scene(container, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
