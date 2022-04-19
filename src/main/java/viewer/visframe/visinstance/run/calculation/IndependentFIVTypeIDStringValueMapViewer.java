package viewer.visframe.visinstance.run.calculation;

import context.project.VisProjectDBContext;
import viewer.AbstractViewer;
import visinstance.run.calculation.IndependentFIVTypeIDStringValueMap;

/**
 * 
 */
public class IndependentFIVTypeIDStringValueMapViewer extends AbstractViewer<IndependentFIVTypeIDStringValueMap, IndependentFIVTypeIDStringValueMapViewerController>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	
	public IndependentFIVTypeIDStringValueMapViewer(IndependentFIVTypeIDStringValueMap value, VisProjectDBContext hostVisProjectDBContext) {
		super(value, IndependentFIVTypeIDStringValueMapViewerController.FXML_FILE_DIR_STRING);
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}


	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

	
}
