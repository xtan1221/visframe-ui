package viewer.visframe.metadata.graph.feature;

import metadata.graph.feature.GraphVertexFeature;
import viewer.AbstractViewer;

/**
 * view only
 */
public class GraphVertexFeatureViewer extends AbstractViewer<GraphVertexFeature, GraphVertexFeatureViewerController>{

	/**
	 * 
	 * @param value
	 */
	public GraphVertexFeatureViewer(GraphVertexFeature value) {
		super(value, GraphVertexFeatureViewerController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
