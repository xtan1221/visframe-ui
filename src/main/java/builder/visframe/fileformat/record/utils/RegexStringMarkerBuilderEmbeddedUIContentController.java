package builder.visframe.fileformat.record.utils;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import fileformat.record.utils.RegexStringMarker;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class RegexStringMarkerBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<RegexStringMarker>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/fileformat/record/utils/RegexStringMarkerBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
		this.isCaseSensitiveCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
//			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		/////////////////////
		
		
		//////////////////////////
		this.stringValueTextField.setOnKeyPressed(e->{
			if(e.getCode().equals(KeyCode.ENTER)) {
				this.stringValueTextField.getParent().requestFocus();
			}
		});
		
		//whenever the stringValueTextField lost focus, it assumes a new value is given, even if it is empty string
		this.stringValueTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.stringValueTextField.isFocused()) {//editing is finished, always set to valid value no matter the 
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
				
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
			}
			
		});
		
		//==============================
		/////TODO debug
		//if the following line is added, a java.lang.IllegalArgumentException: Children: duplicate children added: parent = HBox[id=contentHBox] will be thrown which seems to be a bug of javafx????
		///this bug occurs for text related LeafNodeBuilders including
		//VfNameString, VfNotes, PlaingStringMarker, RegexStringMarker, StringTypeBuilder
		///current solution is to comment out this line if it persists
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	public RegexStringMarkerBuilder getOwnerNodeBuilder() {
		return (RegexStringMarkerBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public RegexStringMarker build() {
		return new RegexStringMarker(this.stringValueTextField.getText(),this.isCaseSensitiveCheckBox.isSelected());
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.isCaseSensitiveCheckBox.setSelected(false);
		this.stringValueTextField.setText("");
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(RegexStringMarker value) {
		
		this.isCaseSensitiveCheckBox.setSelected(value.isCaseSensitive());
		this.stringValueTextField.setText(value.getStringValue());
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	
	@FXML
	private VBox contentVBox;
	@FXML
	private TextField stringValueTextField;
	@FXML
	private CheckBox isCaseSensitiveCheckBox;
}
