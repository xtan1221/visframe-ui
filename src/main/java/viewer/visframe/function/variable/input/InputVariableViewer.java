package viewer.visframe.function.variable.input;

import function.variable.input.InputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.VariableViewer;

public abstract class InputVariableViewer<I extends InputVariable, C extends InputVariableViewerController<I>> extends VariableViewer<I, C>{
	
	protected InputVariableViewer(I value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}

}
