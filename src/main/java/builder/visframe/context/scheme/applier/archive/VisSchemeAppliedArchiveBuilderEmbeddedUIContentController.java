package builder.visframe.context.scheme.applier.archive;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import basic.VfNotes;
import builder.visframe.context.scheme.VisSchemeBuilder;
import builder.visframe.context.scheme.VisSchemeBuilderFactory;
import context.scheme.appliedarchive.VisSchemeAppliedArchive;
import context.scheme.appliedarchive.VisSchemeAppliedArchiveFinisher;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import core.pipeline.cf.steps.BMakeCompositionFunctionStepManager;
import dependency.vccl.utils.NodeCopy;
import dependency.vcd.VCDNodeImpl;
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

public class VisSchemeAppliedArchiveBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VisSchemeAppliedArchive>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/context/scheme/applier/archive/VisSchemeAppliedArchiveBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() {
		//any specific initialization based on the owner builder
		this.appliedVisSchemeUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getAppliedVisScheme().getID().getUID()));
		this.appliedVisSchemeNameTextField.setText(this.getOwnerNodeBuilder().getAppliedVisScheme().getName().getStringValue());
		this.visSchemeAppliedArchiveUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveUID()));
		
	
		this.finishableLabel.textProperty().addListener((obs,ov,nv)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public VisSchemeAppliedArchiveBuilder getOwnerNodeBuilder() {
		return (VisSchemeAppliedArchiveBuilder) this.ownerNodeBuilder;
	}
	
	
	@Override
	public Pane getRootParentNode() {
		return rootContainerVBox;
	}
	
	/**
	 * build and return a VisSchemeAppliedArchive;
	 * 
	 * this method should only be invoked after the owner {@link VisSchemeAppliedArchiveBuilder#isFinishable()} is checked and returns true;
	 * 
	 * see {@link BMakeCompositionFunctionStepManager#finish()} from where this method will be invoked; TODO
	 * 
	 * ================================
	 * note that normally, {@link LeafNodeBuilderEmbeddedUIContentController#build()} method will be invoked during construction of the target entity to check the status of the target, 
	 * but for VisSchemeAppliedArchiveBuilder, this feature is disabled;
	 * all the validation of the status of the target VisSchemeAppliedArchive is achieved by the {@link VisSchemeAppliedArchiveBuilder()#getSolutionSetMetadataMappingManagerChangeMadeAction()} method which updates the {@link VisSchemeAppliedArchiveBuilder#isFinishable()};
	 * 
	 * thus, this method is only used to build and retrieve the current valid VisSchemeAppliedArchive object rather than perform validation;
	 * @throws SQLException 
	 */
	@Override
	public VisSchemeAppliedArchive build() {
		try {
			VisSchemeAppliedArchiveFinisher finisher = new VisSchemeAppliedArchiveFinisher(
					this.getOwnerNodeBuilder().getAppliedVisScheme(),
					new VfNotes(this.notesTextArea.getText()),
					this.getOwnerNodeBuilder().getVisSchemeAppliedArchiveUID(),
					this.getOwnerNodeBuilder().getFullVCDGraph().getUnderlyingGraph(),
					this.getOwnerNodeBuilder().getVcclGraph(),
					this.getOwnerNodeBuilder().getIntegratedCFDGraphTrimmer().getTrimmedIntegratedCFDUnderlyingGraph(),
					this.getOwnerNodeBuilder().getIntegratedDOSGraphTrimmer().getTrimmedIntegratedDOSUnderlyingGraph(),
					this.getOwnerNodeBuilder().getSolutionSetMetadataMappingManager().buildIntegratedDOSGraphNodeMetadataMappingMap()
					);
			return finisher.build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null; //debug
		}
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) throws SQLException, IOException {
		//vfnotes
		this.notesTextArea.setEditable(modifiable);
		
		//set DAGNodeCopyNumberAssignmentManager
		this.getOwnerNodeBuilder().getVcdNodeCopyNumberAssignmentManager().setModifiable(modifiable);
		
		//set CopyLinkAssignerManager
		this.getOwnerNodeBuilder().getCopyLinkAssignerManager().setModifiable(modifiable);
		
		//set DAGSolutionSetSelectorManager
		this.getOwnerNodeBuilder().getSolutionSetSelectorManager().setModifiable(modifiable);
		
		//set SolutionSetMetadataMappingManager
		this.getOwnerNodeBuilder().getSolutionSetMetadataMappingManager().setModifiable(modifiable);
	}
	
	
	
	/**
	 * set the related fields in the owner {@link #VisSchemeAppliedArchiveBuilder} to default empty status 
	 * 		the whole process of VisSchemeAppliedArchive building is set to initial status;
	 * 
	 * also set the UI to default empty;
	 * 
	 * ================================
	 * 
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
		//
		this.notesTextArea.setText("");
		
		
		//restart the VisSchemeAppliedArchive building process
		//this will also set the UI to initial status
		this.getOwnerNodeBuilder().setToNewAndActivateUIOfCopyNumberAssignmentStep();
		
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * 
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(VisSchemeAppliedArchive value) throws SQLException, IOException {
		//0 first set to default empty
		this.setUIToDefaultEmptyStatus();
		
		//1. notes
		this.notesTextArea.setText(value.getNotes().getNotesString());
		
		
		///////////////////////
		//step 1 copy number assignment
		Map<VCDNodeImpl,Map<Integer,NodeCopy<VCDNodeImpl>>> nodeCopyIndexNodeCopyMap = new HashMap<>();
		value.getVisSchemeVCDGraph().vertexSet().forEach(v->{
			nodeCopyIndexNodeCopyMap.put(v, new HashMap<>());
			v.getVSCopyIndexMap().forEach((index, vscopy)->{
				nodeCopyIndexNodeCopyMap.get(v).put(index, new NodeCopy<>(v, index, vscopy.getNotes()));
			});
		});
		this.getOwnerNodeBuilder().getVcdNodeCopyNumberAssignmentManager().setNodeCopyIndexNodeCopyMap(nodeCopyIndexNodeCopyMap);
		//trigger initialization of the next step
		this.getOwnerNodeBuilder().getDAGNodeCopyNumberAssignmentManagerAfterChangeMadeAction().run();
		
		//step 2 copy link assignment
		this.getOwnerNodeBuilder().getCopyLinkAssignerManager().setLinks(value.getDependingNodeCopyIndexDependedNodeLinkedCopyIndexMapMapMap());
		this.getOwnerNodeBuilder().getCopyLinkAssignerManagerAfterChangeMadeAction().run();
		
		//step 3 integrated CFD graph building and trimming
		//do nothing, automatically triggered from this.getOwnerNodeBuilder().getCopyLinkAssignerManagerChangeMadeAction()
		
		//step 4 integrated DOS graph building and trimming
		//do nothing, automatically triggered from this.getOwnerNodeBuilder().getCopyLinkAssignerManagerChangeMadeAction()
		
		//step 5 solution set selection
		this.getOwnerNodeBuilder().getSolutionSetSelectorManager().setSelectedSet(value.getSelectedSolutionSetNodeMappingMap().keySet());
		this.getOwnerNodeBuilder().getSolutionSetSelectorManagerChangeMadeAction().run();
		
		//step 6 solution set metadata mapping creation
		this.getOwnerNodeBuilder().getSolutionSetMetadataMappingManager().setAllMappings(value.getSelectedSolutionSetNodeMappingMap());
		this.getOwnerNodeBuilder().getSolutionSetMetadataMappingManagerChangeMadeAction().run();
		
		
		////////////////////////////
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	
	//////////////////////////
	
	
	@FXML
	public void initialize() {
		FXUtils.set2Disable(this.showStep2Button, true);
		FXUtils.set2Disable(this.showStep3Button, true);
		FXUtils.set2Disable(this.showStep4Button, true);
		FXUtils.set2Disable(this.showStep5Button, true);
		FXUtils.set2Disable(this.showStep6Button, true);
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField appliedVisSchemeUIDTextField;
	@FXML
	private TextField appliedVisSchemeNameTextField;
	@FXML
	private Button viewVisSchemeDetailButton;
	@FXML
	private Button debugButton;
	@FXML
	private TextField visSchemeAppliedArchiveUIDTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	VBox step1RootContainerVBox;
	@FXML
	Button showStep1Button;
	@FXML
	Label step1StatusLabel;
	@FXML
	VBox step2RootContainerVBox;
	@FXML
	Button showStep2Button;
	@FXML
	Label step2StatusLabel;
	@FXML
	VBox step3RootContainerVBox;
	@FXML
	Button showStep3Button;
	@FXML
	Label step3StatusLabel;
	@FXML
	VBox step4RootContainerVBox;
	@FXML
	Button showStep4Button;
	@FXML
	Label step4StatusLabel;
	@FXML
	VBox step5RootContainerVBox;
	@FXML
	Button showStep5Button;
	@FXML
	Label step5StatusLabel;
	@FXML
	VBox step6RootContainerVBox;
	@FXML
	Button showStep6Button;
	@FXML
	Label step6StatusLabel;
	@FXML
	Label finishableLabel;
	@FXML
	ScrollPane stepShowScrollPane;
	
	
	///////////////////
	private Scene visSchemeScene;
	private Stage visSchemeStage;
	private VisSchemeBuilder visSchemeBuilder;
	@FXML
	public void viewVisSchemeDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.visSchemeBuilder==null) {
			visSchemeBuilder = 
					VisSchemeBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext()).build(this.getOwnerNodeBuilder().getAppliedVisScheme());
			visSchemeBuilder.setModifiable(false);
			
			visSchemeScene = new Scene(visSchemeBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visSchemeStage = new Stage();
			
			visSchemeStage.setScene(visSchemeScene);
			
			visSchemeStage.setWidth(1200);
			visSchemeStage.setHeight(800);
			visSchemeStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getAppliedVisScheme().getID().toString();
			visSchemeStage.setTitle(title);
		}
		visSchemeStage.showAndWait();
	}
	
	
	
	// Event Listener on Button[#debugButton].onAction
	@FXML
	public void debugButtonOnAction(ActionEvent event) {
		
	}
	
	/**
	 * show the UI of copy number assignment
	 * @param event
	 */
	// Event Listener on Button[#showStep1Button].onAction
	@FXML
	public void showStep1ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getVcdNodeCopyNumberAssignmentManager().getController().getRootNodeContainer());
	}
	/**
	 * show the UI of the copy link assignment
	 * @param event
	 */
	// Event Listener on Button[#showStep2Button].onAction
	@FXML
	public void showStep2ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getCopyLinkAssignerManager().getController().getRootNodeContainer());
	}
	/**
	 * show the UI of the trimmed integrated CFD graph
	 * @param event
	 */
	// Event Listener on Button[#showStep3Button].onAction
	@FXML
	public void showStep3ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getIntegratedCFDGraphViewerManager().getController().getRootNodeContainer());
	}
	/**
	 * show the UI of the trimmed integrated DOS graph
	 * @param event
	 */
	// Event Listener on Button[#showStep4Button].onAction
	@FXML
	public void showStep4ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getIntegratedDOSGraphViewerManager().getController().getRootNodeContainer());
	}
	/**
	 * show the UI of the solution set selection
	 * @param event
	 */
	// Event Listener on Button[#showStep5Button].onAction
	@FXML
	public void showStep5ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getSolutionSetSelectorManager().getController().getRootNodeContainer());
	}
	/**
	 * show the UI of solution set mapping creation;
	 * @param event
	 */
	// Event Listener on Button[#showStep6Button].onAction
	@FXML
	public void showStep6ButtonOnAction(ActionEvent event) {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getSolutionSetMetadataMappingManager().getController().getRootNodeContainer());
	}
}
