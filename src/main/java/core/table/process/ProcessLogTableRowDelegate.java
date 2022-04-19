package core.table.process;

import context.project.process.logtable.ProcessLogTableRow;
import javafx.beans.property.SimpleBooleanProperty;

public class ProcessLogTableRowDelegate {
	private final ProcessLogTableRow processLogTableRow;
	
	private final SimpleBooleanProperty selected;
	
	/**
	 * 
	 * @param processLogTableRow
	 */
	ProcessLogTableRowDelegate(ProcessLogTableRow processLogTableRow){
		this.processLogTableRow = processLogTableRow;
		this.selected = new SimpleBooleanProperty();
		this.selected.setValue(false);
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

	public ProcessLogTableRow getProcessLogTableRow() {
		return processLogTableRow;
	}

	
}
