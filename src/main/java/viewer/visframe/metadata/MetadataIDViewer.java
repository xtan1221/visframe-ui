package viewer.visframe.metadata;

import context.VisframeContext;
import metadata.MetadataID;
import viewer.AbstractViewer;

/**
 * 
 */
public class MetadataIDViewer extends AbstractViewer<MetadataID, MetadataIDViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public MetadataIDViewer(MetadataID value, VisframeContext hostVisframeContext) {
		super(value, MetadataIDViewerController.FXML_FILE_DIR_STRING);
		if(hostVisframeContext==null)
			throw new IllegalArgumentException("hostVisframeContext");
		
		this.hostVisframeContext = hostVisframeContext;
	}
	
	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}

}
