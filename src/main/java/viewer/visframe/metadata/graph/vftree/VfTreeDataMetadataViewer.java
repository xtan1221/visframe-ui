package viewer.visframe.metadata.graph.vftree;

import context.VisframeContext;
import metadata.graph.vftree.VfTreeDataMetadata;
import viewer.visframe.metadata.MetadataViewerBase;

/**
 * view only
 */
public class VfTreeDataMetadataViewer extends MetadataViewerBase<VfTreeDataMetadata, VfTreeDataMetadataViewerController>{

	
	public VfTreeDataMetadataViewer(
			VfTreeDataMetadata value,
			VisframeContext hostVisframeContext) {
		super(value, VfTreeDataMetadataViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
	}
	
}
