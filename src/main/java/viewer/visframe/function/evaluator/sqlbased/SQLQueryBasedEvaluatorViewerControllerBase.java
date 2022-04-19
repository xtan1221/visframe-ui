package viewer.visframe.function.evaluator.sqlbased;

import function.evaluator.sqlbased.SQLQueryBasedEvaluator;
import viewer.visframe.function.evaluator.EvaluatorViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class SQLQueryBasedEvaluatorViewerControllerBase<E extends SQLQueryBasedEvaluator> extends EvaluatorViewerController<E>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public SQLQueryBasedEvaluatorViewerBase<E,?> getViewer() {
		return (SQLQueryBasedEvaluatorViewerBase<E,?>)this.viewer;
	}
	
	///////////////////////////////////

}
