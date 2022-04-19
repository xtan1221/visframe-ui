package builder.visframe.function.evaluator.sqlbased.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import utils.visframe.SelectElementProcessor;


/**
 * builder for a {@link SelectElementProcessor};
 * 
 * can be used to build a valid select element for {@link SelectElementExpressionBuilder}
 * @author tanxu
 *
 */
public class SelectElementProcessorBuilder extends LeafNodeBuilder<SelectElementProcessor, SelectElementProcessorBuilderEmbeddedUIContentController>{
	
	protected SelectElementProcessorBuilder(
			String name, String description, boolean canBeNull, NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, SelectElementProcessorBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
}
