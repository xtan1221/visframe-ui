package core.table;

import context.project.process.logtable.VfIDCollection;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class VfIDCollectionBasedColumnCellFactoryUtils {
	/**
	 * set cell factory for a VfIDCollection based Column;
	 * 
	 * once table cell of the column is clicked, popup a window containing the full details of the VfIDCollection object of the table cell;
	 * @param column
	 * @param title
	 */
	public static <R> void setVfIDCollectionBasedColumnCellFactory(TableColumn<R, VfIDCollection> column, String title) {
		column.setCellFactory(
				new Callback<TableColumn<R, VfIDCollection>, TableCell<R, VfIDCollection>>(){
					@Override
					public TableCell<R, VfIDCollection> call(
							TableColumn<R, VfIDCollection> p) {
//						if(p.ge)
						VfIDCollectionTableCell<R,VfIDCollection> ret = new VfIDCollectionTableCell<>();
						ret.setTitle(title);
//						System.out.println(ret.getIndex());
//						ret.setContent(p.getCellData(ret.getIndex()));//??
						return ret;
					}
				}
			);
	}
}
