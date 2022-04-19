package utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import rdb.table.AbstractRelationalTableColumn;
import rdb.table.HasIDTypeRelationalTableSchema;

/**
 * utility methods for creating a javaFX {@link TableView} for a HasIDTypeRelationalTableSchema;
 * @author tanxu
 * 
 */
public class HasIDTypeRelationalTableTableViewUtils {
	
	/**
	 * initialize and return a TableView and its TableColumns (as well as {@link TableColumn#setCellValueFactory(Callback)}) for the given HasIDTypeRelationalTableSchema;
	 * 
	 * note that whether the RUID column is included as the first column of the returned TableView or not is indicated by the given toIncludedRUIDCol;
	 * 
	 * all columns in the returned TableView will have value data type as String;
	 * 
	 * @param tableSchema
	 * @return
	 */
	public static TableView<List<String>> makeNewTableView(HasIDTypeRelationalTableSchema<?> tableSchema, boolean toIncludedRUIDCol){
		TableView<List<String>> ret = new TableView<>();
		
		List<? extends AbstractRelationalTableColumn> colList;
		if(toIncludedRUIDCol) {
			colList = tableSchema.getOrderedListOfColumn();
		}else {
			colList = tableSchema.getOrderedListOfNonRUIDColumn();
		}
		
		for(int i=0;i<colList.size();i++) {
			TableColumn<List<String>, String> tableColumn = 
					new TableColumn<>(colList.get(i).getName().getStringValue());
			
			int j = i;
			tableColumn.setCellValueFactory(
					new Callback<CellDataFeatures<List<String>, String>, ObservableValue<String>>() {
						public ObservableValue<String> call(CellDataFeatures<List<String>, String> p) {
							return new SimpleStringProperty(p.getValue().get(j));
					    }
			});
			ret.getColumns().add(tableColumn);
		}
		
		return ret;
	}
	
	
	/**
	 * populate the given TableView with the all the data contained in the given ResultSet;
	 * 
	 * note that any existing data in the given TableView will be cleared;
	 * 
	 * @param tableView
	 * @param rs
	 * @throws SQLException 
	 */
	public static void populateTableView(TableView<List<String>> tableView, ResultSet rs) throws SQLException {
		tableView.getItems().clear();
		
		ObservableList<List<String>> itemList= FXCollections.observableArrayList();
		
		int colNum = rs.getMetaData().getColumnCount();
		while(rs.next()) {
			List<String> item = new ArrayList<>();
			for(int i=1;i<=colNum;i++) {
				item.add(rs.getString(i));
			}
			itemList.add(item);
		}
		
		tableView.setItems(itemList);
	}
	
	
}
