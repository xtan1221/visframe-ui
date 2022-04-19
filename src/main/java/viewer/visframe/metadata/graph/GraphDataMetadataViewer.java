package viewer.visframe.metadata.graph;

import context.VisframeContext;
import metadata.graph.GraphDataMetadata;
import viewer.visframe.metadata.MetadataViewerBase;

/**
 * viewer for non-VfTree type Graph
 */
public class GraphDataMetadataViewer extends MetadataViewerBase<GraphDataMetadata, GraphDataMetadataViewerController>{

	
	public GraphDataMetadataViewer(
			GraphDataMetadata value,
			VisframeContext hostVisframeContext) {
		super(value, GraphDataMetadataViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}
	
}
