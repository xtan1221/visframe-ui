package builder.visframe.function.evaluator.sqlbased.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.evaluator.AbstractEvaluatorBuilder;
import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import function.evaluator.sqlbased.utils.SelectElementExpression;

/**
 * only for SimpleFunction's SqlQueryBasedEvaluator;
 * 
 * @author tanxu
 *
 */
public class SelectElementExpressionBuilderFactory extends NodeBuilderFactoryBase<SelectElementExpression, NonLeafNodeBuilderEmbeddedUIContentController<SelectElementExpression>>{
	
	private final AbstractEvaluatorBuilder<?> hostEvaluatorBuilder; 
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param hostVisProjectDBContext
	 * @param hostCompositionFunctionBuilder
	 * @param hostComponentFunctionBuilder
	 * @param hostEvaluatorBuilder
	 * @param allowingConstantValuedInputVariable
	 * @param mustBeOfSameOwnerRecordDataWithHostCompositionFunction
	 */
	public SelectElementExpressionBuilderFactory(
			String name, String description, boolean canBeNull,
			
			AbstractEvaluatorBuilder<?> hostEvaluatorBuilder) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.hostEvaluatorBuilder = hostEvaluatorBuilder;
	}

	
	@Override
	public SelectElementExpressionBuilder build() throws SQLException, IOException {
		return new SelectElementExpressionBuilder(
				this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder(),
				this.hostEvaluatorBuilder, 
				false//boolean forPiecewiseFunctionCondition
				);
	}
	
}
