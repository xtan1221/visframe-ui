package viewer.visframe.function.evaluator.nonsqlbased;

import function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluator;
import viewer.visframe.function.evaluator.EvaluatorViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class NonSQLQueryBasedEvaluatorViewerController<E extends NonSQLQueryBasedEvaluator> extends EvaluatorViewerController<E>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public NonSQLQueryBasedEvaluatorViewer<E,?> getViewer() {
		return (NonSQLQueryBasedEvaluatorViewer<E,?>)this.viewer;
	}
	
	///////////////////////////////////

}
