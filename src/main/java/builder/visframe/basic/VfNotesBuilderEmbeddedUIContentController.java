package builder.visframe.basic;

import basic.VfNotes;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class VfNotesBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VfNotes>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/basic/VfNotesBuilderEmbeddedUIContent.fxml";
	
	
//	final KeyCombination keyShiftTab = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHIFT_ANY);
	
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.noteStringTextArea.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.noteStringTextArea.isFocused()) {//lose focus
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
			}
		});
		
		//==============================
		/////TODO debug
		//if the following line is added, a java.lang.IllegalArgumentException: Children: duplicate children added: parent = HBox[id=contentHBox] will be thrown which seems to be a bug of javafx????
		///this bug occurs for text related LeafNodeBuilders including
		//VfNameString, VfNotes, PlaingStringMarker, RegexStringMarker, StringTypeBuilder
		///current solution is to comment out this line if it persists
		
		//the default empty status is empty string, which is a valid value for VfNotes!
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	
	@Override
	public VfNotesBuilder getOwnerNodeBuilder() {
		return (VfNotesBuilder) this.ownerNodeBuilder;
	}
	
	
	
	@Override
	public Pane getRootParentNode() {
		return this.rootContainerVBox;
	}
	
	
	@Override
	public VfNotes build() {
		return new VfNotes(this.noteStringTextArea.getText());
//		if(!this.getOwnerNodeBuilder().getCurrentStatus().hasValidValue()) {
//			throw new UnsupportedOperationException("owner NodeBuilder does not have valid value!");
//		}else {
//			try {
//				Constructor<?> cons;
//				
//				cons = this.getOwnerNodeBuilder().getType().getConstructor(String.class);
//				//create an object with the constructor
//				Object o = cons.newInstance(this.stringValueTextField.getText());
//				
//				return (T)o;
//			}catch(Exception e) {
//				throw new VisframeException(e.getMessage());
//			}
//		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.noteStringTextArea.setText("");
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(VfNotes value) {
		this.noteStringTextArea.setText(value.getNotesString());
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	
	@FXML
	public void initialize() {
		//
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextArea noteStringTextArea;
	@FXML
	private Button saveButton;

	// Event Listener on Button[#saveButton].onAction
	@FXML
	public void saveButtonOnAction(ActionEvent event) {
		//this will trigger the event handler of the focusedProperty of the noteStringTextArea
		this.noteStringTextArea.requestFocus();
		this.noteStringTextArea.getParent().requestFocus();
	}
	
}
