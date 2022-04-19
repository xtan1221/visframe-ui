package viewer.visframe.graphics.property.tree;

import graphics.property.tree.GraphicsPropertyTree;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import viewer.AbstractViewerController;
import viewer.visframe.graphics.property.node.treeview.GraphicsPropertyNodeTreeViewFactory;

/**
 * 
 * @author tanxu
 *
 */
public class GraphicsPropertyTreeViewerController extends AbstractViewerController<GraphicsPropertyTree>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/graphics/property/tree/GraphicsPropertyTreeViewer.fxml";
	
	
	@Override
	protected void setup() {
		this.rootContainerTitledPane.setText(this.getViewer().getValue().getName().getStringValue());
//		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		this.treeViewVBox.getChildren().add(GraphicsPropertyNodeTreeViewFactory.makeTreeView(this.getViewer().getValue().getRootNode()));
//		this.treeViewVBox.getChildren().add(GraphicsPropertyNodeTreeViewFactory.makeTreeView(this.getViewer().getValue().getRootNode()));
	}
	
	@Override
	public Parent getRootContainerPane() {
		return this.rootContainerTitledPane;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public GraphicsPropertyTreeViewer getViewer() {
		return (GraphicsPropertyTreeViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private TitledPane rootContainerTitledPane;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private VBox treeViewVBox;
}
