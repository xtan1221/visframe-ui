package builder.basic.collection.list2;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import builder.basic.collection.list2.element.FixedSizeArrayListElementController;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import core.builder.ui.integrative.NonLeafNodeBuilderIntegrativeUIController;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



/**
 * 
 * @author tanxu
 *
 * @param <K>
 * @param <V>
 */
public class FixedSizeArrayListNonLeafNodeBuilderIntegrativeUIController<E> extends NonLeafNodeBuilderIntegrativeUIController {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/list2/FixedSizeArrayListNonLeafNodeBuilderIntegrativeUI.fxml";
	
	private List<FixedSizeArrayListElementController<E>> elementControllerList;
	private List<Parent> elementParentNodeList;
	
	/**
	 * @return the elementControllerList
	 */
	public List<FixedSizeArrayListElementController<E>> getElementControllerList() {
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
	public FixedSizeArrayListNonLeafNodeBuilder<E> getOwnerNodeBuilder() {
		return (FixedSizeArrayListNonLeafNodeBuilder<E>) ownerNonLeafNodeBuilder;
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
		
		for(FixedSizeArrayListElementController<E> controller:this.elementControllerList){
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
	
	/**
	 * set the child list element builders (with default empty value and with the current size in owner FixedSizeArrayListNonLeafNodeBuilder) and update the UI;
	 * 
	 * @throws IOException
	 * @throws SQLException 
	 */
	void resetChildrenListElementBuilders() throws IOException, SQLException {
		this.removeAllElements();
		
		for(int index = 0;index<this.getOwnerNodeBuilder().getSize();index++) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(FixedSizeArrayListElementController.FXML_FILE_DIR_STRING));
			
	    	//this must be invoked before the getController() method!!!!!!!
			loader.load();
	    	@SuppressWarnings("unchecked")
	    	FixedSizeArrayListElementController<E> controller = (FixedSizeArrayListElementController<E>)loader.getController();
	    	
	    	NodeBuilder<E,?> nb = this.getOwnerNodeBuilder().getListElementNodeBuilderFactory().build();
	    	controller.setBuilder(
	    			nb, this.getOwnerNodeBuilder()
			    );
	    	
	    	//first add the controller to the elementControllerList before setting the value of the element builder;
	    	this.elementControllerList.add(controller);
	    	this.elementParentNodeList.add(controller.getRootPane());
	    	
	    	//
	    	this.elementVBox.getChildren().add(controller.getRootPane());
	    	
	    	
	    	//this will trigger status change event actions of the ArrayListNonLeafNodeBuilder, 
	    	//which will make use of the elementControllerList; 
	    	//thus must add the controller to the elementControllerList first before setting the value!
	    	controller.getElementBuilder().setToDefaultEmpty();
	    	
	    	
	    	controller.setIndex(index);
		}
	}
	
	/**
	 * set the entries to the given map;
	 * 
	 * @param map
	 * @throws SQLException 
	 * @throws IOException
	 */
	void setValue(ArrayList<E> list) throws SQLException, IOException {
		if(list.size()!=this.getOwnerNodeBuilder().getSize())
			throw new VisframeException("size of given list is not correct!");
		
		this.resetChildrenListElementBuilders();
		
		int index = 0;
		for(E e:list) {
			if(e==null) {
				this.elementControllerList.get(index).getElementBuilder().setToDefaultEmpty();
	    	}else {
	    		this.elementControllerList.get(index).getElementBuilder().setValue(e, false); 
	    	}
			index++;
		}
	}

	
	void removeAllElements() {
		this.elementControllerList.clear();
		this.elementParentNodeList.clear();
		this.elementVBox.getChildren().clear();
	}
	
	////////////////////////////////////////
	
	
	public void moveDown(FixedSizeArrayListElementController<E> elementController) throws IOException {
		if(this.elementControllerList.indexOf(elementController)==this.elementControllerList.size()-1) {
			return;
		}
		
		this.exchangeIndex(this.elementControllerList.indexOf(elementController), this.elementControllerList.indexOf(elementController)+1);
		
		this.updateElementNodePosition();
	}
	
	
	public void moveUp(FixedSizeArrayListElementController<E> elementController) throws IOException {
		if(this.elementControllerList.indexOf(elementController)==0) {
			return;
		}
		
		this.exchangeIndex(this.elementControllerList.indexOf(elementController), this.elementControllerList.indexOf(elementController)-1);
		
		this.updateElementNodePosition();
	}
	
	public void remove(FixedSizeArrayListElementController<E> elementController) throws IOException {
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
		for(FixedSizeArrayListElementController<E> controller:this.elementControllerList) {
			this.elementVBox.getChildren().add(this.elementParentNodeList.get(i));
			controller.setIndex(i);
			i++;
		}
	}
	
	/////////////////////////////////////////
	/**
	 * any initialization not based on {@link #ownerNonLeafNodeBuilder}
	 */
	@FXML @Override
	public void initialize() {
		this.elementControllerList = new ArrayList<>();
		this.elementParentNodeList = new ArrayList<>();
	}
	
	
	@FXML
	private VBox containerVBox;
	@FXML
	private VBox contentVBox;
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
	@FXML @Override
	public void finishButtonOnAction(ActionEvent event) throws SQLException, IOException {
		super.finishButtonOnAction(event);
	}
	
	/**
	 * need to remove all entries
	 * @throws SQLException 
	 * @throws IOException 
	 */
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
