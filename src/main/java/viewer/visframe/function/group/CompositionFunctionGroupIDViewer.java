package viewer.visframe.function.group;

import java.sql.SQLException;

import context.VisframeContext;
import function.group.CompositionFunctionGroup;
import function.group.CompositionFunctionGroupID;
import viewer.AbstractViewer;

/**
 * 
 */
public class CompositionFunctionGroupIDViewer extends AbstractViewer<CompositionFunctionGroupID, CompositionFunctionGroupIDViewerController>{
	private final VisframeContext hostVisframeContext;
	
	private CompositionFunctionGroup cfg;
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public CompositionFunctionGroupIDViewer(CompositionFunctionGroupID value, VisframeContext hostVisframeContext) {
		super(value, CompositionFunctionGroupIDViewerController.FXML_FILE_DIR_STRING);
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

	/**
	 * @return the cfg
	 */
	public CompositionFunctionGroup getCompositionFunctionGroup() {
		if(this.cfg == null) {
			try {
				this.cfg = this.hostVisframeContext.getCompositionFunctionGroupLookup().lookup(this.getValue());
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return cfg;
	}

	
}
