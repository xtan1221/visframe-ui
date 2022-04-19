package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringReplaceEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringReplaceEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringReplaceEvaluator, StringReplaceEvaluatorViewerController>{
	
	public StringReplaceEvaluatorViewer(
			StringReplaceEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringReplaceEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
