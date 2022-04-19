package viewer.visframe.function.variable.independent;

import context.VisframeContext;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import viewer.AbstractViewer;

/**
 * 
 */
public class IndependentFreeInputVariableTypeIDViewer extends AbstractViewer<IndependentFreeInputVariableTypeID, IndependentFreeInputVariableTypeIDViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public IndependentFreeInputVariableTypeIDViewer(
			IndependentFreeInputVariableTypeID value,
			VisframeContext hostVisframeContext) {
		super(value, IndependentFreeInputVariableTypeViewerController.FXML_FILE_DIR_STRING);
		
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
