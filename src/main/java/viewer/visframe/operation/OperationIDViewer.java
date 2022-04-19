package viewer.visframe.operation;

import java.sql.SQLException;

import context.VisframeContext;
import operation.Operation;
import operation.OperationID;
import viewer.AbstractViewer;

/**
 * 
 */
public class OperationIDViewer extends AbstractViewer<OperationID, OperationIDViewerController>{
	private final VisframeContext hostVisframeContext;
	
	private Operation operation;
	/**
	 * 
	 * @param value
	 * @param hostVisframeContext
	 */
	public OperationIDViewer(OperationID value, VisframeContext hostVisframeContext) {
		super(value, OperationIDViewerController.FXML_FILE_DIR_STRING);
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
	 * 
	 * @return
	 */
	public Operation getOperation() {
		if(this.operation == null) {
			try {
				this.operation = this.hostVisframeContext.getOperationLookup().lookup(this.getValue());
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return operation;
	}

	
}
