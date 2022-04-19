package builder.basic.collection.map.leaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import builder.basic.collection.map.MapFeatureEntryController;
import core.builder.LeafNodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
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

public class HashMapBuilderEmbeddedUIContentController<K,V> extends LeafNodeBuilderEmbeddedUIContentController<HashMap<K,V>> {
	public static final String FXML_FILE_DIR = "/application/basic/primitive/IntegerTypeBuilderEmbeddedUIContent.fxml";
	
	
	private Map<MapFeatureEntryController<K,V>,Parent> entryControllerParentNodeMap;
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		
		//any specific initialization based on the owner builder
		
		//when save button is clicked, try to save the current map to the current status 
		//need to first check if duplicate map key exist with {@link #checkButtonOnAction(ActionEvent)} method
		//inside a try-catch clause, invoke {@link LeafNodeBuilder#updateNonNullValueFromContentController(Object)} with the value returned by {@link #build()};
		 
		this.saveButton.setOnAction(e->{
			//TODO??
			
			
			
			
			
			
		});
		
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMapBuilder<K,V> getOwnerNodeBuilder() {
		return (HashMapBuilder<K,V>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return contentVBox;
	}
	
	
	@Override
	public HashMap<K,V> build() {
		HashMap<K,V> ret = new HashMap<>();
		
		for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
			ret.put(entryController.getKeyBuilder().getCurrentValue(), entryController.getValueBuilder().getCurrentValue());
		}
		
		return ret;
	}
	


	@Override
	public void setUIToNonNullValue(HashMap<K, V> value) throws SQLException, IOException {
		//clear all
		this.entryControllerParentNodeMap.clear();
		this.entryVBox.getChildren().clear();
		
		//
		if(value!=null) {
			Map<K,V> map = (Map<K,V>)value;
			for(K key:map.keySet()) {
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource(MapFeatureEntryController.FXML_FILE_DIR));
				
		    	//this must be invoked before the getController() method!!!!!!!
		    	Parent entryParentNode;
//				try {
					entryParentNode = loader.load();
					@SuppressWarnings("unchecked")
					MapFeatureEntryController<K,V> entryController = (MapFeatureEntryController<K,V>)loader.getController();
			    	
			    	entryController.setBuilder(
			    			this.getOwnerNodeBuilder().getMapKeyNodeBuilderFactory().build(), 
			    			this.getOwnerNodeBuilder().getMapValueNodeBuilderFactory().build());
			    	
			    	//set value
			    	entryController.getKeyBuilder().setValue(key, false);
			    	entryController.getValueBuilder().setValue(map.get(key),false);
			    	
			    	//add to controller map
			    	this.entryControllerParentNodeMap.put(entryController, entryParentNode);
			    	//add graph node
			    	this.entryVBox.getChildren().add(entryParentNode);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					System.exit(1);
//				}
			}
			
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		for(MapFeatureEntryController<K,V> entryController:this.entryControllerParentNodeMap.keySet()) {
//			if(entryController.isSelected()) {
			this.entryVBox.getChildren().remove(this.entryControllerParentNodeMap.get(entryController));
//				this.entryControllerParentNodeMap.remove(entryController);
//			}
		}
		
		
		
		//TODO
	}
	
	
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

	//////////////////////////

	
	@FXML
	public void initialize() {
		this.entryControllerParentNodeMap = new HashMap<>();
		
	}
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox operationButtonHBox;
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
	private Button saveButton;
	@FXML
	private Button cancelButton;
	
	// Event Listener on Button[#addButton].onAction
	@FXML
	public void addButtonOnAction(ActionEvent event) throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(MapFeatureEntryController.FXML_FILE_DIR));
		
    	//this must be invoked before the getController() method!!!!!!!
    	Parent entryParentNode = loader.load();
    	
    	@SuppressWarnings("unchecked")
		MapFeatureEntryController<K,V> entryController = (MapFeatureEntryController<K,V>)loader.getController();
    	
    	entryController.setBuilder(
    			this.getOwnerNodeBuilder().getMapKeyNodeBuilderFactory().build(), 
		    	this.getOwnerNodeBuilder().getMapValueNodeBuilderFactory().build()
		    );
    			
    	
    	//add
    	this.entryControllerParentNodeMap.put(entryController, entryParentNode);
    	
    	//
    	this.entryVBox.getChildren().add(entryParentNode);
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
	///////////////////////////////////////////
	/**
	 * save the current map to the current status of owner node builder;
	 * 
	 * need to first check if duplicate map key exist with {@link #checkButtonOnAction(ActionEvent)} method
	 * 
	 * inside a try-catch clause, invoke {@link LeafNodeBuilder#updateNonNullValueFromContentController(Object)} with the value returned by {@link #build()};
	 * 
	 * 
	 * note that this is 
	 * @param event
	 */
	@FXML
	public void saveButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
		
		//TODO replaced by setupLogicToCheckEffectiveUIInput() method?????
	}
	
	/**
	 * cancel the changes since last time save button is clicked by invoking {@link #setUIToNonNullValue(HashMap)} with the current value of owner node builder
	 * 
	 * @param event
	 */
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
}
