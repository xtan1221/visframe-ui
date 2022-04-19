package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing;

import function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluator;
import viewer.visframe.function.evaluator.nonsqlbased.NonSQLQueryBasedEvaluatorViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class StringProcessingEvaluatorViewerController<E extends StringProcessingEvaluator> extends NonSQLQueryBasedEvaluatorViewerController<E>{
	
	//////////////////////////////////////////////
	
	/**
	 * @return the viewer
	 */
	@Override
	public StringProcessingEvaluatorViewer<E,?> getViewer() {
		return (StringProcessingEvaluatorViewer<E,?>)this.viewer;
	}
	
	///////////////////////////////////

}
