package builder.basic.collection.set.nonleaf;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import builder.basic.collection.set.SetFeatureElementController;
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
public class HashSetNonLeafNodeBuilderIntegrativeUIController<E> extends NonLeafNodeBuilderIntegrativeUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/set/nonleaf/HashSetNonLeafNodeBuilderIntegrativeUI.fxml";
	
	/**
	 * 
	 */
	private Map<SetFeatureElementController<E>,Parent> elementControllerParentNodeMap;
	
	
	public Map<SetFeatureElementController<E>,Parent> getElementControllerParentNodeMap() {
		return elementControllerParentNodeMap;
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
		
		
		this.elementControllerParentNodeMap = new HashMap<>();
		
	}
	
	/**
	 * @return the ownerNonLeafNodeBuilder
	 */
	@SuppressWarnings("unchecked")
	@Override
	public HashSetNonLeafNodeBuilder<E> getOwnerNodeBuilder() {
		return (HashSetNonLeafNodeBuilder<E>) ownerNonLeafNodeBuilder;
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
		
		this.setOperationButtonHBox.setVisible(modifiable);
		
		for(SetFeatureElementController<E> controller:this.elementControllerParentNodeMap.keySet()){
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
	
	public void removeElement(SetFeatureElementController<E> controller) {
		this.elementControllerParentNodeMap.remove(controller);
		this.elementVBox.getChildren().remove(this.elementControllerParentNodeMap.get(controller));
	}
	
	public void removeAllElements() {
		this.elementControllerParentNodeMap.clear();
		this.elementVBox.getChildren().clear();
	}
	
	/**
	 * set the entries to the given map;
	 * 
	 * @param map
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void setValue(HashSet<E> set) throws SQLException, IOException {
		this.removeAllElements();
		//add new entries
		for(E e:set) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SetFeatureElementController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
//	    	try {
				loader.load();
				@SuppressWarnings("unchecked")
		    	SetFeatureElementController<E> controller = (SetFeatureElementController<E>)loader.getController();
		    	
		    	controller.setBuilder(
		    			this.getOwnerNodeBuilder().getSetElementNodeBuilderFactory().build()
				    );
		    	
		    	controller.getElementBuilder().setValue(e, false);
		    	
		    	//add
		    	this.elementControllerParentNodeMap.put(controller, controller.getRootPane());
		    	
		    	//
		    	this.elementVBox.getChildren().add(controller.getRootPane());
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				System.exit(1);
//			}
		}
		
	}
	////////////////////////////////////////
	private boolean doesDuplicateElementExist() throws IOException {
		Set<E> elementSet = new HashSet<>();
		
		for(SetFeatureElementController<E> elementController:this.elementControllerParentNodeMap.keySet()) {
			E key = elementController.getElementBuilder().getCurrentValue();
			if(elementSet.contains(key)) {
				return true;
			}else {
				elementSet.add(key);
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
	private HBox setOperationButtonHBox;
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
	private ScrollPane elementScrollPane;
	@FXML
	private VBox elementVBox;
	@FXML
	private HBox operationButtonContainerHBox;
	@FXML
	private Button finishButton;
	@FXML
	private Button setToDefaultEmptyButton;
	@FXML
	private Button cancelButton;

	@FXML
	public void addButtonOnAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(SetFeatureElementController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
	    	Parent elementParentNode = loader.load();
	    	
	    	@SuppressWarnings("unchecked")
			SetFeatureElementController<E> elementController = (SetFeatureElementController<E>)loader.getController();
	    	
	    	elementController.setBuilder(this.getOwnerNodeBuilder().getSetElementNodeBuilderFactory().build());
	    	
	    	//add
	    	this.elementControllerParentNodeMap.put(elementController, elementParentNode);
	    	
	    	//
	    	this.elementVBox.getChildren().add(elementParentNode);
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SetFeatureEmbeddedController.addButtonOnAction", e, this.getStage());
		}
	}
	
	
	// Event Listener on Button[#deleteButton].onAction
	@FXML
	public void deleteButtonOnAction(ActionEvent event) {
		try {
			Set<SetFeatureElementController<E>> elementSetToRemove = new HashSet<>();
			
			for(SetFeatureElementController<E> elementController:this.elementControllerParentNodeMap.keySet()) {
				if(elementController.isSelected()) {
					this.elementVBox.getChildren().remove(this.elementControllerParentNodeMap.get(elementController));
	//				this.entryControllerParentNodeMap.remove(entryController);
					elementSetToRemove.add(elementController);
				}
			}
			
			if(!elementSetToRemove.isEmpty()) {
				this.elementControllerParentNodeMap.keySet().removeAll(elementSetToRemove);
			}else {
				AlertUtils.popAlert("Warning", "No item selected.");
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SetFeatureEmbeddedController.deleteButtonOnAction", e, this.getStage());
		}
	}
	// Event Listener on Button[#selectAllButton].onAction
	@FXML
	public void selectAllButtonOnAction(ActionEvent event) {
		try {
			for(SetFeatureElementController<E> elementController:this.elementControllerParentNodeMap.keySet()) {
				elementController.setSelected(true);
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SetFeatureEmbeddedController.selectAllButtonOnAction", e, this.getStage());
		}
	}
	// Event Listener on Button[#deSelectAllButton].onAction
	@FXML
	public void deSelectAllButtonOnAction(ActionEvent event) {
		try {
			for(SetFeatureElementController<E> elementController:this.elementControllerParentNodeMap.keySet()) {
				elementController.setSelected(false);
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SetFeatureEmbeddedController.deSelectAllButtonOnAction", e, this.getStage());
		}
		
	}
	// Event Listener on Button[#checkButton].onAction
	@FXML
	public void checkButtonOnAction(ActionEvent event) {
		try {
			if(this.doesDuplicateElementExist()) {
				Alert alert = new Alert(AlertType.ERROR);
				
				alert.setTitle("Set element Error");
				alert.setHeaderText("duplicate element found!");
				alert.setContentText("Duplicate elements are found!");
				alert.showAndWait();
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show("SetFeatureEmbeddedController.deSelectAllButtonOnAction", e, this.getStage());
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
