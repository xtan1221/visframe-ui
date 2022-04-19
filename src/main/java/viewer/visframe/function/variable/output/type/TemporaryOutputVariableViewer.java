package viewer.visframe.function.variable.output.type;

import function.variable.output.type.TemporaryOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.output.ValueTableColumnOutputVariableViewer;

public class TemporaryOutputVariableViewer extends ValueTableColumnOutputVariableViewer<TemporaryOutputVariable, TemporaryOutputVariableViewerController>{
	
	public TemporaryOutputVariableViewer(
			TemporaryOutputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, TemporaryOutputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
