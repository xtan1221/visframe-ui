package builder.visframe.visinstance.nativevi.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.FXUtils;

/**
 * 
 * @author tanxu
 *
 */
public class CoreShapeCFGController {
	public static final String FXML_FILE_DIR_STRING = "/builder/visframe/visinstance/nativevi/utils/CoreShapeCFG.fxml";
	
	///////////////////////////////////
	private CoreShapeCFGManager manager;
	
	/**
	 * set manager;
	 * 
	 * then add each CoreShapeCFManager to the UI
	 * @param manager
	 */
	void setManager(CoreShapeCFGManager manager) {
		this.manager = manager;
		
		///
		this.cfgNameTextField.setText(this.manager.getCoreShapeCFG().getName().getStringValue());
		this.ownerRecordDataNameTextField.setText(this.manager.getCoreShapeCFG().getOwnerRecordDataMetadataID().getName().getStringValue());
		
		//add each CoreShapeCFManager to the UI with the order based on the cf index;
		List<Integer> cfIndexList = new ArrayList<>();
		
		this.getManager().getCfIndexCoreShapeCFManagerMap().keySet().forEach(index->{
			cfIndexList.add(index);
		});
		
		Collections.sort(cfIndexList);
		
		cfIndexList.forEach(index->{
			this.compositionFunctionContainerVBox.getChildren().add(
					this.getManager().getCfIndexCoreShapeCFManagerMap().get(index).getController().getRootNodeContainer()
			);
		});
	}
	
	/**
	 * @return the manager
	 */
	public CoreShapeCFGManager getManager() {
		return manager;
	}
	
	
	
	public Parent getRootNodeContainer() {
		return this.rootContainerVBox;
	}
	
	
	void setSelectedCFNumber(int num) {
		this.compositionFunctionSelectionTitledPane.setText("CompositionFunction selection (selected # = ".concat(Integer.toString(num).concat(")")));
	}
	

	public void setCompositionFunctionSelectionTitledPaneExpanded(boolean expanded) {
		this.compositionFunctionSelectionTitledPane.setExpanded(expanded);
	}
	
	
	/**
	 * 
	 * @param modifiable
	 */
	void setModifiable(boolean modifiable) {
		FXUtils.set2Disable(this.removeCFGButton, !modifiable);
		FXUtils.set2Disable(compositionFunctionSelectionControlHBox, !modifiable);
	}
	
	/////////////////////////////////////
	@FXML
	public void initialize() {
		
		//
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField cfgNameTextField;
	@FXML
	private TextField ownerRecordDataNameTextField;
	@FXML
	private Button viewCFGDetailButton;
	@FXML
	private Button removeCFGButton;
	@FXML
	private TitledPane compositionFunctionSelectionTitledPane;
	@FXML
	private HBox compositionFunctionSelectionControlHBox;
	@FXML
	private Button selectAllCompositionFunctionButton;
	@FXML
	private Button deSelectAllCompositionFunctionButton;
	@FXML
	private VBox compositionFunctionContainerVBox;

	// Event Listener on Button[#removeCFGButton].onAction
	@FXML
	public void removeCFGButtonOnAction(ActionEvent event) throws SQLException {
		this.getManager().getHostNativeVisInstanceBuilder().removeSelectedShapeCFG(this.getManager().getCoreShapeCFG().getID());
	}
	
	// Event Listener on Button[#selectAllCompositionFunctionButton].onAction
	@FXML
	public void selectAllCompositionFunctionButtonOnAction(ActionEvent event) throws SQLException {
		this.getManager().getCfIndexCoreShapeCFManagerMap().forEach((index,manager)->{
			manager.setSelected(true);
		});
		
		//
		this.getManager().getHostNativeVisInstanceBuilder().checkFinishable();
	}
	
	// Event Listener on Button[#deSelectAllCompositionFunctionButton].onAction
	@FXML
	public void deSelectAllCompositionFunctionButtonOnAction(ActionEvent event) {
		this.getManager().getCfIndexCoreShapeCFManagerMap().forEach((index,manager)->{
			manager.setSelected(false);
		});
		
		this.getManager().getHostNativeVisInstanceBuilder().checkFinishable();
	}
	
	
	//////////////////////
	private Scene scene;
	private Stage stage;
	/**
	 * 
	 * @param event
	 * @throws SQLException
	 * @throws IOException
	 */
	@FXML
	public void viewCFGDetailButtonOnAction(ActionEvent event) throws SQLException, IOException {
		if(this.stage == null) {
			//set builder modifiable 
			this.manager.getCoreShapeCFGBuilder().setModifiable(false);
			
			scene = new Scene(this.manager.getCoreShapeCFGBuilder().getIntegrativeUIController().getRootNode());
			
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
