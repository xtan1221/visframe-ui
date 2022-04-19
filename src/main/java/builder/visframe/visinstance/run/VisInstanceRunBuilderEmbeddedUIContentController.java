package builder.visframe.visinstance.run;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import basic.VfNotes;
import builder.visframe.visinstance.VisInstanceBuilderBase;
import builder.visframe.visinstance.VisInstanceBuilderFactory;
import core.builder.ui.embedded.content.LeafNodeBuilderEmbeddedUIContentController;
import exception.VisframeException;
import function.variable.independent.IndependentFreeInputVariableTypeID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import visinstance.run.VisInstanceRun;
import visinstance.run.VisInstanceRunImpl;
import visinstance.run.calculation.IndependentFIVTypeIDStringValueMap;

/**
 * 
 * @author tanxu
 *
 */
public class VisInstanceRunBuilderEmbeddedUIContentController extends LeafNodeBuilderEmbeddedUIContentController<VisInstanceRun>{
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/VisInstanceRunBuilderEmbeddedUIContent.fxml";
	
	
	//////////////////////
	
	///////////////////////////////////
	@Override
	protected void setupLogicToCheckEffectiveUIInput() throws SQLException {
		//any specific initialization based on the owner builder
		this.selectedVisInstanceUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getVisInstance().getUID()));
		this.selectedVisInstanceNameTextField.setText(this.getOwnerNodeBuilder().getVisInstance().getName().getStringValue());
		this.visInstanceRunUIDTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getRunUID()));
		
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((v,manager)->{
			this.indieFIVTypeValueAssignerFlowPane.getChildren().add(manager.getController().getRootContainerPane());
		});
					
		this.numOfIndieFIVTypeOfSelectedVisInstanceTextField.setText(Integer.toString(this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().size()));
		
		///initialize
		this.getOwnerNodeBuilder().updateNonNullValueFromContentController(false);
	}
	
	@Override
	public VisInstanceRunBuilder getOwnerNodeBuilder() {
		return (VisInstanceRunBuilder) this.ownerNodeBuilder;
	}
	
	/**
	 * build and return a VisInstanceRun;
	 * @throws SQLException 
	 */
	@Override
	public VisInstanceRun build() throws SQLException {
		//check if every IndependentFreeInputVariableType has been assigned a valid value;
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((k,v)->{
			if(!v.hasValidValueAssigned()) {
				throw new VisframeException("at least one IndependentFreeInputVariableType has not been assigned a valid value!");
			}
		});
		
		///////////////
		Map<IndependentFreeInputVariableTypeID,String> independetFreeInputVariableTypeIDAssignedStringValueMap = new LinkedHashMap<>();
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((k,v)->{
			independetFreeInputVariableTypeIDAssignedStringValueMap.put(k.getID(), v.getStringValue());
		});
		IndependentFIVTypeIDStringValueMap CFDGraphIndependetFIVStringValueMap = new IndependentFIVTypeIDStringValueMap(independetFreeInputVariableTypeIDAssignedStringValueMap);
		
		VisInstanceRun ret = 
				new VisInstanceRunImpl(
					this.getOwnerNodeBuilder().getRunUID(), 
					this.getOwnerNodeBuilder().getVisInstance().getID(),
					CFDGraphIndependetFIVStringValueMap,
					new VfNotes(this.visInstanceRunNotesTextArea.getText())
					);
		
		return ret;
	}
	
	
	@Override
	public void setModifiable(boolean modifiable) {
		this.visInstanceRunNotesTextArea.setEditable(modifiable);
		
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((k,v)->{
			v.setModifiable(modifiable);
		});
	}
	
	/**
	 * 
	 */
	@Override
	public void setUIToDefaultEmptyStatus() {
		this.visInstanceRunNotesTextArea.setText("");
		
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((v,manager)->{
			manager.setStringValue("");
		});
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(false);
	}
	
	/**
	 */
	@Override
	public void setUIToNonNullValue(VisInstanceRun value) {
		this.getOwnerNodeBuilder().getIndependentFreeInputVariableTypeValueAssignerManagerMap().forEach((k,v)->{
			v.setStringValue(value.getCFDGraphIndependetFIVStringValueMap().getAssignedStringValue(k.getID()));
		});
			
		
		//
		this.getOwnerNodeBuilder().setUIVisualEffect(true);
	}
	

	
	@Override
	public Pane getRootParentNode() {
		return this.rootContainerVBox;
	}
	
	////////////////////////////
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField selectedVisInstanceUIDTextField;
	@FXML
	private TextField selectedVisInstanceNameTextField;
	@FXML
	private Button viewSelectedVisInstanceDetailButton;
	@FXML
	private TextField visInstanceRunUIDTextField;
	@FXML
	private TextArea visInstanceRunNotesTextArea;
	@FXML
	private TextField numOfIndieFIVTypeOfSelectedVisInstanceTextField;
	@FXML
	private FlowPane indieFIVTypeValueAssignerFlowPane;

	///////////////////////
	private Scene visInstanceScene;
	private Stage visInstanceStage;
	private VisInstanceBuilderBase<?,?> visInstanceBuilder;
	@FXML
	public void viewSelectedVisInstanceDetailButtonOnAction(ActionEvent event) throws IOException, SQLException {
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

}
