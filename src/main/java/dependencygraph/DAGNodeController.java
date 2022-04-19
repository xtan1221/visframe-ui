package dependencygraph;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class DAGNodeController<V> {

	protected DAGNodeManager<V,?> manager;
	
	/**
	 * 
	 * @param manager
	 */
	protected void setManager(DAGNodeManager<V, ?> manager) {
		this.manager = manager;
		
		//set size of root node
		this.getRootNodeContainer().setMaxHeight(this.getManager().getMainManager().getNodeRootFXNodeHeight());
		this.getRootNodeContainer().setMinHeight(this.getManager().getMainManager().getNodeRootFXNodeHeight());
		this.getRootNodeContainer().setPrefHeight(this.getManager().getMainManager().getNodeRootFXNodeHeight());
		
		this.getRootNodeContainer().setMaxWidth(this.getManager().getMainManager().getNodeRootFXNodeWidth());
		this.getRootNodeContainer().setMinWidth(this.getManager().getMainManager().getNodeRootFXNodeWidth());
		this.getRootNodeContainer().setPrefWidth(this.getManager().getMainManager().getNodeRootFXNodeWidth());
		
		//set layout
		this.getRootNodeContainer().setLayoutX(this.getManager().getCenterCoord().getX()-this.getManager().getMainManager().getNodeRootFXNodeWidth()/2);
		this.getRootNodeContainer().setLayoutY(this.getManager().getCenterCoord().getY()-this.getManager().getMainManager().getNodeRootFXNodeHeight()/2);

	}
	
	protected abstract DAGNodeManager<V, ?> getManager();
	
	protected abstract Pane getRootNodeContainer();
	
	/////////////////////////////////////////
	@FXML
	public abstract void initialize();
}
