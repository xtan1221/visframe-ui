package viewer.visframe.operation.graph.layout.JUNG;

import javafx.scene.Parent;
import operation.graph.layout.JUNG.FRLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class FRLayout2DOperationViewerController extends GraphNode2DLayoutOperationBaseViewerController<FRLayout2DOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromSingleExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public FRLayout2DOperationViewer getViewer() {
		return (FRLayout2DOperationViewer)this.viewer;
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
