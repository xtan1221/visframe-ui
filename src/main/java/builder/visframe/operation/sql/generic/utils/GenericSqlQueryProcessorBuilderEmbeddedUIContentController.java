package builder.visframe.operation.sql.generic.utils;

import org.antlr.v4.runtime.misc.ParseCancellationException;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import operation.sql.generic.utils.GenericSQLQueryProcessor;
import operation.sql.generic.utils.GenericSQLQueryProcessor.DottedFullColumnName;

public class GenericSqlQueryProcessorBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<GenericSQLQueryProcessor> {
	public static final String FXML_FILE_DIR = "/builder/visframe/operation/sql/generic/utils/GenericSqlQueryProcessorBuilderEmbeddedUIContent.fxml";
	
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
					GenericSQLQueryProcessor processor = this.build();
					this.currentString = this.stringValueTextField.getText().trim();
					
					this.detectedTableAndColumnsVBox.getChildren().clear();
					processor.getTableNameDottedFullColumnNameSetMap().forEach((k,v)->{
//						Label tf = new Label(e);
						HBox tableHBox = new HBox();
						TextField tableNameTF = new TextField(k);
						tableNameTF.setEditable(false);
						tableNameTF.setPrefWidth(50);
						
						TextField columnNamesTF = new TextField();
						columnNamesTF.setEditable(false);
						
						String columnNamesString = "";
						boolean nothingAddedYet = true;
						for(DottedFullColumnName colName:v) {
							if(nothingAddedYet) {
								nothingAddedYet = false;
							}else {
								columnNamesString = columnNamesString.concat(",");
							}
							columnNamesString = columnNamesString.concat(colName.getColName());
						}
						columnNamesTF.setText(columnNamesString);
						
						
						tableHBox.getChildren().add(tableNameTF);
						tableHBox.getChildren().add(columnNamesTF);
						
						this.detectedTableAndColumnsVBox.getChildren().add(tableHBox);
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
	public GenericSqlQueryProcessorBuilder getOwnerNodeBuilder() {
		return (GenericSqlQueryProcessorBuilder) this.ownerNodeBuilder;
	}
	
	@Override
	public Pane getRootParentNode() {
		return rootVBox;
	}
	
	
	@Override
	public GenericSQLQueryProcessor build() {
		
		if(this.stringValueTextField.getText().trim().isEmpty())
			throw new VisframeException("sql string cannot be empty!");
		
		try {
			GenericSQLQueryProcessor processor = new GenericSQLQueryProcessor(this.stringValueTextField.getText().trim());
			
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
		this.detectedTableAndColumnsVBox.getChildren().clear();
//		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	@Override
	public void setUIToNonNullValue(GenericSQLQueryProcessor value) {
		this.stringValueTextField.setText(value.getSingleLineSqlQueryString());
		
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
	private VBox detectedTableAndColumnsVBox;

	
	@FXML
	public void initialize() {
		//
	}


}
