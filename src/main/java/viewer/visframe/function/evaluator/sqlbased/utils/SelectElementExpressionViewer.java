package viewer.visframe.function.evaluator.sqlbased.utils;

import function.evaluator.sqlbased.utils.SelectElementExpression;
import viewer.AbstractViewer;
import viewer.visframe.function.evaluator.sqlbased.SimpleSQLQueryEvaluatorViewer;

/**
 * 
 */
public class SelectElementExpressionViewer extends AbstractViewer<SelectElementExpression, SelectElementExpressionViewerController>{
	
	private final SimpleSQLQueryEvaluatorViewer hostSimpleSQLQueryEvaluatorViewer;
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public SelectElementExpressionViewer(SelectElementExpression value, SimpleSQLQueryEvaluatorViewer hostSimpleSQLQueryEvaluatorViewer) {
		super(value, SelectElementExpressionViewerController.FXML_FILE_DIR_STRING);
		if(hostSimpleSQLQueryEvaluatorViewer==null)
			throw new IllegalArgumentException("hostSimpleSQLQueryEvaluatorViewer cannot benull");
		
		this.hostSimpleSQLQueryEvaluatorViewer = hostSimpleSQLQueryEvaluatorViewer;
	}

	/**
	 * @return the hostSimpleSQLQueryEvaluatorViewer
	 */
	public SimpleSQLQueryEvaluatorViewer getHostSimpleSQLQueryEvaluatorViewer() {
		return hostSimpleSQLQueryEvaluatorViewer;
	}
	
	
}
