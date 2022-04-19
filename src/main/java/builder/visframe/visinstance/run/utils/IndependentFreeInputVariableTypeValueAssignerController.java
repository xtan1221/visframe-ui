package builder.visframe.visinstance.run.utils;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.composition.CompositionFunctionBuilder;
import builder.visframe.function.composition.CompositionFunctionBuilderFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rdb.sqltype.VfDefinedPrimitiveSQLDataType;
import utils.AlertUtils;

/**
 * 
 * @author tanxu
 *
 */
public class IndependentFreeInputVariableTypeValueAssignerController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/utils/IndependentFreeInputVariableTypeValueAssigner.fxml";
	
	/////////////////
	private IndependentFreeInputVariableTypeValueAssignerManager manager;
	
	void setManager(IndependentFreeInputVariableTypeValueAssignerManager manager) {
		this.manager = manager;
		
		//
		this.ownerCompositionFunctionTextField.setText(
				this.getManager().getIndieFIVType().getOwnerCompositionFunctionID().getHostCompositionFunctionGroupID().getName().getStringValue()
				.concat("-")
				.concat(Integer.toString(this.getManager().getIndieFIVType().getOwnerCompositionFunctionID().getIndexID())));
		
		this.notesTextArea.setText(this.getManager().getIndieFIVType().getNotes().getNotesString());
		this.nameTextField.setText(this.getManager().getIndieFIVType().getName().getStringValue());
		this.sqlDataTypeTextField.setText(this.getManager().getIndieFIVType().getSQLDataType().getSQLString());
		
		//////////////////
		this.valueStringTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.valueStringTextField.focusedProperty().get()) {//lose focus
				VfDefinedPrimitiveSQLDataType sqlDataType = (VfDefinedPrimitiveSQLDataType)this.getManager().getIndieFIVType().getSQLDataType();
				if(!sqlDataType.isValidStringValue(this.valueStringTextField.getText())) {
					AlertUtils.popAlert("Error", "Input string value is not valid!");
					this.valueStringTextField.setText("");
					this.rootContainerGridPane.setStyle("-fx-border-color:red");
				}else {//
					//
					this.rootContainerGridPane.setStyle("-fx-border-color:green");
				}
				
				//whenever the valueStringTextField loses focus, trigger the process of checking whether the owner VisInstanceRunBuilder has a valid value or not;
				this.getManager().getOwnerVisInstanceRunBuilder().updateNonNullValueFromContentController(false);
			}
		});
		
		this.valueStringTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.valueStringTextField.getParent().requestFocus();
			}
		});
		
		//trigger the process of checking whether the owner VisInstanceRunBuilder has a valid value or not;
		this.getManager().getOwnerVisInstanceRunBuilder().updateNonNullValueFromContentController(false);
	}
	
	void setStringValue(String value) {
		this.valueStringTextField.setText(value);
		this.valueStringTextField.requestFocus();
		this.valueStringTextField.getParent().requestFocus();
	}
	
	IndependentFreeInputVariableTypeValueAssignerManager getManager() {
		return this.manager;
	}
	
	
	public Pane getRootContainerPane() {
		return this.rootContainerGridPane;
	}
	
	
	TextField getValueStringTextField() {
		return this.valueStringTextField;
	}
	
	////////////////////////////////////////
	@FXML
	public void initialize() {
		
	}
	
	
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private TextField ownerCompositionFunctionTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextField sqlDataTypeTextField;
	@FXML
	private Button viewOwnerCompositionFunctionDetailButton;
	@FXML
	private Button viewNotesButton;
	@FXML
	private TextField valueStringTextField;

	/////////////////////////////////
	private Scene CFScene;
	private Stage CFStage;
	private CompositionFunctionBuilder CFBuilder;
	@FXML
	public void viewOwnerCompositionFunctionDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.CFBuilder==null) {
			CFBuilder = 
					CompositionFunctionBuilderFactory.singleton(this.getManager().getOwnerVisInstanceRunBuilder().getHostVisProjectDBContext())
					.build(this.getManager().getOwnerCompositionFunction());
			CFBuilder.setModifiable(false);
			
			CFScene = new Scene(CFBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			CFStage = new Stage();
			
			CFStage.setScene(CFScene);
			
			CFStage.setWidth(1200);
			CFStage.setHeight(800);
			CFStage.initModality(Modality.NONE);
			String title = this.getManager().getOwnerCompositionFunction().getID().toString();
			CFStage.setTitle(title);
		}
		CFStage.showAndWait();
	}
	// Event Listener on Button[#viewNotesButton].onAction
	@FXML
	public void viewNotesButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
}
