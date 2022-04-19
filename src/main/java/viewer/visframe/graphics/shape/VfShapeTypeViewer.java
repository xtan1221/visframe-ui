package viewer.visframe.graphics.shape;

import graphics.shape.VfShapeType;
import viewer.AbstractViewer;

/**
 * 
 */
public class VfShapeTypeViewer extends AbstractViewer<VfShapeType, VfShapeTypeViewerController>{
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public VfShapeTypeViewer(VfShapeType value) {
		super(value, VfShapeTypeViewerController.FXML_FILE_DIR_STRING);
		
	}
	
}
