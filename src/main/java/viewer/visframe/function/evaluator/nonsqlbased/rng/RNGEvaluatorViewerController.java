package viewer.visframe.function.evaluator.nonsqlbased.rng;

import function.evaluator.nonsqlbased.rng.RNGEvaluator;
import viewer.visframe.function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluatorViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class RNGEvaluatorViewerController<E extends RNGEvaluator> extends NonSQLQueryBasedEvaluatorViewerController<E>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public RNGEvaluatorViewer<E,?> getViewer() {
		return (RNGEvaluatorViewer<E,?>)this.viewer;
	}
	
	///////////////////////////////////

}
