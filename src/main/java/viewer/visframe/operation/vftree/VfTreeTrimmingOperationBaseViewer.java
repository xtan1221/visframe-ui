package viewer.visframe.operation.vftree;

import context.VisframeContext;
import operation.vftree.VfTreeTrimmingOperationBase;
import viewer.visframe.operation.OperationViewerBase;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class VfTreeTrimmingOperationBaseViewer<G extends VfTreeTrimmingOperationBase, C extends VfTreeTrimmingOperationBaseViewerController<G>> extends OperationViewerBase<G, C>{
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected VfTreeTrimmingOperationBaseViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
