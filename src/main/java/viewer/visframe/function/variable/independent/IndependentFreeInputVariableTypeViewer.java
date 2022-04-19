package viewer.visframe.function.variable.independent;

import context.VisframeContext;
import function.variable.independent.IndependentFreeInputVariableType;
import viewer.AbstractViewer;

/**
 * 
 */
public class IndependentFreeInputVariableTypeViewer extends AbstractViewer<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public IndependentFreeInputVariableTypeViewer(
			IndependentFreeInputVariableType value,
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
