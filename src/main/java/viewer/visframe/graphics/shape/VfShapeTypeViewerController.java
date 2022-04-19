package viewer.visframe.graphics.shape;

import java.util.ArrayList;
import java.util.List;

import graphics.shape.VfShapeType;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import viewer.AbstractViewerController;
import viewer.visframe.graphics.property.tree.GraphicsPropertyTreeViewer;


/**
 * 
 * @author tanxu
 *
 */
public class VfShapeTypeViewerController extends AbstractViewerController<VfShapeType>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/graphics/shape/VfShapeTypeViewer.fxml";
	
	private List<TitledPane> graphicsTreeTitledPaneList;
	private TitledPane openedGraphicsTreeTitledPane; //there will be at most one graphics tree's root titledPane expanded at a time
	@Override
	protected void setup() {
		this.shapeTypeNameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		
		this.graphicsTreeTitledPaneList = new ArrayList<>();
		this.getViewer().getValue().getGraphicsPropertyTreeNameMap().forEach((name, tree)->{
			GraphicsPropertyTreeViewer viewer = new GraphicsPropertyTreeViewer(tree);
			
			TitledPane tp = (TitledPane) viewer.getController().getRootContainerPane();
			this.graphicsPropertyTreeVBox.getChildren().add(tp);
			this.graphicsTreeTitledPaneList.add(tp);
			
			tp.expandedProperty().addListener((obs,ov,nv)->{
				if(nv) {
					if(openedGraphicsTreeTitledPane!=null) {
						openedGraphicsTreeTitledPane.setExpanded(false);
					}
					this.openedGraphicsTreeTitledPane = tp;
				}else {
					//
					this.openedGraphicsTreeTitledPane = null;
				}
			});
			
		});
		
		
		
//		GraphicsPropertyTreeViewer viewer = new GraphicsPropertyTreeViewer(VfLine.COLOR_PROPERTY_TREE);
//		this.graphicsPropertyTreeVBox.getChildren().add(viewer.getController().getRootContainerPane());
//		GraphicsPropertyTreeViewer viewer2 = new GraphicsPropertyTreeViewer(VfLine.STROKE_PROPERTY_TREE);
//		this.graphicsPropertyTreeVBox.getChildren().add(viewer2.getController().getRootContainerPane());
//		GraphicsPropertyTreeViewer viewer3 = new GraphicsPropertyTreeViewer(VfLine.START_PROPERTY_TREE);
//		this.graphicsPropertyTreeVBox.getChildren().add(viewer3.getController().getRootContainerPane());
//		GraphicsPropertyTreeViewer viewer4 = new GraphicsPropertyTreeViewer(VfLine.END_PROPERTY_TREE);
//		this.graphicsPropertyTreeVBox.getChildren().add(viewer4.getController().getRootContainerPane());
	}
	
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}
	
	/**
	 * @return the viewer
	 */
	@Override
	public VfShapeTypeViewer getViewer() {
		return (VfShapeTypeViewer)this.viewer;
	}
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField shapeTypeNameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private VBox graphicsPropertyTreeVBox;

}
