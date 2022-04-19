package builder.visframe.graphics.property.node;

import core.builder.factory.NodeBuilderFactoryBase;
import graphics.property.node.GraphicsPropertyNode;

public class GraphicsPropertyNodeSelectorFactory extends NodeBuilderFactoryBase<GraphicsPropertyNode, GraphicsPropertyNodeSelectorEmbeddedUIContentController>{
	
	/**
	 * constructor
	 * @param name
	 * @param description
	 * @param canBeNull
	 */
	protected GraphicsPropertyNodeSelectorFactory(
			String name, String description, boolean canBeNull) {
		super(name, description, canBeNull, GraphicsPropertyNodeSelectorEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public GraphicsPropertyNodeSelector build() {
		return new GraphicsPropertyNodeSelector(this.getName(), this.getDescription(), this.canBeNull(), this.getParentNodeBuilder());
	}

}
