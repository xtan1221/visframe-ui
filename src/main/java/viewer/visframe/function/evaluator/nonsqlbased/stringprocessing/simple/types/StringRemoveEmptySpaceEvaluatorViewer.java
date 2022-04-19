package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringRemoveEmptySpaceEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringRemoveEmptySpaceEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringRemoveEmptySpaceEvaluator, StringRemoveEmptySpaceEvaluatorViewerController>{
	
	public StringRemoveEmptySpaceEvaluatorViewer(
			StringRemoveEmptySpaceEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringLengthEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
