package utils;

import javafx.scene.Node;


/**
 * utility methods for javaFX nodes;
 * 
 * @author tanxu
 *
 */
public final class FXUtils {
	
	/**
	 * set disabled property of the given Node;
	 * 
	 * this method should be used since there is a visual effect bug that 
	 * 1. after a Node is changed from disabled to enabled, the opacity will still be in about 0.4 rather than set to 1;
	 * 2. likewise, if from enabled to disabled, the opacity may still be 1 rather than 0.4;
	 * 
	 * thus need to explicitly set the opacity of the Node when disabled property is changed;;
	 * 
	 * @param rb
	 * @param disabled
	 */
	public static void set2Disable(Node rb, boolean disabled) {
		if(disabled) {
			rb.setDisable(true);
			rb.setOpacity(0.4);
		}else {
			rb.setDisable(false);
			rb.setOpacity(1);
		}
	}
}
