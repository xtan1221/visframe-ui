package viewer.visframe.function.variable.input.recordwise;

import function.variable.input.recordwise.RecordwiseInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.InputVariableViewer;

public abstract class RecordwiseInputVariableViewer<R extends RecordwiseInputVariable, C extends RecordwiseInputVariableViewerController<R>> extends InputVariableViewer<R, C>{
	
	protected RecordwiseInputVariableViewer(R value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}

}
