package viewer.visframe.operation.graph;

import context.VisframeContext;
import operation.graph.SingleGenericGraphAsInputOperation;
import viewer.visframe.operation.OperationViewerBase;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class SingleGenericGraphAsInputOperationViewer<G extends SingleGenericGraphAsInputOperation, C extends SingleGenericGraphAsInputOperationViewerController<G>> extends OperationViewerBase<G, C>{

	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected SingleGenericGraphAsInputOperationViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
