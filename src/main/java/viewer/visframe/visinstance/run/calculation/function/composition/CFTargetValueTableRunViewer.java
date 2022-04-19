package viewer.visframe.visinstance.run.calculation.function.composition;

import context.project.VisProjectDBContext;
import viewer.AbstractViewer;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;

/**
 * 
 */
public class CFTargetValueTableRunViewer extends AbstractViewer<CFTargetValueTableRun, CFTargetValueTableRunViewerController>{
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisProjectDBContext
	 */
	public CFTargetValueTableRunViewer(CFTargetValueTableRun value, VisProjectDBContext hostVisProjectDBContext) {
		super(value, CFTargetValueTableRunViewerController.FXML_FILE_DIR_STRING);
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("hostVisProjectDBContext");
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
	}
	
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

}
