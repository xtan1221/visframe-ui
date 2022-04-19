package builder.basic.primitive;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class StringTypeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<String> {
	public static final String FXML_FILE_DIR = "/builder/basic/primitive/StringTypeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		
		
		this.stringValueTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {

				this.stringValueTextField.getParent().requestFocus();

			}
		});
		
		this.stringValueTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.stringValueTextField.focusedProperty().get()) {//focus is lost
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			}
			
		});
		
		
		
		/////TODO debug
		//if the following line is added, a java.lang.IllegalArgumentException: Children: duplicate children added: parent = HBox[id=contentHBox] will be thrown which seems to be a bug of javafx????
		///this bug occurs for text related LeafNodeBuilders including
		//VfNameString, VfNotes, PlaingStringMarker, RegexStringMarker, StringTypeBuilder
		///current solution is to comment out this line if it persists
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
		//to make the default status of string builder as finished since empty string is allowed;
//		this.stringValueTextField.setText("test");
//		this.stringValueTextField.requestFocus();
//		this.stringValueTextField.getParent().requestFocus();
//		this.stringValueTextField.setText("");
//		this.stringValueTextField.requestFocus();
//		this.stringValueTextField.getParent().requestFocus();
	}

	
	
	@Override
	public StringTypeBuilder getOwnerNodeBuilder() {
		return (StringTypeBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return rootHBox;
	}
	
	
	@Override
	public String build() {
		return this.stringValueTextField.getText();
	}
	
	/**
	 * do not need to set UI effect to not built because empty string is still valid
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.stringValueTextField.setText("");
//		
//		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(String value) {
		this.stringValueTextField.setText(value);
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	@FXML
	private HBox rootHBox;
	@FXML
	private TextField stringValueTextField;
	
	@FXML
	public void initialize() {
		
	}


}
