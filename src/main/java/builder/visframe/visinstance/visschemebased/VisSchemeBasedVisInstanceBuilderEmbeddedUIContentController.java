package builder.visframe.visinstance.visschemebased;

import java.io.IOException;
import java.sql.SQLException;

import basic.SimpleName;
import basic.VfNotes;
import builder.visframe.context.scheme.VisSchemeBuilder;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilder;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilderFactory;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
import builder.visframe.context.scheme.applier.reproduceandinserter.VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory;
import builder.visframe.visinstance.VisInstanceBuilderEmbeddedUIContentControllerBase;
import dependency.vcd.VCDNodeImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import visinstance.VisSchemeBasedVisInstance;

public class VisSchemeBasedVisInstanceBuilderEmbeddedUIContentController extends VisInstanceBuilderEmbeddedUIContentControllerBase<VisSchemeBasedVisInstance>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/visschemebased/VisSchemeBasedVisInstanceBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() throws SQLException {
		//any specific initialization based on the owner builder
		this.appliedVisSchemeUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisScheme().getUID()));
		this.appliedVisSchemeNameTextField.setText(this.getOwnerNodeBuilder().getVisScheme().getName().getStringValue());
		this.visSchemeAppliedArchiveUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchive().getUID()));
		this.visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducedAndInsertedInstance().getUID()));
		
		this.visInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisInstanceUID()));
		
		//add the copy node selection manager
		this.vcclGraphLayoutScrollPane.setContent(this.getOwnerNodeBuilder().getCopyNodeSelectionManager().getController().getRootNodeContainer());
		
		
		//name textfield
		this.nameTextField.setStyle("-fx-border-color:red");//initialize
		this.nameTextField.focusedProperty().addListener((o,oldValue,newValue)->{
			if(!this.nameTextField.focusedProperty().get()) {//lose focus
				//check if valid name is given and update the border color
				try {
					@SuppressWarnings("unused")
					SimpleName sn = new SimpleName(this.nameTextField.getText());
					this.nameTextField.setStyle("-fx-border-color:green");
				}catch(Exception e) {
					this.nameTextField.setStyle("-fx-border-color:red");
				}
				//invoke checkFinishable
				this.getOwnerNodeBuilder().checkFinishable();
			}
		});
		
		this.nameTextField.setOnKeyPressed(e->{
			//note that ENTER key pressed will lead to the TextField lose focus!!!!!!!!!!!!!!!!!!!!!!
			if(e.getCode().equals(KeyCode.ENTER)) {
				//lose focus and thus trigger the validation
				this.nameTextField.getParent().requestFocus();
			}
		});
		
		
		//
		this.finishableLabel.textProperty().addListener((obs,ov,nv)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
	}
	
	@Override
	public VisSchemeBasedVisInstanceBuilder getOwnerNodeBuilder() {
		return (VisSchemeBasedVisInstanceBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return this.rootContainerVBox;
	}
	
	/**
	 * build and return a VisSchemeBasedVisInstance;
	 * 
	 * exception will be thrown if constraints are violated in the constructor of VisSchemeBasedVisInstance;
	 */
	@Override
	public VisSchemeBasedVisInstance build() {
		
		return new VisSchemeBasedVisInstance(
				new SimpleName(this.nameTextField.getText()),
				new VfNotes(this.notesTextArea.getText()),
				this.getOwnerNodeBuilder().getVisInstanceUID(),
				this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDSet(),
				this.getOwnerNodeBuilder().getSelectedCoreShapeCFGIDCFIDSetMap(),
				
				this.getOwnerNodeBuilder().getVisSchemeID(),
				this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveID(),
				this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceID(),
				this.getOwnerNodeBuilder().getSelectedVSCopySetInCoreShapeCFGSet()
				);
	}
	
	
	/**
	 * set modifiable
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		this.nameTextField.setEditable(modifiable);
		this.notesTextArea.setEditable(modifiable);
		
	}
	
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
		this.nameTextField.setText("");
		this.notesTextArea.setText("");
		this.nameTextField.setStyle("-fx-border-color:red");
		
		this.getOwnerNodeBuilder().getCopyNodeSelectionManager().selectAll();
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	/**
	 * set to the give value;
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(VisSchemeBasedVisInstance value) throws SQLException, IOException {
		this.setUIToDefaultEmptyStatus();
		
		//
		this.nameTextField.setText(value.getName().getStringValue());
		this.nameTextField.setStyle("-fx-border-color:green");
		
		this.notesTextArea.setText(value.getNotes().getNotesString());
		
		//
		this.getOwnerNodeBuilder().getCopyNodeSelectionManager().clearAll();
		value.getSelectedVSCopySetInCoreShapeCFGSet().forEach(copy->{
			VCDNodeImpl vcdNode = copy.getOwnerVCDNode();
			
			int copyIndex = copy.getIndex();
			
			this.getOwnerNodeBuilder().getCopyNodeSelectionManager().getDAGNodeManagerMap().get(vcdNode).getCopyIndexNodeCopyManagerMap().get(copyIndex).setSelected(true);
		});
		
		//
		this.getOwnerNodeBuilder().checkFinishable();
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		//TODO
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField appliedVisSchemeUIDTextField;
	@FXML
	private TextField appliedVisSchemeNameTextField;
	@FXML
	private Button viewAppliedVisSchemeDetailButton;
	@FXML
	private TextField visSchemeAppliedArchiveUIDTextField;
	@FXML
	private Button viewVisSchemeAppliedArchiveDetailButton;
	@FXML
	private TextField visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField;
	@FXML
	private Button viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButton;
	@FXML
	private TextField visInstanceUIDTextField;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	Label finishableLabel;
	@FXML
	private Button viewCFDGraphButton;
	@FXML
	private Button checkDuplicateVisInstanceButton;
	@FXML
	private ScrollPane vcclGraphLayoutScrollPane;

	///////////////////
	private Scene visSchemeScene;
	private Stage visSchemeStage;
	private VisSchemeBuilder visSchemeBuilder;
	
	/**
	 * @param event
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@FXML
	public void viewAppliedVisSchemeDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.visSchemeBuilder==null) {
			visSchemeBuilder = 
					VisSchemeBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()).build(this.getOwnerNodeBuilder().getVisScheme());
			visSchemeBuilder.setModifiable(false);
			
			visSchemeScene = new Scene(visSchemeBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visSchemeStage = new Stage();
			
			visSchemeStage.setScene(visSchemeScene);
			
			visSchemeStage.setWidth(1200);
			visSchemeStage.setHeight(800);
			visSchemeStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getVisScheme().getID().toString();
			visSchemeStage.setTitle(title);
		}
		visSchemeStage.showAndWait();
	}
	
	
	////////////////////////
	private Scene visSchemeAppliedArchiveScene;
	private Stage visSchemeAppliedArchiveStage;
	private VisSchemeAppliedArchiveBuilder visSchemeAppliedArchiveBuilder;
	@FXML
	public void viewVisSchemeAppliedArchiveDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.visSchemeAppliedArchiveBuilder==null) {
			visSchemeAppliedArchiveBuilder = 
					VisSchemeAppliedArchiveBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()).build(this.getOwnerNodeBuilder().getVisSchemeAppliedArchive());
			visSchemeAppliedArchiveBuilder.setModifiable(false);
			
			visSchemeAppliedArchiveScene = new Scene(visSchemeAppliedArchiveBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visSchemeAppliedArchiveStage = new Stage();
			
			visSchemeAppliedArchiveStage.setScene(visSchemeAppliedArchiveScene);
			
			visSchemeAppliedArchiveStage.setWidth(1200);
			visSchemeAppliedArchiveStage.setHeight(800);
			visSchemeAppliedArchiveStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getVisSchemeAppliedArchive().getID().toString();
			visSchemeAppliedArchiveStage.setTitle(title);
		}
		visSchemeAppliedArchiveStage.showAndWait();
	}
	
	
	
	//////////////////////////
	private Scene visSchemeAppliedArchiveReproducedAndInsertedInstanceScene;
	private Stage visSchemeAppliedArchiveReproducedAndInsertedInstanceStage;
	private VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder;
	@FXML
	public void viewVisSchemeAppliedArchiveReproducedAndInsertedInstanceDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder==null) {
			visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder = 
					VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()).build(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducedAndInsertedInstance());
			visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.setModifiable(false);
			
			visSchemeAppliedArchiveReproducedAndInsertedInstanceScene = new Scene(visSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage = new Stage();
			
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.setScene(visSchemeAppliedArchiveReproducedAndInsertedInstanceScene);
			
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.setWidth(1200);
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.setHeight(800);
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getVisScheme().getID().toString();
			visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.setTitle(title);
		}
		visSchemeAppliedArchiveReproducedAndInsertedInstanceStage.showAndWait();
	}
	
	
	// Event Listener on Button[#viewCFDGraphButton].onAction
	@FXML
	public void viewCFDGraphButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
	// Event Listener on Button[#checkDuplicateVisInstanceButton].onAction
	@FXML
	public void checkDuplicateVisInstanceButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
}
