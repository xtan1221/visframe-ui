package viewer.visframe.operation.graph.layout.JUNG;

import javafx.scene.Parent;
import operation.graph.layout.JUNG.SpringLayout2DOperation;
import viewer.visframe.operation.graph.layout.GraphNode2DLayoutOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class SpringLayout2DOperationViewerController extends GraphNode2DLayoutOperationBaseViewerController<SpringLayout2DOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromSingleExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public SpringLayout2DOperationViewer getViewer() {
		return (SpringLayout2DOperationViewer)this.viewer;
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
