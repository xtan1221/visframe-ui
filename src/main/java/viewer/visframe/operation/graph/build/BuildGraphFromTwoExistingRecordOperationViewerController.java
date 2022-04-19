package viewer.visframe.operation.graph.build;

import javafx.scene.Parent;
import operation.graph.build.BuildGraphFromTwoExistingRecordOperation;

/**
 * 
 * @author tanxu
 *
 */
public class BuildGraphFromTwoExistingRecordOperationViewerController extends BuildGraphFromExistingRecordOperationViewerControllerBase<BuildGraphFromTwoExistingRecordOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromTwoExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public BuildGraphFromTwoExistingRecordOperationViewer getViewer() {
		return (BuildGraphFromTwoExistingRecordOperationViewer)this.viewer;
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
