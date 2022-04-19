package viewer.visframe.function.variable.input.nonrecordwise;

import function.variable.input.nonrecordwise.NonRecordwiseInputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.input.InputVariableViewer;

public abstract class NonRecordwiseInputVariableViewer<R extends NonRecordwiseInputVariable, C extends NonRecordwiseInputVariableViewerController<R>> extends InputVariableViewer<R, C>{
	
	protected NonRecordwiseInputVariableViewer(R value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}

}
