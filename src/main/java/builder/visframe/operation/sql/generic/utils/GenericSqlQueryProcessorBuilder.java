package builder.visframe.operation.sql.generic.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import operation.sql.generic.utils.GenericSQLQueryProcessor;

public class GenericSqlQueryProcessorBuilder extends LeafNodeBuilder<GenericSQLQueryProcessor, GenericSqlQueryProcessorBuilderEmbeddedUIContentController>{

	protected GenericSqlQueryProcessorBuilder(String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, GenericSqlQueryProcessorBuilderEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
	
}
