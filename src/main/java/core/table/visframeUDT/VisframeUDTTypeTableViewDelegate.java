package core.table.visframeUDT;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import basic.SimpleName;
import basic.lookup.PrimaryKeyID;
import basic.lookup.VisframeUDT;
import basic.lookup.project.type.VisframeUDTManagementTableRow;
import basic.lookup.project.type.VisframeUDTTypeManagerBase;
import context.project.process.logtable.VfIDCollection;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import core.table.VfIDCollectionBasedColumnCellFactoryUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import rdb.table.lookup.ManagementTableColumn;
import visinstance.run.VisInstanceRun;
import visinstance.run.calculation.function.composition.CFTargetValueTableRun;


/**
 * contains a TableView of all entities of a {@link VisframeUDT} type in a host VisProjectDBContext with a {@link VisframeUDTTypeManagerBase}
 * @author tanxu
 *
 * @param <T>
 * @param <I>
 */
public class VisframeUDTTypeTableViewDelegate<T extends VisframeUDT,  I extends PrimaryKeyID<T>> {
	//
	private final VisframeUDTTypeTableViewerManager<T,I> ownerManager;
	
	//
	private TableView<VisframeUDTManagementTableRowDelegate<T,I>> tableView;
	
	/**
	 * there could be at most one row selected at a time;
	 */
	private VisframeUDTManagementTableRowDelegate<T,I> currentlySelectedRow = null;
	
	/**
	 * 
	 * @param ownerManager
	 * @throws SQLException 
	 */
	VisframeUDTTypeTableViewDelegate(VisframeUDTTypeTableViewerManager<T,I> ownerManager) throws SQLException{
		this.ownerManager = ownerManager;
		
		this.initializeTableColumns();
		
		this.fetchData();
	}
	
	
	private void initializeTableColumns() {
		this.tableView = new TableView<>();
		this.tableView.setEditable(true);
		
		this.setTableRowMouseClickEventHandler();
		
		//
//		if(!this.ownerManager.isViewOnly()) {
		this.createSelectionCheckBoxColumn(); //even for view only mode, still need selection check box column to select item for detail display
//		}
		
		//create primary key ID related columns
		this.createPrimaryKeyAttributeColumns();

		/////////////
		//type specific columns
		this.createTypeSpecificColumns();
		
		
		//////////process related columns
		this.createProcessRelatedColumns();
		
	}
	
