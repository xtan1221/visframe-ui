package viewer.visframe.function.variable;

import function.variable.Variable;
import viewer.AbstractViewer;
import viewer.visframe.function.evaluator.EvaluatorViewer;

public abstract class VariableViewer<V extends Variable, C extends VariableViewerController<V>> extends AbstractViewer<V, C>{

	private final EvaluatorViewer<?,?> hostEvaluatorViewer;
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostEvaluatorViewer
	 */
	protected VariableViewer(V value, String FXMLFileDirString, EvaluatorViewer<?,?> hostEvaluatorViewer) {
		super(value, FXMLFileDirString);
		// TODO Auto-generated constructor stub
		
		this.hostEvaluatorViewer = hostEvaluatorViewer;
	}

	/**
	 * @return the hostEvaluatorViewer
	 */
	public EvaluatorViewer<?,?> getHostEvaluatorViewer() {
		return hostEvaluatorViewer;
	}
	
	
}
