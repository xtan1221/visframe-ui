package viewer.visframe.function.variable.output;

import function.variable.output.OutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;
import viewer.visframe.function.variable.VariableViewer;

public abstract class OutputVariableViewer<O extends OutputVariable, C extends OutputVariableViewerController<O>> extends VariableViewer<O, C>{
	
	protected OutputVariableViewer(O value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}

	
}
