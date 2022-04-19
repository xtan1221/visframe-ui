package viewer.visframe.metadata.graph.vftree.feature;

import metadata.graph.vftree.feature.VfTreeNodeFeature;
import viewer.AbstractViewer;

/**
 * view only
 */
public class VfTreeNodeFeatureViewer extends AbstractViewer<VfTreeNodeFeature, VfTreeNodeFeatureViewerController>{

	/**
	 * 
	 * @param value
	 */
	public VfTreeNodeFeatureViewer(VfTreeNodeFeature value) {
		super(value, VfTreeNodeFeatureViewerController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
	}
	
}
