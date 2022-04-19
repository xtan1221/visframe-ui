package utils;

import javafx.scene.layout.Region;

public class SizePropertyUtils {
	
	/**
	 * 
	 * @param child
	 * @param parent
	 */
	public static void childNodeResizeWithParentNode(Region child, Region parent) {
//		parent.heightProperty().addListener(e->{
//			child.setMaxHeight(parent.getHeight());
//			child.setMinHeight(parent.getHeight());
//		});
//		
//		parent.widthProperty().addListener(e->{
//			child.setMaxWidth(parent.getWidth());
//			child.setMinWidth(parent.getWidth());
//		});
		
		child.prefHeightProperty().bind(parent.heightProperty());
		child.prefWidthProperty().bind(parent.widthProperty());
	}
}
