package viewer.visframe.function.evaluator.nonsqlbased.rng;

import function.evaluator.nonsqlbased.rng.SimpleRNGEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class SimpleRNGEvaluatorViewer extends RNGEvaluatorViewer<SimpleRNGEvaluator, SimpleRNGEvaluatorViewerController>{
	
	public SimpleRNGEvaluatorViewer(
			SimpleRNGEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, SimpleRNGEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
