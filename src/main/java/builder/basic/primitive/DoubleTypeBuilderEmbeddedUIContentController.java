package builder.basic.primitive;

import core.builder.NodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class DoubleTypeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<Double> {
	public static final String FXML_FILE_DIR = "/builder/basic/primitive/DoubleTypeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//set up the event listener for the TextField
//		TextFieldUtils.onlyAcceptInteger(this.integerStringValueTextField, this.getOwnerNodeBuilder().getDomain(), this.getOwnerNodeBuilder().getDomainDescription());
		this.doubleStringValueTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.doubleStringValueTextField.focusedProperty().get()) {//focus is lost
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
//				if(this.doubleStringValueTextField.getText().isEmpty()) {//? TODO empty
//					this.getOwnerNodeBuilder().setValue(null, true);
//					return;
//				}
//				
//				//non-empty
//				try {
//					double value = Double.parseDouble(this.doubleStringValueTextField.getText());
//					
//					if(this.getOwnerNodeBuilder().getDomain()!=null && !this.getOwnerNodeBuilder().getDomain().test(value)) {//check domain constraint
//						Alert alert = new Alert(AlertType.ERROR);
//						alert.setTitle("Text Content Error");
//						alert.setHeaderText("Error!");
//						alert.setContentText(this.getOwnerNodeBuilder().getDomainDescription());
//						
//						alert.showAndWait();
//						
//						this.doubleStringValueTextField.setText("");
//						this.doubleStringValueTextField.requestFocus();
//						
//						
//					}else {//valid non null
//						this.getOwnerNodeBuilder().setValue(value, false);
//					}
//					
//				}catch(Exception ex) {
//					Alert alert = new Alert(AlertType.ERROR);
//					alert.setTitle("Text Content Error");
//					alert.setHeaderText("Error!");
//					alert.setContentText(ex.getClass().getSimpleName()+":"+ex.getMessage());
//					alert.showAndWait();
//					
//					this.doubleStringValueTextField.setText("");
//					this.doubleStringValueTextField.requestFocus();
//				}
			}
		});
		
		this.doubleStringValueTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {

				this.doubleStringValueTextField.getParent().requestFocus();

			}
		});
		
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	
	/////////////////////////////////////////////
	@Override
	public DoubleTypeBuilder getOwnerNodeBuilder() {
		return (DoubleTypeBuilder) this.ownerNodeBuilder;
	}
	


//	@Override
//	public void setModifiable(boolean modifiable) {
//		this.doubleStringValueTextField.setMouseTransparent(!modifiable);
//	}

	
	@Override
	public Pane getRootParentNode() {
		return this.rootHBox;
	}
	
	
	/**
	 * must first check if owner {@link NodeBuilder}'s current status is valid value or not;
	 */
	@Override
	public Double build() {
		double value;
		
		try {
			value = Double.parseDouble(this.doubleStringValueTextField.getText());
		}catch(NumberFormatException e) {
			this.doubleStringValueTextField.setText("");
			throw new VisframeException("given string is not an double number!");
		}
		
		if(this.getOwnerNodeBuilder().getDomain()!=null && !this.getOwnerNodeBuilder().getDomain().test(value)) {//check domain constraint
			this.doubleStringValueTextField.setText("");
			throw new VisframeException(this.getOwnerNodeBuilder().getDomainDescription());
			
		}
		
		return value;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.doubleStringValueTextField.setText("");
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(Double value) {
		this.doubleStringValueTextField.setText(Double.toString(value));
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	@FXML 
	private HBox rootHBox;
	@FXML
	private TextField doubleStringValueTextField;
	
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
