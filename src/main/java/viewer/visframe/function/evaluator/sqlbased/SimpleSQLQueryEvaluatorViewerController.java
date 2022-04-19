package viewer.visframe.function.evaluator.sqlbased;

import function.evaluator.sqlbased.SimpleSQLQueryEvaluator;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import viewer.visframe.function.evaluator.sqlbased.utils.SelectElementExpressionViewer;
import viewer.visframe.function.evaluator.sqlbased.utils.WhereConditionExpressionViewer;

/**
 * 
 * @author tanxu
 *
 */
public class SimpleSQLQueryEvaluatorViewerController extends SQLQueryBasedEvaluatorViewerControllerBase<SimpleSQLQueryEvaluator>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/function/evaluator/sqlbased/SimpleSQLQueryEvaluatorViewer.fxml";
	
	//////////////////////////////////////////////
	
	@Override
	protected void setup() {
		this.getViewer().getValue().getSelectClauseExpressionList().forEach(exp->{
			SelectElementExpressionViewer seev = new SelectElementExpressionViewer(exp, this.getViewer());
			this.selectElementExpressionViewersVBox.getChildren().add(seev.getController().getRootContainerPane());
		});
		
		WhereConditionExpressionViewer whereConditionExpressionViewer = 
				new WhereConditionExpressionViewer(this.getViewer().getValue().getWhereConditionExpression(), this.getViewer());
		this.whereConditionExpressionViewerHBox.getChildren().add(whereConditionExpressionViewer.getController().getRootContainerPane());
		
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
	}

	/**
	 * @return the viewer
	 */
	@Override
	public SimpleSQLQueryEvaluatorViewer getViewer() {
		return (SimpleSQLQueryEvaluatorViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return rootContainerVBox;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	///////////////////////////////////
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private VBox selectElementExpressionViewersVBox;
	@FXML
	private HBox whereConditionExpressionViewerHBox;
	@FXML
	private TextArea notesTextArea;
}
