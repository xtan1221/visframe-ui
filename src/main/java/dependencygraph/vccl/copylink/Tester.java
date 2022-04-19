package dependencygraph.vccl.copylink;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Tester extends Application{
	@Override
	 public void start(Stage stage) throws Exception {

	    Group root = new Group();
	    Scene scene = new Scene(root);

	    Button button = new Button("Hello Worlddfafdfsdf");
	    HBox hbox = new HBox();
		hbox.getChildren().add(button);
		
	    root.getChildren().add(hbox);
	    
	    root.applyCss();
	    root.layout();
	    
	    
	    double width = hbox.getWidth();
	    double height = hbox.getHeight();
	    
	    System.out.println(width + ", " + height);
	    
	    double width2 = button.getWidth();
	    double height2 = button.getHeight();

	    System.out.println(width2 + ", " + height2);
	    
	    stage.setScene(scene);
	    stage.show();
	 }
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
