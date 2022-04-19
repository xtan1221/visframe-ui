package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringEndsWithEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringEndsWithEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringEndsWithEvaluator, StringEndsWithEvaluatorViewerController>{
	
	public StringEndsWithEvaluatorViewer(
			StringEndsWithEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringEndsWithEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
