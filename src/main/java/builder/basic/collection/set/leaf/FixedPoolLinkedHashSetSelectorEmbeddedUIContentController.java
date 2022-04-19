package builder.basic.collection.set.leaf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class FixedPoolLinkedHashSetSelectorEmbeddedUIContentController<T> extends LeafNodeBuilderEmbeddedUIContentController<LinkedHashSet<T>> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/set/leaf/FixedPoolLinkedHashSetSelectorEmbeddedUIContent.fxml";
	
	private List<EntrySelectionHBox<T>> entrySelectionHBoxList;
//	private List<EntryReorderHBox<T>> selectedEntryReorderHBoxList;
//	private Map<T, Integer> selectedEntryOrderIndexMap;
	
	/**
	 * reset the data table columns to the given ones
	 */
	public void resetPool(Collection<T> entrySet) {
		this.entrySelectionHBoxList.clear();
//		this.selectedEntryReorderHBoxList.clear();
//		this.selectedEntryOrderIndexMap.clear();
		this.entryVBox.getChildren().clear();
		
		this.setUIToDefaultEmptyStatus();
//		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(new LinkedHashSet<>());
//		
		for(T entry:entrySet) {
			
			EntrySelectionHBox<T> entryHBox = new EntrySelectionHBox<>(entry, this.getOwnerNodeBuilder().getElementToStringFunction());
			
			entryHBox.selectionCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
//				if(entryHBox.selectionCheckBox.isSelected()) {
//					EntryReorderHBox<T> selectedEntry = new EntryReorderHBox<>(entry, this.getOwnerNodeBuilder().getElementToStringFunction());
//					
//					
//				}else {
//					
//				}
//				
				//update
//				try{
//					LinkedHashSet<T> value = this.build();
//					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(value);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update!");
//				}
				
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
				
			});
			
			this.entrySelectionHBoxList.add(entryHBox);
			this.entryVBox.getChildren().add(entryHBox.containerHBox);
		}
		
		
		
	}
	
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FixedPoolLinkedHashSetSelector<T> getOwnerNodeBuilder() {
		return (FixedPoolLinkedHashSetSelector<T>) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	@Override
	public LinkedHashSet<T> build() {
		LinkedHashSet<T> ret = new LinkedHashSet<>();
		for(EntrySelectionHBox<T> colHBox:this.entrySelectionHBoxList) {
			if(colHBox.selectionCheckBox.isSelected()) {
				ret.add(colHBox.entry);
			}
		}
		return ret;
	}
	
	
	/**
	 * set all the columns in {@link #entrySelectionHBoxList} to un-selected
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.entrySelectionHBoxList.forEach(e->{
			e.selectionCheckBox.setSelected(false);
		});
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * select the columns in the given set;
	 */
	@Override
	public void setUIToNonNullValue(LinkedHashSet<T> value) {
		
		this.entrySelectionHBoxList.forEach(e->{
			value.forEach(v->{
//				System.out.println(v.equals(e.entry));
			});
			
			if(value.contains(e.entry)) {
				e.selectionCheckBox.setSelected(true);
			}else {
				e.selectionCheckBox.setSelected(false);
			}
		});
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		this.entrySelectionHBoxList = new ArrayList<>();
//		this.selectedEntryReorderHBoxList = new ArrayList<>();
//		this.selectedEntryOrderIndexMap = new HashMap<>();
	}
	
	@FXML
	private VBox contentVBox;
	@FXML
	private VBox entryVBox;
	@FXML
	private HBox operationButtHBox;
	@FXML
	private Button clearButton;
	@FXML
	private Button selectAllButton;

	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.entrySelectionHBoxList.forEach(e->{
			e.selectionCheckBox.setSelected(false);
		});
	}
	// Event Listener on Button[#selectAllButton].onAction
	@FXML
	public void selectAllButtonOnAction(ActionEvent event) {
		this.entrySelectionHBoxList.forEach(e->{
			e.selectionCheckBox.setSelected(true);
		});
	}
	
	//////////////////////////
	private class EntrySelectionHBox<E>{
		private final HBox containerHBox;
		private final CheckBox selectionCheckBox;
		private final TextField entryTextField;
		private final E entry;
		private final Function<E,String> toStringFunction;
		
		/**
		 * constructor
		 * @param entry
		 * @param elementToStringFunction
		 */
		EntrySelectionHBox(E entry, Function<E,String> elementToStringFunction){
			this.entry = entry;
			this.toStringFunction = elementToStringFunction;
			
			/////
			this.containerHBox = new HBox();
			
			this.selectionCheckBox = new CheckBox();
			
			this.entryTextField = new TextField(this.toStringFunction.apply(entry));
			this.entryTextField.setEditable(false);
			
			this.containerHBox.getChildren().add(this.selectionCheckBox);
			this.containerHBox.getChildren().add(this.entryTextField);
		}
	}
	
	
	
//	private class EntryReorderHBox<E>{
//		private final HBox containerHBox;
//		private final Button moveUpButton;
//		private final Button moveDownButton;
//		private final TextField entryTextField;
//		
//		private final E entry;
//		private final Function<E,String> toStringFunction;
//		
//		EntryReorderHBox(E entry, Function<E,String> elementToStringFunction){
//			
//			this.entry = entry;
//			this.toStringFunction = elementToStringFunction;
//			////
//			this.containerHBox = new HBox();
//			this.moveUpButton = new Button("Move up");
//			this.moveDownButton = new Button("Move down");
//			
//			this.entryTextField = new TextField(this.toStringFunction.apply(entry));
//			this.entryTextField.setEditable(false);
//			
//			this.containerHBox.getChildren().add(this.entryTextField);
//			this.containerHBox.getChildren().add(this.moveUpButton);
//			this.containerHBox.getChildren().add(this.moveDownButton);
//		}
//	}
}
