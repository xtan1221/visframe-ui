package viewer.visframe.rdb.table;

import rdb.table.AbstractRelationalTableColumn;
import viewer.AbstractViewer;

/**
 * 
 */
public class AbstractRelationalTableColumnViewer extends AbstractViewer<AbstractRelationalTableColumn, AbstractRelationalTableColumnViewerController>{
	private final int index;
	
	/**
	 * constructor
	 * @param value
	 * @param index
	 */
	public AbstractRelationalTableColumnViewer(AbstractRelationalTableColumn value, int index) {
		super(value, AbstractRelationalTableColumnViewerController.FXML_FILE_DIR_STRING);
		// TODO Auto-generated constructor stub
		
		this.index = index;
	}
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
}
