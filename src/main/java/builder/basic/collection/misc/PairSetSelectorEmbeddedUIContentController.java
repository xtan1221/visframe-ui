package builder.basic.collection.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import utils.AlertUtils;
import utils.Pair;

public class PairSetSelectorEmbeddedUIContentController<T, R> extends LeafNodeBuilderEmbeddedUIContentController<Pair<LinkedHashSet<R>,LinkedHashSet<R>>> {
	public static final String FXML_FILE_DIR_STRING = "/builder/basic/collection/misc/PairSetSelectorEmbeddedUIContent.fxml";
	
	////////////////////////
	private List<PairHBox<T>> currentPairHBoxList;
	private Set<T> pool;
	private Map<R,T> returnedTypeInputTypeMap;
	
	/**
	 * reset the data table columns to the given ones
	 */
	public void setPool(Collection<T> pool) {
		this.currentPairHBoxList.clear();
		this.returnedTypeInputTypeMap.clear();
		this.pool.clear();
		
		this.setUIToDefaultEmptyStatus();
		
		pool.forEach(e->{
			if(this.returnedTypeInputTypeMap.containsKey(this.getOwnerNodeBuilder().getReturnedTypeFunction())) {
				throw new IllegalArgumentException("multiple input type values have the same returned type value!");
			}
			this.returnedTypeInputTypeMap.put(this.getOwnerNodeBuilder().getReturnedTypeFunction().apply(e), e);
			
			this.pool.add(e);
		});
		
		
//		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(new Pair<>(new LinkedHashSet<>(), new LinkedHashSet<>()));
	}
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PairSetSelector<T, R> getOwnerNodeBuilder() {
		return (PairSetSelector<T, R>) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	/**
	 * only include pairs with both elements selected
	 */
	@Override
	public Pair<LinkedHashSet<R>,LinkedHashSet<R>> build() {
		LinkedHashSet<R> first = new LinkedHashSet<>();
		LinkedHashSet<R> second = new LinkedHashSet<>();
		
		//
		this.currentPairHBoxList.forEach(e->{
			if(e.bothAreSelected()) {//skip entries with at least one not selected
				R selectedFirst = this.getOwnerNodeBuilder().getReturnedTypeFunction().apply(e.first);
				R selectedSecond = this.getOwnerNodeBuilder().getReturnedTypeFunction().apply(e.second);
				
				//check duplicate
				if(first.contains(selectedFirst) || first.contains(selectedSecond) || second.contains(selectedFirst) || second.contains(selectedSecond)) {
					throw new VisframeException("duplicate selected element found!");
				}
				
				first.add(selectedFirst);
				second.add(selectedSecond);
			}else {
				throw new VisframeException("at least one entry has one or two item not selected!");//TODO?????
			}
		});
		
		return new Pair<>(first, second);
	}
	
	
	/**
	 * set all the columns in {@link #entrySelectionHBoxList} to un-selected
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.currentPairHBoxList.clear();
		this.pairVBox.getChildren().clear();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * select the columns in the given set;
	 */
	@Override
	public void setUIToNonNullValue(Pair<LinkedHashSet<R>,LinkedHashSet<R>> value) {
		this.setUIToDefaultEmptyStatus();
		
		Iterator<R> firstIterator = value.getFirst().iterator();
		Iterator<R> secondIterator = value.getSecond().iterator();
		
		while(firstIterator.hasNext()) {
			this.addPair(
					this.returnedTypeInputTypeMap.get(firstIterator.next()), 
					this.returnedTypeInputTypeMap.get(secondIterator.next()));
		}
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	/**
	 * 
	 * @param first
	 * @param second
	 */
	private void addPair(T first, T second) {
		
		if((first!=null&&!this.pool.contains(first)))
			throw new IllegalArgumentException("given values are not found in the pool:"+first.toString());
		if((second!=null&&!this.pool.contains(second)))
			throw new IllegalArgumentException("given values are not found in the pool:"+second.toString());
		
		PairHBox<T> pairHBox = new PairHBox<>(this.getOwnerNodeBuilder().getToStringFunction());
		pairHBox.firstComboBox.getItems().addAll(this.pool);
		pairHBox.secondComboBox.getItems().addAll(this.pool);
		
		//
		pairHBox.firstComboBox.getSelectionModel().selectedItemProperty().addListener((oo, oldValue, newValue)->{
			
			pairHBox.first = newValue;
			
			if(pairHBox.first!=null) {
				//check duplicate
				if(!duplicateSelectedValues().isEmpty()) {
					AlertUtils.popAlert("Warning", "duplicate values are selected:"+duplicateSelectedValues());
					
					pairHBox.firstComboBox.setValue(oldValue);
					
					return;
				}
				
				//
				if(pairHBox.bothAreSelected()) {
					Pair<T,T> pair = new Pair<>(pairHBox.firstComboBox.getSelectionModel().getSelectedItem(),
							pairHBox.secondComboBox.getSelectionModel().getSelectedItem());
					
					//check constraints
					if(!this.getOwnerNodeBuilder().getPairConstraint().apply(pair)) {
						//show alert
						
						AlertUtils.popAlert("Warning", this.getOwnerNodeBuilder().getPairConstraintDescription());
						
						//reset to null
						pairHBox.firstComboBox.setValue(oldValue);
						
					}else {//both are selected and obey the constraints, try to build and update
//						try {
//							this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//						}catch(VisframeException ex) {
//							AlertUtils.popAlert("Warning", ex.getMessage());
//							
//							//reset to null
//							pairHBox.firstComboBox.setValue(oldValue);
//						}
						this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
					}
				}
				
			}
			
		});
		
		pairHBox.firstComboBox.setValue(first);
		
		////////////////////////
		pairHBox.secondComboBox.getSelectionModel().selectedItemProperty().addListener((oo, oldValue, newValue)->{
			
			pairHBox.second = newValue;
			
			if(pairHBox.second!=null) {
				//check duplicate
				if(!duplicateSelectedValues().isEmpty()) {
					AlertUtils.popAlert("Warning", "duplicate values are selected:"+duplicateSelectedValues());
				
					pairHBox.secondComboBox.setValue(oldValue);
					return;
				}
				
				if(pairHBox.bothAreSelected()) {
					Pair<T,T> pair = new Pair<>(pairHBox.firstComboBox.getSelectionModel().getSelectedItem(),
							pairHBox.secondComboBox.getSelectionModel().getSelectedItem());
					
					if(!this.getOwnerNodeBuilder().getPairConstraint().apply(pair)) {
						//show alert
						
						AlertUtils.popAlert("Warning", this.getOwnerNodeBuilder().getPairConstraintDescription());
						
						//reset to null
						pairHBox.secondComboBox.setValue(oldValue);
					}else {//both are selected and obey the constraints, update
//						try {
//							this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//						}catch(VisframeException ex) {
//							AlertUtils.popAlert("Warning", ex.getMessage());
//							
//							//reset to null
//							pairHBox.secondComboBox.setValue(oldValue);
//						}
						this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
					}
				}
			}
		});
		
		pairHBox.secondComboBox.setValue(second);
		
		
		//
		pairHBox.removeButton.setOnAction(e->{
			this.pairVBox.getChildren().remove(pairHBox.containerHBox);
			this.currentPairHBoxList.remove(pairHBox);
			
//			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		///add
		this.pairVBox.getChildren().add(pairHBox.containerHBox);
		this.currentPairHBoxList.add(pairHBox);
	}
	
	/**
	 * find out all duplicate elements that are selected
	 * @return
	 */
	private Set<T> duplicateSelectedValues(){
		Set<T> selectedValues = new HashSet<>();
		
		Set<T> duplicateValues = new HashSet<>();
		
		this.currentPairHBoxList.forEach(e->{
			if(e.first!=null) {
				if(selectedValues.contains(e.first)) {
					duplicateValues.add(e.first);
				}else {
					selectedValues.add(e.first);
				}
			}
			
			if(e.second!=null) {
				if(selectedValues.contains(e.second)) {
					duplicateValues.add(e.second);
				}else {
					selectedValues.add(e.second);
				}
			}
		});
		
		return duplicateValues;
	}
	
	//////////////////////////
	@FXML
	public void initialize() {
		this.pool = new LinkedHashSet<>();
		currentPairHBoxList = new ArrayList<>();
		this.returnedTypeInputTypeMap = new HashMap<>();
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private VBox pairVBox;
	@FXML
	private HBox operationButtHBox;
	@FXML
	private Button clearButton;
	@FXML
	private Button addButton;

	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.setUIToDefaultEmptyStatus();
		
		////update
//		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
	}
	// Event Listener on Button[#addButton].onAction
	@FXML
	public void addButtonOnAction(ActionEvent event) {
		if(this.pool==null||this.pool.isEmpty()) {
			AlertUtils.popAlert("Warning", "no pool to select from!");
			return;
		}
		this.addPair(null, null);
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	/////////////////////////////////
	private class PairHBox<E>{
		
		private final HBox containerHBox;
		private final Button removeButton;
		private final ComboBox<E> firstComboBox;
		private final ComboBox<E> secondComboBox;
		private final Function<E,String> toStringFunction;
		
		private E first;
		private E second;
		
		PairHBox(Function<E,String> toStringFunction){
			this.toStringFunction = toStringFunction;
			
			/////////////
			this.containerHBox = new HBox();
			this.removeButton = new Button("remove");
			this.firstComboBox = new ComboBox<>();
			this.secondComboBox = new ComboBox<>();
			
			this.containerHBox.getChildren().add(removeButton);
			this.containerHBox.getChildren().add(firstComboBox);
			this.containerHBox.getChildren().add(secondComboBox);
			
			
			///set up how the items of combobox are displayed
			this.firstComboBox.setCellFactory(e->{
				return new ListCell<E>() {
		            @Override
		            protected void updateItem(E value, boolean empty) {
		                super.updateItem(value, empty);
		                if (value == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics
		                    setGraphic(new Label(PairHBox.this.toStringFunction.apply(value)));
//		                	setGraphic(new Label("dfsdfdf"));
		                }
		            }
		        };
			});
			
			//set up how the selected item of the combobox is displayed
			this.firstComboBox.setButtonCell(new ListCell<E>() {
	            @Override
	            protected void updateItem(E value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setText("");
	                } else {
	                    setText(PairHBox.this.toStringFunction.apply(value));
	                }

	            }
	        });
			
			///
			this.secondComboBox.setCellFactory(e->{
				return new ListCell<E>() {
		            @Override
		            protected void updateItem(E value, boolean empty) {
		                super.updateItem(value, empty);
		                if (value == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics
		                    setGraphic(new Text(PairHBox.this.toStringFunction.apply(value)));
		                }
		            }
		        };
			});
			//set up how the selected item of the combobox is displayed
			this.secondComboBox.setButtonCell(new ListCell<E>() {
	            @Override
	            protected void updateItem(E value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setText("");
	                } else {
	                    setText(PairHBox.this.toStringFunction.apply(value));
	                }

	            }
	        });
		}
		
		boolean bothAreSelected() {
			return first!=null && second!=null;
		}
		
	}
	
}
