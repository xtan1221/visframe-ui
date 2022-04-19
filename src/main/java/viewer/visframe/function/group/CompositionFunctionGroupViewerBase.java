package viewer.visframe.function.group;

import context.VisframeContext;
import function.group.CompositionFunctionGroup;
import viewer.AbstractViewer;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class CompositionFunctionGroupViewerBase<G extends CompositionFunctionGroup, C extends CompositionFunctionGroupViewerControllerBase<G>> extends AbstractViewer<G, C>{
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
	protected CompositionFunctionGroupViewerBase(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
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
