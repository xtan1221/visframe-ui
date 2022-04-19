package viewer.visframe.generic.tree.trim.helper;

import generic.tree.trim.helper.SiblingReorderPattern;
import viewer.AbstractViewer;

/**
 * 
 */
public class SiblingReorderPatternViewer extends AbstractViewer<SiblingReorderPattern, SiblingReorderPatternViewerController>{
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public SiblingReorderPatternViewer(SiblingReorderPattern value) {
		super(value, SiblingReorderPatternViewerController.FXML_FILE_DIR_STRING);
		
	}

}
