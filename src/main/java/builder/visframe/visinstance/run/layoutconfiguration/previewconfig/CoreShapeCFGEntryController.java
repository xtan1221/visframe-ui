package builder.visframe.visinstance.run.layoutconfiguration.previewconfig;

import java.io.IOException;
import java.sql.SQLException;

import builder.visframe.function.group.AbstractCompositionFunctionGroupBuilder;
import builder.visframe.function.group.CompositionFunctionGroupBuilderFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class CoreShapeCFGEntryController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/run/layoutconfiguration/previewconfig/CoreShapeCFGEntry.fxml";
	
	///////////////////////
	private CoreShapeCFGEntryManager manager;
	
	void setManager(CoreShapeCFGEntryManager manager) {
		this.manager = manager;
		
		this.coreShapeCFGIDLabel.setText(this.getManager().getCoreShapeCFG().getID().getName().getStringValue());
	}
	
	CoreShapeCFGEntryManager getManager() {
		return this.manager;
	}
	
	public Parent getRootNodePane() {
		return this.rootContainerHBox;
	}
	
	/**
	 * only invoked when showing an existing VisInstanceRunLayoutConfiguration;
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		if(modifiable) {
			throw new UnsupportedOperationException("not supported?");
		}else {
			this.controlHBox.setMouseTransparent(true);
		}
	}
	
	///////////////////////////////////////////////////
	@FXML
	public void initialize() {
		//
	}
	
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private HBox controlHBox;
	@FXML
	CheckBox toIncludeInLayoutCheckBox;
	@FXML
	private Button moveUpButton;
	@FXML
	private Button moveDownButton;
	@FXML
	private Button viewCoreShapeCFGDetailButton;
	@FXML
	ColorPicker coreShapeCFGColorPicker;
	@FXML
	private Label coreShapeCFGIDLabel;
	// Event Listener on CheckBox[#toShowCheckBox].onAction
	@FXML
	public void toIncludeInLayoutCheckBoxOnAction(ActionEvent event) throws IOException {
		if(this.toIncludeInLayoutCheckBox.isSelected()) {
			this.getManager().setIncludedInLayout(true);
			this.getManager().getOwnerPreviewAndConfigManager().updateSelectedCoreShapeCFGOrderListAfterSelection();
		}else {
			this.getManager().setIncludedInLayout(false);
			this.getManager().getOwnerPreviewAndConfigManager().removeCoreShapeCFGFromCanvass(this.getManager().getCoreShapeCFG().getID());
		}
		
	}
	
	// Event Listener on Button[#moveUpButton].onAction
	@FXML
	public void moveUpButtonOnAction(ActionEvent event) throws IOException {
		int index = this.getManager().getOwnerPreviewAndConfigManager().getCoreShapeCFGIDLayoutOrderList().indexOf(this.getManager().getCoreShapeCFG().getID());
		if(index==0) {
			//do nothing
		}else {
			this.getManager().getOwnerPreviewAndConfigManager().swapCoreShapeCFG(index, index-1);
		}
	}
	// Event Listener on Button[#moveDownButton].onAction
	@FXML
	public void moveDownButtonOnAction(ActionEvent event) throws IOException {
		int index = this.getManager().getOwnerPreviewAndConfigManager().getCoreShapeCFGIDLayoutOrderList().indexOf(this.getManager().getCoreShapeCFG().getID());
		if(index==this.getManager().getOwnerPreviewAndConfigManager().getCoreShapeCFGIDLayoutOrderList().size()-1) {
			//do nothing
		}else {
			this.getManager().getOwnerPreviewAndConfigManager().swapCoreShapeCFG(index, index+1);
		}
	}
	
	
	//////////////////////
	private Scene scene;
	private Stage stage;
	private AbstractCompositionFunctionGroupBuilder<?> coreShapeCFGBuilder;
	@FXML
	public void viewCoreShapeCFGDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage == null) {
			this.coreShapeCFGBuilder = 
					CompositionFunctionGroupBuilderFactory.singleton(this.getManager().getOwnerPreviewAndConfigManager().getHostVisProjectDBContext())
					.build(this.getManager().getCoreShapeCFG().getClass());
			
			this.coreShapeCFGBuilder.setValue(this.getManager().getCoreShapeCFG(), false);
			
			//set builder modifiable 
			this.coreShapeCFGBuilder.setModifiable(false);
			
			scene = new Scene(coreShapeCFGBuilder.getIntegrativeUIController().getRootNode());
			
			//////////////
			stage = new Stage();
			stage.setScene(scene);
			
			stage.setWidth(1200);
			stage.setHeight(800);
			stage.initModality(Modality.NONE);
			String title = this.manager.getCoreShapeCFG().getID().toString();
			stage.setTitle(title);
		}
		
		stage.showAndWait();
	}
}
