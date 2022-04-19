package builder.basic.collection.map.nonleaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import builder.basic.collection.map.MapFeatureEntryController;
import core.builder.NonLeafNodeBuilder;
import core.builder.ui.integrative.NonLeafNodeBuilderIntegrativeUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;



/**
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class HashMapNonLeafNodeBuilderIntegrativeUIController<K,V> extends NonLeafNodeBuilderIntegrativeUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/map/nonleaf/HashMapNonLeafNodeBuilderIntegrativeUI.fxml";
	
	/**
	 * 
	 */
	private Map<MapFeatureEntryController<K,V>,Parent> entryControllerParentNodeMap;
	
	/**
	 * 
	 * @return
	 */
	public Map<MapFeatureEntryController<K,V>,Parent> getEntryControllerParentNodeMap() {
		return entryControllerParentNodeMap;
	}

	
	/**
	 * set the owner and perform any initialization based on the owner;
	 * 
	 * 1. add children {@link NodeBuilder}s by adding the root node of their UI to the ;
	 * 		EmbeddedUIRootContainerNodeController
	 * @param owner
	 * @throws IOException 
	 */
	@Override
	public void setOwnerNonLeafNodeBuilder(NonLeafNodeBuilder<?> owner) throws IOException {
		this.ownerNonLeafNodeBuilder = owner;
		
		
		this.entryControllerParentNodeMap = new HashMap<>();
		
	}
	
	/**
	 * @return the ownerNonLeafNodeBuilder
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashMapNonLeafNodeBuilder<K,V> getOwnerNodeBuilder() {
		return (HashMapNonLeafNodeBuilder<K, V>) ownerNonLeafNodeBuilder;
	}

	
	///////////////////////////////////////////////////
	
	/**
	 * set whether this UI is modifiable or not;
	 * 
	 * @param modifiable
	 * @throws SQLException 
	 * @throws IOException 
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		super.setModifiable(modifiable);
		
		this.mapOperationButtonHBox.setVisible(modifiable);
		
		for(MapFeatureEntryController<K, V> controller:this.entryControllerParentNodeMap.keySet()){
			controller.setModifiable(modifiable);
		}
		
	}
	
	///////////////////////////////////////////////
	@Override
	public Parent getRootNode() {
		return this.containerVBox;
	}
	
	/**
	 * return the Parent node to which all children {@link NodeBuilder}'s root node should be added;
	 * 
	 * @return
	 */
	@Override
	public Pane getChildrenEmbeddedNodeContainerPane() {
		return this.contentVBox;
	}
	
	public void removeEntry(MapFeatureEntryController<K,V> entry) {
		this.entryControllerParentNodeMap.remove(entry);
		this.entryVBox.getChildren().remove(this.entryControllerParentNodeMap.get(entry));
	}
	
	public void removeAllEntry() {
		this.entryControllerParentNodeMap.clear();
		this.entryVBox.getChildren().clear();
	}
	
	/**
	 * set the entries to the given map;
	 * 
	 * @param map
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void setValue(HashMap<K,V> map) throws SQLException, IOException {
		this.removeAllEntry();
		//add new entries
		for(K key:map.keySet()) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(MapFeatureEntryController.FXML_FILE_DIR));
			
	    	//this must be invoked before the getController() method!!!!!!!
//	    	try {
				loader.load();
		    	@SuppressWarnings("unchecked")
				MapFeatureEntryController<K,V> entryController = (MapFeatureEntryController<K,V>)loader.getController();
		    	
		    	entryController.setBuilder(
		    			this.getOwnerNodeBuilder().getMapKeyNodeBuilderFactory().build(), 
				    	this.getOwnerNodeBuilder().getMapValueNodeBuilderFactory().build()
				    );
		    	
		    	entryController.getKeyBuilder().setValue(key, false);
		    	entryController.getValueBuilder().setValue(map.get(key), false);
		    	
		    	//add
		    	this.entryControllerParentNodeMap.put(entryController, entryController.getRootPane());
		    	
		    	//
		    	this.entryVBox.getChildren().add(entryController.getRootPane());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				System.exit(1);
//			}
		}
		
	}
	////////////////////////////////////////
	/**
	 * check if there are entries with duplicate keys
	 * @return
	 * @throws IOException 
	 */
	boolean doesDuplicateKeyExist() throws IOException {
		Set<K> keySet = new HashSet<>();
		
		for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
			K key = entryController.getKeyBuilder().getCurrentValue();//?
			if(keySet.contains(key)) {
				return true;
			}else {
				keySet.add(key);
			}
		}
		
		return false;
	}
	
	/**
	 * any initialization not based on {@link #ownerNonLeafNodeBuilder}
	 */
	@FXML @Override
	public void initialize() {
		//
	}
	
	
	@FXML
	private VBox containerVBox;
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox mapOperationButtonHBox;
	@FXML
	private Button addButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button selectAllButton;
	@FXML
	private Button deSelectAllButton;
	@FXML
	private Button checkButton;
	@FXML
	private ScrollPane entryScrollPane;
	@FXML
	private VBox entryVBox;
	@FXML
	private HBox operationButtonContainerHBox;
	@FXML
	private Button finishButton;
	@FXML
	private Button setToDefaultEmptyButton;
	@FXML
	private Button cancelButton;
	
	/**
	 * add a new entry
	 * @param event
	 * @throws IOException
	 * @throws SQLException 
	 */
	@FXML
	public void addButtonOnAction(ActionEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(MapFeatureEntryController.FXML_FILE_DIR));
		
    	//this must be invoked before the getController() method!!!!!!!
    	loader.load();
    	
    	@SuppressWarnings("unchecked")
		MapFeatureEntryController<K,V> entryController = (MapFeatureEntryController<K,V>)loader.getController();
    	
    	entryController.setBuilder(
    			this.getOwnerNodeBuilder().getMapKeyNodeBuilderFactory().build(), 
		    	this.getOwnerNodeBuilder().getMapValueNodeBuilderFactory().build()
		    );
    	
    	
    	//add
    	this.entryControllerParentNodeMap.put(entryController, entryController.getRootPane());
    	
    	//
    	this.entryVBox.getChildren().add(entryController.getRootPane());
	}
	// Event Listener on Button[#deleteButton].onAction
	@FXML
	public void deleteButtonOnAction(ActionEvent event) {
		Set<MapFeatureEntryController<K,V>> keySetToRemove = new HashSet<>();
		
		for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
			if(entryController.isSelected()) {
				this.entryVBox.getChildren().remove(this.entryControllerParentNodeMap.get(entryController));
//				this.entryControllerParentNodeMap.remove(entryController);
				keySetToRemove.add(entryController);
			}
		}
		if(!keySetToRemove.isEmpty()) {
			this.entryControllerParentNodeMap.keySet().removeAll(keySetToRemove);
		}else {
			AlertUtils.popAlert("Warning", "No item selected.");
		}
	}
	// Event Listener on Button[#selectAllButton].onAction
	@FXML
	public void selectAllButtonOnAction(ActionEvent event) {
		try {
			for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
				entryController.setSelected(true);
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("MapFeatureEmbeddedController.selectAllButtonOnAction", e, this.getStage());
		}
	}
	// Event Listener on Button[#deSelectAllButton].onAction
	@FXML
	public void deSelectAllButtonOnAction(ActionEvent event) {
		try {
			for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
				entryController.setSelected(false);
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("MapFeatureEmbeddedController.deSelectAllButtonOnAction", e, this.getStage());
		}
	}
	// Event Listener on Button[#checkButton].onAction
	@FXML
	public void checkButtonOnAction(ActionEvent event) throws IOException {
		if(this.doesDuplicateKeyExist()) {
			Alert alert = new Alert(AlertType.ERROR);
			
			alert.setTitle("Map key Error");
			alert.setHeaderText("duplicate key found!");
			alert.setContentText("Multiple entries with same key value are found!");
			alert.showAndWait();
		}
	}
	
	/////////////////////////
	/**
	 * first check if the given input data are valid to build a value;
	 * if no, show alert
	 * if yes, update the built value to the owner node builder
	 * 
	 * the same with super class's method
	 * @throws SQLException 
	 * @throws IOException 
	 */
	// Event Listener on Button[#finishButton].onAction
	@FXML @Override
	public void finishButtonOnAction(ActionEvent event) throws SQLException, IOException {
		super.finishButtonOnAction(event);
	}
	
	/**
	 * need to remove all entries
	 * @throws IOException 
	 */
	// Event Listener on Button[#setToDefaultEmptyButton].onAction
	@FXML @Override
	public void setToDefaultEmptyButtonOnAction(ActionEvent event) {
		this.getOwnerNodeBuilder().setToDefaultEmpty();
	}
	
	/**
	 * same with super class
	 * @throws SQLException 
	 * @throws IOException 
	 * 
	 */
	// Event Listener on Button[#cancelButton].onAction
	@FXML @Override
	public void cancelButtonOnAction(ActionEvent event) throws SQLException, IOException {
		super.cancelButtonOnAction(event);
	}

	

}
