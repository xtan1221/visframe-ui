package builder.basic.collection.misc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.AlertUtils;
import utils.Pair;

public class DoubleMapValueSelectorEmbeddedUIContentController<K1,V1,K2,V2> extends LeafNodeBuilderEmbeddedUIContentController<Pair<LinkedHashMap<K2,V2>, LinkedHashMap<K2,V2>>> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/misc/DoubleMapValueSelectorEmbeddedUIContent.fxml";
	
	////////////////////////
	private MapVBox<K1,V1> firstMapVBox;
	private MapVBox<K1,V1> secondMapVBox;
	
	
	private Set<K1> predefinedMapKeySet;
	private Set<V1> selectionMapValuePoolSet;
	
	private Map<K2,K1> returnedTypeInputTypeMapKeyMap;
	private Map<V2,V1> returnedTypeInputTypeMapValueMap;
	
	/**
	 * reset the data table columns to the given ones
	 */
	public void setPool(Collection<K1> predefinedMapKeys, Collection<V1> selectionMapValuePool) {
		this.mapHBox.getChildren().clear();
		
		//
		this.predefinedMapKeySet.clear();
		this.selectionMapValuePoolSet.clear();
		this.returnedTypeInputTypeMapKeyMap.clear();
		this.returnedTypeInputTypeMapValueMap.clear();
		
		
		predefinedMapKeys.forEach(e->{
			if(this.returnedTypeInputTypeMapKeyMap.containsKey(this.getOwnerNodeBuilder().getMapKeyFunction().apply(e))) {
				throw new IllegalArgumentException("multiple input map keys have the same returned type value!");
			}
			
			this.returnedTypeInputTypeMapKeyMap.put(this.getOwnerNodeBuilder().getMapKeyFunction().apply(e), e);
			
			this.predefinedMapKeySet.add(e);
		});
		
		selectionMapValuePool.forEach(e->{
			if(this.returnedTypeInputTypeMapValueMap.containsKey(this.getOwnerNodeBuilder().getMapValueFunction().apply(e))) {
				throw new IllegalArgumentException("multiple input map values have the same returned type value!");
			}
			
			this.returnedTypeInputTypeMapValueMap.put(this.getOwnerNodeBuilder().getMapValueFunction().apply(e), e);
			
			this.selectionMapValuePoolSet.add(e);
		});
		
		
		if(this.predefinedMapKeySet.size()>this.selectionMapValuePoolSet.size()) {
			throw new IllegalArgumentException("given map key pool size is larger then map value pool size!");
		}
		
		
		///////////////
		this.firstMapVBox = new MapVBox<>(predefinedMapKeySet, selectionMapValuePoolSet, 
				this.getOwnerNodeBuilder().getInputMapKeyToStringFunction(), this.getOwnerNodeBuilder().getInputMapValueToStringFunction());
		this.secondMapVBox = new MapVBox<>(predefinedMapKeySet, selectionMapValuePoolSet, 
				this.getOwnerNodeBuilder().getInputMapKeyToStringFunction(), this.getOwnerNodeBuilder().getInputMapValueToStringFunction());
		
		/////set up the event listener to the change of selected item of each combobox of each map entry
		this.firstMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
			v.valueComboBox.getSelectionModel().selectedItemProperty().addListener((oo, oldValue, newValue)->{
				v.value = newValue;
				//only when a valid value for all values of both maps are selected, the {@link Status} of owner {@link DoubleMapValueSelector} will be updated!
				
				if(v.value!=null) {
					//check constraints
					if(!this.getOwnerNodeBuilder().getMapKeyValueConstraint().test(new Pair<>(v.key, v.value))) {
						AlertUtils.popAlert("Warning", this.getOwnerNodeBuilder().getMapKeyValueConstraintDescription());
						v.valueComboBox.setValue(oldValue);
						return;
					}
					
					//check duplicate
					if(!this.getSelectedDuplicateMapValues().isEmpty()) {
						AlertUtils.popAlert("Warning", "duplicate selected element is found:"+this.getSelectedDuplicateMapValues());
						
						v.valueComboBox.setValue(oldValue);
						return;
					}
					
					//try to update if all map values are selected
//					if(allMapValuesAreSelected()) {
//						try {
//							this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//						}catch(VisframeException ex) {
//							AlertUtils.popAlert("Warning", ex.getMessage());
//							v.valueComboBox.setValue(oldValue);
//							
//						}
//					}
					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
					
				}
			});
		});
		
		this.secondMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
			v.valueComboBox.getSelectionModel().selectedItemProperty().addListener((oo, oldValue, newValue)->{
				v.value = newValue;
				
				if(v.value!=null) {
					//check constraints
					if(!this.getOwnerNodeBuilder().getMapKeyValueConstraint().test(new Pair<>(v.key, v.value))) {
						AlertUtils.popAlert("Warning", this.getOwnerNodeBuilder().getMapKeyValueConstraintDescription());
						v.valueComboBox.setValue(oldValue);
						return;
					}
					
					//check duplicate
					if(!this.getSelectedDuplicateMapValues().isEmpty()) {
						AlertUtils.popAlert("Warning", "duplicate selected element is found:"+this.getSelectedDuplicateMapValues());
						
						v.valueComboBox.setValue(oldValue);
						return;
					}
					
					//try to update if all map values are selected
					if(allMapValuesAreSelected()) {
//						try {
//							this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//						}catch(VisframeException ex) {
//							AlertUtils.popAlert("Warning", ex.getMessage());
//							v.valueComboBox.setValue(oldValue);
//						}
						this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
					}
				}
			});
		});
		
		this.mapHBox.getChildren().add(this.firstMapVBox.containerHBox);
		this.mapHBox.getChildren().add(this.secondMapVBox.containerHBox);
		
		
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean allMapValuesAreSelected() {
		for(MapEntryHBox<K1, V1> entry:this.firstMapVBox.mapKeyEntryHBoxMap.values()) {
			if(entry.value==null) {
				return false;
			}
		}
		for(MapEntryHBox<K1, V1> entry:this.secondMapVBox.mapKeyEntryHBoxMap.values()) {
			if(entry.value==null) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * TODO debug
	 * 
	 * when a map value is changed from null to a non-null value and it is duplicate with another map value, a 
	 * java.lang.IndexOutOfBoundsException: [ fromIndex: 0, toIndex: 1, size: 0 ] will be thrown???
	 * 
	 * fixed this;
	 * 
	 * 
	 * @return
	 */
	private Set<V1> getSelectedDuplicateMapValues(){
		Set<V1> selectedMapValues = new HashSet<>();
		
		Set<V1> duplicateSelectedMapValues = new HashSet<>();
		
		
			this.firstMapVBox.mapKeyEntryHBoxMap.forEach((k,e)->{
				if(e.value!=null) {
					if(selectedMapValues.contains(e.value)) {
						duplicateSelectedMapValues.add(e.value);
					}else {
						selectedMapValues.add(e.value);
					}
				}
			});
			
	//		for(K1 key:this.firstMapVBox.mapKeyEntryHBoxMap.keySet()) {
	//			V1 v = this.firstMapVBox.mapKeyEntryHBoxMap.get(key).value;
	//			if(v!=null) {
	//				if(selectedMapValues.contains(v)) {
	//					duplicateSelectedMapValues.add(v);
	//				}else {
	//					selectedMapValues.add(v);
	//				}
	//			}
	//		}
		
			this.secondMapVBox.mapKeyEntryHBoxMap.forEach((k,e)->{
				if(e.value!=null) {
					if(selectedMapValues.contains(e.value)) {
						duplicateSelectedMapValues.add(e.value);
					}else {
						selectedMapValues.add(e.value);
					}
				}
			});
	//		for(K1 key:this.secondMapVBox.mapKeyEntryHBoxMap.keySet()) {
	//			V1 v = this.secondMapVBox.mapKeyEntryHBoxMap.get(key).value;
	//			if(v!=null) {
	//				if(selectedMapValues.contains(v)) {
	//					duplicateSelectedMapValues.add(v);
	//				}else {
	//					selectedMapValues.add(v);
	//				}
	//			}
	//		}
			
			
		
		return duplicateSelectedMapValues;
	}
	
	
	
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DoubleMapValueSelector<K1, V1, K2, V2> getOwnerNodeBuilder() {
		return (DoubleMapValueSelector<K1, V1, K2, V2>) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	/**
	 * if
	 * 1. there is at least one map entry with value not selected for any of the two maps, throw exception;
	 * 2. there is any map entry that violate the entry constraints, throw exception;
	 * 3. there are any duplicate map values selected within or between the two maps
	 * 
	 * 
	 */
	@Override
	public Pair<LinkedHashMap<K2,V2>, LinkedHashMap<K2,V2>> build() {
		LinkedHashMap<K2,V2> first = new LinkedHashMap<>();
		LinkedHashMap<K2,V2> second = new LinkedHashMap<>();
		
		Set<V1> selectedMapValueSet = new HashSet<>();
		this.firstMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
			if(v.value==null) {
				throw new VisframeException("at least one map value is not selected!");
			}
			if(!this.getOwnerNodeBuilder().getMapKeyValueConstraint().test(new Pair<>(v.key, v.value))) {//
				throw new VisframeException(this.getOwnerNodeBuilder().getMapKeyValueConstraintDescription());
			}
			
			if(selectedMapValueSet.contains(v.value)) {
				throw new VisframeException("map value is found to be selected multiple times:"+this.getOwnerNodeBuilder().getInputMapValueToStringFunction().apply(v.value));
			}
			selectedMapValueSet.add(v.value);
			
			
			first.put(
					this.getOwnerNodeBuilder().getMapKeyFunction().apply(v.key), 
					this.getOwnerNodeBuilder().getMapValueFunction().apply(v.value));
		});
		
		this.secondMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
			if(v.value==null) {
				throw new VisframeException("at least one map value is not selected!");
			}
			if(!this.getOwnerNodeBuilder().getMapKeyValueConstraint().test(new Pair<>(v.key, v.value))) {//
				throw new VisframeException(this.getOwnerNodeBuilder().getMapKeyValueConstraintDescription());
			}
			
			if(selectedMapValueSet.contains(v.value)) {
				throw new VisframeException("map value is found to be selected multiple times:"+this.getOwnerNodeBuilder().getInputMapValueToStringFunction().apply(v.value));
			}
			selectedMapValueSet.add(v.value);
			
			
			second.put(
					this.getOwnerNodeBuilder().getMapKeyFunction().apply(v.key), 
					this.getOwnerNodeBuilder().getMapValueFunction().apply(v.value));
		});
		
		return new Pair<>(first, second);
	}
	
	
	/**
	 * set the map values to null for the two MapVBox
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		if(this.firstMapVBox!=null) {
			this.firstMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
				v.valueComboBox.setValue(null);
			});
		}
		if(this.secondMapVBox!=null) {
			this.secondMapVBox.mapKeyEntryHBoxMap.forEach((k,v)->{
				v.valueComboBox.setValue(null);
			});
		}
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * ;
	 */
	@Override
	public void setUIToNonNullValue(Pair<LinkedHashMap<K2,V2>, LinkedHashMap<K2,V2>> value) {
		this.setUIToDefaultEmptyStatus();
		
		value.getFirst().forEach((k,v)->{
			this.firstMapVBox.mapKeyEntryHBoxMap.get(this.returnedTypeInputTypeMapKeyMap.get(k)).valueComboBox.setValue(this.returnedTypeInputTypeMapValueMap.get(v));
		});
		
		value.getSecond().forEach((k,v)->{
			this.secondMapVBox.mapKeyEntryHBoxMap.get(this.returnedTypeInputTypeMapKeyMap.get(k)).valueComboBox.setValue(this.returnedTypeInputTypeMapValueMap.get(v));
		});
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	
	//////////////////////////
	@FXML
	public void initialize() {
		this.predefinedMapKeySet = new LinkedHashSet<>();
		this.selectionMapValuePoolSet = new LinkedHashSet<>();
		
		this.returnedTypeInputTypeMapKeyMap = new HashMap<>();
		this.returnedTypeInputTypeMapValueMap = new HashMap<>();
		
		
		
	}
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox operationButtHBox;
	@FXML
	private Button clearButton;
	@FXML
	private HBox mapHBox;
	
	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.setUIToDefaultEmptyStatus();
	}
	
	
	/////////////////////////////////
	private class MapVBox<K,V>{
		
		private final VBox containerHBox;
		
		private final Map<K,MapEntryHBox<K,V>> mapKeyEntryHBoxMap;
		
		MapVBox(Set<K> mapKeys, Set<V> valuePool, Function<K,String> mapKeyToStringFunction, Function<V,String> mapValueToStringFunction){
			
			this.containerHBox = new VBox();
			this.mapKeyEntryHBoxMap = new HashMap<>();
			
			mapKeys.forEach(e->{
				MapEntryHBox<K,V> entry = new MapEntryHBox<>(e, valuePool, mapKeyToStringFunction, mapValueToStringFunction);
				
				containerHBox.getChildren().add(entry.containerHBox);
				
				mapKeyEntryHBoxMap.put(e, entry);
			});
			
		}
	}
	
	private class MapEntryHBox<K,V>{
		private final K key;
		private final HBox containerHBox;
		private final TextField keyTextField;
		private final ComboBox<V> valueComboBox;
		
		/////
		private V value = null;
		
		MapEntryHBox(K key, Set<V> valuePool, Function<K,String> mapKeyToStringFunction, Function<V,String> mapValueToStringFunction){
			this.key = key;
			
			this.containerHBox = new HBox();
			///
			this.keyTextField = new TextField();
			this.keyTextField.setText(mapKeyToStringFunction.apply(this.key));
			this.keyTextField.setMinWidth(100d);
			this.keyTextField.setEditable(false);
//			this.keyTextField.setPrefWidth(Control.USE_COMPUTED_SIZE);
//			this.keyTextField.setMinWidth(Control.USE_COMPUTED_SIZE);
			////
			this.valueComboBox = new ComboBox<>();
			
			this.valueComboBox.getItems().addAll(valuePool);
			
			//this will set up how items are displayed
			this.valueComboBox.setCellFactory(e->{
				return new ListCell<V>() {
		            @Override
		            protected void updateItem(V value, boolean empty) {
		                super.updateItem(value, empty);
		                if (value == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics for the tree cell here with a specific operationType
		                	Label label = new Label(
		                			mapValueToStringFunction.apply(value));
		                    setGraphic(label);
		                }
		            }
		        };
			});
			
			//set up how the selected item of the combobox is displayed
			this.valueComboBox.setButtonCell(new ListCell<V>() {
	            @Override
	            protected void updateItem(V value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setText("");
	                } else {
	                    setText(mapValueToStringFunction.apply(value));
	                }

	            }
	        });
			
			
			///
			this.containerHBox.getChildren().add(keyTextField);
			this.containerHBox.getChildren().add(valueComboBox);
		}
		
	
		
	}
	
}
