package builder.basic.primitive;

import core.builder.NodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class IntegerTypeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<Integer> {
	public static final String FXML_FILE_DIR = "/builder/basic/primitive/IntegerTypeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
		//set up the event listener for the TextField
//		TextFieldUtils.onlyAcceptInteger(this.integerStringValueTextField, this.getOwnerNodeBuilder().getDomain(), this.getOwnerNodeBuilder().getDomainDescription());
		this.integerStringValueTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.integerStringValueTextField.focusedProperty().get()) {//focus is lost
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
//				if(this.integerStringValueTextField.getText().isEmpty()) {//?TODO
//					this.getOwnerNodeBuilder().setValue(null, true);
//					return;
//				}
//				
//				//non-empty
//				try {
//					int value = Integer.parseInt(this.integerStringValueTextField.getText());
//					
//					if(this.getOwnerNodeBuilder().getDomain()!=null && !this.getOwnerNodeBuilder().getDomain().test(value)) {//check domain constraint
//						Alert alert = new Alert(AlertType.ERROR);
//						alert.setTitle("Text Content Error");
//						alert.setHeaderText("Error!");
//						alert.setContentText(
//								this.getOwnerNodeBuilder().getDomainDescription());
//						
//						alert.showAndWait();
//						
//						this.integerStringValueTextField.setText("");
//						this.integerStringValueTextField.requestFocus();
//						
//						
//					}else {//valid non null
//						this.getOwnerNodeBuilder().setValue(this.build(), false);
//					}
//					
//				}catch(Exception ex) {
//					Alert alert = new Alert(AlertType.ERROR);
//					alert.setTitle("Text Content Error");
//					alert.setHeaderText("Error!");
//					alert.setContentText(ex.getClass().getSimpleName()+":"+ex.getMessage());
//					alert.showAndWait();
//					
//					this.integerStringValueTextField.setText("");
//					this.integerStringValueTextField.requestFocus();
//				}
			}
		});
		
		this.integerStringValueTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {

				this.integerStringValueTextField.getParent().requestFocus();

			}
		});
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
	}
	
	@Override
	public IntegerTypeBuilder getOwnerNodeBuilder() {
		return (IntegerTypeBuilder) this.ownerNodeBuilder;
	}
	

//	@Override
//	public void setModifiable(boolean modifiable) {
//		this.integerStringValueTextField.setMouseTransparent(!modifiable);
//	}

	@Override
	public Pane getRootParentNode() {
		return this.rootHBox;
	}
	
	
	/**
	 * must first check if owner {@link NodeBuilder}'s current status is valid value or not;
	 */
	@Override
	public Integer build() {
		int value;
		try {
			value = Integer.parseInt(this.integerStringValueTextField.getText());
		}catch(NumberFormatException e) {
			this.integerStringValueTextField.setText("");
			throw new VisframeException("given string is not an integer!");
		}
		
		if(this.getOwnerNodeBuilder().getDomain()!=null && !this.getOwnerNodeBuilder().getDomain().test(value)) {//check domain constraint
			this.integerStringValueTextField.setText("");
			throw new VisframeException(this.getOwnerNodeBuilder().getDomainDescription());
		}
		
		return value;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.integerStringValueTextField.setText("");
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}

	@Override
	public void setUIToNonNullValue(Integer value) {
		this.integerStringValueTextField.setText(Integer.toString(value));
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	@FXML 
	private HBox rootHBox;
	@FXML
	private TextField integerStringValueTextField;
	
	@FXML
	public void initialize() {
		
//		this.integerStringValueTextField.textProperty().addListener((o,oldValue,newValue)->{
//			try {
//				this.getOwnerNodeBuilder().setValue(Integer.parseInt(this.integerStringValueTextField.getText()));
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		});
//		
		
		
	}


}
