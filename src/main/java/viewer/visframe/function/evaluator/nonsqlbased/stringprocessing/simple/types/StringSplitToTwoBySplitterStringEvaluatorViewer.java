package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.types;

import function.evaluator.nonsqlbased.stringprocessing.StringSplitToTwoBySplitterStringEvaluator;
import viewer.visframe.function.component.ComponentFunctionViewerBase;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple.SimpleStringProcessingEvaluatorViewer;

public class StringSplitToTwoBySplitterStringEvaluatorViewer extends SimpleStringProcessingEvaluatorViewer<StringSplitToTwoBySplitterStringEvaluator, StringSplitToTwoBySplitterStringEvaluatorViewerController>{
	
	public StringSplitToTwoBySplitterStringEvaluatorViewer(
			StringSplitToTwoBySplitterStringEvaluator value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, StringSplitToTwoBySplitterStringEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
