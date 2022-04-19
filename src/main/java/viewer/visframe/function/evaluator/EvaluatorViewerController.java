package viewer.visframe.function.evaluator;

import function.evaluator.Evaluator;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class EvaluatorViewerController<E extends Evaluator> extends AbstractViewerController<E>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public EvaluatorViewer<E,?> getViewer() {
		return (EvaluatorViewer<E,?>)this.viewer;
	}
	
	///////////////////////////////////

}
