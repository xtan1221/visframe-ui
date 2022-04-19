package viewer.visframe.operation.sql.generic;

import context.VisframeContext;
import operation.sql.generic.GenericSQLQuery;
import viewer.AbstractViewer;

/**
 * 
 */
public class GenericSQLQueryViewer extends AbstractViewer<GenericSQLQuery, GenericSQLQueryViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public GenericSQLQueryViewer(GenericSQLQuery value, VisframeContext hostVisframeContext) {
		super(value, GenericSQLQueryViewerController.FXML_FILE_DIR_STRING);
		
		
		this.hostVisframeContext = hostVisframeContext;
	}

	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}

}
