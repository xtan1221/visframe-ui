package utils;

import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DraggableTabPaneExample extends Application {

    private final Random rng = new Random();
    
    @Override
    public void start(Stage primaryStage) {
        TabPane[] panes = new TabPane[] {new TabPane(), new TabPane(), new TabPane() };
        VBox root = new VBox(10, panes);
        for (int i = 1 ; i <= 15; i++) {
            Tab tab = new Tab("Tab "+i);
            tab.setGraphic(new Rectangle(16, 16, randomColor()));
            Region region = new Region();
            region.setMinSize(100, 150);
            tab.setContent(region);
            panes[(i-1) % panes.length].getTabs().add(tab);
        }
        
        DraggableTabPaneSupport support1 = new DraggableTabPaneSupport();
        support1.addSupport(panes[0]);
        support1.addSupport(panes[1]);
        DraggableTabPaneSupport support2 = new DraggableTabPaneSupport();
        support2.addSupport(panes[2]);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Color randomColor() {
        return Color.rgb(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
    }

    public static void main(String[] args) {
        launch(args);
    }
}