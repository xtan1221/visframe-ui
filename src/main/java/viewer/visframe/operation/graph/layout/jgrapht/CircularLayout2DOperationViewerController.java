package viewer.visframe.operation.graph.layout.jgrapht;

import javafx.scene.Parent;
import operation.graph.layout.jgrapht.CircularLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class CircularLayout2DOperationViewerController extends GraphNode2DLayoutOperationBaseViewerController<CircularLayout2DOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromSingleExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public CircularLayout2DOperationViewer getViewer() {
		return (CircularLayout2DOperationViewer)this.viewer;
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
