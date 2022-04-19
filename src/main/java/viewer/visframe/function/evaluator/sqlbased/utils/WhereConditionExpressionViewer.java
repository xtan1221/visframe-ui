package viewer.visframe.function.evaluator.sqlbased.utils;

import function.evaluator.sqlbased.utils.WhereConditionExpression;
import viewer.AbstractViewer;
import viewer.visframe.function.evaluator.sqlbased.SimpleSQLQueryEvaluatorViewer;

/**
 * 
 */
public class WhereConditionExpressionViewer extends AbstractViewer<WhereConditionExpression, WhereConditionExpressionViewerController>{
	private final SimpleSQLQueryEvaluatorViewer hostSimpleSQLQueryEvaluatorViewer;
		
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public WhereConditionExpressionViewer(WhereConditionExpression value, SimpleSQLQueryEvaluatorViewer hostSimpleSQLQueryEvaluatorViewer) {
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
