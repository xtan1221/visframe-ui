package builder.visframe.graphics.property.node;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import graphics.property.node.GraphicsPropertyNode;



/**
 * selector for a {@link GraphicsPropertyNode} defined by visframe;
 * 
 * see factory classes in graphics.property.shape2D.factory package of visframe
 * 
 * 
 * @author tanxu
 *
 */
public class GraphicsPropertyNodeSelector extends LeafNodeBuilder<GraphicsPropertyNode, GraphicsPropertyNodeSelectorEmbeddedUIContentController>{
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public GraphicsPropertyNodeSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, GraphicsPropertyNodeSelectorEmbeddedUIContentController.FXML_FILE_DIR);
		// TODO Auto-generated constructor stub
	}
	
}
