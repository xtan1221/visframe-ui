package viewer.visframe.graphics.property.tree;

import graphics.property.tree.GraphicsPropertyTree;
import viewer.AbstractViewer;

/**
 * 
 */
public class GraphicsPropertyTreeViewer extends AbstractViewer<GraphicsPropertyTree, GraphicsPropertyTreeViewerController>{
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public GraphicsPropertyTreeViewer(GraphicsPropertyTree value) {
		super(value, GraphicsPropertyTreeViewerController.FXML_FILE_DIR_STRING);
	
	}
}
