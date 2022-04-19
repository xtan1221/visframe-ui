package viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.simple;

import function.evaluator.nonsqlbased.stringprocessing.SimpleStringProcessingEvaluator;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluatorViewer;
import viewer.visframe.function.evaluator.nonsqlbased.stringprocessing.StringProcessingEvaluatorViewerController;

/**
 * 
 * @author tanxu
 *
 */
public abstract class SimpleStringProcessingEvaluatorViewerController<E extends SimpleStringProcessingEvaluator> extends StringProcessingEvaluatorViewerController<E>{
	
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
