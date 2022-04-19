package viewer.visframe.operation.graph.build;

import javafx.scene.Parent;
import operation.graph.build.BuildGraphFromSingleExistingRecordOperation;

/**
 * 
 * @author tanxu
 *
 */
public class BuildGraphFromSingleExistingRecordOperationViewerController extends BuildGraphFromExistingRecordOperationViewerControllerBase<BuildGraphFromSingleExistingRecordOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/graph/build/BuildGraphFromSingleExistingRecordOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {

	}
	

	@Override
	public BuildGraphFromSingleExistingRecordOperationViewer getViewer() {
		return (BuildGraphFromSingleExistingRecordOperationViewer)this.viewer;
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
