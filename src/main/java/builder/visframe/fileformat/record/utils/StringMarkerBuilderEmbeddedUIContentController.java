package builder.visframe.fileformat.record.utils;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import fileformat.record.utils.PlainStringMarker;
import fileformat.record.utils.RegexStringMarker;
import fileformat.record.utils.StringMarker;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class StringMarkerBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<StringMarker>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/fileformat/record/utils/StringMarkerBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.isCaseSensitiveCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
//			if(this.typeChoiceBox.getSelectionModel().getSelectedItem() == null) {//no type is selected, do not set the value yet
//				//
//			}else {
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		/////////////////////
		this.typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
//			if(this.typeChoiceBox.getSelectionModel().getSelectedItem()==null) {//no item is selected, do not trigger setting value
//				//
//			}else {
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//			}
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		//////////////////////////
		this.stringValueTextField.setOnKeyPressed(e->{
			if(e.getCode().equals(KeyCode.ENTER)) {
				this.stringValueTextField.getParent().requestFocus();
			}
		});
		
		this.stringValueTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.stringValueTextField.isFocused()) {
				
//				if(this.typeChoiceBox.getSelectionModel().getSelectedItem() == null) {//no type is selected, do not set the value yet
//					
//				}else {
//					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
//				}
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
			}
			
			
		});
		
		/////
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	public StringMarkerBuilder getOwnerNodeBuilder() {
		return (StringMarkerBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public StringMarker build() {
		if(typeChoiceBox.getSelectionModel().getSelectedItem()==null) {
			throw new VisframeException("Type is not selected!");
		}
		
		
		if(this.typeChoiceBox.getSelectionModel().getSelectedItem().contentEquals(PLAIN_STRING_TYPE)) {
			return new PlainStringMarker(this.stringValueTextField.getText(),this.isCaseSensitiveCheckBox.isSelected());
		}else {
			return new RegexStringMarker(this.stringValueTextField.getText(),this.isCaseSensitiveCheckBox.isSelected());
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		//!!!!!note that invocation order here is vital; typeChoiceBox must invoked first because it is used as an indicator 
		//whether status is default empty or not in setOwnerNodeBuilder() method!!!!!
		this.typeChoiceBox.setValue(null);
		this.isCaseSensitiveCheckBox.setSelected(false);
		this.stringValueTextField.setText("");
		
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(StringMarker value) {
		if(value instanceof PlainStringMarker) {
			this.typeChoiceBox.setValue(PLAIN_STRING_TYPE);
		}else {
			this.typeChoiceBox.setValue(REGULAR_EXPRESSION_TYPE);
		}
		
		this.isCaseSensitiveCheckBox.setSelected(value.isCaseSensitive());
		this.stringValueTextField.setText(value.getStringValue());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	static final String PLAIN_STRING_TYPE = "PLAIN STRING";
	static final String REGULAR_EXPRESSION_TYPE = "REGULAR EXPRESSION";
	
	@FXML
	public void initialize() {
		typeChoiceBox.getItems().add(PLAIN_STRING_TYPE);
		typeChoiceBox.getItems().add(REGULAR_EXPRESSION_TYPE);
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private ChoiceBox<String> typeChoiceBox;
	@FXML
	private CheckBox isCaseSensitiveCheckBox;
	@FXML
	private TextField stringValueTextField;
}
