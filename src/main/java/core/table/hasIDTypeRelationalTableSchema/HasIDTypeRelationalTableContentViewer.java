package core.table.hasIDTypeRelationalTableSchema;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import basic.lookup.VisframeUDT;
import context.project.VisProjectDBContext;
import core.table.hasIDTypeRelationalTableSchema.column.ColumnSorterManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rdb.table.HasIDTypeRelationalTableSchema;

/**
 * viewer for content of a HasIDTypeRelationalTableSchema of a VisProjectDBContext;
 * 
 * 
 * 
 * @author tanxu
 *
 */
public class HasIDTypeRelationalTableContentViewer {
	private final VisProjectDBContext hostVisProjectDBContext;
	
	/**
	 * to which the {@link #hasIDTypeRelationalTableSchema} is belonging;
	 */
	private final VisframeUDT tableSourceVisframeUDTObject;
	
	private final HasIDTypeRelationalTableSchema<?> hasIDTypeRelationalTableSchema;
	
	/////////////////////////////////
	private HasIDTypeRelationalTableContentViewerController controller;
	private List<ColumnSorterManager> columnSorterManagerList;
	
	/**
	 * 
	 * @param hostVisProjectDBContext
	 * @param recordMetadata
	 */
	public HasIDTypeRelationalTableContentViewer(
			VisProjectDBContext hostVisProjectDBContext,
			VisframeUDT tableSourceVisframeUDTObject,
			HasIDTypeRelationalTableSchema<?> hasIDTypeRelationalTableSchema
			){
		if(hostVisProjectDBContext==null)
			throw new IllegalArgumentException("given hostVisProjectDBContext cannot be null!");
		if(tableSourceVisframeUDTObject==null)
			throw new IllegalArgumentException("given tableSourceVisframeUDTObject cannot be null!");
		if(hasIDTypeRelationalTableSchema==null)
			throw new IllegalArgumentException("given hasIDTypeRelationalTableSchema cannot be null!");
		
		
		this.hostVisProjectDBContext = hostVisProjectDBContext;
		this.tableSourceVisframeUDTObject = tableSourceVisframeUDTObject;
		this.hasIDTypeRelationalTableSchema = hasIDTypeRelationalTableSchema;
		
		//////////////
		this.columnSorterManagerList = new ArrayList<>();
		
	}
	
	
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws SQLException 
	 */
	public HasIDTypeRelationalTableContentViewerController getController() {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(HasIDTypeRelationalTableContentViewerController.FXML_FILE_DIR_STRING));
			
			try {
				loader.load();
				this.controller = loader.getController();
				
				this.controller.setManager(this);
			} catch (IOException | SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		}
		
		return this.controller;
	}
	
	
	//////////////////////////////////////////
	private Stage stage;
	private Scene scene;
	/**
	 * display a pop up window containing the TableView;
	 * @param primaryStage
	 * @throws IOException
	 * @throws SQLException 
	 */
	public void showWindow(Stage primaryStage) {
		
		if(this.stage == null) {
			this.scene = new Scene(this.getController().getRootContainerNode());
			
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(primaryStage);
			
		}
		
		this.stage.show();
	}


	////////////////////////////////////
	/**
	 * @return the hostVisProjectDBContext
	 */
	public VisProjectDBContext getHostVisProjectDBContext() {
		return hostVisProjectDBContext;
	}

	/**
	 * @return the tableSourceVisframeUDTObject
	 */
	public VisframeUDT getTableSourceVisframeUDTObject() {
		return tableSourceVisframeUDTObject;
	}
	
	/**
	 * @return the columnSorterManagerList
	 */
	public List<ColumnSorterManager> getColumnSorterManagerList() {
		return columnSorterManagerList;
	}

	/**
	 * @return the hasIDTypeRelationalTableSchema
	 */
	public HasIDTypeRelationalTableSchema<?> getHasIDTypeRelationalTableSchema() {
		return hasIDTypeRelationalTableSchema;
	}

	/////////////////////////////
	public LinkedHashMap<String, Boolean> getColumnNameASCSortedMap(){
		LinkedHashMap<String, Boolean> ret = new LinkedHashMap<>();
		this.getColumnSorterManagerList().forEach(manager->{
			if(manager.isSelected())
				ret.put(manager.getColumnName(), manager.isSortingByASC());
		});
		return ret;
	}
	
	public boolean allRowSelected() {
		return this.getController().includeAllRecordCheckBox.isSelected();
	}
	
	public int getSelectedRowNum() {
		return this.getController().getSelectedRowNum();
	}
	
	public int getStartRowIndex() {
		return this.getController().includeAllRecordCheckBox.isSelected()?
				1:Integer.parseInt(this.getController().startRecordIndexTextField.getText());
	}
	public int getEndRowIndex() {
		return this.getController().includeAllRecordCheckBox.isSelected()?
				this.getController().getTotalRowNum():Integer.parseInt(this.getController().endRecordIndexTextField.getText());
	}

}
