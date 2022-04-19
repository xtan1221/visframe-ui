package viewer.visframe.metadata;

import context.VisframeContext;
import metadata.Metadata;
import viewer.AbstractViewer;

public abstract class MetadataViewerBase<T extends Metadata, C extends MetadataViewerControllerBase<T>> extends AbstractViewer<T, C> {
	/**
	 * if of VisProjectDBContext, data table can be viewed; otherwise (VisScheme), cannot;
	 */
	private final VisframeContext hostVisframeContext;
	
	
	protected MetadataViewerBase(
			T value, String FXMLFileDirString, 
			VisframeContext hostVisframeContext) {
		super(value, FXMLFileDirString);
		
		if(hostVisframeContext==null)
			throw new IllegalArgumentException("given hostVisframeContext cannot be null!");
		
		this.hostVisframeContext = hostVisframeContext;
	}


	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}
	
}
