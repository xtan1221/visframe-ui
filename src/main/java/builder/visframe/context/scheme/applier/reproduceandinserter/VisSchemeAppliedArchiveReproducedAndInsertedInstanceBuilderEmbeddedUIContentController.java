package builder.visframe.context.scheme.applier.reproduceandinserter;

import java.io.IOException;
import java.sql.SQLException;

import basic.VfNotes;
import builder.visframe.context.scheme.VisSchemeBuilder;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilder;
import builder.visframe.context.scheme.applier.archive.VisSchemeAppliedArchiveBuilderFactory;
import context.scheme.VisScheme;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstance;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.pipeline.visscheme.appliedarchive.reproducedandinsertedinstance.steps.BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager;
import core.table.process.ProcessLogTableViewerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.FXUtils;

public class VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VisSchemeAppliedArchiveReproducedAndInsertedInstance>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/reproduceandinserter/VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() throws SQLException {
		//any specific initialization based on the owner builder
		this.appliedVisSchemeUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getAppliedVisSchemeID().getUID()));
		VisScheme appliedVisScheme = this.getOwnerNodeBuilder().getHostVisProjectDBContext().getHasIDTypeManagerController().getVisSchemeManager().lookup(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getAppliedVisSchemeID());
		this.appliedVisSchemeNameTextField.setText(appliedVisScheme.getName().getStringValue());
		this.visSchemeAppliedArchiveUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getID().getUID()));
		this.visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceUID()));
		
		//TODO
		this.finishableLabel.textProperty().addListener((obs,ov,nv)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		
	}
	
	@Override
	public VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder getOwnerNodeBuilder() {
		return (VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	/**
	 * build and return a VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 
	 * this method should only be invoked after the owner {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#isFinishable()} is checked and returns true;
	 * 
	 * see {@link BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager#finish()} from where this method will be invoked; TODO
	 * 
	 * ================================
	 * note that normally, {@link LeafNodeBuilderEmbeddedUIContentController#build()} method will be invoked during construction of the target entity to check the status of the target, 
	 * 
	 * TODO
	 * but for VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder, this feature is disabled;
	 * all the validation of the status of the target VisSchemeAppliedArchive is achieved by the {@link VisSchemeAppliedArchiveBuilder()#getSolutionSetMetadataMappingManagerChangeMadeAction()} method which updates the {@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#isFinishable()};
	 * 
	 * thus, this method is only used to build and retrieve the current valid VisSchemeAppliedArchiveReproducedAndInsertedInstance object rather than perform validation;
	 * @throws SQLException 
	 */
	@Override
	public VisSchemeAppliedArchiveReproducedAndInsertedInstance build() {
		
		return new VisSchemeAppliedArchiveReproducedAndInsertedInstance(
				new VfNotes(this.notesTextArea.getText()),
				this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getAppliedVisSchemeID(),
				this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getAppliedArchive().getID(),
				this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveReproducerAndInserter().getVisSchemeAppliedArchiveReproducedAndInsertedInstanceUID(),
				this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().getOperationReproducingAndInsertionTracker().getOriginalOperationIDWithParameterDependentOnInputDataTableContentParameterNameAssignedObjectValueMapMap(),
				this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().getOperationReproducingAndInsertionTracker().getOriginalOperationIDCopyIndexReproducedOperationIDMapMap(),
				this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().getOperationReproducingAndInsertionTracker().getReproducedAndInsertedOperationIDList(),
				this.getOwnerNodeBuilder().getCFGReproducingAndInsertionManager().getCFGReproducingAndInsertionTracker().getOriginalCFGIDCopyIndexReproducedCFGIDMapMap(),
				this.getOwnerNodeBuilder().getCFGReproducingAndInsertionManager().getCFGReproducingAndInsertionTracker().getReproducedCFGIDListByInsertionOrder(),
				this.getOwnerNodeBuilder().getCFReproducingAndInsertionManager().getCFReproducingAndInsertionTracker().getOriginalCFIDCopyIndexReproducedCFIDMapMap(),
				this.getOwnerNodeBuilder().getCFReproducingAndInsertionManager().getCFReproducingAndInsertionTracker().getReproducedCompositionFunctionIDListOrderedByDependency()
				);
	
	}
	
	
	/**
	 * set modifiable
	 * 
	 * note that this is only used when an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance is to be viewed in a view-only mode;
	 * 		thus the given modifiable must be false;
	 * 
	 * thus should always be used after {@link #setUIToNonNullValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance)} method;
	 * 
	 * specifically
	 * 1. initialize a VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder and set the value of it with a non-null VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 		{@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory#build(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 		which will eventually invoke method {@link #setUIToNonNullValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 2. set the returned VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder to un-modifiable
	 * 		{@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setModifiable(boolean)}
	 * 		which will eventually invoke {@link #setModifiable(boolean)} method
	 */
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//1 first set up the main window
		this.notesTextArea.setEditable(false);
		
		//2. set up the OperationReproducingAndInsertionManager
		this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().setModifiable(modifiable);
		
		//3 set up the CFGReproducingAndInsertionManager
		this.getOwnerNodeBuilder().getCFGReproducingAndInsertionManager().setModifiable(modifiable);
		
		//4. set up the CFReproducingAndInsertionManager
		this.getOwnerNodeBuilder().getCFReproducingAndInsertionManager().setModifiable(modifiable);
	}
	
	
	/**
	 * note that to reset a VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder, need to abort and roll back the currently running VisSchemeAppliedArchiveReproducerAndInserter than just simply reset the UI;
	 * 
	 * this feature is realized in {@link BMakeVisSchemeAppliedArchiveReproducedAndInsertedInstanceStepManager#reset()} which will also reset the UI;
	 * 
	 * thus this method should never be used;
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
//		throw new UnsupportedOperationException("unsupported"); //TODO
	}
	
	/**
	 * set to the give value;
	 * 
	 * note that this could only occur when an existing VisSchemeAppliedArchiveReproducedAndInsertedInstance is selected to be viewed for its details;
	 * 
	 * need to explicitly set up all the features
	 * 
	 * must be invoked together with {@link #setModifiable(boolean)} with parameter being false;
	 * 
	 * specifically
	 * 1. initialize a VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder and set the value of it with a non-null VisSchemeAppliedArchiveReproducedAndInsertedInstance;
	 * 		{@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilderFactory#build(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 		which will eventually invoke method {@link #setUIToNonNullValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance)}
	 * 2. set the returned VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder to un-modifiable
	 * 		{@link VisSchemeAppliedArchiveReproducedAndInsertedInstanceBuilder#setModifiable(boolean)}
	 * 		which will eventually invoke {@link #setModifiable(boolean)} method
	 * 
	 * 
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(VisSchemeAppliedArchiveReproducedAndInsertedInstance value) throws SQLException, IOException {
		////////////////////////////
		//1 first set up the main window
		this.notesTextArea.setText(value.getNotes().getNotesString());
		FXUtils.set2Disable(this.showStep1Button, false);
		FXUtils.set2Disable(this.showStep2Button, false);
		FXUtils.set2Disable(this.showStep3Button, false);
		
		this.step1StatusLabel.setText("FINISHED");
		this.step2StatusLabel.setText("FINISHED");
		this.step3StatusLabel.setText("FINISHED");
		
		//2. set the OperationReproducingAndInsertionManager
		//2.1 initialize the OperationReproducingAndInsertionManager without invoke the {@link OperationReproducingAndInsertionManager#initialize()} method
		this.getOwnerNodeBuilder().setToNewAndActivateUIOfOperationReproducingAndInsertion(true);
		this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().setValue(value);
		
		
		//3 set the CFGReproducingAndInsertionManager
		this.getOwnerNodeBuilder().setToNewAndActivateUIOfCFGReproducingAndInsertion(true);
		this.getOwnerNodeBuilder().getCFGReproducingAndInsertionManager().setValue(value);
		
		
		//4 set the CFReproducingAndInsertionManager
		this.getOwnerNodeBuilder().setToNewAndActivateUIDOfCFReproducingAndInsertion();
		this.getOwnerNodeBuilder().getCFReproducingAndInsertionManager().setValue(value);
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		FXUtils.set2Disable(this.showStep2Button, true);
		FXUtils.set2Disable(this.showStep3Button, true);
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
	private Button debugButton;
	@FXML
	private TextField visSchemeAppliedArchiveUIDTextField;
	@FXML
	private Button viewVisSchemeAppliedArchiveDetailButton;
	@FXML
	private TextField visSchemeAppliedArchiveReproducedAndInsertedInstanceUIDTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private VBox step1RootContainerVBox;
	@FXML
	private Button showStep1Button;
	@FXML
	Label step1StatusLabel;
	@FXML
	private VBox step2RootContainerVBox;
	@FXML
	Button showStep2Button;
	@FXML
	public Label step2StatusLabel;
	@FXML
	private VBox step3RootContainerVBox;
	@FXML
	Button showStep3Button;
	@FXML
	public Label step3StatusLabel;
	@FXML Label finishableLabel;
	@FXML
	ScrollPane stepShowScrollPane;

	///////////////////
	private Scene visSchemeScene;
	private Stage visSchemeStage;
	private VisSchemeBuilder visSchemeBuilder;
	@FXML
	public void viewAppliedVisSchemeDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.visSchemeBuilder==null) {
			VisScheme visScheme = this.getOwnerNodeBuilder().getVisScheme();
			visSchemeBuilder = 
					VisSchemeBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()).build(visScheme);
			visSchemeBuilder.setModifiable(false);
			
			visSchemeScene = new Scene(visSchemeBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visSchemeStage = new Stage();
			
			visSchemeStage.setScene(visSchemeScene);
			
			visSchemeStage.setWidth(1200);
			visSchemeStage.setHeight(800);
			visSchemeStage.initModality(Modality.NONE);
			
			String title = visScheme.getID().toString();
			visSchemeStage.setTitle(title);
		}
		visSchemeStage.showAndWait();
	}
	
	/**
	 * show the process log table
	 * @param event
	 * @throws SQLException
	 * @throws IOException 
	 */
	@FXML
	public void debugButtonOnAction(ActionEvent event) throws SQLException, IOException {
		ProcessLogTableViewerManager manager = 
				new ProcessLogTableViewerManager(this.getOwnerNodeBuilder().getHostVisProjectDBContext().getProcessLogTableAndProcessPerformerManager());
		manager.showWindow(this.getStage());
	}
	
	///////////////////////////
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
	
	
	@FXML
	public void showStep1ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getOperationReproducingAndInsertionManager().getController().getRootNodeContainer());
	}
	@FXML
	public void showStep2ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getCFGReproducingAndInsertionManager().getController().getRootNodeContainer());
	}
	@FXML
	public void showStep3ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getCFReproducingAndInsertionManager().getController().getRootNodeContainer());
	}
}
