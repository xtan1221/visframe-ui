package builder.visframe.function.evaluator.sqlbased.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import utils.visframe.WhereConditionProcessor;


/**
 * builder for a {@link WhereConditionProcessor};
 * 
 * can be used to build a valid select element for {@link WhereConditionExpressionBuilder}
 * @author tanxu
 *
 */
public class WhereConditionProcessorBuilder extends LeafNodeBuilder<WhereConditionProcessor, WhereConditionProcessorBuilderEmbeddedUIContentController>{
	
	protected WhereConditionProcessorBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, WhereConditionProcessorBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
}
