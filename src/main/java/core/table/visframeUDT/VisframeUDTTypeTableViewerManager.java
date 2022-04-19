package core.table.visframeUDT;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Predicate;

import basic.SimpleName;
import basic.lookup.PrimaryKeyID;
import basic.lookup.VisframeUDT;
import basic.lookup.project.type.VisframeUDTTypeManagerBase;
import core.builder.factory.VisframeUDTTypeBuilderFactory;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import viewer.ViewerFactory;

/**
 * manager class for a table view of all entites of a VisframeUDT type in a host VisProjectDBContext
 * @author tanxu
 *
 * @param <T>
 * @param <I>
 */
public class VisframeUDTTypeTableViewerManager<T extends VisframeUDT, I extends PrimaryKeyID<T>> {
	/**
	 * 
	 */
	private final VisframeUDTTypeManagerBase<T,I> visframeUDTTypeTableManager;
	/**
	 * factory class for {@link NonLeafNodeBuilder} of type T
	 */
	private final VisframeUDTTypeBuilderFactory<T> nodeBuilderFactory;
	/**
	 * 
	 */
	private final ViewerFactory<T> viewerFactory;
	/**
	 * filter condition sql string to query entities; can be null;
	 */
	private final String sqlFilterConditionString;
	/**
	 * only those entities retrieved by the sql query and passes this Predicate test will be shown on the resulted table view;
	 * can be null; if null, all entities retrieved by the sql query will be shown in the table view;
	 */
	private final Predicate<T> entityFilteringCondition;
	/**
	 * whether the TableView will be view-only or not(selectable)
	 */
	private final TableViewerMode mode; //true if the TableView is for view only mode-no SELECTION column is added and no buttons;
	/**
	 * only relevant then {@link #viewOnly} is false;
	 * 
	 * TextField from invoker's UI to display the selected VisframeUDT entity's primary key id attributes string values;
	 * text must be set when a selection is made or cleared when none is selected;
	 */
	private final Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap;
	/**
	 * operation to perform when a selection is successfully made; only relevant then {@link #viewOnly} is false
	 */
	private Runnable operationAfterSelectionIsDone = null;
	
	/////////////
	private VisframeUDTTypeTableViewDelegate<T,I> tableViewDelegate;
	
	/**
	 * 
	 */
	private VisframeUDTTypeTableViewerController<T,I> controller;
	
	/////////
	private Stage stage;
	private Scene scene;
	private Parent rootNode;
	
	/**
	 * constructor
	 * @param visframeUDTTypeTableManager
	 * @param nodeBuilderFactory
	 * @param sqlFilterConditionString
	 * @param entityFilteringCondition
	 * @param mode mode of this manager table; either VIEW only or SELECTABLE;
	 * @param invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap must be null if viewOnly is true,  must be non-null otherwise; note that this map does not need to cover the full set of primary key id attributes, the API will automatically fill the given TextFields;
	 * @throws SQLException
	 */
	public VisframeUDTTypeTableViewerManager(
			VisframeUDTTypeManagerBase<T,I> visframeUDTTypeTableManager,
			VisframeUDTTypeBuilderFactory<T> nodeBuilderFactory,
			String sqlFilterConditionString,
			Predicate<T> entityFilteringCondition,
			TableViewerMode mode,
			Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
			) throws SQLException{
		if(mode.equals(TableViewerMode.VIEW_ONLY)) {
			if(invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap!=null) {
				throw new IllegalArgumentException("invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap must be null when viewOnly is true");
			}
		}else {
			//can be null, but should set the operationAfterSelectionIsDone
//			if(invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap==null) {
//				throw new IllegalArgumentException("invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap must be non-null when viewOnly is false");
//			}
		}
		
		this.visframeUDTTypeTableManager = visframeUDTTypeTableManager;
		this.nodeBuilderFactory = nodeBuilderFactory;
		this.viewerFactory = null;//
		this.sqlFilterConditionString = sqlFilterConditionString;
		this.entityFilteringCondition = entityFilteringCondition;
		this.mode = mode;
		
		this.invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap;
		
		this.initializeTableViewDelegate();
	}
	
	/**
	 * constructor for ViewerFactory;
	 * 
	 * @param visframeUDTTypeTableManager
	 * @param nodeBuilderFactory
	 * @param sqlFilterConditionString
	 * @param entityFilteringCondition
	 * @param mode mode of this manager table; either VIEW only or SELECTABLE;
	 * @param invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap must be null if viewOnly is true,  must be non-null otherwise; note that this map does not need to cover the full set of primary key id attributes, the API will automatically fill the given TextFields;
	 * @throws SQLException
	 */
	public VisframeUDTTypeTableViewerManager(
			VisframeUDTTypeManagerBase<T,I> visframeUDTTypeTableManager,
			ViewerFactory<T> viewerFactory,
			String sqlFilterConditionString,
			Predicate<T> entityFilteringCondition
			) throws SQLException{
		
		
		this.visframeUDTTypeTableManager = visframeUDTTypeTableManager;
		this.nodeBuilderFactory = null;
		this.viewerFactory = viewerFactory;
		this.sqlFilterConditionString = sqlFilterConditionString;
		this.entityFilteringCondition = entityFilteringCondition;
		
		
		this.mode = TableViewerMode.VIEW_ONLY;
		this.invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = null;
		
		this.initializeTableViewDelegate();
	}
	
	
	public void setOperationAfterSelectionIsDone(Runnable operationAfterSelectionIsDone) {
		this.operationAfterSelectionIsDone = operationAfterSelectionIsDone;
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
		return VisframeUDTTypeTableViewerController.FXML_FILE_DIR_STRING;
	}
	
	public VisframeUDTTypeTableViewerController<T,I> getController() throws IOException {
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
	 * display a pop up window containing the TableView;
	 * @param primaryStage
	 * @throws IOException
	 */
	public void showWindow(Stage primaryStage) throws IOException {
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
		            	  VisframeUDTTypeTableViewerManager.this.getController().cancelButtonOnAction(null);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		          }
		      });        
		}
//		System.out.println("2");
		this.stage.show();
	}
	
	void closeWindow() {
		this.stage.close();
	}
	
	public T getSelectedItem() {
		return this.getTableViewDelegate().getCurrentlySelectedRow()==null?null:this.getTableViewDelegate().getCurrentlySelectedRow().getTableRow().getEntity();
	}
	
	public Map<SimpleName,TextField> getInvokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap() {
		return invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap;
	}
	
	public void refresh() throws SQLException {
		this.getTableViewDelegate().refresh();
	}
	
	public VisframeUDTTypeBuilderFactory<T> getNodeBuilderFactory() {
		return nodeBuilderFactory;
	}

	/**
	 * @return the viewerFactory
	 */
	public ViewerFactory<T> getViewerFactory() {
		return viewerFactory;
	}

	public Runnable getOperationAfterSelectionIsDone() {
		return operationAfterSelectionIsDone;
	}
	
	
	public TableViewerMode getMode() {
		return mode;
	}



	/////////////////
	public static enum TableViewerMode{
		VIEW_ONLY,
		SELECTION;
	}
}
