package builder.visframe.function.variable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import basic.SimpleName;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager;
import core.table.visframeUDT.VisframeUDTTypeTableViewerManager.TableViewerMode;
import exception.VisframeException;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroupID;
import function.group.CompositionFunctionGroupName;
import function.variable.independent.IndependentFreeInputVariableType;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class IndependentFreeInputVariableTypeSelectorEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<IndependentFreeInputVariableType>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/function/variable/IndependentFreeInputVariableTypeSelectorEmbeddedUIContent.fxml";
	
	
	///////////////////////////////////
	
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	
	@Override
	public IndependentFreeInputVariableTypeSelector getOwnerNodeBuilder() {
		return (IndependentFreeInputVariableTypeSelector) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	
	@Override
	public IndependentFreeInputVariableType build() {
		if(this.compositionFunctionGroupNameTextField.getText().isEmpty()) {
			throw new VisframeException("no IndependentFreeInputVariableType is selected!");
		}
		
		CompositionFunctionID ownerCompositionFunctionID = new CompositionFunctionID(
				new CompositionFunctionGroupID(new CompositionFunctionGroupName(this.compositionFunctionGroupNameTextField.getText())),
				Integer.parseInt(this.ownerCompositionFunctionIndexIDTextField.getText()));
		
		try {
			return this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getIndependentFreeInputVariableTypeManager().lookup(
					new IndependentFreeInputVariableTypeID(ownerCompositionFunctionID, new SimpleName(this.indieFIVTypeNameTextField.getText())));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		//never reached!
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.compositionFunctionGroupNameTextField.setText("");
		this.indieFIVTypeNameTextField.setText("");
		this.ownerCompositionFunctionIndexIDTextField.setText("");
		///
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * 1. set the give value to the delegate viewer IndependentFreeInputVariableTypeBuilder;
	 * 2. add the root node of the integrative UI content of the delegate IndependentFreeInputVariableTypeBuilder to the selectedIntegrativeUIContainerVBox
	 */
	@Override
	public void setUIToNonNullValue(IndependentFreeInputVariableType value) {
		this.compositionFunctionGroupNameTextField.setText(value.getOwnerCompositionFunctionID().getHostCompositionFunctionGroupID().getName().getStringValue());
		this.indieFIVTypeNameTextField.setText(value.getName().getStringValue());
		this.ownerCompositionFunctionIndexIDTextField.setText(Integer.toString(value.getOwnerCompositionFunctionID().getIndexID()));
		
		/////
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	/**
	 * builder method; save memory source without holding it as a field;
	 * @return the fileFormatManagementTableViewerManager
	 * @throws SQLException
	 */
	protected VisframeUDTTypeTableViewerManager<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeID> getIndependentFreeInputVariableTypeManagementTableViewerManager() throws SQLException {
		Map<SimpleName,TextField> invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap = new LinkedHashMap<>();
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(CompositionFunctionGroupID.NAME_COLUMN.getName(), this.compositionFunctionGroupNameTextField);
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(CompositionFunctionID.INDEX_ID_COLUMN.getName(), this.ownerCompositionFunctionIndexIDTextField);
		invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap.put(IndependentFreeInputVariableTypeID.NAME_COLUMN.getName(), this.indieFIVTypeNameTextField);
		
		Runnable operationAfterSelectionIsDone = ()->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		};
		
		VisframeUDTTypeTableViewerManager<IndependentFreeInputVariableType, IndependentFreeInputVariableTypeID> managementTableViewerManager = 
				new VisframeUDTTypeTableViewerManager<>(
					this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getIndependentFreeInputVariableTypeManager(),
					null,//FileFormatBuilderFactory.singleton(),
//					TableContentSQLStringFactory.buildColumnValueEquityCondition(MetadataID.TYPE_COLUMN.getName().getStringValue(), allowedDataTypeNameStringSet, true, LogicOperator.OR),
					null,
					this.getOwnerNodeBuilder().getFilteringCondition(),
					TableViewerMode.SELECTION,
					invokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap
//					operationAfterSelectionIsDone
				);
		
		managementTableViewerManager.setOperationAfterSelectionIsDone(operationAfterSelectionIsDone);
		
		return managementTableViewerManager;
	}
	//////////////////////////
//	private ToggleGroup toggleGroup;
	@FXML
	public void initialize() {
//		//TODO
//		toggleGroup = new ToggleGroup();
//		toggleGroup.getToggles().add(this.selectFromExistingCompositionFunctionRadioButton);
//		toggleGroup.getToggles().add(this.selectFromHostCompositionFunctionRadioButton);
//		
//		
//		this.selectFromHostCompositionFunctionRadioButton.selectedProperty().addListener((o,ol,n)->{
//			if(this.selectFromHostCompositionFunctionRadioButton.isSelected()) {
//				this.selectFromHostCompositionFunctionContainerVBox.setDisable(false);
//				this.selectFromExistingCompositionFunctionContainerVBox.setDisable(true);
//			}else {
//				this.selectFromHostCompositionFunctionContainerVBox.setDisable(true);
//				this.selectFromExistingCompositionFunctionContainerVBox.setDisable(false);
//			}
//		});
//		
//		
//		this.selectFromHostCompositionFunctionRadioButton.setSelected(true);
//		
//		
//		
//		////////////////////////
//		this.originalTypesOfHostCompositionFunctionComboBox.setConverter(new StringConverter<IndependentFreeInputVariableType>() {
//            @Override
//			public String toString(IndependentFreeInputVariableType item) {
//				if (item== null){
//					return null;
//				} else {
//					return item.getName().getStringValue()+";"+item.getSQLDataType().getSQLString();
//				}
//			}
//			
//			@Override
//			public IndependentFreeInputVariableType fromString(String id) {
//				return null;
//			}
//		});
//		
//		this.originalTypesOfHostCompositionFunctionComboBox.setCellFactory(new Callback<ListView<IndependentFreeInputVariableType>,ListCell<IndependentFreeInputVariableType>>(){
//            @Override
//            public ListCell<IndependentFreeInputVariableType> call(ListView<IndependentFreeInputVariableType> l){
//                return new ListCell<IndependentFreeInputVariableType>(){
//                    @Override
//                    protected void updateItem(IndependentFreeInputVariableType item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (item == null || empty) {
//                            setGraphic(null);
//                        } else {
//                            setText(item.getName().getStringValue()+";"+item.getSQLDataType().getSQLString());
//                        }
//                    }
//                } ;
//            }
//        });
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private Button chooseButton;
	@FXML
	private Button clearButton;
	@FXML
	private TextField compositionFunctionGroupNameTextField;
	@FXML
	private TextField ownerCompositionFunctionIndexIDTextField;
	@FXML
	private TextField indieFIVTypeNameTextField;
	
	// Event Listener on Button[#chooseButton].onAction
	@FXML
	public void chooseButtonOnAction(ActionEvent event) throws IOException, SQLException {
		//open a table showing all existing IndependentFreeInputVariableTypes in the host VisProjectDBContext
		this.getIndependentFreeInputVariableTypeManagementTableViewerManager().showWindow(this.getStage());
		
	}
	// Event Listener on Button[#clearButton].onAction
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		this.setUIToDefaultEmptyStatus();
	}
	
	
}
