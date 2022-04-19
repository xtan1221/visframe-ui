package viewer.visframe.function.target;

import function.target.LeafGraphicsPropertyCFGTarget;
import graphics.property.tree.GraphicsPropertyTree;

/**
 * 
 */
public class LeafGraphicsPropertyCFGTargetViewer extends CFGTargetViewerBase<LeafGraphicsPropertyCFGTarget<?>, LeafGraphicsPropertyCFGTargetViewerController>{
	private final GraphicsPropertyTree ownerGraphicsPropertyTree;
	
	/**
	 * 
	 * @param value
	 * @param ownerGraphicsPropertyTree
	 */
	public LeafGraphicsPropertyCFGTargetViewer(LeafGraphicsPropertyCFGTarget<?> value, GraphicsPropertyTree ownerGraphicsPropertyTree) {
		super(value, LeafGraphicsPropertyCFGTargetViewerController.FXML_FILE_DIR_STRING);
		
		this.ownerGraphicsPropertyTree = ownerGraphicsPropertyTree;
	}


	/**
	 * @return the ownerGraphicsPropertyTree
	 */
	public GraphicsPropertyTree getOwnerGraphicsPropertyTree() {
		return ownerGraphicsPropertyTree;
	}
	
	
}
