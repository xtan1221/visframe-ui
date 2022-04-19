package utils;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LayoutCoordinateAndSizeUtils {
	
	/**
	 * calculate and return the layout coordinate of the given targetNode in the given outest Pane
	 * @param outestPane
	 * @param targetNode
	 * @return
	 */
	public static Point2D getLayoutPoint2D(Pane outestPane, Node targetNode) {
		
		Node currentNode = targetNode;
		double layoutX = currentNode.getLayoutX();
		double layoutY = currentNode.getLayoutY();

		Parent parentPane = currentNode.getParent();
		
		while(parentPane!=outestPane) {
			layoutX = layoutX+parentPane.getLayoutX();
			layoutY = layoutY+parentPane.getLayoutY();
			
			currentNode = parentPane;
			parentPane = currentNode.getParent();
		}
		
		return new Point2D(layoutX, layoutY);
	}
	
	/**
	 * find out and return the real width and height of the given pane;
	 * 
	 * note that this method will add the pane to a dummy Group and Scene, thus must be invoked before the target Pane is added to its real Parent!!!!
	 * otherwise, error may occur!!!!!
	 * 
	 * 
	 * @param pane
	 * @return
	 */
	public static Pair<Double, Double> findOutRealWidthAndHeight(Pane pane){
		Group root = new Group();
	    @SuppressWarnings("unused")
		Scene scene = new Scene(root);
		
//		Pane dagNodeRootRegion = manager.getController().getRootNodeContainer();
//		Button button = new Button("Hello Worlddfafdfsdf");
		
//		HBox hbox = new HBox();
//		hbox.getChildren().add(button);
		
		//add the dag Node to the dummy canvass
		root.getChildren().add(pane);
//		root.getChildren().add(button);
		//this will make the getWidth() and getHegiht() method return the real size of the node;
		//otherwise, the two methods will return 0;
		root.applyCss();
		root.layout();
		
//		dagNodeRootRegion.applyCss();
//		dagNodeRootRegion.layout();
		
		double width = pane.getWidth();
		double height = pane.getHeight();
		
		root.getChildren().clear();
		
		
		
		return new Pair<>(width, height);
	}
}
