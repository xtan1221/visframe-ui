package viewer.visframe.function.variable.output.type;

import function.variable.output.type.PFConditionEvaluatorBooleanOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.output.OutputVariableViewer;

public class PFConditionEvaluatorBooleanOutputVariableViewer extends OutputVariableViewer<PFConditionEvaluatorBooleanOutputVariable, PFConditionEvaluatorBooleanOutputVariableViewerController>{

	public PFConditionEvaluatorBooleanOutputVariableViewer(
			PFConditionEvaluatorBooleanOutputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, PFConditionEvaluatorBooleanOutputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
