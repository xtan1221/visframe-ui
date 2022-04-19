package viewer.visframe.operation.graph.transform;

import javafx.scene.Parent;
import operation.graph.transform.TransformGraphOperation;
import viewer.visframe.operation.graph.SingleGenericGraphAsInputOperationViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class TransformGraphOperationViewerController extends SingleGenericGraphAsInputOperationViewerController<TransformGraphOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromSingleExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public TransformGraphOperationViewer getViewer() {
		return (TransformGraphOperationViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return null; ///TODO
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
}
