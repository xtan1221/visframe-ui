package core.table.process;

import java.sql.SQLException;
import java.sql.Timestamp;

import context.project.process.logtable.ProcessLogTableRow;
import context.project.process.logtable.StatusType;
import context.project.process.logtable.VfIDCollection;
import core.table.VfIDCollectionBasedColumnCellFactoryUtils;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * delegate to a TableView specifically for a process log table of a VisProjectDBContext;
 * 
 * @author tanxu
 *
 */
public class ProcessLogTableViewDelegate {
	private final ProcessLogTableViewerManager processLogTableViewerManager;
	
	/////////////////
	private TableView<ProcessLogTableRowDelegate> tableView;
	
	/**
	 * table column for selection of a row;
	 * wrapping a CheckBox
	 */
	private TableColumn<ProcessLogTableRowDelegate, Boolean> selectionTableColumn;
	/**
	 * there could be at most one row selected at a time;
	 */
	private ProcessLogTableRowDelegate currentlySelectedRow = null;
	
	/**
	 * constructor
	 * @param processLogTableViewerManager
	 * @throws SQLException
	 */
	ProcessLogTableViewDelegate(ProcessLogTableViewerManager processLogTableViewerManager) throws SQLException{
		this.processLogTableViewerManager = processLogTableViewerManager;
		
		this.initializeTableColumns();
		
		this.fetchData();
	}
	
	private void initializeTableColumns() {
		this.tableView = new TableView<>();
		this.tableView.setEditable(true);
		
		this.createSelectionCheckBoxColumn();
		this.addNonVfIDCollectionBasedColumns();
		this.addVfIDCollectionBasedColumns();
	}
	
	/**
	 * checkbox column to select one row;
	 * 
	 * !!!example on how to set up CheckBoxTableCell based column that allows at most one row selected at a time;
	 * 
	 */
	void createSelectionCheckBoxColumn() {
		selectionTableColumn = new TableColumn<>("SELECTION");
		
		selectionTableColumn.setCellValueFactory(
//				p -> new SimpleBooleanProperty(p.getValue().isSelected())
				new PropertyValueFactory<ProcessLogTableRowDelegate, Boolean>("selected")
				);
		
//		tableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(tableColumn));
		selectionTableColumn.setCellFactory(
				CheckBoxTableCell.forTableColumn(
						new Callback<Integer, ObservableValue<Boolean>>() {
							@Override
							public ObservableValue<Boolean> call(Integer param) {//here param is the row index of the selected cell?
								ProcessLogTableRowDelegate row =  //new row
										((ProcessLogTableRowDelegate) ProcessLogTableViewDelegate.this.tableView.getItems().get(param));
								
//								System.out.println("new row:"+((FileFormatID)row.getTableRow().getEntity().getID()).getName().getStringValue());
					        	if(row.isSelected()) {//new row is selected
//					        		System.out.println("selected");
					        		if(ProcessLogTableViewDelegate.this.currentlySelectedRow!=null) {//there was a non null currentlySelectedRow
					        			
//					        			System.out.println("previous selected row:"+((FileFormatID)VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow.getTableRow().getEntity().getID()).getName().getStringValue());
					        			
					        			if(ProcessLogTableViewDelegate.this.currentlySelectedRow!=row) {//currently row is not the same with the new row
					        				ProcessLogTableViewDelegate.this.currentlySelectedRow.setSelected(false);
					        				ProcessLogTableViewDelegate.this.currentlySelectedRow = row;
					        			}
					        		}else {
					        			ProcessLogTableViewDelegate.this.currentlySelectedRow = row;
					        		}
					        		
					        	}else {//unselected
					        		//currentlySelectedRow is the row, simply remove it from currentlySelectedRow;
					        		if(ProcessLogTableViewDelegate.this.currentlySelectedRow!=null&&ProcessLogTableViewDelegate.this.currentlySelectedRow==row) {
					        			ProcessLogTableViewDelegate.this.currentlySelectedRow = null;
					        		}
					        	}
					        	
						        return ProcessLogTableViewDelegate.this.tableView.getItems().get(param).selectedProperty();
						    }
						}
				)
		);
		
		///////
		this.tableView.getColumns().add(selectionTableColumn);
	}
	
	
	
	/**
	 * @return the selectionTableColumn
	 */
	public TableColumn<ProcessLogTableRowDelegate, Boolean> getSelectionTableColumn() {
		return selectionTableColumn;
	}
	
