package builder.visframe.function.evaluator.sqlbased.utils;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.visframe.WhereConditionProcessor;

public class WhereConditionProcessorBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<WhereConditionProcessor> {
	public static final String FXML_FILE_DIR = "/builder/visframe/function/evaluator/sqlbased/utils/WhereConditionProcessorBuilderEmbeddedUIContent.fxml";
	
	private String currentString = "";
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
				
				
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
				
				////
				try {
					WhereConditionProcessor processor = this.build();
					this.currentString = this.stringValueTextField.getText().trim();
					
					this.detectedVariablesFlowPane.getChildren().clear();
					processor.getVariableNameStringSet().forEach(e->{
//						Label tf = new Label(e);
						TextField tf = new TextField(e);
						tf.setEditable(false);
						tf.setPrefWidth(50);
						this.detectedVariablesFlowPane.getChildren().add(tf);
						
					});
					
				}catch(Exception e) {
					
					if(!this.stringValueTextField.getText().trim().equals(this.currentString)) {
						this.stringValueTextField.setText(this.currentString);
						this.stringValueTextField.requestFocus();
						this.stringValueTextField.getParent().requestFocus();
					}
				}
			}
		});
		
		
		
		/////TODO debug
		//if the following line is added, a java.lang.IllegalArgumentException: Children: duplicate children added: parent = HBox[id=contentHBox] will be thrown which seems to be a bug of javafx????
		///this bug occurs for text related LeafNodeBuilders including
		//VfNameString, VfNotes, PlaingStringMarker, RegexStringMarker, StringTypeBuilder
		///current solution is to comment out this line if it persists
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
	}

	
	
	@Override
	public WhereConditionProcessorBuilder getOwnerNodeBuilder() {
		return (WhereConditionProcessorBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return rootVBox;
	}
	
	
	@Override
	public WhereConditionProcessor build() {
		
		if(this.stringValueTextField.getText().trim().isEmpty())
			throw new VisframeException("sql string cannot be empty!");
		
		try {
			WhereConditionProcessor processor = new WhereConditionProcessor(this.stringValueTextField.getText().trim());
			
			return processor;
		}catch(ParseCancellationException e) {
			throw new VisframeException(e.getMessage());
		}
		
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.stringValueTextField.setText("");
		this.detectedVariablesFlowPane.getChildren().clear();
//		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(WhereConditionProcessor value) {
		this.stringValueTextField.setText(value.getSingleLineWhereConditionString());
		
		this.stringValueTextField.requestFocus();
		this.stringValueTextField.getParent().requestFocus();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	@FXML
	private VBox rootVBox;
	@FXML
	private TextField stringValueTextField;
	@FXML
	private FlowPane detectedVariablesFlowPane;
	
	@FXML
	public void initialize() {
		//
	}


}
