package utils;

import javafx.scene.control.Button;

public class ButtonUtils {
	public static void setUnclickable(Button button) {
		FXUtils.set2Disable(button, true);
	}
	
	public static void setClickable(Button button) {
		FXUtils.set2Disable(button, false);
	}
	
}
