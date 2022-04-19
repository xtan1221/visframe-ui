package viewer.visframe.metadata.record;

import context.VisframeContext;
import metadata.record.RecordDataMetadata;
import viewer.visframe.metadata.MetadataViewerBase;

/**
 * view only
 */
public class RecordDataMetadataViewer extends MetadataViewerBase<RecordDataMetadata, RecordDataMetadataViewerController>{
	
	public RecordDataMetadataViewer(RecordDataMetadata value, VisframeContext hostVisframeContext) {
		super(value, RecordDataMetadataViewerController.FXML_FILE_DIR_STRING, hostVisframeContext);
		// TODO Auto-generated constructor stub
		
	}

}
