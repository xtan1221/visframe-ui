package viewer.visframe.metadata.graph.feature;

import metadata.graph.feature.GraphEdgeFeature;
import viewer.AbstractViewer;

/**
 * view only
 */
public class GraphEdgeFeatureViewer extends AbstractViewer<GraphEdgeFeature, GraphEdgeFeatureViewerController>{

	
	public GraphEdgeFeatureViewer(GraphEdgeFeature value) {
		super(value, GraphEdgeFeatureViewerController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
