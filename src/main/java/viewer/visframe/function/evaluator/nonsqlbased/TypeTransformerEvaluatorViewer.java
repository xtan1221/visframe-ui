package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.TypeTransformer;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public class TypeTransformerEvaluatorViewer extends NonSQLQueryBasedEvaluatorViewer<TypeTransformer, TypeTransformerEvaluatorViewerController>{
	
	public TypeTransformerEvaluatorViewer(
			TypeTransformer value,
			ComponentFunctionViewerBase<?, ?> hostComponentFunctionViewer) {
		super(value, TypeTransformerEvaluatorViewerController.FXML_FILE_DIR_STRING, hostComponentFunctionViewer);
		// TODO Auto-generated constructor stub
	}
	
}
