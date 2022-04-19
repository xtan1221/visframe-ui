package builder.visframe.fileformat.record.attribute;

import java.io.IOException;
import java.sql.SQLException;

import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import fileformat.record.attribute.AbstractRecordAttributeFormat;
import fileformat.record.attribute.CompositeTagRecordAttributeFormat;
import fileformat.record.attribute.PrimitiveRecordAttributeFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import utils.FXUtils;
/**
 * 
 * 
 * @author tanxu
 *
 */
public class AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<AbstractRecordAttributeFormat>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/fileformat/record/attribute/AbstractRecordAttributeFormatLeafNodeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//whenever the status of the selected type attribute format builder is changed, the status of this builder should change to the same value as well!
		Runnable runnable = ()->{this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);};
		this.getOwnerNodeBuilder().getPrimitiveRecordAttributeFormatBuilder().addStatusChangedAction(runnable);
		this.getOwnerNodeBuilder().getCompositeTagRecordAttributeFormatBuilder().addStatusChangedAction(runnable);
		
		///////
		this.attributeTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((o,oldValue,newValue)->{
			//
			this.attributeHBox.getChildren().clear();
			if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem()==null) {
				
			}else {
				FXUtils.set2Disable(this.saveButton, false);
				
				if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem().equals(PRIMITIVE_TYPE)) {
					
					this.attributeHBox.getChildren().add(this.getOwnerNodeBuilder().getPrimitiveRecordAttributeFormatBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
					
					
				}else if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem().equals(COMPOSITE_TYPE)){// if(this.compositeTagRecordAttributeFormatRadioButton.isSelected()) {
					
					this.attributeHBox.getChildren().add(this.getOwnerNodeBuilder().getCompositeTagRecordAttributeFormatBuilder().getEmbeddedUIRootContainerNodeController().getRootContainerPane());
					
				}else {
					//unrecognized type?
				}
			}
		});
		
		
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public AbstractRecordAttributeFormatLeafNodeBuilder getOwnerNodeBuilder() {
		return (AbstractRecordAttributeFormatLeafNodeBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.contentVBox;
	}
	
	@Override
	public AbstractRecordAttributeFormat build() throws IOException {
		
		if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem().equals(PRIMITIVE_TYPE)) {
			return this.getOwnerNodeBuilder().getPrimitiveRecordAttributeFormatBuilder().build();
		}else if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem().equals(COMPOSITE_TYPE)){// if(this.compositeTagRecordAttributeFormatRadioButton.isSelected()) {
			return this.getOwnerNodeBuilder().getCompositeTagRecordAttributeFormatBuilder().build();
		}else {
			throw new UnsupportedOperationException("no attribute type is selected!");
		}
	}
	
	/**
	 * note that the {@link attributeHBox} which contains the embedded UI of the {@link PrimitiveRecordAttributeFormatBuilder} or {@link CompositeTagRecordAttributeFormatBuilder}
	 * should NOT be set to mouse transparent!!!
	 * 
	 */
	@Override
	public final void setModifiable(boolean modifiable) {
		this.operationHBox.setMouseTransparent(!modifiable);
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.attributeTypeChoiceBox.setValue(null);
		this.attributeHBox.getChildren().clear();
		
		FXUtils.set2Disable(this.saveButton, true);
		//
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	public void setUIToNonNullValue(AbstractRecordAttributeFormat value) throws SQLException, IOException {
		if(value instanceof PrimitiveRecordAttributeFormat) {
			this.attributeTypeChoiceBox.setValue(PRIMITIVE_TYPE);
			this.getOwnerNodeBuilder().getPrimitiveRecordAttributeFormatBuilder().setValue(value, false);
		}else if (value instanceof CompositeTagRecordAttributeFormat) {
			this.attributeTypeChoiceBox.setValue(COMPOSITE_TYPE);
			this.getOwnerNodeBuilder().getCompositeTagRecordAttributeFormatBuilder().setValue(value, false);
		}else {
			throw new IllegalArgumentException("unrecognized type");
		}
		
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
		
	}
	
	
	//////////////////////////
	private final static String PRIMITIVE_TYPE = "Primitive type";
	private final static String COMPOSITE_TYPE = "Composite type";
	
	@FXML
	public void initialize() {
		
		attributeTypeChoiceBox.getItems().add(PRIMITIVE_TYPE);
		attributeTypeChoiceBox.getItems().add(COMPOSITE_TYPE);
		
	}
	
	@FXML
	private VBox contentVBox;
	@FXML
	private HBox operationHBox;
	@FXML
	private ChoiceBox<String> attributeTypeChoiceBox;
	@FXML
	private Button saveButton;
	@FXML
	private HBox attributeHBox;
	
	/**
	 * changes made can only be saved after Save button is clicked;
	 * 
	 * try to save the current attribute type's current value to the current value of the owner node builder;
	 * 
	 * if any violation is found, 
	 * @param event
	 */
	// Event Listener on Button[#saveButton].onAction
	@FXML
	public void saveButtonOnAction(ActionEvent event) {
		
		if(this.attributeTypeChoiceBox.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Text Content Error");
			alert.setHeaderText("Error!");
			alert.setContentText(
					"No Attribute type is selected, cannot save!"
					);
			alert.showAndWait();
			
		}else {
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(true);
//			try {
//				
//				AbstractRecordAttributeFormat value = this.build();
//				
//				this.getOwnerNodeBuilder().updateNonNullValueFromContentController(value);
//				
//			}catch(Exception ex) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Text Content Error");
//				alert.setHeaderText("Error!");
//				alert.setContentText(ex.getClass().getSimpleName()+":"+ex.getMessage());
//				alert.showAndWait();
//			}
		}
		
	}

}
