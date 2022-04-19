package viewer.visframe.metadata;

import metadata.Metadata;
import viewer.AbstractViewerController;

public abstract class MetadataViewerControllerBase<T extends Metadata> extends AbstractViewerController<T> {
	/**
	 * @return the viewer
	 */
	@Override
	public abstract MetadataViewerBase<T, ?> getViewer();
}
