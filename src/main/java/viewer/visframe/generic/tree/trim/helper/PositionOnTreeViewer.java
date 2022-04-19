package viewer.visframe.generic.tree.trim.helper;

import generic.tree.trim.helper.PositionOnTree;
import viewer.AbstractViewer;

/**
 * 
 */
public class PositionOnTreeViewer extends AbstractViewer<PositionOnTree, PositionOnTreeViewerController>{
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public PositionOnTreeViewer(PositionOnTree value) {
		super(value, PositionOnTreeViewerController.FXML_FILE_DIR_STRING);
		
	}

}
