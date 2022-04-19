package builder.visframe.context.scheme;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import basic.SimpleName;
import basic.VfNotes;
import context.scheme.VSComponent;
import context.scheme.VisScheme;
import context.scheme.VisSchemeBuilderImpl;
import core.builder.NodeBuilder;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroupID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import metadata.MetadataID;
import operation.OperationID;
import utils.AlertUtils;

public class VisSchemeBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VisScheme> {
	public static final String FXML_FILE_DIR = "/builder/visframe/context/scheme/VisSchemeBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		//name must be SimpleName
		this.nameTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.nameTextField.focusedProperty().get()) {//lose focus
				try {
					new SimpleName(this.nameTextField.getText());
					
					this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
					
				}catch(Exception ex) {
					AlertUtils.popAlert("Error found", ex.getMessage());
					this.nameTextField.setText("");
				}
			}
		});
		
		this.nameTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.nameTextField.getParent().requestFocus();
			}
		});
		
		this.notesTextArea.focusedProperty().addListener((o,oldValue,newValue)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		//whenever a VSComopnent is added/deleted or status changes, need to invoke the updateNonNullValueFromContentController() method
		
		//TODO
		
		
		
		
		//////////////
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public VisSchemeBuilder getOwnerNodeBuilder() {
		return (VisSchemeBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	
	/**
	 * must first check if owner {@link NodeBuilder}'s current status is valid value or not;
	 */
	@Override
	public VisScheme build() {
		SimpleName name = new SimpleName(this.nameTextField.getText());
		VfNotes notes = new VfNotes(this.notesTextArea.getText());
		List<VSComponent> componentPrecedenceList = new ArrayList<>();
		this.getOwnerNodeBuilder().getComponentBuilderList().forEach(cb->{
			try{
				VSComponent component = cb.getEmbeddedUIContentController().build();
				componentPrecedenceList.add(component);
			}catch(Exception e) {
				throw new VisframeException("at least one VSComponent is not valid!");
			}
		});
		
		VisSchemeBuilderImpl builder = new VisSchemeBuilderImpl(
				this.getOwnerNodeBuilder().getHostVisProjectDBContext(),
				name, 
				notes,
				componentPrecedenceList
				);
		
		return builder.getVisScheme();
	}
	
	
	/**
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException {
		this.nameTextField.setText("");
		this.notesTextArea.setText("");
		
		this.getOwnerNodeBuilder().deleteAllVSComponentBuilder();
		
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	
	@Override
	public void setUIToNonNullValue(VisScheme value) throws SQLException, IOException {
		this.nameTextField.setText(value.getName().getStringValue());
		this.notesTextArea.setText(value.getNotes().getNotesString());
		
		
		
		for(VSComponent c:value.getVSComponentPrecedenceList().getList()){
			VSComponentBuilder builder = new VSComponentBuilder(this.getOwnerNodeBuilder());
			builder.setTempVisScheme(value);//set the given VisScheme to the VSComponentBuilder before invoke the setValue method, this is because VisScheme is needed in the setValue method of the VSComponentBuilder 
			builder.setValue(c, false);
			
			this.getOwnerNodeBuilder().addVSComponentBuilder(builder);
		}
		
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	public VBox getVSComponentPrecedenceListVBox() {
		return this.vscomponentPrecedenceListVBox;
	}
	
	public HBox getVCDGraphLayoutAnchorPaneContainerHBox() {
		return this.VCDGraphLayoutAnchorPaneContainerHBox;
	}
	
	ListView<MetadataID> getMetadataListView(){
		return this.metadataListView;
	}
	ListView<OperationID> getOperationListView(){
		return this.operationListView;
	}
	ListView<CompositionFunctionGroupID> getCompositionFunctionGroupListView(){
		return this.compositionFunctionGroupListView;
	}
	ListView<CompositionFunctionID> getCompositionFunctionListView(){
		return this.compositionFunctionListView;
	}
	
	//////////////////////////
	@FXML
	private Button debugButton;
	// Event Listener on Button[#debugButton].onAction
	@FXML
	public void debugButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
		System.out.println(this.vscomponentPrecedenceListVBox.getChildren().size());
		System.out.println(this.getOwnerNodeBuilder().getComponentBuilderList().size());
		
		System.out.println();
	}
	
	
	@FXML
	public void initialize() {
		//
		this.metadataListView.setCellFactory(
				new Callback<ListView<MetadataID>, ListCell<MetadataID>>(){
					@Override
					public ListCell<MetadataID> call(ListView<MetadataID> arg0) {
						return new ListCell<>() {
							@Override
		                    protected void updateItem(MetadataID item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (item != null) {
		                            setText(item.getName().getStringValue().concat(";").concat(item.getDataType().toString()));
		                        } else {
		                            setText("");
		                        }
		                    }
						};
					}
					
				});
		this.operationListView.setCellFactory(
				new Callback<ListView<OperationID>, ListCell<OperationID>>(){
					@Override
					public ListCell<OperationID> call(ListView<OperationID> arg0) {
						return new ListCell<>() {
							@Override
		                    protected void updateItem(OperationID item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (item != null) {
		                            setText(item.getInstanceName().getStringValue());
		                        } else {
		                            setText("");
		                        }
		                    }
						};
					}
					
				});
		
		this.compositionFunctionGroupListView.setCellFactory(
				new Callback<ListView<CompositionFunctionGroupID>, ListCell<CompositionFunctionGroupID>>(){
					@Override
					public ListCell<CompositionFunctionGroupID> call(ListView<CompositionFunctionGroupID> arg0) {
						return new ListCell<>() {
							@Override
		                    protected void updateItem(CompositionFunctionGroupID item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (item != null) {
		                            setText(item.getName().getStringValue());
		                        } else {
		                            setText("");
		                        }
		                    }
						};
					}
					
				});
		this.compositionFunctionListView.setCellFactory(
				new Callback<ListView<CompositionFunctionID>, ListCell<CompositionFunctionID>>(){
					@Override
					public ListCell<CompositionFunctionID> call(ListView<CompositionFunctionID> arg0) {
						return new ListCell<>() {
							@Override
		                    protected void updateItem(CompositionFunctionID item, boolean empty) {
		                        super.updateItem(item, empty);
		                        if (item != null) {
		                            setText(item.getHostCompositionFunctionGroupID().getName().getStringValue().concat(".").concat(Integer.toString(item.getIndexID())));
		                        } else {
		                            setText("");
		                        }
		                    }
						};
					}
					
				});
		
	}

	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private Button addVSComponentButton;
	@FXML
	private Button clearVSComponentPrecedenceListButton;
	@FXML
	private Button collapseAllButton;
	@FXML
	private Button expandAllButton;
	@FXML
	private VBox vscomponentPrecedenceListVBox;
	@FXML
	private HBox VCDGraphLayoutAnchorPaneContainerHBox;
	@FXML
	private ListView<MetadataID> metadataListView;
	@FXML
	private ListView<OperationID> operationListView;
	@FXML
	private ListView<CompositionFunctionGroupID> compositionFunctionGroupListView;
	@FXML
	private ListView<CompositionFunctionID> compositionFunctionListView;
	

	
	// Event Listener on Button[#addVSComponentButton].onAction
	@FXML
	public void addVSComponentButtonOnAction(ActionEvent event) {
		VSComponentBuilder builder = new VSComponentBuilder(this.getOwnerNodeBuilder());
		this.getOwnerNodeBuilder().addVSComponentBuilder(builder);
	}
	// Event Listener on Button[#clearVSComponentPrecedenceListButton].onAction
	@FXML
	public void clearVSComponentPrecedenceListButtonOnAction(ActionEvent event) throws SQLException {
		this.getOwnerNodeBuilder().deleteAllVSComponentBuilder();
	}
	
	// Event Listener on Button[#collapseAllButton].onAction
	@FXML
	public void collapseAllButtonOnAction(ActionEvent event) {
		this.getOwnerNodeBuilder().getComponentBuilderList().forEach(cb->{
			cb.getEmbeddedUIContentController().getSelectedCoreShapeCFGSetTitledPane().setExpanded(false);
		});
	}
	// Event Listener on Button[#expandAllButton].onAction
	@FXML
	public void expandAllButtonOnAction(ActionEvent event) {
		this.getOwnerNodeBuilder().getComponentBuilderList().forEach(cb->{
			cb.getEmbeddedUIContentController().getSelectedCoreShapeCFGSetTitledPane().setExpanded(true);
		});
	}
}
