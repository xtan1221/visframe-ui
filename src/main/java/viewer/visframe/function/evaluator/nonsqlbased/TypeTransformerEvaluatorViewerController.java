package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.TypeTransformer;
import javafx.scene.Parent;

/**
 * 
 * @author tanxu
 * 
 */
public class TypeTransformerEvaluatorViewerController extends NonSQLQueryBasedEvaluatorViewerController<TypeTransformer>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/composition/CompositionFunctionViewer.fxml";
	
	//////////////////////////////////////////////
	

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the viewer
	 */
	@Override
	public TypeTransformerEvaluatorViewer getViewer() {
		return (TypeTransformerEvaluatorViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////

}
