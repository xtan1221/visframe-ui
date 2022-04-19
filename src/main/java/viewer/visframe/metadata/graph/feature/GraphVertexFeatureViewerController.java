package viewer.visframe.metadata.graph.feature;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.graph.feature.GraphVertexFeature;
import viewer.AbstractViewerController;

public class GraphVertexFeatureViewerController extends AbstractViewerController<GraphVertexFeature>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/feature/GraphVertexFeatureViewer.fxml";

	@Override
	public GraphVertexFeatureViewer getViewer() {
		return (GraphVertexFeatureViewer)this.viewer;
	}

	@Override
	protected void setup() {
		//
		this.getViewer().getValue().getIDColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.vertexIDFeatureNameVBox.getChildren().add(tf);
		});
		
		//
		this.getViewer().getValue().getAdditionalFeatureColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.vertexAdditionalIDFeatureNameVBox.getChildren().add(tf);
		});
		
	}

	
	/////////////////////////
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}

	
	//////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private VBox vertexIDFeatureNameVBox;
	@FXML
	private VBox vertexAdditionalIDFeatureNameVBox;

}
