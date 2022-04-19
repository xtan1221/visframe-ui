package builder.visframe.operation.sql.generic.utils;

import java.io.IOException;
import java.sql.SQLException;

import context.project.VisProjectDBContext;
import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import operation.sql.generic.DataTableAndColumnsMapping;

public class TableAndColumnsMappingBuilderFactory extends NodeBuilderFactoryBase<DataTableAndColumnsMapping, NonLeafNodeBuilderEmbeddedUIContentController<DataTableAndColumnsMapping>>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public TableAndColumnsMappingBuilderFactory(
			String name, String description, boolean canBeNull,
			VisProjectDBContext hostVisProjectDBContext) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;

	}
	
	
	@Override
	public TableAndColumnsMappingBuilder build() throws SQLException, IOException {
		return new TableAndColumnsMappingBuilder(
				this.getName(), this.getDescription(), this.canBeNull(),
				this.getParentNodeBuilder(),
				hostVisProjectDBContext);
	}

}
