package builder.visframe.rdb.sqltype;

import java.util.function.Predicate;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import rdb.sqltype.SQLBooleanType;
import rdb.sqltype.SQLDataTypeFactory;
import rdb.sqltype.SQLDoubleType;
import rdb.sqltype.SQLIntegerType;
import rdb.sqltype.SQLIntegerType.IntegerType;
import rdb.sqltype.SQLStringType;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;

public class SQLDataTypeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VfDefinedPrimitiveSQLDataType>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/rdb/sqltype/SQLDataTypeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	void setDataTypeConstraints(Predicate<VfDefinedPrimitiveSQLDataType> dataTypeConstraints) {
		this.sqlDataTypeChoiceBox.getItems().clear();
		
		if(dataTypeConstraints.test(SQLDataTypeFactory.integerType())) {
			this.sqlDataTypeChoiceBox.getItems().add(INTEGER_TYPE);
		}
		if(dataTypeConstraints.test(SQLDataTypeFactory.doubleType())) {
			this.sqlDataTypeChoiceBox.getItems().add(DOUBLE_TYPE);
		}
		if(dataTypeConstraints.test(SQLDataTypeFactory.booleanType())) {
			this.sqlDataTypeChoiceBox.getItems().add(BOOLEAN_TYPE);
		}
		
		if(dataTypeConstraints.test(new SQLStringType(22, true))) {
			this.sqlDataTypeChoiceBox.getItems().add(STRING_TYPE);
		}
		
	}
	
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		//
		this.setDataTypeConstraints(this.getOwnerNodeBuilder().getDataTypeConstraints());
		
		this.setSqlDataTypeChoiceBox2();
		
		this.setStringMaxLengthTextField();
		
		this.setStringAutoPaddingCheckBox2();
		
		this.setIntegerTypeChoiceBox();
		
		
		////
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	private void setSqlDataTypeChoiceBox2() {
		this.sqlDataTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue)->{
			String dataType = this.sqlDataTypeChoiceBox.getSelectionModel().getSelectedItem();
			if(dataType==null) {
				this.hideAdditionalSettingsHBox();
			}else {
				if(dataType.equals(BOOLEAN_TYPE)) {
					this.hideAdditionalSettingsHBox();
				}else if(dataType.equals(INTEGER_TYPE)) {
					this.setIntegerAdditionalSettings(null);
				}else if(dataType.equals(DOUBLE_TYPE)) {
					this.hideAdditionalSettingsHBox();
				}else if(dataType.equals(STRING_TYPE)) {
					this.setStringAdditionalSettings(null);
				}
			}
			
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
	}
	
	/**
	 * when stringMaxLengthTextField lost focus, 
	 * check if given value is valid;
	 * 
	 * if valid, invoke owner node builder's setValue(T, boolean) with isEmpty = false;
	 */
	private void setStringMaxLengthTextField() {
		this.stringMaxLengthTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.stringMaxLengthTextField.focusedProperty().get()) {//focus is lost
				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
			}
		});
		
		this.stringMaxLengthTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				this.stringMaxLengthTextField.getParent().requestFocus();
			}
		});
		
	}
	
	private void setStringAutoPaddingCheckBox2() {
		this.stringAutoPaddingCheckBox.selectedProperty().addListener((o,oldValue,newValue)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
	}
	
	private void setIntegerTypeChoiceBox() {
		this.integerTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
	}
	/////////////////////////////////////////////////
	@Override
	public VfDefinedPrimitiveSQLDataTypeBuilder getOwnerNodeBuilder() {
		return (VfDefinedPrimitiveSQLDataTypeBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	
	@Override
	public VfDefinedPrimitiveSQLDataType build() {
		
		VfDefinedPrimitiveSQLDataType value;
		
		if(this.sqlDataTypeChoiceBox.getValue().equalsIgnoreCase(BOOLEAN_TYPE)) {
			value = SQLDataTypeFactory.booleanType();
        }else if(this.sqlDataTypeChoiceBox.getValue().equalsIgnoreCase(INTEGER_TYPE)) {
        	IntegerType type = this.integerTypeChoiceBox.getSelectionModel().getSelectedItem();
        	if(type==null) {
        		throw new VisframeException("type is not selected for integer data type!");
        	}
        	value  = new SQLIntegerType(type);
        }else if(this.sqlDataTypeChoiceBox.getValue().equalsIgnoreCase(DOUBLE_TYPE)) {
        	value = SQLDataTypeFactory.doubleType();
        }else if(this.sqlDataTypeChoiceBox.getValue().equalsIgnoreCase(STRING_TYPE)) {
        	
        	int maxLength = 0;
        	try {
        		maxLength = Integer.parseInt(this.stringMaxLengthTextField.getText());
        	}catch(NumberFormatException e){
        		this.stringMaxLengthTextField.setText("");
        		throw new VisframeException("string for max length is not a valid integer!");
        	}
        	
        	if(maxLength<=0||maxLength>Integer.MAX_VALUE) {
        		this.stringMaxLengthTextField.setText("");
        		throw new VisframeException("max length for string type must be positive integer and less than ".concat(Integer.toString(Integer.MAX_VALUE)));
        	}
        	
        	
        	value = new SQLStringType(Integer.parseInt(this.stringMaxLengthTextField.getText()), this.stringAutoPaddingCheckBox.isSelected());
        	
        }else {
        	throw new UnsupportedOperationException();
        }
		
		return value;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.sqlDataTypeChoiceBox.setValue(null);
		this.stringMaxLengthTextField.setText("");
		this.stringAutoPaddingCheckBox.setSelected(false);
		this.integerTypeChoiceBox.setValue(null);
		
		hideAdditionalSettingsHBox();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(VfDefinedPrimitiveSQLDataType value) {
		if(value instanceof SQLBooleanType) {
			this.sqlDataTypeChoiceBox.setValue(BOOLEAN_TYPE);
			this.hideAdditionalSettingsHBox();
		}else if(value instanceof SQLIntegerType) {
			this.sqlDataTypeChoiceBox.setValue(INTEGER_TYPE);
			this.setIntegerAdditionalSettings((SQLIntegerType)value);
		}else if(value instanceof SQLDoubleType) {
			this.sqlDataTypeChoiceBox.setValue(DOUBLE_TYPE);
			this.hideAdditionalSettingsHBox();
		}else if(value instanceof SQLStringType) {
			this.sqlDataTypeChoiceBox.setValue(STRING_TYPE);
			this.setStringAdditionalSettings((SQLStringType)value);
			
		}else {
			throw new IllegalArgumentException("");
		}
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	////////////////////////////////
	private void hideAdditionalSettingsHBox() {
//		this.additionaIntegerSettingsHBox.setVisible(false);
//		this.additionalStringSettingsHBox.setVisible(false);
		this.getRootParentNode().getChildren().remove(this.additionaIntegerSettingsHBox);
		this.getRootParentNode().getChildren().remove(this.additionalStringSettingsHBox);
	}
	
	private void setIntegerAdditionalSettings(SQLIntegerType type) {
		this.getRootParentNode().getChildren().remove(this.additionalStringSettingsHBox);
		if(!this.getRootParentNode().getChildren().contains(this.additionaIntegerSettingsHBox)) {
			this.getRootParentNode().getChildren().add(this.additionaIntegerSettingsHBox);
		}
		
		if(type!=null) {
			this.integerTypeChoiceBox.setValue(type.getIntegerType());
			this.integerTypeDescriptionLabel.setText(type.getIntegerType().getDescription());
		}
	}
	
	private void setStringAdditionalSettings(SQLStringType type) {
		this.getRootParentNode().getChildren().remove(this.additionaIntegerSettingsHBox);
		if(!this.getRootParentNode().getChildren().contains(this.additionalStringSettingsHBox)) {
			this.getRootParentNode().getChildren().add(this.additionalStringSettingsHBox);
		}
		
		if(type!=null) {
			this.stringMaxLengthTextField.setText(Integer.toString(type.getMaxLength()));
			this.stringAutoPaddingCheckBox.setSelected(type.isAutoPadding());
		}
	}
	
	//////////////////////////
	
	private static final String BOOLEAN_TYPE = "BOOLEAN";
	private static final String INTEGER_TYPE = "INTEGER";
	private static final String DOUBLE_TYPE = "DOUBLE";
	private static final String STRING_TYPE = "STRING";
	
	
	@FXML
	public void initialize() {
		sqlDataTypeChoiceBox.getItems().add(BOOLEAN_TYPE);
		sqlDataTypeChoiceBox.getItems().add(INTEGER_TYPE);
		sqlDataTypeChoiceBox.getItems().add(DOUBLE_TYPE);
		sqlDataTypeChoiceBox.getItems().add(STRING_TYPE);
		
		this.sqlDataTypeChoiceBox.valueProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue==null) {
		    		//UI is set to default empty
		    		return;
		    	}
		        if(newValue.equalsIgnoreCase(BOOLEAN_TYPE)) {
		        	SQLDataTypeBuilderEmbeddedUIContentController.this.hideAdditionalSettingsHBox();
		        }else if(newValue.equalsIgnoreCase(INTEGER_TYPE)) {
		        	SQLDataTypeBuilderEmbeddedUIContentController.this.setIntegerAdditionalSettings(null);
		        }else if(newValue.equalsIgnoreCase(DOUBLE_TYPE)) {
		        	SQLDataTypeBuilderEmbeddedUIContentController.this.hideAdditionalSettingsHBox();
		        }else if(newValue.equalsIgnoreCase(STRING_TYPE)) {
		        	SQLDataTypeBuilderEmbeddedUIContentController.this.setStringAdditionalSettings(null);
		        }else {
		        	//
		        	
		        }
		    }
		});
		
		for(IntegerType type:IntegerType.values()) {
			this.integerTypeChoiceBox.getItems().add(type);
		}
		
		this.integerTypeChoiceBox.valueProperty().addListener(new ChangeListener<IntegerType>() {
		    @Override
		    public void changed(ObservableValue<? extends IntegerType> observable, IntegerType oldValue, IntegerType newValue) {
		    	if(newValue==null)
		    		SQLDataTypeBuilderEmbeddedUIContentController.this.integerTypeDescriptionLabel.setText("");
		    	else
		    		SQLDataTypeBuilderEmbeddedUIContentController.this.integerTypeDescriptionLabel.setText(newValue.getDescription());
		    }
		});
		
		
//		TextFieldUtils.onlyAcceptInteger(
//				this.stringMaxLengthTextField, 
//				e-> e>=1 && e<=Integer.MAX_VALUE,//between 1, Integer.MAX_VALUE
//				"Integer type value must be between 1 and ".concat(Integer.toString(Integer.MAX_VALUE))
//				);
	}
	
	@FXML
	private VBox contentVBox;
	@FXML
	private ChoiceBox<String> sqlDataTypeChoiceBox;
	@FXML
	private HBox additionalStringSettingsHBox;
	@FXML
	private TextField stringMaxLengthTextField;
	@FXML
	private CheckBox stringAutoPaddingCheckBox;
	@FXML
	private HBox additionaIntegerSettingsHBox;
	@FXML
	private ChoiceBox<IntegerType> integerTypeChoiceBox;
	@FXML
	private Label integerTypeDescriptionLabel;
	
}
