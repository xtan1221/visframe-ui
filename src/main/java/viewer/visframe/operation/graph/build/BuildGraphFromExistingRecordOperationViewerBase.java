package viewer.visframe.operation.graph.build;

import context.VisframeContext;
import operation.graph.build.BuildGraphFromExistingRecordOperationBase;
import viewer.visframe.operation.OperationViewerBase;
import viewer.visframe.operation.OperationViewerControllerBase;

/**
 * 
 * @author tanxu
 *
 * @param <G>
 * @param <C>
 */
public abstract class BuildGraphFromExistingRecordOperationViewerBase<G extends BuildGraphFromExistingRecordOperationBase, C extends OperationViewerControllerBase<G>> extends OperationViewerBase<G, C>{

	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostCompositionFunctionViewer
	 */
	protected BuildGraphFromExistingRecordOperationViewerBase(G value, String FXMLFileDirString, VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}

}
