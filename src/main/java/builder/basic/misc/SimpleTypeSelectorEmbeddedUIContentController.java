package builder.basic.misc;

import java.util.Collection;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

public class SimpleTypeSelectorEmbeddedUIContentController<T> extends LeafNodeBuilderEmbeddedUIContentController<T> {
	public static final String FXML_FILE_DIR = "/builder/basic/misc/SimpleTypeSelectorEmbeddedUIContent.fxml";
	
	/**
	 * first clear current pool;
	 * 
	 * then 
	 * @param pool
	 */
	public void setPool(Collection<T> pool) {
		this.setUIToDefaultEmptyStatus();
		this.selectionComboBox.getItems().clear();
		this.selectionComboBox.setValue(null);
		this.getOwnerNodeBuilder().getPool().forEach(e->{
			this.selectionComboBox.getItems().add(e);
		});
	}
	
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		selectionComboBox.setConverter(new StringConverter<T>(){

	        @Override
	        public String toString(T object) {
	            return object == null ? null : SimpleTypeSelectorEmbeddedUIContentController.this.getOwnerNodeBuilder().getTypeToStringRepresentationFunction().apply(object);
	        }
	        
	        @Override
	        public T fromString(String string) {
	            return null; //TODO? validate
	        }

	    });
		//set the combo box item to show the string representation of each type item
		selectionComboBox.setCellFactory(e->{
			return new ListCell<T>() {
	            @Override
	            protected void updateItem(T value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setGraphic(null);
	                } else {
	                	//set the Graphics for the tree cell here with a specific operationType
//	                	Label label = new Label(
//	                			SimpleTypeSelectorEmbeddedUIContentController.this.getOwnerNodeBuilder().getTypeToStringRepresentationFunction().apply(value));
//	                	
//	                	Tooltip tp = new Tooltip("at stack tool");
//	                	Tooltip.install(label, tp);
	                	
	                	setText(SimpleTypeSelectorEmbeddedUIContentController.this.getOwnerNodeBuilder().getTypeToStringRepresentationFunction().apply(value));
	                	
//	                    setGraphic(label);
	                }
	            }
	        };
		});
		
		//link combo box and text field
		selectionComboBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
//			System.out.println("selection changed");
			if(this.selectionComboBox.getSelectionModel().getSelectedItem()==null) {
				selectionComboBox.setTooltip(null);
				
			}else {
				Tooltip tp = new Tooltip(this.getOwnerNodeBuilder().getTypeToDescriptionFunction().apply(this.selectionComboBox.getSelectionModel().getSelectedItem()));
            	selectionComboBox.setTooltip(tp);
            	
			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public SimpleTypeSelector<T> getOwnerNodeBuilder() {
		return (SimpleTypeSelector<T>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentHBox;
	}
	
	@Override
	public T build() {
		T value = this.selectionComboBox.getSelectionModel().getSelectedItem();
		
		if(value==null) {
			throw new VisframeException("no item is selected!");
		}
		
		return value;
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.selectionComboBox.setValue(null);
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(T value) {
		this.selectionComboBox.setValue(value);
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////

	@FXML
	public void initialize() {
		//TODO
		
	}
	
	
	@FXML
	private HBox contentHBox;
	@FXML
	private ComboBox<T> selectionComboBox;
	
	
}
