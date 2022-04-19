package viewer.visframe.function.composition;

import context.VisframeContext;
import function.composition.CompositionFunctionID;
import viewer.AbstractViewer;

/**
 * 
 */
public class CompositionFunctionIDViewer extends AbstractViewer<CompositionFunctionID, CompositionFunctionIDViewerController>{
	private final VisframeContext hostVisframeContext;
	
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public CompositionFunctionIDViewer(CompositionFunctionID value, VisframeContext hostVisframeContext) {
		super(value, CompositionFunctionIDViewerController.FXML_FILE_DIR_STRING);
		if(hostVisframeContext==null)
			throw new IllegalArgumentException("hostVisframeContext");
		
		this.hostVisframeContext = hostVisframeContext;
	}
	
	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}

}
