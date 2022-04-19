package viewer.visframe.operation.sql.predefined.utils;

import symja.VfSymjaSinglePrimitiveOutputExpression;
import viewer.AbstractViewer;

/**
 * 
 */
public class VfSymjaSinglePrimitiveOutputExpressionViewer extends AbstractViewer<VfSymjaSinglePrimitiveOutputExpression, VfSymjaSinglePrimitiveOutputExpressionViewerController>{
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public VfSymjaSinglePrimitiveOutputExpressionViewer(VfSymjaSinglePrimitiveOutputExpression value) {
		super(value, VfSymjaSinglePrimitiveOutputExpressionViewerController.FXML_FILE_DIR_STRING);
	}
	
}
