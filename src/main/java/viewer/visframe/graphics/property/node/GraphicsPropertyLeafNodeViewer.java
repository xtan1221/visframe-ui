package viewer.visframe.graphics.property.node;

import graphics.property.node.GraphicsPropertyLeafNode;
import viewer.AbstractViewer;

/**
 * 
 */
public class GraphicsPropertyLeafNodeViewer extends AbstractViewer<GraphicsPropertyLeafNode<?>, GraphicsPropertyLeafNodeViewerController>{
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public GraphicsPropertyLeafNodeViewer(GraphicsPropertyLeafNode<?> value) {
		super(value, GraphicsPropertyLeafNodeViewerController.FXML_FILE_DIR_STRING);

	}
	
}
