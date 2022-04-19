package image.save;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * 
 * @author tanxu
 *
 */
public class FXNodeSaveAsImageManager {
	private final Node FXNode;
	private final double nodeHeight;
	private final double nodeWidth;
	
	/////////////////////
	private FXNodeSaveAsImageController controller;
	
	/**
	 * 
	 * @param fxNode
	 */
	public FXNodeSaveAsImageManager(
			Node fxNode,
			double nodeWidth,
			double nodeHeight) {
		//
		
		this.FXNode = fxNode;
		this.nodeWidth = nodeWidth;
		this.nodeHeight = nodeHeight;
	}
	
	
	/**
	 * @return the controller
	 * @throws SQLException 
	 */
	public FXNodeSaveAsImageController getController() throws SQLException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXNodeSaveAsImageController.FXML_FILE_DIR_STRING));
			try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
		}
		return controller;
	}


	/**
	 * @return the fXNode
	 */
	Node getFXNode() {
		return FXNode;
	}


	/**
	 * @return the nodeHeight
	 */
	double getNodeHeight() {
		return nodeHeight;
	}


	/**
	 * @return the nodeWidth
	 */
	double getNodeWidth() {
		return nodeWidth;
	}
	
	

}
