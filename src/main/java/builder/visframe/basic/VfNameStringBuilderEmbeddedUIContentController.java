package builder.visframe.basic;

import java.lang.reflect.Constructor;
import basic.VfNameString;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class VfNameStringBuilderEmbeddedUIContentController<T extends VfNameString> extends LeafNodeBuilderEmbeddedUIContentController<T>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/basic/VfNameStringBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.stringValueTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.stringValueTextField.focusedProperty().get()) {//lose focus
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
//				if(this.stringValueTextField.getText().isEmpty()) {//empty string, set to default empty
//				
//					this.getOwnerNodeBuilder().setToDefaultEmpty();
//					
//					return;
//				}else {//not empty string, check if the string is valid
////					try {
//////						this.getOwnerNodeBuilder().updateNonNullValueFromContentController(this.build());
////						this.getOwnerNodeBuilder().updateNonNullValueFromContentController();
////					}catch(Exception ex) {
////						Alert alert = new Alert(AlertType.ERROR);
////						alert.setTitle("Text Content Error");
////						alert.setHeaderText("Error!");
////						alert.setContentText("text must obey constraints of VfNameString(...)!!!!");
////						
////						alert.showAndWait();
////						
////						stringValueTextField.setText("");
////						stringValueTextField.requestFocus();
////					}
//					
//				}
			}
		});
		
		stringValueTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				stringValueTextField.getParent().requestFocus();
			}
		});
		
		/////TODO debug
		//if the following line is added, a java.lang.IllegalArgumentException: Children: duplicate children added: parent = HBox[id=contentHBox] will be thrown which seems to be a bug of javafx????
		///this bug occurs for text related LeafNodeBuilders including
		//VfNameString, VfNotes, PlaingStringMarker, RegexStringMarker, StringTypeBuilder
		///current solution is to comment out this line if still it persists
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public VfNameStringBuilder<T> getOwnerNodeBuilder() {
		return (VfNameStringBuilder<T>) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentHBox;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	/**
	 * try to build a valid VfNameString object based on current UI data;
	 * 
	 * throw VisframeException if invalid;
	 */
	public T build() {
			try {
				Constructor<?> cons;
				
				cons = this.getOwnerNodeBuilder().getType().getConstructor(String.class);
				//create an object with the constructor
				Object o = cons.newInstance(this.stringValueTextField.getText());
				
				return (T)o;
			}catch(Exception e) {
				throw new VisframeException(e.getMessage());
			}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.stringValueTextField.setText("");
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(T value) {
		this.stringValueTextField.setText(value.getStringValue());
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	@FXML
	public void initialize() {
		//

	}
	
	@FXML
	private HBox contentHBox;
	@FXML
	private TextField stringValueTextField;
	
	
	
	



}
