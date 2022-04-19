package viewer.visframe.graphics.property.node;

import graphics.property.node.GraphicsPropertyNonLeafNode;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import viewer.AbstractViewerController;

/**
 * 
 * @author tanxu
 * 
 */
public class GraphicsPropertyNonLeafNodeViewerController extends AbstractViewerController<GraphicsPropertyNonLeafNode>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/graphics/property/node/GraphicsPropertyNonLeafNodeViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.nodeInforTextField.setText(this.getViewer().getValue().toString());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public GraphicsPropertyNonLeafNodeViewer getViewer() {
		return (GraphicsPropertyNonLeafNodeViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		
	}
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private TextField nodeInforTextField;

}
