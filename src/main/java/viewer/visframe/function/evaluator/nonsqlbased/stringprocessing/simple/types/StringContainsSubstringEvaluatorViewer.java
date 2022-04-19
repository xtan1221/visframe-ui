package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringContainsSubstringEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringContainsSubstringEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringContainsSubstringEvaluator, StringContainsSubstringEvaluatorViewerController>{
	
	public StringContainsSubstringEvaluatorViewer(
			StringContainsSubstringEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringContainsSubstringEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
