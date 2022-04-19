package viewer.visframe.function.evaluator;

import function.evaluator.Evaluator;
import viewer.AbstractViewer;
import viewer.visframe.function.component.ComponentFunctionViewerBase;

public abstract class EvaluatorViewer<E extends Evaluator, C extends EvaluatorViewerController<E>> extends AbstractViewer<E, C>{

	private final ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer;
	
	/**
	 * 
	 * @param value
	 * @param FXMLFileDirString
	 * @param hostComponentFunctionViewer
	 */
	protected EvaluatorViewer(E value, String FXMLFileDirString, ComponentFunctionViewerBase<?,?> hostComponentFunctionViewer) {
		super(value, FXMLFileDirString);
		// TODO Auto-generated constructor stub
		
		this.hostComponentFunctionViewer = hostComponentFunctionViewer;
	}

	/**
	 * @return the hostComponentFunctionViewer
	 */
	public ComponentFunctionViewerBase<?,?> getHostComponentFunctionViewer() {
		return hostComponentFunctionViewer;
	}
	
	
}
