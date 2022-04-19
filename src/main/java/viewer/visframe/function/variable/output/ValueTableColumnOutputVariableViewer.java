package viewer.visframe.function.variable.output;

import function.variable.output.type.ValueTableColumnOutputVariable;
import viewer.visframe.function.evaluator.EvaluatorViewer;

public abstract class ValueTableColumnOutputVariableViewer<O extends ValueTableColumnOutputVariable, C extends OutputVariableViewerController<O>> extends OutputVariableViewer<O, C>{
	
	protected ValueTableColumnOutputVariableViewer(O value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString, hostEvaluatorViewer);
		// TODO Auto-generated constructor stub
	}

	
}
