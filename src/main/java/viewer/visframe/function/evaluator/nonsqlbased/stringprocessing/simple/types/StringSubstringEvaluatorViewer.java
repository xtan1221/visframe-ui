package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringSubstringEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringSubstringEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringSubstringEvaluator, StringSubstringEvaluatorViewerController>{
	
	public StringSubstringEvaluatorViewer(
			StringSubstringEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringSubstringEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
