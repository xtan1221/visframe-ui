package viewer.visframe.operation.graph;

import context.VisframeContext;
import operation.graph.InputGraphTypeBoundedOperation;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class InputGraphTypeBoundedOperationViewer<G extends InputGraphTypeBoundedOperation, C extends InputGraphTypeBoundedOperationViewerController<G>> extends SingleGenericGraphAsInputOperationViewer<G, C>{

	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected InputGraphTypeBoundedOperationViewer(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
