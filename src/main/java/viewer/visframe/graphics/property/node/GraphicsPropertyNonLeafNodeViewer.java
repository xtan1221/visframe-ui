package viewer.visframe.graphics.property.node;

import graphics.property.node.GraphicsPropertyNonLeafNode;
import viewer.AbstractViewer;

/**
 * 
 */
public class GraphicsPropertyNonLeafNodeViewer extends AbstractViewer<GraphicsPropertyNonLeafNode, GraphicsPropertyNonLeafNodeViewerController>{
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public GraphicsPropertyNonLeafNodeViewer(GraphicsPropertyNonLeafNode value) {
		super(value, GraphicsPropertyNonLeafNodeViewerController.FXML_FILE_DIR_STRING);

	}
	
}