	/**
	 * for debug
	 */
	private void setTableRowMouseClickEventHandler() {
		tableView.setRowFactory( tv -> {
		    TableRow<VisframeUDTManagementTableRowDelegate<T,I>> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
		        	VisframeUDTManagementTableRowDelegate<T,I> rowData = row.getItem();
		            System.out.println(rowData.isSelected());
		        }
		    });
		    return row ;
		});
	}
	
	
	/**
	 * checkbox column to select one row;
	 * 
	 * !!!example on how to set up CheckBoxTableCell based column that allows at most one row selected at a time;
	 * 
	 */
	private void createSelectionCheckBoxColumn() {
		TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, Boolean> tableColumn = new TableColumn<>("SELECTION");
		
		tableColumn.setCellValueFactory(
//				p -> new SimpleBooleanProperty(p.getValue().isSelected())
				new PropertyValueFactory<VisframeUDTManagementTableRowDelegate<T,I>, Boolean>("selected")
//				cellData -> cellData.getValue().selectedProperty()
				);
		
		
		
//		tableColumn.setCellFactory(CheckBoxTableCell.forTableColumn(tableColumn));
		//set the cell factory so that there could only be at most one row selected at a time;
		tableColumn.setCellFactory(
				CheckBoxTableCell.forTableColumn(
						new Callback<Integer, ObservableValue<Boolean>>() {
							@Override
							public ObservableValue<Boolean> call(Integer param) {//here param is the row index of the selected cell?
								VisframeUDTManagementTableRowDelegate<T,I> row =  //new row
										((VisframeUDTManagementTableRowDelegate<T,I>) VisframeUDTTypeTableViewDelegate.this.tableView.getItems().get(param));
								
//								System.out.println("new row:"+((FileFormatID)row.getTableRow().getEntity().getID()).getName().getStringValue());
					        	if(row.isSelected()) {//new row is selected
//					        		System.out.println("selected");
					        		if(VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow!=null) {//there was a non null currentlySelectedRow
					        			
//					        			System.out.println("previous selected row:"+((FileFormatID)VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow.getTableRow().getEntity().getID()).getName().getStringValue());
					        			
					        			if(VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow!=row) {//currently row is not the same with the new row
					        				System.out.println("hello");
					        				VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow.setSelected(false);
					        				VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow = row;
					        			}
					        		}else {
					        			VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow = row;
					        		}
					        		
					        	}else {//unselected
					        		//currentlySelectedRow is the row, simply remove it from currentlySelectedRow;
					        		if(VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow!=null&&VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow==row) {
					        			VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow = null;
					        		}
					        	}
					        	
						        return VisframeUDTTypeTableViewDelegate.this.tableView.getItems().get(param).selectedProperty();
						    }
						}
				)
		);
		
//		
//		tableColumn.setOnEditCommit(
//	            new EventHandler<CellEditEvent<VisframeUDTManagementTableRowDelegate<T,I>, Boolean>>() {
//	                @Override
//	                public void handle(CellEditEvent<VisframeUDTManagementTableRowDelegate<T,I>, Boolean> t) {
//	                    ((VisframeUDTManagementTableRowDelegate<T,I>) t.getTableView().getItems().get(
//	                            t.getTablePosition().getRow())
//	                            ).setSelected(t.getNewValue());
//	                }
//	            }
//	        );
//		
//		tableColumn.setEditable(true);
		
//		
//		//this will ensure that there is at most one row's CheckBox of this column selected and the selected row is stored in {@link #currentlySelectedRow};
//		tableColumn.setOnEditCommit(
////			new EventHandler<CellEditEvent<VisframeUDTManagementTableRowTableViewItemType<T,I>, Boolean>>() {
////			@Override
////			public void handle(CellEditEvent<VisframeUDTManagementTableRowTableViewItemType<T,I>, Boolean> t) 
//			t->{
//				System.out.println("EditCommit");
//	        	//update the {@link VisframeUDTManagementTableRowTableViewItemType#selected} field when the checkbox is edited
//	        	VisframeUDTManagementTableRowDelegate<T,I> row = 
//	        			((VisframeUDTManagementTableRowDelegate<T,I>) t.getTableView().getItems().get(t.getTablePosition().getRow()));
//	        	
//	        	row.setSelected(t.getNewValue());
//	        	
//	        	if(t.getNewValue()) {//new row is selected
//	        		if(VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow!=null) {//there was a non null currentlySelectedRow
//	        			VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow.setSelected(false);
//	        		}
//	        		VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow = row;
//	        		
//	        	}else {//unselected
//	        		//currentlySelectedRow is the row, simply remove it from currentlySelectedRow;
//	        		if(VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow!=null&&VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow==row) {
//	        			VisframeUDTTypeTableViewDelegate.this.currentlySelectedRow = null;
//	        		}
//	        	}
//	        }
////			}
//		);
//		tableColumn.setOnEditStart(t->{System.out.println("EditStart");});
//		tableColumn.setOnEditCancel(t->{System.out.println("EditCancel");});
		
		this.tableView.getColumns().add(tableColumn);
	}
	
	
	/**
	 * create primary key ID related columns
	 */
	private void createPrimaryKeyAttributeColumns() {
		
		for(SimpleName colName:this.ownerManager.getTableManager().getPrimaryKeyAttributeNameMap().keySet()) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, String> tableColumn = new TableColumn<>(colName.getStringValue());
			
			tableColumn.setCellValueFactory(
					//p is of type CellDataFeatures<VisframeUDTManagementTableRow<T,I>, String> 
					p -> new SimpleStringProperty(p.getValue().getTableRow().getEntity().getID().getPrimaryKeyAttributeNameStringValueMap().get(colName))
			);
			
			this.tableView.getColumns().add(tableColumn);
		}
	}
	
	
	/**
	 * see {@link VisframeUDTTypeManagerBase#setProcessRelatedColumnValues(PreparedStatement, T)} for details of columns in this method;
	 * 
	 */
	private void createProcessRelatedColumns() {
		//insertionTimeColumn - all VisframeUDT types
		TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, Timestamp> insertionTimeColumn = new TableColumn<>("insertionTime");
		insertionTimeColumn.setCellValueFactory(
				(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getInsertionTimestamp()) //
		);
		this.tableView.getColumns().add(insertionTimeColumn);
		
		//isTempColumn - all VisframeUDT types except for CfTargetValueTableRun
		if(!this.ownerManager.getTableManager().getManagedType().equals(CFTargetValueTableRun.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, Boolean> temporaryColumn = new TableColumn<>("temporary");
			temporaryColumn.setCellValueFactory(
					(p)-> new SimpleBooleanProperty(p.getValue().getTableRow().getIsTemporary())
			);
			this.tableView.getColumns().add(temporaryColumn);
		}
		
		//insertionProcessID column - all VisframeUDT types except for CfTargetValueTableRun
		if(!this.ownerManager.getTableManager().getManagedType().equals(CFTargetValueTableRun.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, String> insertionProcessIDColumn = new TableColumn<>("insertionProcessID");
			insertionProcessIDColumn.setCellValueFactory(
					(p)-> new SimpleStringProperty(p.getValue().getTableRow().getInsertionProcessID()==null?"N/A":p.getValue().getTableRow().getInsertionProcessID().toString())
			);
			this.tableView.getColumns().add(insertionProcessIDColumn);
		}
		
		//isReproducedColumn and VSAAReproducedAndInsertedInstanceUIDColumn - ReproduceableProcessTypes
		if(this.ownerManager.getTableManager().isOfReproduceableProcessType()) {
			//isReproducedColumn
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, Boolean> reproducedColumn = new TableColumn<>("reproduced");
			reproducedColumn.setCellValueFactory(
					(p)-> new SimpleBooleanProperty(p.getValue().getTableRow().getIsReproduced())
			);
			this.tableView.getColumns().add(reproducedColumn);
			
			//VSAAReproducedAndInsertedInstanceUIDColumn = use String type instead of integer
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, String> VSAAReproducedAndInsertedInstanceUIDColumn = new TableColumn<>("VSAAReproducedAndInsertedInstanceUID");
			VSAAReproducedAndInsertedInstanceUIDColumn.setCellValueFactory(
					(p)-> new SimpleStringProperty(p.getValue().getTableRow().getVSAAReproducedAndInsertedInstanceUID()==null?"N/A":p.getValue().getTableRow().getVSAAReproducedAndInsertedInstanceUID())
			);
			this.tableView.getColumns().add(VSAAReproducedAndInsertedInstanceUIDColumn);
		}
		
		//dependentProcessIDSetColumn - all process type (note that CfTargetValueTableRun is NonProcessType!)
		if(this.ownerManager.getTableManager().isOfProcessType()) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> dependentProcessIDSetColumn = new TableColumn<>("dependentProcessIDSet");
			dependentProcessIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getDependentProcessIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(dependentProcessIDSetColumn, "dependentProcessIDSet");
			this.tableView.getColumns().add(dependentProcessIDSetColumn);
		}	
		
		//baseProcessIDSet - all process type
		if(this.ownerManager.getTableManager().isOfProcessType()) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> baseProcessIDSetColumn = new TableColumn<>("baseProcessIDSet");
			baseProcessIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getBaseProcessIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(baseProcessIDSetColumn, "baseProcessIDSet");
			this.tableView.getColumns().add(baseProcessIDSetColumn);
		}
		
		//insertedNonProcessIDSetColumn - process type except for VisSchemeAppliedArchiveReproducedAndInsertedInstance and VisInstanceRun
		if(this.ownerManager.getTableManager().isOfProcessType() && 
				!this.ownerManager.getTableManager().getManagedType().equals(VisSchemeAppliedArchiveReproducedAndInsertedInstance.class) &&
				!this.ownerManager.getTableManager().getManagedType().equals(VisInstanceRun.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> insertedNonProcessIDSetColumn = new TableColumn<>("insertedNonProcessIDSet");
			insertedNonProcessIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getInsertedNonProcessIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(insertedNonProcessIDSetColumn, "insertedNonProcessIDSet");
			this.tableView.getColumns().add(insertedNonProcessIDSetColumn);
		}
		
		//insertedProcessIDSetColumn - VisSchemeAppliedArchiveReproducedAndInsertedInstance specific
		if(this.ownerManager.getTableManager().getManagedType().equals(VisSchemeAppliedArchiveReproducedAndInsertedInstance.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> insertedProcessIDSetColumn = new TableColumn<>("insertedProcessIDSetColumn");
			insertedProcessIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getInsertedProcessIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(insertedProcessIDSetColumn, "insertedProcessIDSetColumn");
			this.tableView.getColumns().add(insertedProcessIDSetColumn);
		}
		
		//involvedCfTargetValueTableRunIDSetColumn - VisInstanceRun specific
		if(this.ownerManager.getTableManager().getManagedType().equals(VisInstanceRun.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> involvedCfTargetValueTableRunIDSetColumn = new TableColumn<>("involvedCfTargetValueTableRunIDSet");
			involvedCfTargetValueTableRunIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getInvolvedCfTargetValueTableRunIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(
					involvedCfTargetValueTableRunIDSetColumn, 
					"involvedCfTargetValueTableRunIDSet");
			
			this.tableView.getColumns().add(involvedCfTargetValueTableRunIDSetColumn);
		}
		
		//employerVisInstanceRunIDSetColumn - CfTargetValueTableRun specific
		if(this.ownerManager.getTableManager().getManagedType().equals(CFTargetValueTableRun.class)) {
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, VfIDCollection> employerVisInstanceRunIDSetColumn = new TableColumn<>("employerVisInstanceRunIDSet");
			employerVisInstanceRunIDSetColumn.setCellValueFactory(
					(p)-> new SimpleObjectProperty<>(p.getValue().getTableRow().getEmployerVisInstanceRunIDSet())
			);
			VfIDCollectionBasedColumnCellFactoryUtils.setVfIDCollectionBasedColumnCellFactory(
					employerVisInstanceRunIDSetColumn, 
					"employerVisInstanceRunIDSet");
			this.tableView.getColumns().add(employerVisInstanceRunIDSetColumn);
		}
		
	}
	

	/**
	 * 
	 */
	private void createTypeSpecificColumns() {
		for(ManagementTableColumn col:this.ownerManager.getTableManager().getTypeSpecificManagementTableColumnList()) {
			
			TableColumn<VisframeUDTManagementTableRowDelegate<T,I>, String> tableColumn = new TableColumn<>(col.getName().getStringValue());
			
			tableColumn.setCellValueFactory(
					//p is of type CellDataFeatures<VisframeUDTManagementTableRow<T,I>, String> 
					p -> new SimpleStringProperty(
							p.getValue().getTableRow().getTypeSpecificAttributeNameObjectValueMap().get(col.getName())==null?
									"NULL":p.getValue().getTableRow().getTypeSpecificAttributeNameObjectValueMap().get(col.getName()).toString()
									)
			);
			
			this.tableView.getColumns().add(tableColumn);
			
		}
	}
	
	
	/**
	 * fetch data from the rdb table and populate the tableview
	 * @throws SQLException
	 */
	private void fetchData() throws SQLException {
		for(VisframeUDTManagementTableRow<T,I> row:this.ownerManager.getTableManager().retrieveAllAsList(this.ownerManager.getSqlFilterConditionString(), this.ownerManager.getEntityFilteringCondition())) {
			this.tableView.getItems().add(new VisframeUDTManagementTableRowDelegate<>(row));
		}
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	void refresh() throws SQLException {
		this.tableView.getItems().clear();
		this.fetchData();
	}
	
	/////////////////////
	TableView<VisframeUDTManagementTableRowDelegate<T,I>> getTableView(){
		//
		return this.tableView;
	}
	

	/**
	 * @return the currentlySelectedRow
	 */
	public VisframeUDTManagementTableRowDelegate<T, I> getCurrentlySelectedRow() {
		return currentlySelectedRow;
	}
	
	/**
	 * 
	 */
	public void clearSelectedRow() {
		this.currentlySelectedRow = null;
	}
}
