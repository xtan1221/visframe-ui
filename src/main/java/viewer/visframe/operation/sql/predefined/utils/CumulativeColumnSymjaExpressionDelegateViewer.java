package viewer.visframe.operation.sql.predefined.utils;

import context.VisframeContext;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import viewer.AbstractViewer;

/**
 * 
 */
public class CumulativeColumnSymjaExpressionDelegateViewer extends AbstractViewer<CumulativeColumnSymjaExpressionDelegate, CumulativeColumnSymjaExpressionDelegateViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public CumulativeColumnSymjaExpressionDelegateViewer(CumulativeColumnSymjaExpressionDelegate value, VisframeContext hostVisframeContext) {
		super(value, CumulativeColumnSymjaExpressionDelegateViewerController.FXML_FILE_DIR_STRING);
		
		this.hostVisframeContext = hostVisframeContext;
	}

	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}
	
}
