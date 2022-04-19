package viewer.visframe.operation.sql.generic;

import context.VisframeContext;
import operation.sql.generic.DataTableAndColumnsMapping;
import viewer.AbstractViewer;

/**
 * 
 */
public class DataTableAndColumnsMappingViewer extends AbstractViewer<DataTableAndColumnsMapping, DataTableAndColumnsMappingViewerController>{
	private final VisframeContext hostVisframeContext;
	
	/**
	 * constructor
	 * @param value
	 * @param hostVisframeContext
	 */
	public DataTableAndColumnsMappingViewer(DataTableAndColumnsMapping value, VisframeContext hostVisframeContext) {
		super(value, DataTableAndColumnsMappingViewerController.FXML_FILE_DIR_STRING);
		
		this.hostVisframeContext = hostVisframeContext;
	}

	/**
	 * @return the hostVisframeContext
	 */
	public VisframeContext getHostVisframeContext() {
		return hostVisframeContext;
	}
	
}
