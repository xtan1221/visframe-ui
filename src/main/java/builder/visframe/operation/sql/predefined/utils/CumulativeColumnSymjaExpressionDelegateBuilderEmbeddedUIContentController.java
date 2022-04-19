package builder.visframe.operation.sql.predefined.utils;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.sql.predefined.utils.CumulativeColumnSymjaExpressionDelegate;
import rdb.sqltype.SQLDataTypeFactory;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;
import rdb.table.data.DataTableColumn;
import rdb.table.data.DataTableColumnName;
import rdb.table.data.DataTableSchema;
import symja.SymjaUtils;
import symja.VfSymjaExpressionString;
import symja.VfSymjaSinglePrimitiveOutputExpression;
import symja.VfSymjaVariableName;
import utils.AlertUtils;
import utils.FXUtils;

public class CumulativeColumnSymjaExpressionDelegateBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<CumulativeColumnSymjaExpressionDelegate> {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/operation/sql/predefined/utils/CumulativeColumnSymjaExpressionDelegateBuilderEmbeddedUIContent.fxml";
	
	/////////////////////
	private DataTableSchema dataTableSchema;
	
	/**
	 * either store empty string or current valid expression;
	 * thus indicator for whether there is valid expresion or not
	 */
	private String currentExpressionString = "";
	
	private Map<VfSymjaVariableName, VariableHBox> variableNameHBoxMap;
	
