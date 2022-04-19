package utils;

import java.util.Random;

import javafx.scene.paint.Color;

public class ColorUtils {
	
	/**
	 * build and return the css string for the given color
	 * rgb(r,g,b)
	 * where r, g and b are in range 0-255
	 * @param color
	 * @return
	 */
	public static String makeCSSColorString(Color color) {
		int r = (int) (color.getRed()*255);
		int g = (int) (color.getGreen()*255);
		int b = (int) (color.getBlue()*255);
		
		String ret = 
				"rgb("
				.concat(Integer.toString(r)).concat(",")
				.concat(Integer.toString(g)).concat(",")
				.concat(Integer.toString(b))
				.concat(")");
		
		return ret;
	}
	
	/**
	 * make and return a random color
	 * @return
	 */
	public static Color randomColor() {
		Random rand =new Random();
		
		int r = (int) (rand.nextDouble()*255);
		int g = (int) (rand.nextDouble()*255);
		int b = (int) (rand.nextDouble()*255);
		
		return Color.rgb(r, g, b);
	}
}
