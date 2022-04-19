package core.table.visframeUDT;

import basic.lookup.PrimaryKeyID;
import basic.lookup.VisframeUDT;
import basic.lookup.project.type.VisframeUDTManagementTableRow;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * contains a single VisframeUDTManagementTableRow object and a boolean type selected field;
 * 
 * facilitate {@link VisframeUDTTypeTableViewDelegate}
 * 
 * @author tanxu
 *
 * @param <T>
 * @param <I>
 */
public class VisframeUDTManagementTableRowDelegate<T extends VisframeUDT,  I extends PrimaryKeyID<T>> {
	private final VisframeUDTManagementTableRow<T,I> tableRow;
	
	private final SimpleBooleanProperty selected;
	
	/**
	 * constructor
	 * @param tableRow
	 */
	VisframeUDTManagementTableRowDelegate(VisframeUDTManagementTableRow<T,I> tableRow){
		this.tableRow = tableRow;
		this.selected = new SimpleBooleanProperty();
//		System.out.println("constructor");
		this.selected.setValue(false);
		
//		selected.addListener(e->{
//			System.out.println("changed");
//		});
	}
	
	public boolean isSelected() {
		return this.selected.get();
	}
	
	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
	
	public SimpleBooleanProperty selectedProperty() {
		return this.selected;
	}
	
	VisframeUDTManagementTableRow<T,I> getTableRow() {
		return tableRow;
	}
	
}
