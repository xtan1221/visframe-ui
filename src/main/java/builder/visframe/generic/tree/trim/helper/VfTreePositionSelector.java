package builder.visframe.generic.tree.trim.helper;

import core.builder.LeafNodeBuilder;
import core.builder.NonLeafNodeBuilder;
import generic.tree.trim.helper.PositionOnTree;


/**
 * select a PositionOnTree 
 * @author tanxu
 *
 */
public class VfTreePositionSelector extends LeafNodeBuilder<PositionOnTree, VfTreePositionSelectorEmbeddedUIContentController>{

	/**
	 * 
	 * @param name
	 * @param description
	 * @param canBeNull
	 * @param parentNodeBuilder
	 */
	public VfTreePositionSelector(
			String name, String description, boolean canBeNull,
			NonLeafNodeBuilder<?> parentNodeBuilder) {
		super(name, description, canBeNull, parentNodeBuilder, VfTreePositionSelectorEmbeddedUIContentController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 * @param tree
	 */
	public void setInteractiveTreeGraphics(InteractiveRectangleVfTreeGraphics tree) {
		if(tree!=null) {
			if(tree.isAllowingToMarkMultipleNodes()) {
				throw new IllegalArgumentException("given tree SHOULD NOT allow multiple selection of nodes!");
			}
			
			if(!tree.isAllowingLeafNodeMarked()) {
				throw new IllegalArgumentException("given tree SHOULD allow selection of leaf nodes!");
			}
			
			
			if(!tree.isAllowingSingleChildNodeMarked()) {
				throw new IllegalArgumentException("given tree SHOULD allow multiple selection of nodes with single child!");
			}
		}
		this.getEmbeddedUIContentController().setTree(tree);
	}
}
