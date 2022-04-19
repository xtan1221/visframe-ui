package builder.basic.collection.list;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import builder.basic.collection.list.element.ListFeatureElementController;
import core.builder.NonLeafNodeBuilder;
import core.builder.ui.integrative.NonLeafNodeBuilderIntegrativeUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
public class ArrayListNonLeafNodeBuilderIntegrativeUIController<E> extends NonLeafNodeBuilderIntegrativeUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/list/ArrayListNonLeafNodeBuilderIntegrativeUI.fxml";
	
	private List<ListFeatureElementController<E>> elementControllerList;
	private List<Parent> elementParentNodeList;
	
	/**
	 * @return the elementControllerList
	 */
	public List<ListFeatureElementController<E>> getElementControllerList() {
		return elementControllerList;
	}
	
	/**
	 * @return the elementParentNodeList
	 */
	public List<Parent> getElementParentNodeList() {
		return elementParentNodeList;
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
		
	}
	
	/**
	 * @return the ownerNonLeafNodeBuilder
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ArrayListNonLeafNodeBuilder<E> getOwnerNodeBuilder() {
		return (ArrayListNonLeafNodeBuilder<E>) ownerNonLeafNodeBuilder;
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
		
		this.listOperationButtonHBox.setVisible(modifiable);
		
		for(ListFeatureElementController<E> controller:this.elementControllerList){
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
	
	public void removeElement(ListFeatureElementController<E> controller) {
		this.elementControllerList.remove(controller);
		this.elementParentNodeList.remove(controller.getRootPane());
		this.elementVBox.getChildren().remove(controller.getRootPane());
	}
	
	public void removeAllElements() {
		this.elementControllerList.clear();
		this.elementParentNodeList.clear();
		this.elementVBox.getChildren().clear();
	}
	
	/**
	 * set the entries to the given map;
	 * 
	 * @param map
	 * @throws SQLException 
	 * @throws IOException
	 */
	public void setValue(List<E> list) throws SQLException, IOException {
		this.removeAllElements();
		int index = 0;
		//add new entries
		for(E e:list) {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ListFeatureElementController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
//	    	try {
				loader.load();
		    	@SuppressWarnings("unchecked")
		    	ListFeatureElementController<E> controller = (ListFeatureElementController<E>)loader.getController();
		    	
		    	controller.setBuilder(
		    			this.getOwnerNodeBuilder().getListElementNodeBuilderFactory().build(), this.getOwnerNodeBuilder()
				    );
		    	
		    	//first add the controller to the elementControllerList before setting the value of the element builder;
		    	this.elementControllerList.add(controller);
		    	this.elementParentNodeList.add(controller.getRootPane());
		    	
		    	//
		    	this.elementVBox.getChildren().add(controller.getRootPane());
		    	
		    	
		    	//this will trigger status change event actions of the ArrayListNonLeafNodeBuilder, 
		    	//which will make use of the elementControllerList; 
		    	//thus must add the controller to the elementControllerList first before setting the value!
		    	if(e==null) {
		    		controller.getElementBuilder().setToDefaultEmpty();
		    	}else {
		    		controller.getElementBuilder().setValue(e, false); 
		    	}
		    	
		    	controller.setIndex(index);
		    	//add
		    	
				
		    	index++;
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				System.exit(1);
//			}
		}
		
	}
	
	////////////////////////////////////////
	
	
	public void moveDown(ListFeatureElementController<E> elementController) throws IOException {
		if(this.elementControllerList.indexOf(elementController)==this.elementControllerList.size()-1) {
			return;
		}
		
		this.exchangeIndex(this.elementControllerList.indexOf(elementController), this.elementControllerList.indexOf(elementController)+1);
		
		this.updateElementNodePosition();
	}
	
	
	public void moveUp(ListFeatureElementController<E> elementController) throws IOException {
		if(this.elementControllerList.indexOf(elementController)==0) {
			return;
		}
		
		this.exchangeIndex(this.elementControllerList.indexOf(elementController), this.elementControllerList.indexOf(elementController)-1);
		
		this.updateElementNodePosition();
	}
	
	public void remove(ListFeatureElementController<E> elementController) throws IOException {
		this.elementParentNodeList.remove(this.elementControllerList.indexOf(elementController));
		this.elementControllerList.remove(elementController);
		//
		this.updateElementNodePosition();
	}
	
	private void exchangeIndex(int index1, int index2) throws IOException {
		
		Collections.swap(this.elementControllerList, index1, index2);
		
		Collections.swap(this.elementParentNodeList, index1, index2);
		
	}
	
	private void updateElementNodePosition() throws IOException {
		this.elementVBox.getChildren().clear();
		
		int i=0;
		for(ListFeatureElementController<E> controller:this.elementControllerList) {
			this.elementVBox.getChildren().add(this.elementParentNodeList.get(i));
			controller.setIndex(i);
			i++;
		}
		
//		this.setValue(this.getOwnerNodeBuilder().build());
		
	}
	
	/////////////////////////////////////////
	/**
	 * any initialization not based on {@link #ownerNonLeafNodeBuilder}
	 */
	@FXML @Override
	public void initialize() {
		//
		this.elementControllerList = new ArrayList<>();
		this.elementParentNodeList = new ArrayList<>();
	}
	
	
	@FXML
	private VBox containerVBox;
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox listOperationButtonHBox;
	@FXML
	private Button addButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button selectAllButton;
	@FXML
	private Button deSelectAllButton;
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

	// Event Listener on Button[#addButton].onAction
	@FXML
	public void addButtonOnAction(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ListFeatureElementController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
	    	Parent elementParentNode;
			
				elementParentNode = loader.load();
			
	    	
	    	@SuppressWarnings("unchecked")
			ListFeatureElementController<E> elementController = (ListFeatureElementController<E>)loader.getController();
	    	
	    	elementController.setBuilder(
	    			this.getOwnerNodeBuilder().getListElementNodeBuilderFactory().build(),
	    			this.getOwnerNodeBuilder()
	    			);
	    	
	    	
	    	//add to controller map
	    	this.elementControllerList.add(elementController);
	    	this.elementParentNodeList.add(elementParentNode);
	    	
	    	elementController.setIndex(this.elementControllerList.indexOf(elementController));
	    	
	//    	
	//    	//set value
	//    	elementController.getElementBuilder().setCurrentValue(this.getOwnerBuilder().getElementNodeDefaultValue());
	//    	elementController.setIndex(this.elementControllerList.indexOf(elementController));
	//    	
	    	
	    	//add graph node
	    	this.elementVBox.getChildren().add(elementParentNode);
		} catch (Exception e) {
			ExceptionHandlerUtils.show("ListFeatureEmbeddedController.addButtonOnAction", e, this.getStage());
		}
	}
	
	// Event Listener on Button[#deleteButton].onAction
	@FXML
	public void deleteButtonOnAction(ActionEvent event) {
		try {
			Set<ListFeatureElementController<E>> elementSetToRemove = new HashSet<>();
			
			for(ListFeatureElementController<E> elementController:this.elementControllerList) {
				if(elementController.isSelected()) {
					this.elementVBox.getChildren().remove(this.elementParentNodeList.get(this.elementControllerList.indexOf(elementController)));
	//				this.entryControllerParentNodeMap.remove(entryController);
					elementSetToRemove.add(elementController);
				}
			}
			
			if(!elementSetToRemove.isEmpty()) {
				this.elementControllerList.removeAll(elementSetToRemove);
				this.updateElementNodePosition();
			}else {
				AlertUtils.popAlert("Warning", "No item selected.");
			}
		} catch (Exception e) {
			ExceptionHandlerUtils.show("ListFeatureEmbeddedController.deleteButtonOnAction", e, this.getStage());
		}
	}
	
	// Event Listener on Button[#selectAllButton].onAction
	@FXML
	public void selectAllButtonOnAction(ActionEvent event) {
		try {
			for(ListFeatureElementController<E> elementController:this.elementControllerList) {
				elementController.setSelected(true);
			}
		} catch (Exception e) {
			ExceptionHandlerUtils.show("ListFeatureEmbeddedController.selectAllButtonOnAction", e, this.getStage());
		}
	}
	
	// Event Listener on Button[#deSelectAllButton].onAction
	@FXML
	public void deSelectAllButtonOnAction(ActionEvent event) {
		try {
			for(ListFeatureElementController<E> elementController:this.elementControllerList) {
				elementController.setSelected(false);
			}
		} catch (Exception e) {
			ExceptionHandlerUtils.show("ListFeatureEmbeddedController.deSelectAllButtonOnAction", e, this.getStage());
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
	 * @throws SQLException 
	 * @throws IOException 
	 */
	// Event Listener on Button[#setToDefaultEmptyButton].onAction
	@FXML @Override
	public void setToDefaultEmptyButtonOnAction(ActionEvent event) throws SQLException, IOException {
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
