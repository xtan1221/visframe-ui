package viewer.visframe.operation;

import context.VisframeContext;
import operation.Operation;
import viewer.AbstractViewer;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class OperationViewerBase<G extends Operation, C extends OperationViewerControllerBase<G>> extends AbstractViewer<G, C>{
	/**
	 * 
	 */
	private final VisframeContext hostVisframeContext;
	////////////////////////////////	
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected OperationViewerBase(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString);
		// TODO Auto-generated constructor stub
		
		this.hostVisframeContext = hostVisframeContext;
		
	}

	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}
	
}
