package builder.visframe.graphics.property.tree;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.factory.NodeBuilderFactoryBase;
import core.builder.ui.embedded.content.NonLeafNodeBuilderEmbeddedUIContentController;
import graphics.property.tree.GraphicsPropertyTree;

public class GraphicsPropertyTreeBuilderFactory extends NodeBuilderFactoryBase<GraphicsPropertyTree, NonLeafNodeBuilderEmbeddedUIContentController<GraphicsPropertyTree>>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 */
	public GraphicsPropertyTreeBuilderFactory(
			String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, NonLeafNodeBuilderEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public GraphicsPropertyTreeBuilder build() throws SQLException, IOException {
		return new GraphicsPropertyTreeBuilder(this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder());
	}
	
}
