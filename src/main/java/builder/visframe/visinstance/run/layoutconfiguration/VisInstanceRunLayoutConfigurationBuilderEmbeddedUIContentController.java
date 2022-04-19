package builder.visframe.visinstance.run.layoutconfiguration;

import java.io.IOException;
import java.sql.SQLException;

import basic.VfNotes;
import builder.visframe.visinstance.VisInstanceBuilderBase;
import builder.visframe.visinstance.VisInstanceBuilderFactory;
import builder.visframe.visinstance.run.VisInstanceRunBuilder;
import builder.visframe.visinstance.run.VisInstanceRunBuilderFactory;
import builder.visframe.visinstance.run.layoutconfiguration.previewconfig.VisInstanceRunLayoutConfigurationWrapper;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.FXUtils;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfiguration;
import visinstance.run.layoutconfiguration.VisInstanceRunLayoutConfigurationImpl;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunLayoutConfigurationBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VisInstanceRunLayoutConfiguration>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/layoutconfiguration/VisInstanceRunLayoutConfigurationBuilderEmbeddedUIContent.fxml";
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() throws SQLException {
		//any specific initialization based on the owner builder
		this.visInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisInstance().getUID()));
		this.visInstanceRunUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisInstanceRun().getRunUID()));
		this.visInstanceRunLayoutConfigurationUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getUID()));
		
		//
		this.finishableLabel.textProperty().addListener((obs,ov,nv)->{
			this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
		});
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public VisInstanceRunLayoutConfigurationBuilder getOwnerNodeBuilder() {
		return (VisInstanceRunLayoutConfigurationBuilder) this.ownerNodeBuilder;
	}
	
	/**
	 * build and return a VisInstanceRunLayoutConfiguration;
	 * 
	 * exception will be thrown if not finishable???
	 * 
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public VisInstanceRunLayoutConfiguration build() throws IOException {
		VisInstanceRunLayoutConfigurationWrapper configurationWrapper = 
				this.getOwnerNodeBuilder().getPreviewAndConfigManager().getCurrentVisInstanceRunLayoutConfigurationWrapper();
		
		return new VisInstanceRunLayoutConfigurationImpl(
				this.getOwnerNodeBuilder().getVisInstanceRunID(),
				this.getOwnerNodeBuilder().getUID(),
				new VfNotes(this.visInstanceRunLayoutConfigurationUIDTextFieldNotesTextArea.getText()),
				/////////////
				this.getOwnerNodeBuilder().getPreviewAndConfigManager().getFullRegionStartX(),
				this.getOwnerNodeBuilder().getPreviewAndConfigManager().getFullRegionStartY(),
				this.getOwnerNodeBuilder().getPreviewAndConfigManager().getFullRegionEndX(),
				this.getOwnerNodeBuilder().getPreviewAndConfigManager().getFullRegionEndY(),
				////////////
				configurationWrapper.getSelectedCoreShapeCFGIDListInLayoutOrder(),
				configurationWrapper.isFullRegionSelected(),
				configurationWrapper.getX1(),
				configurationWrapper.getY1(),
				configurationWrapper.getX2(),
				configurationWrapper.getY2()
				);
			
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) throws IOException {
		//vfnotes
		this.visInstanceRunLayoutConfigurationUIDTextFieldNotesTextArea.setEditable(modifiable);
		
		//
		this.getOwnerNodeBuilder().getPreviewAndConfigManager().setModifiable(modifiable);
	}
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() throws SQLException, IOException {
		//
		this.visInstanceRunLayoutConfigurationUIDTextFieldNotesTextArea.setText("");
		
		this.getOwnerNodeBuilder().setToNewAndActivateUIOfPrevoiusAndConfigurationStep();
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 * @throws IOException 
	 * @throws SQLException 
	 */
	@Override
	public void setUIToNonNullValue(VisInstanceRunLayoutConfiguration value) throws SQLException, IOException {
		//0 first set to default empty
		this.setUIToDefaultEmptyStatus();
		
		//1. notes
		this.visInstanceRunLayoutConfigurationUIDTextFieldNotesTextArea.setText(value.getNotes().getNotesString());
		
		//set step 1
		VisInstanceRunLayoutConfigurationWrapper config = 
				new VisInstanceRunLayoutConfigurationWrapper(
						value.isFullRegionSelected(),
						value.getX1(),
						value.getY1(),
						value.getX2(),
						value.getY2(),
						value.getCoreShapeCFGIDListInLayoutOrder()
						);
		
		
		this.getOwnerNodeBuilder().getPreviewAndConfigManager().setConfiguration(config);
		this.getOwnerNodeBuilder().getPreviewAndConfigManagerAfterChangeMadeAction().run();
		
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	

	
	@Override
	public Parent getRootParentNode() {
		return this.rootContainerSplitPane;
	}
	
	////////////////////////////
	
	@FXML
	public void initialize() {
		FXUtils.set2Disable(this.showStep2Button, true);
		FXUtils.set2Disable(this.showStep3Button, true);
	}

	@FXML
	private SplitPane rootContainerSplitPane;
	@FXML
	private TextField visInstanceUIDTextField;
	@FXML
	private Button viewVisInstanceDetailButton;
	@FXML
	private TextField visInstanceRunUIDTextField;
	@FXML
	private Button viewVisInstanceRunDetailButton;
	@FXML
	private TextField visInstanceRunLayoutConfigurationUIDTextField;
	@FXML
	private TextArea visInstanceRunLayoutConfigurationUIDTextFieldNotesTextArea;
	@FXML
	private VBox step1RootContainerVBox;
	@FXML
	private Button showStep1Button;
	@FXML Label step1StatusLabel;
	@FXML VBox step2RootContainerVBox;
	@FXML Button showStep2Button;
	@FXML Label step2StatusLabel;
	@FXML VBox step3RootContainerVBox;
	@FXML Button showStep3Button;
	@FXML Label finishableLabel;
	@FXML
	private ScrollPane stepShowScrollPane;

	///////////////////////
	private Scene visInstanceScene;
	private Stage visInstanceStage;
	private VisInstanceBuilderBase<?,?> visInstanceBuilder;
	@FXML
	public void viewVisInstanceDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.visInstanceBuilder==null) {
			visInstanceBuilder = 
					VisInstanceBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext())
					.build(this.getOwnerNodeBuilder().getVisInstance());
			visInstanceBuilder.setModifiable(false);
			
			visInstanceScene = new Scene(visInstanceBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
				//////////////
			visInstanceStage = new Stage();
			
			visInstanceStage.setScene(visInstanceScene);
			
			visInstanceStage.setWidth(1200);
			visInstanceStage.setHeight(800);
			visInstanceStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getVisInstance().getID().toString();
			visInstanceStage.setTitle(title);
		}
		visInstanceStage.showAndWait();
	}

	
	/////////////////////
	private Scene visInstanceRunScene;
	private Stage visInstanceRunStage;
	private VisInstanceRunBuilder visInstanceRunBuilder;
	@FXML
	public void viewVisInstanceRunDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.visInstanceRunBuilder==null) {
			visInstanceRunBuilder = 
					VisInstanceRunBuilderFactory.singleton(this.getOwnerNodeBuilder().getHostVisProjectDBContext())
					.build(this.getOwnerNodeBuilder().getVisInstanceRun());
			visInstanceRunBuilder.setModifiable(false);
			
			visInstanceRunScene = new Scene(visInstanceRunBuilder.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				
			//////////////
			visInstanceRunStage = new Stage();
			
			visInstanceRunStage.setScene(visInstanceRunScene);
			
			visInstanceRunStage.setWidth(1200);
			visInstanceRunStage.setHeight(800);
			visInstanceRunStage.initModality(Modality.NONE);
			String title = this.getOwnerNodeBuilder().getVisInstance().getID().toString();
			visInstanceRunStage.setTitle(title);
		}
		
		visInstanceRunStage.showAndWait();
	}
	
	
	// Event Listener on Button[#showStep1Button].onAction
	@FXML
	public void showStep1ButtonOnAction(ActionEvent event) throws IOException {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getPreviewAndConfigManager().getController().getRootContainerPane());
	}
	// Event Listener on Button[#showStep2Button].onAction
	@FXML
	public void showStep2ButtonOnAction(ActionEvent event) throws SQLException {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getDisplayManager().getController().getRootContainerPane());
		
//		//also add the canvass AnchorPane back to the step (this is needed if the canvass AnchorPane is used to save as image which will put it in a new Scene)
//		this.getOwnerNodeBuilder().getDisplayManager().getController().reAddCanvassAnchorPaneToScrollPane();
	}
	// Event Listener on Button[#showStep3Button].onAction
	@FXML
	public void showStep3ButtonOnAction(ActionEvent event) throws SQLException {
		this.stepShowScrollPane.setContent(this.getOwnerNodeBuilder().getFXNodeSaveAsImageManager().getController().getRootContainerPane());
	}
}
