package builder.basic.collection.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Function;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.FXUtils;
import utils.Pair;

public class DoubleListSelectorEmbeddedUIContentController<E1,E2> extends LeafNodeBuilderEmbeddedUIContentController<Pair<LinkedHashSet<E1>, ArrayList<E2>>> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/misc/DoubleListSelectorEmbeddedUIContent.fxml";
	
	
	///////////////////////////////
	private Map<E1, E1ElementHBox<E1, E2>> e1ElementHBoxMap; 
	
	
	/**
	 * reset the data table columns to the given ones
	 */
	public void setPools(Collection<E1> e1Pool, Collection<E2> e2Pool) {
		this.e1ElementHBoxMap.clear();
		this.e1poolVBox.getChildren().clear();
		
		
		e1Pool.forEach(e1->{
			E1ElementHBox<E1,E2> elementHBox = 
					new E1ElementHBox<>(e1, e2Pool, 
							this.getOwnerNodeBuilder().getE1ToStringFunction(), this.getOwnerNodeBuilder().getE2ToStringFunction());
			
			e1ElementHBoxMap.put(e1, elementHBox);
			
			this.e1poolVBox.getChildren().add(elementHBox.containerHBox);
		});
		
		
		
	}
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DoubleListSelector<E1,E2> getOwnerNodeBuilder() {
		return (DoubleListSelector<E1,E2>) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	/**
	 * try to build a value;
	 * 
	 * exeption will be thrown if cannot build a valid value with current UI data;
	 */
	@Override
	public Pair<LinkedHashSet<E1>, ArrayList<E2>> build() {
		LinkedHashSet<E1> set = new LinkedHashSet<>();
		
		ArrayList<E2> list = new ArrayList<>();
		
		this.e1ElementHBoxMap.forEach((k,v)->{
			if(v.t1CheckBox.isSelected()) {
				if(v.t2ComboBox.getSelectionModel().getSelectedItem()!=null) {
					set.add(v.t1Value);
					list.add(v.t2ComboBox.getSelectionModel().getSelectedItem());
				}else {
					throw new VisframeException("at least one element not fully selected!");
				}
			}
		});
		
		return new Pair<>(set, list);
	}
	
	
	/**
	 * set all the columns in {@link #entrySelectionHBoxList} to un-selected
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.e1ElementHBoxMap.forEach((k,v)->{
			
			v.setToDefaultEmptyStatus();
			
		});
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * select the columns in the given set;
	 */
	@Override
	public void setUIToNonNullValue(Pair<LinkedHashSet<E1>, ArrayList<E2>> value) {
		this.setUIToDefaultEmptyStatus();
		
		LinkedHashSet<E1> set = value.getFirst();
		ArrayList<E2> list = value.getSecond();
		
		int index = 0;
		for(E1 e1:set) {
			this.e1ElementHBoxMap.get(e1).t1CheckBox.setSelected(true);
			this.e1ElementHBoxMap.get(e1).t2ComboBox.setValue(list.get(index));
			
			index++;
		}
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	@FXML
	public void initialize() {
		this.e1ElementHBoxMap = new LinkedHashMap<>();
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox operationButtonHBox;
	@FXML
	private Button clearButton;
	@FXML
	private VBox e1poolVBox;
	
	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.setUIToDefaultEmptyStatus();
	}
	
	
	//////////////////////////////////////
	private class E1ElementHBox<T1,T2>{
		private final T1 t1Value;
		private final Collection<T2> t2Pool;
		private final Function<T1,String> t1ToStringFunction;
		private final Function<T2,String> t2ToStringFunction;
		
		/////////////
		
		/////////////
		private HBox containerHBox;
		private TextField t1TextField;
		private CheckBox t1CheckBox;
		private ComboBox<T2> t2ComboBox;
		
		E1ElementHBox(T1 t1Value, Collection<T2> t2Pool, 
				Function<T1,String> t1ToStringFunction, 
				Function<T2,String> t2ToStringFunction){
			
			this.t1Value = t1Value;
			this.t2Pool = t2Pool;
			this.t1ToStringFunction = t1ToStringFunction;
			this.t2ToStringFunction = t2ToStringFunction;
			
			//////////////////////////
			this.containerHBox = new HBox();
			this.t1TextField = new TextField(this.t1ToStringFunction.apply(this.t1Value));
			this.t1TextField.setEditable(false);
			
			this.t1CheckBox = new CheckBox();
			
			this.t2ComboBox = new ComboBox<>();
			this.t2ComboBox.getItems().addAll(this.t2Pool);
			
			FXUtils.set2Disable(this.t2ComboBox, true);
			//////////////////////////
			this.containerHBox.getChildren().add(this.t1TextField);
			this.containerHBox.getChildren().add(this.t1CheckBox);
			this.containerHBox.getChildren().add(this.t2ComboBox);
			
			
			this.setEventHandler();
		}

		
		void setToDefaultEmptyStatus() {
			this.t1CheckBox.setSelected(false);
			this.t2ComboBox.setValue(null);
		}
		
		private void setEventHandler() {
			this.t1CheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
				FXUtils.set2Disable(this.t2ComboBox, !this.t1CheckBox.isSelected());
				
				getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
			
			
			//try to update
			this.t2ComboBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
//				try{
//					Pair<LinkedHashSet<E1>, ArrayList<E2>> type = build();
//					getOwnerNodeBuilder().updateNonNullValueFromContentController(type);
//				}catch(Exception ex) {//if any exception is thrown, it indicates that the current UI does contain a valid value for the target property, thus, do not update the non-null value;
//					//skip
//					System.out.println("exception thrown, skip update!");
//				}
				
				getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
			
			
			//this will set up how items are displayed
			this.t2ComboBox.setCellFactory(e->{
				return new ListCell<T2>() {
		            @Override
		            protected void updateItem(T2 value, boolean empty) {
		                super.updateItem(value, empty);
		                if (value == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics for the tree cell here with a specific operationType
		                	Label label = new Label(
		                			t2ToStringFunction.apply(value));
		                    setGraphic(label);
		                }
		            }
		        };
			});
			
			//set up how the selected item of the combobox is displayed
			this.t2ComboBox.setButtonCell(new ListCell<T2>() {
	            @Override
	            protected void updateItem(T2 value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setText("");
	                } else {
	                    setText(t2ToStringFunction.apply(value));
	                }

	            }
	        });
		}
		
	}
}
