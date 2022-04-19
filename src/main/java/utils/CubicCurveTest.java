package utils;

import javafx.application.Application;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class CubicCurveTest extends Application{
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		///////////////////////
		AnchorPane ap = new AnchorPane();
		ap.setStyle("-fx-background-color:red");
		
		CubicCurve cc = new CubicCurve();
		
		cc.setStartX(500);
		cc.setStartY(500);
		
		cc.setEndX(800);
		cc.setEndY(600);
		
		cc.setControlX1(600);
		cc.setControlY1(500);
		
		cc.setControlX2(700);
		cc.setControlY2(600);
		
		cc.setStroke(Color.BLUE);
	    cc.setStrokeWidth(2);
	    cc.setStrokeLineCap(StrokeLineCap.ROUND);
//	    cc.setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.6));
	    cc.setFill(null);
	    
	    
		ap.getChildren().add(cc);
		
        primaryStage.setScene(new Scene(ap, 1200, 800));
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}