	/**
	 * 
	 * @param dataTableSchema
	 */
	public void setDataTableSchema(DataTableSchema dataTableSchema) {
		this.dataTableSchema = dataTableSchema;
		
		this.setUIToDefaultEmptyStatus();
	}
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		this.expressionTextField.focusedProperty().addListener((o,oldValue,newValue) -> {
			if(!this.expressionTextField.focusedProperty().get()) {//focus is lost
				//no change
				if(Objects.equal(this.currentExpressionString, this.expressionTextField.getText().trim())) {//no change from previous one, do nothing
					return;
				}
				
				if(this.expressionTextField.getText().trim().isEmpty()) {
					
					this.expressionTextField.setText(this.currentExpressionString);
					
					return;
				}
				
				
				////new non-empty value
				try {
					SymjaUtils.validateExpressionString(this.expressionTextField.getText());
					
					Set<VfSymjaVariableName> variables = SymjaUtils.extractVariableNameSet(this.expressionTextField.getText().trim());
					
					if(variables.isEmpty()) {//there is no variable detected, not valid since at least one variable for previouseRecordCumulativeColumnSymjaVariableName
						AlertUtils.popAlert("Error", "Given expression string contains no variable for previouseRecordCumulativeColumnSymjaVariableName!");
						
						this.expressionTextField.setText(this.currentExpressionString);
						
						return;
					}
					
					//non-empty variable set
					//reset any variables of previous expressions;
					this.variableNameHBoxMap.clear();
					this.variableTableColumnMapVBox.getChildren().clear();
					//update
					this.currentExpressionString = this.expressionTextField.getText().trim();
					
					//
					this.toggleGroup = new ToggleGroup();
					
					
					Set<DataTableColumn> numericColSet = new LinkedHashSet<>();
					 this.dataTableSchema.getOrderedListOfNonRUIDColumn().forEach(ex->{
						 if(ex.getSqlDataType().isNumeric()) {
							 numericColSet.add(ex);
						 }
					 });
					for(VfSymjaVariableName variable:variables) {
						VariableHBox hbox = new VariableHBox(variable,numericColSet); 
						
						this.variableNameHBoxMap.put(variable, hbox);
						
						this.toggleGroup.getToggles().add(hbox.isPreviousRecordCumulativeValueVariableRadioButton);
						
						this.variableTableColumnMapVBox.getChildren().add(hbox.containerHBox);
					}
				}catch(Exception e) {
					AlertUtils.popAlert("Error", "Given expression string is invalid:"+e.getMessage());
					
					this.expressionTextField.setText(this.currentExpressionString);
					
					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
					
					return;
				}
//				
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
	public CumulativeColumnSymjaExpressionDelegateBuilder getOwnerNodeBuilder() {
		return (CumulativeColumnSymjaExpressionDelegateBuilder) this.ownerNodeBuilder;
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
	public CumulativeColumnSymjaExpressionDelegate build() {
		//if the expressionTextField does not contain a valid symja expression, throw exception;
		if(this.dataTableSchema==null) {
			throw new VisframeException("data table schema is not given!");
		}
		
		if(this.currentExpressionString.isEmpty()) {
			throw new VisframeException("no valid expression is given!");
		}
		
		
		//if there is no variable selected as the previouseRecordCumulativeColumnSymjaVariableName, throw exception;
		if(this.toggleGroup.getSelectedToggle()==null) {
			throw new VisframeException("no variable is selected as previouseRecordCumulativeColumnSymjaVariable");
		}
		
		//if not all variables are assigned to a column, throw exception;
		//if any column is assigned to multiple variables, throw exception;
		Map<VfSymjaVariableName, VfDefinedPrimitiveSQLDataType> variableNameSQLDataTypeMap = new LinkedHashMap<>();
		BiMap<DataTableColumnName,VfSymjaVariableName> columnNameSymjaVariableNameMap = HashBiMap.create();
		VfSymjaVariableName previouseRecordCumulativeColumnSymjaVariableName = null;
		
		Set<DataTableColumn> assignedColumnSet = new HashSet<>();
		
		for(VariableHBox v:this.variableNameHBoxMap.values()) {
			if(!v.isPreviousRecordCumulativeValueVariableRadioButton.isSelected()) {
				DataTableColumn col = v.dataTableColumnComboBox.getSelectionModel().getSelectedItem();
				if(col==null) {
					throw new VisframeException("at least one variable is not assigned to a column;");
				}
				
				if(assignedColumnSet.contains(col)) {
					throw new VisframeException("at least one column is assigned to multiple variables:"+col.getName().getStringValue());
				}
				
				assignedColumnSet.add(col);
				
				variableNameSQLDataTypeMap.put(v.variableName, col.getSqlDataType());
				
				columnNameSymjaVariableNameMap.put(col.getName(), v.variableName);
			}else {
				previouseRecordCumulativeColumnSymjaVariableName = v.variableName;
				
				variableNameSQLDataTypeMap.put(previouseRecordCumulativeColumnSymjaVariableName, SQLDataTypeFactory.doubleType());//the sql data type for cumulative column is double;
			}
		}
		
		////////////////////////////////////////////////
		//all valid, start build
		VfSymjaSinglePrimitiveOutputExpression expression = new VfSymjaSinglePrimitiveOutputExpression(
				SQLDataTypeFactory.doubleType(),//the data type for the expression, equivalent to the data type of the cumulative column
				new VfSymjaExpressionString(this.currentExpressionString),
				variableNameSQLDataTypeMap
				);
		
		return new CumulativeColumnSymjaExpressionDelegate(
				expression,
				columnNameSymjaVariableNameMap,
				previouseRecordCumulativeColumnSymjaVariableName
				);
	}
	
	
	/**
	 * the default empty status of this UI is 
	 * 
	 * 1. selection is cleared;
	 * 2. 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		if(this.dataTableSchema==null) {//if no data table schema, not editable
			FXUtils.set2Disable(this.expressionHBox, true);
		}else {
			FXUtils.set2Disable(this.expressionHBox, false);
		}
		
		
		this.expressionTextField.setText("");
		
		this.variableNameHBoxMap.clear();
		
		this.variableTableColumnMapVBox.getChildren().clear();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	
	@Override
	public void setUIToNonNullValue(CumulativeColumnSymjaExpressionDelegate value) {
		this.expressionTextField.setText(value.getSymjaExpression().getExpressionString().getValueString());
		
		//trigger the event handler to update the variableNameHBoxMap 
		this.expressionTextField.requestFocus();
		this.expressionTextField.getParent().requestFocus();
		
		///
		Map<VfSymjaVariableName, DataTableColumn> variableNameAssignedColumnMap = new HashMap<>();
		
		value.getColumnSymjaVariableNameMap().forEach((k,v)->{
			variableNameAssignedColumnMap.put(v, this.dataTableSchema.getColumn(k));
		});
		
		//set up VariableHBoxs in variableNameHBoxMap
		this.variableNameHBoxMap.forEach((k,v)->{
			if(k.equals(value.getPreviouseRecordCumulativeColumnSymjaVariableName())) {
				v.isPreviousRecordCumulativeValueVariableRadioButton.setSelected(true);
			}else {
				v.dataTableColumnComboBox.setValue(variableNameAssignedColumnMap.get(k));
			}
		});
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	
	//////////////////////////
	private ToggleGroup toggleGroup;
	/**
	 * perform initialization that is independent of the tree and the owner node builder
	 */
	@FXML
	public void initialize() {
		this.variableNameHBoxMap = new LinkedHashMap<>();
	}
	
	@FXML
	private VBox containerVBox;
	@FXML
	private HBox expressionHBox;
	@FXML
	private TextField expressionTextField;
	@FXML
	private VBox variableTableColumnMapVBox;

	
	
	///////////////////////////////////
	private class VariableHBox{
		
		private final VfSymjaVariableName variableName;
		
		
		private HBox containerHBox;
		private TextField variableNameTextField;
		private RadioButton isPreviousRecordCumulativeValueVariableRadioButton;
		private ComboBox<DataTableColumn> dataTableColumnComboBox;
		private TextField columnDataTypeTextField;
		
		
		
		VariableHBox(VfSymjaVariableName variableName, Collection<DataTableColumn> dataTableColumns){
			this.variableName = variableName;
			
			//////////////////
			this.containerHBox = new HBox();
			this.variableNameTextField = new TextField(this.variableName.getValue());
			this.variableNameTextField.setEditable(false);
			
			this.isPreviousRecordCumulativeValueVariableRadioButton = new RadioButton();
			
			this.dataTableColumnComboBox = new ComboBox<>();
			this.dataTableColumnComboBox.getItems().addAll(dataTableColumns);
			
			this.columnDataTypeTextField = new TextField();
			this.columnDataTypeTextField.setEditable(false);
			
			////////////////
			this.containerHBox.getChildren().add(this.variableNameTextField);
			this.containerHBox.getChildren().add(this.isPreviousRecordCumulativeValueVariableRadioButton);
			this.containerHBox.getChildren().add(this.dataTableColumnComboBox);
			this.containerHBox.getChildren().add(this.columnDataTypeTextField);
			
			
			//////////////////
			this.setEventHandler();
		}
		
		private void setEventHandler() {
			this.isPreviousRecordCumulativeValueVariableRadioButton.selectedProperty().addListener((o,oldValue,newValue)->{
				FXUtils.set2Disable(this.dataTableColumnComboBox, this.isPreviousRecordCumulativeValueVariableRadioButton.isSelected());
				FXUtils.set2Disable(this.columnDataTypeTextField, this.isPreviousRecordCumulativeValueVariableRadioButton.isSelected());
//				
				getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
			});
			
			
			//when this is changed, try to build and update the value to owner builder
			this.dataTableColumnComboBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
				
				getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
				
				DataTableColumn col = this.dataTableColumnComboBox.getSelectionModel().getSelectedItem();
				
				if(col==null) {
					this.columnDataTypeTextField.setText("");
				}else {
					this.columnDataTypeTextField.setText(col.getSqlDataType().getSQLString());
				}
			});
			
			
			//this will set up how items are displayed
			this.dataTableColumnComboBox.setCellFactory(e->{
				return new ListCell<DataTableColumn>() {
		            @Override
		            protected void updateItem(DataTableColumn value, boolean empty) {
		                super.updateItem(value, empty);
		                if (value == null || empty) {
		                    setGraphic(null);
		                } else {
		                	//set the Graphics for the tree cell here with a specific operationType
		                	Label label = new Label(value.getName().getStringValue().concat(";").concat(value.getSqlDataType().getSQLString()));
		                    setGraphic(label);
		                }
		            }
		        };
			});
			
			//set up how the selected item of the combobox is displayed
			this.dataTableColumnComboBox.setButtonCell(new ListCell<DataTableColumn>() {
	            @Override
	            protected void updateItem(DataTableColumn value, boolean empty) {
	                super.updateItem(value, empty);
	                if (value == null || empty) {
	                    setText("");
	                } else {
	                    setText(value.getName().getStringValue());
	                }

	            }
	        });
		}
		
	}
}
