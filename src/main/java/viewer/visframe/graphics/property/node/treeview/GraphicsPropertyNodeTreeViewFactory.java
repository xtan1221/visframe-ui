package viewer.visframe.graphics.property.node.treeview;

import graphics.property.node.GraphicsPropertyLeafNode;
import graphics.property.node.GraphicsPropertyNode;
import graphics.property.node.GraphicsPropertyNonLeafNode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import viewer.visframe.graphics.property.node.GraphicsPropertyLeafNodeViewer;
import viewer.visframe.graphics.property.node.GraphicsPropertyNonLeafNodeViewer;

public class GraphicsPropertyNodeTreeViewFactory {
	
	/**
	 * 
	 * @param rootNode
	 * @return
	 */
	public static TreeView<GraphicsPropertyNode> makeTreeView(GraphicsPropertyNode rootNode) {
	
		TreeView<GraphicsPropertyNode> ret = new TreeView<>();
		
		ret.setRoot(makeTreeItem(rootNode));
		
		return ret;
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	static TreeItem<GraphicsPropertyNode> makeTreeItem(GraphicsPropertyNode node){
		if(node instanceof GraphicsPropertyLeafNode) {
			return makeLeafTreeItem((GraphicsPropertyLeafNode<?>)node);
		}else {
			return makeNonLeafTreeItem((GraphicsPropertyNonLeafNode)node);
		}
	}
	
	
	/**
	 * 
	 * @param leafNode
	 * @return
	 */
	static TreeItem<GraphicsPropertyNode> makeLeafTreeItem(GraphicsPropertyLeafNode<?> leafNode){
		TreeItem<GraphicsPropertyNode> ret = new TreeItem<>();
		ret.setValue(leafNode);
		
		GraphicsPropertyLeafNodeViewer viewer = new GraphicsPropertyLeafNodeViewer(leafNode);
		ret.setGraphic(viewer.getController().getRootContainerPane());
		
		return ret;
	}
	
	/**
	 * 
	 * @param nonLeafNode
	 * @return
	 */
	static TreeItem<GraphicsPropertyNode> makeNonLeafTreeItem(GraphicsPropertyNonLeafNode nonLeafNode){
		TreeItem<GraphicsPropertyNode> ret = new TreeItem<>();
		
		//
		ret.setValue(nonLeafNode);
		
		GraphicsPropertyNonLeafNodeViewer viewer = new GraphicsPropertyNonLeafNodeViewer(nonLeafNode);
		ret.setGraphic(viewer.getController().getRootContainerPane());
		
		nonLeafNode.getChildrenNodeNameMap().forEach((name, childNode)->{
			ret.getChildren().add(makeTreeItem(childNode));
		});
		
		return ret;
	}
	
}
