package image.save;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;


/**
 * save a snap shot of a javafx Node by first generating snapshot for each tile with a fixed size separately; 
 * then stitch them together to a full complete image;
 * 
 * this method is a solution to the known problem if dimensions of the node are larger than max texture size;
 * 		https://bugs.openjdk.java.net/browse/JDK-8088198 (description of the problem)
 * 
 * this class is a modification of the code found in 
 * 		https://stackoverflow.com/questions/49260841/how-to-work-around-unrecognized-image-loader-null-in-javafx (solution)
 * 
 * 
 * @author tanxu
 *
 */
public class FXNodeTiledSnapShot {
	private static final int TILE_WIDTH = 10000; // width of a tile; must be smaller than the max texture width;
	private static final int TILE_HEIGHT = 10000; // height of a tile; must be smaller than the max texture height;
	
	/**
	 * snap shot the given node and save it as an image to the given file path;
	 * 
	 * @param node node to be snapshot
	 * @param filePath file path to save the 
	 * @param image file format
	 */
	static void exportPng(final Node node, final String filePath, String formatType) {

	    final int totalWidth = (int) node.getLayoutBounds().getWidth();
	    final int totalHeight = (int) node.getLayoutBounds().getHeight();
	    final WritableImage full = new WritableImage(totalWidth, totalHeight);
	    
	    // defines the number of tiles to export (use higher value for bigger resolution)
	    final int totalTileColNum = totalWidth/TILE_WIDTH + 1; //total number of tiles on the x axis(columns) = (total_width modulo tile_width) +1
	    final int totalTileRowNum = totalHeight/TILE_HEIGHT + 1;//total number of tiles on the y axis(rows) = (total_height modulo tile_height) +1
	    
	    System.out.println("Exporting node (building " + (totalTileColNum * totalTileRowNum) + " tiles)");
	    
	    try {
	        for (int col = 0; col < totalTileColNum; ++col) {//column of tiles
	            for (int row = 0; row < totalTileRowNum; ++row) {//row of tiles
	            	System.out.println("processing tile:"+col+"*"+row);
	                int tileTopLeftX = col * TILE_WIDTH;
	                int tileTopLeftY = row * TILE_HEIGHT;
	                
	                int realTileWidth;
	                int realTileHeight;
	                //
	                if(col==totalTileColNum-1) {//the last column
	                	realTileWidth = totalWidth - TILE_WIDTH*(totalTileColNum-1);
	                }else {
	                	realTileWidth = TILE_WIDTH;
	                }
	                
	                if(row==totalTileRowNum-1) {//the last row
	                	realTileHeight = totalHeight - TILE_HEIGHT*(totalTileRowNum-1);
	                }else {
	                	realTileHeight = TILE_HEIGHT;
	                }
	                
	                SnapshotParameters params = new SnapshotParameters();
	                params.setViewport(new Rectangle2D(tileTopLeftX, tileTopLeftY, realTileWidth, realTileHeight));   
	                
//	                final CompletableFuture<Image> future = new CompletableFuture<>();
	                // keeps fx application thread unblocked ??
//	                Platform.runLater(() -> future.complete(node.snapshot(params, null)));
	                
	                full.getPixelWriter().setPixels(tileTopLeftX, tileTopLeftY, realTileWidth, realTileHeight, node.snapshot(params, null).getPixelReader(), 0, 0);
	            }
	        }
	        
	        System.out.println("Exporting node (saving to file)");
	        
	        ImageIO.write(SwingFXUtils.fromFXImage(full, null), formatType, new File(filePath));
	        
	        System.out.println("Exporting node (finished)");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
