package viewer.visframe.function.variable.input.nonrecordwise.type;

import function.variable.input.nonrecordwise.type.SQLAggregateFunctionBasedInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.nonrecordwise.NonRecordwiseInputVariableViewer;

public class SQLAggregateFunctionBasedInputVariableViewer extends NonRecordwiseInputVariableViewer<SQLAggregateFunctionBasedInputVariable, SQLAggregateFunctionBasedInputVariableViewerController>{

	public SQLAggregateFunctionBasedInputVariableViewer(
			SQLAggregateFunctionBasedInputVariable value,
			EvaluatorViewer<?, ?> hostEvaluatorViewer) {
		super(value, SQLAggregateFunctionBasedInputVariableViewerController.FXML_FILE_DIR_STRING, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}
	
}
