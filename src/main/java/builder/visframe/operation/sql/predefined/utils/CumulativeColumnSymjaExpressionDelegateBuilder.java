package builder.visframe.operation.sql.predefined.utils;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import rdb.table.data.DataTableSchema;


/**
 * builder of a {@link CumulativeColumnSymjaExpressionDelegate} for {@link AddNumericCumulativeColumnOperation};
 * 
 * 
 * @author tanxu
 *
 */
public class CumulativeColumnSymjaExpressionDelegateBuilder extends LeafNodeBuilder<CumulativeColumnSymjaExpressionDelegate, CumulativeColumnSymjaExpressionDelegateBuilderEmbeddedUIContentController>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public CumulativeColumnSymjaExpressionDelegateBuilder(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, CumulativeColumnSymjaExpressionDelegateBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 * @param dataTableSchema
	 */
	public void setDataTableSchema(DataTableSchema dataTableSchema) {
		this.getEmbeddedUIContentController().setDataTableSchema(dataTableSchema);
	}
	
}
