package core.table.visframeUDT.multipleselection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

import basic.lookup.PrimaryKeyID;
import basic.lookup.VisframeUDT;
import basic.lookup.project.type.VisframeUDTTypeManagerBase;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * manager class for a table view of all entites of a VisframeUDT type in a host VisProjectDBContext
 * @author tanxu
 *
 * @param <T>
 * @param <I>
 */
public class VisframeUDTTypeMultiSelectorManager<T extends VisframeUDT, I extends PrimaryKeyID<T>> {
	/**
	 * 
	 */
	private final VisframeUDTTypeManagerBase<T,I> visframeUDTTypeTableManager;
	/**
	 * factory class for {@link NonLeafNodeBuilder} of type T
	 */
	private final VisframeUDTTypeBuilderFactory<T> nodeBuilderFactory;
	/**
	 * filter condition sql string to query entities; can be null;
	 */
	private final String sqlFilterConditionString;
	/**
	 * only those entities retrieved by the sql query and passes this Predicate test will be shown on the resulted table view;
	 * can be null; if null, all entities retrieved by the sql query will be shown in the table view;
	 */
	private final Predicate<T> entityFilteringCondition;
	
	///////////////////////////////////////
//	/**
//	 * operation to perform when selection is successfully made;
//	 */
//	private Runnable operationAfterSelectionIsDone = null;
	
	/////////////
	private VisframeUDTTypeTableViewDelegate<T,I> tableViewDelegate;
	
	/**
	 * 
	 */
	private VisframeUDTTypeMultiSelectorController<T,I> controller;
	
	
	/////////
	private Stage stage;
	private Scene scene;
	private Parent rootNode;
	
	/**
	 * 
	 * @param visframeUDTTypeTableManager
	 * @param nodeBuilderFactory
	 * @param sqlFilterConditionString
	 * @param mode mode of this manager table; either VIEW only or SELECTABLE;
	 * @param invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap must be null if viewOnly is true,  must be non-null otherwise; note that this map does not need to cover the full set of primary key id attributes, the API will automatically fill the given TextFields;
	 * @throws SQLException
	 */
	public VisframeUDTTypeMultiSelectorManager(
			VisframeUDTTypeManagerBase<T,I> visframeUDTTypeTableManager,
			VisframeUDTTypeBuilderFactory<T> nodeBuilderFactory,
			String sqlFilterConditionString,
			Predicate<T> entityFilteringCondition
			) throws SQLException{
		
		this.visframeUDTTypeTableManager = visframeUDTTypeTableManager;
		this.nodeBuilderFactory = nodeBuilderFactory;
		this.sqlFilterConditionString = sqlFilterConditionString;
		this.entityFilteringCondition = entityFilteringCondition;
		
		
		this.initializeTableViewDelegate();
	}
	
	
	/////////////////////////
	private void initializeTableViewDelegate() throws SQLException {
		this.tableViewDelegate = new VisframeUDTTypeTableViewDelegate<>(this);
	}
	
	
	VisframeUDTTypeManagerBase<T,I> getTableManager(){
		return this.visframeUDTTypeTableManager;
	}
	
	VisframeUDTTypeTableViewDelegate<T,I> getTableViewDelegate(){
		return this.tableViewDelegate;
	}
	
	/**
	 * return the selected set of entities
	 * @return
	 */
	public Set<T> getSelectedEntitySet(){
		Set<T> ret = new LinkedHashSet<>();
		
		this.getTableViewDelegate().getCurrentlySelectedRowSet().forEach(e->{
			ret.add(e.getTableRow().getEntity());
		});
		
		return ret;
	}
	
	
	
	
	/**
	 * condition string in Where clause to filter records from the table;
	 * if null, select all;
	 * @return
	 */
	String getSqlFilterConditionString() {
		return this.sqlFilterConditionString;
	}
	
	/**
	 * @return the entityFilteringCondition
	 */
	public Predicate<T> getEntityFilteringCondition() {
		return entityFilteringCondition;
	}


	String getFXMLFileDirString() {
		return VisframeUDTTypeMultiSelectorController.FXML_FILE_DIR_STRING;
	}
	
	public VisframeUDTTypeMultiSelectorController<T,I> getController() throws IOException {
		if(this.controller==null) {
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource(this.getFXMLFileDirString()));
			
			this.rootNode = loader.load();
			
			this.controller = loader.getController();
			
			this.controller.setManager(this);
			
		}
		
		return this.controller;
	}
	
	Parent getRootNode() throws IOException {
		if(this.rootNode == null) {
			this.getController();
		}
		return this.rootNode;
	}
	
	/**
	 * display a pop up window containing the TableView and wait until it is closed and return to the caller;
	 * @param primaryStage
	 * @throws IOException
	 */
	public void showAndWait(Stage primaryStage) throws IOException {
//		System.out.println("1");
		if(this.stage == null) {
//			System.out.println("3");
			this.scene = new Scene(this.getRootNode());
//			scene.getStylesheets().add("/core/table/TableView.css");
			
			this.stage = new Stage();
			this.stage.setScene(this.scene);
			this.stage.initModality(Modality.WINDOW_MODAL);
			this.stage.initOwner(primaryStage);
			
			//when window is directly closed by upper-right X, clear the currently selected row;
			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		              System.out.println("Stage is closing");
		              try {
		            	  VisframeUDTTypeMultiSelectorManager.this.getController().cancelButtonOnAction(null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		          }
		      });        
		}
//		System.out.println("2");
		this.stage.showAndWait();
	}
	
	void closeWindow() {
		this.stage.close();
	}
	
	
	public void refresh() throws SQLException {
		this.getTableViewDelegate().refresh();
	}
	
	public VisframeUDTTypeBuilderFactory<T> getNodeBuilderFactory() {
		return nodeBuilderFactory;
	}
	
	
	
}
