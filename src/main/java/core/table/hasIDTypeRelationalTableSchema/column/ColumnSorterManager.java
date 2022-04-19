package core.table.hasIDTypeRelationalTableSchema.column;

import java.io.IOException;
import java.sql.SQLException;

import core.table.hasIDTypeRelationalTableSchema.HasIDTypeRelationalTableContentViewer;
import javafx.fxml.FXMLLoader;
import utils.FXUtils;
import utils.LayoutCoordinateAndSizeUtils;

public class ColumnSorterManager {
	private final HasIDTypeRelationalTableContentViewer hostHasIDTypeRelationalTableContentViewer;


	private final String columnName;
	
	///////////////////////////////////
	private ColumnSorterController controller;
	private int orderIndex;
	
	private double realWidth;
	/**
	 * 
	 * @param columnName
	 */
	public ColumnSorterManager(HasIDTypeRelationalTableContentViewer hostHasIDTypeRelationalTableContentViewer, String columnName){
		
		this.hostHasIDTypeRelationalTableContentViewer = hostHasIDTypeRelationalTableContentViewer;
		this.columnName = columnName;
	}
	
	/**
	 * @param orderIndex the orderIndex to set
	 */
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
		
		FXUtils.set2Disable(this.getController().moveUpButton, this.orderIndex == 0);
		FXUtils.set2Disable(this.getController().moveDownButton, this.orderIndex == this.hostHasIDTypeRelationalTableContentViewer.getHasIDTypeRelationalTableSchema().getOrderedListOfColumn().size()-1);
	}
	
	public void setSelected(boolean selected) {
		this.getController().includedCheckBox.setSelected(selected);
	}
	
	public boolean isSelected() {
		return this.getController().includedCheckBox.isSelected();
	}
	
	public boolean isSortingByASC() {
		return this.getController().sortByASCToggleButton.isSelected();
	}
	
	/////////////////////////////
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public ColumnSorterController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(ColumnSorterController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
				
				this.controller = loader.getController();
				
				this.controller.setManager(this);
				
				this.realWidth = LayoutCoordinateAndSizeUtils.findOutRealWidthAndHeight(this.controller.getRootContainerNode()).getFirst();
			} catch (IOException | SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		return this.controller;
	}

	
	///////////////////////////////////////////
	/**
	 * @return the hostHasIDTypeRelationalTableContentViewer
	 */
	public HasIDTypeRelationalTableContentViewer getHostHasIDTypeRelationalTableContentViewer() {
		return hostHasIDTypeRelationalTableContentViewer;
	}
	
	/**
	 * @return the orderIndex
	 */
	public int getOrderIndex() {
		return orderIndex;
	}

	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * @return the realWidth
	 */
	public double getRealWidth() {
		return realWidth;
	}

}