	private void addNonVfIDCollectionBasedColumns() {
		//UIDColumn
		TableColumn<ProcessLogTableRowDelegate, Integer> UIDColumn = new TableColumn<>("UID");
		UIDColumn.setCellValueFactory(
				(p)-> new SimpleIntegerProperty(p.getValue().getProcessLogTableRow().getUID()).asObject() //convert SimpleIntegerProperty to ObservableValue<Integer>
		);
		this.tableView.getColumns().add(UIDColumn);
		
		//startTimeColumn
		//table column will automatically invoke Timestamp.toString() method to get the content to show in the table cell by default?;
		TableColumn<ProcessLogTableRowDelegate, Timestamp> startTimeColumn = new TableColumn<>("startTime");
		startTimeColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getStartTimestamp()) //
		);
		this.tableView.getColumns().add(startTimeColumn);
		
		//processIDColumn
		TableColumn<ProcessLogTableRowDelegate, String> processIDColumn = new TableColumn<>("processID");
		processIDColumn.setCellValueFactory(
				(p)-> new SimpleStringProperty(p.getValue().getProcessLogTableRow().getProcessID().toString())
		);
		this.tableView.getColumns().add(processIDColumn);
		
		TableColumn<ProcessLogTableRowDelegate, String> insertionProcessIDColumn = new TableColumn<>("insertionProcessID");
		insertionProcessIDColumn.setCellValueFactory(
				(p)-> new SimpleStringProperty(p.getValue().getProcessLogTableRow().getInsertionProcessID().toString())
		);
		this.tableView.getColumns().add(insertionProcessIDColumn);
		
		//processStatusColumn
		TableColumn<ProcessLogTableRowDelegate, StatusType> processStatusColumn = new TableColumn<>("processStatus");
		processStatusColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getStatus())
		);
		this.tableView.getColumns().add(processStatusColumn);
		
		//isReproducedColumn
		//the column is of string type because this column in the process log table may have NULL value;
		TableColumn<ProcessLogTableRowDelegate, String> isReproducedColumn = new TableColumn<>("isReproduced");
		isReproducedColumn.setCellValueFactory(
				(p)-> new SimpleStringProperty(p.getValue().getProcessLogTableRow().getReproduced()==null?"N/A":Boolean.toString(p.getValue().getProcessLogTableRow().getReproduced()))
		);
		this.tableView.getColumns().add(isReproducedColumn);
	}
	
	private void addVfIDCollectionBasedColumns() {
		//baseProcessIDSetColumn
		TableColumn<ProcessLogTableRowDelegate, VfIDCollection> baseProcessIDSetColumn = new TableColumn<>("baseProcessIDSet");
		baseProcessIDSetColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getBaseProcessIDSet())
		);
		VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(baseProcessIDSetColumn, "baseProcessIDSet");
		this.tableView.getColumns().add(baseProcessIDSetColumn);
		
		//insertedProcessIDSetColumn
		TableColumn<ProcessLogTableRowDelegate, VfIDCollection> insertedProcessIDSetColumn = new TableColumn<>("insertedProcessIDSet");
		insertedProcessIDSetColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getInsertedProcessIDSet())
		);
		VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(insertedProcessIDSetColumn, "insertedProcessIDSet");
		this.tableView.getColumns().add(insertedProcessIDSetColumn);
		
		//insertedNonProcessIDSetColumn
		TableColumn<ProcessLogTableRowDelegate, VfIDCollection> insertedNonProcessIDSetColumn = new TableColumn<>("insertedNonProcessIDSet");
		insertedNonProcessIDSetColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getInsertedNonProcessIDSet())
		);
		VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(insertedNonProcessIDSetColumn, "insertedNonProcessIDSet");
		this.tableView.getColumns().add(insertedNonProcessIDSetColumn);
		
		//involvedCfTargetValueTableRunIDSet - VisInstanceRun specific; null for other types
		TableColumn<ProcessLogTableRowDelegate, VfIDCollection> involvedCfTargetValueTableRunIDSetColumn = new TableColumn<>("involvedCfTargetValueTableRunIDSet");
		involvedCfTargetValueTableRunIDSetColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getProcessLogTableRow().getInvolvedCFTargetValueTableRunIDSet())
		);
		VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(involvedCfTargetValueTableRunIDSetColumn, "involvedCfTargetValueTableRunIDSet");
		this.tableView.getColumns().add(involvedCfTargetValueTableRunIDSetColumn);
	}
	
	/**
	 * fetch data from the rdb table and populate the tableview
	 * @throws SQLException
	 */
	private void fetchData() throws SQLException {
		for(ProcessLogTableRow row:this.processLogTableViewerManager.getProcessLogTableAndProcessPerformerManager().retrieveAllAsList()) {
			this.tableView.getItems().add(new ProcessLogTableRowDelegate(row));
		}
	}
	
	/**
	 * @throws SQLException 
	 * 
	 */
	public void refresh() {
		this.tableView.getItems().clear();
		try {
			this.fetchData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//////////
	TableView<ProcessLogTableRowDelegate> getTableView(){
		//
		return this.tableView;
	}
	
	/**
	 * to show/hide the selection column;
	 * @param hidden
	 */
	void setSelectionColumnHidden(boolean hidden) {
		if(hidden) {
//			if(this.getTableView().getColumns().contains(this.getSelectionTableColumn())) {
//				this.getTableView().getColumns().remove(this.getSelectionTableColumn());
//			}
//			this.getSelectionTableColumn().setVisible(false);
			this.getSelectionTableColumn().setEditable(false);
		}else {
//			if(!this.getTableView().getColumns().contains(this.getSelectionTableColumn())) {
//				this.getTableView().getColumns().add(0,this.getSelectionTableColumn());
//			}
//			this.getSelectionTableColumn().setVisible(true);
			this.getSelectionTableColumn().setEditable(true);
		}
	}
	/**
	 * @return the currentlySelectedRow
	 */
	public ProcessLogTableRowDelegate getCurrentlySelectedRow() {
		return currentlySelectedRow;
	}
	
}
