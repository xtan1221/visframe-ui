package builder.visframe.symja;


import java.util.Set;

import com.google.common.base.Objects;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import symja.SymjaUtils;
import symja.VfSymjaExpressionString;
import symja.VfSymjaVariableName;

public class VfSymjaExpressionStringBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VfSymjaExpressionString> {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/symja/VfSymjaExpressionStringBuilderEmbeddedUIContent.fxml";
	
	private String currentExpressionString = "";
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		this.expressionTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.expressionTextField.focusedProperty().get()) {//focus is lost
				//no change
				if(Objects.equal(this.currentExpressionString, this.expressionTextField.getText().trim())) {//no change from previous one, do nothing
					return;
				}
				
				//empty
				if(this.expressionTextField.getText().trim().isEmpty()) {
					
					this.expressionTextField.setText(this.currentExpressionString);
					
					return;
				}
				
				////////////
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
				
				try {
					this.build();
					
					this.currentExpressionString = this.expressionTextField.getText().trim();
					
					//extract variable
					Set<VfSymjaVariableName> variables = SymjaUtils.extractVariableNameSet(this.expressionTextField.getText().trim());
					
					this.detectedVariableNumTextField.setText(Integer.toString(variables.size()));
					
					this.detectedVariableNameTextFieldHBox.getChildren().clear();
					
					
					for(VfSymjaVariableName variable:variables) {
						TextField variableNameTF = new TextField(variable.getValue());
						variableNameTF.setEditable(false);
						this.detectedVariableNameTextFieldHBox.getChildren().add(variableNameTF);
					}
					
				}catch(Exception e) {
					
					this.expressionTextField.setText(this.currentExpressionString);
					
					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
					
					return;
				}
				
			}
		});
		
		
		this.expressionTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				this.expressionTextField.getParent().requestFocus();
			}
		});
		
		
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	///////////////////////////////////////////
	@Override
	public VfSymjaExpressionStringBuilder getOwnerNodeBuilder() {
		return (VfSymjaExpressionStringBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Parent getRootParentNode() {
		return this.containerVBox;
	}
	
	/**
	 * try to build a {@link CumulativeColumnSymjaExpressionDelegate} from the current UI;
	 * exception will be thrown if not buildable;
	 */
	@Override
	public VfSymjaExpressionString build() {
		//if the expressionTextField does not contain a valid symja expression, throw exception;
		
		try {
			SymjaUtils.validateExpressionString(this.expressionTextField.getText().trim());
		}catch(Exception e) {
			throw new VisframeException("Given expression string is invalid: "+e.getMessage());
		}
		
		return new VfSymjaExpressionString(this.expressionTextField.getText().trim());
	}
	
	
	/**
	 * the default empty status of this UI is 
	 * 
	 * 1. selection is cleared;
	 * 2. 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		
		this.expressionTextField.setText("");
		
		this.currentExpressionString = "";
		
		this.detectedVariableNameTextFieldHBox.getChildren().clear();
		
		this.detectedVariableNumTextField.setText("N/A");
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(VfSymjaExpressionString value) {
		this.expressionTextField.setText(value.getValueString());
		//!!!!!!!!!!
		this.expressionTextField.requestFocus();
		this.expressionTextField.getParent().requestFocus();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	
	/**
	 * perform initialization that is independent of the tree and the owner node builder
	 */
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private VBox containerVBox;
	@FXML
	private HBox expressionHBox;
	@FXML
	private TextField expressionTextField;
	@FXML
	private TextField detectedVariableNumTextField;
	@FXML
	private HBox detectedVariableNameTextFieldHBox;

}
