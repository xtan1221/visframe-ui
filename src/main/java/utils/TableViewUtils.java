package utils;

import javafx.scene.control.TableView;

public class TableViewUtils {

	public static void bindColWidthPropertyWithTableWidthProperty(TableView<?> tableView) {
		
		int totalColSize = tableView.getColumns().size();
		
		tableView.getColumns().forEach(e->{
			e.prefWidthProperty().bind(tableView.widthProperty().divide(totalColSize));
		});
		
	}
	
	//TODO not tested
	public static void disableColumnSortingFeature(TableView<?> tableView) {
		tableView.getColumns().forEach(e->{
			e.setSortable(false);
		});
	}
}
