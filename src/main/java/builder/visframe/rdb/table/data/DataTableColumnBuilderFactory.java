package builder.visframe.rdb.table.data;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import rdb.table.data.DataTableColumn;

public class DataTableColumnBuilderFactory extends NodeBuilderFactoryBase<DataTableColumn, NonLeafNodeBuilderEmbeddedUIContentController<DataTableColumn>>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public DataTableColumnBuilderFactory(String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
	}
	
	
	@Override
	public DataTableColumnBuilder build() throws SQLException, IOException {
		return new DataTableColumnBuilder(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder());
	}

}
