package core.table.visframeUDT.multipleselection;

import basic.lookup.PrimaryKeyID;
import basic.lookup.VisframeUDT;
import core.builder.LeafNodeBuilder;
import core.builder.NodeBuilder;
import core.builder.NonLeafNodeBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.SizePropertyUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class VisframeUDTTypeMultiSelectorController<T extends VisframeUDT, I extends PrimaryKeyID<T>> {
	public static final String FXML_FILE_DIR_STRING = "/core/table/visframeUDT/multipleselection/VisframeUDTTypeMultiSelector.fxml";
	
	private VisframeUDTTypeMultiSelectorManager<T,I> manager;
	
	/**
	 * set manager;
	 * 
	 * also set the TableView
	 * @param manager
	 */
	void setManager(VisframeUDTTypeMultiSelectorManager<T,I> manager) {
		this.manager = manager;
		
		//hide the selection related buttons
//		this.selectionRelatedButtonHBox.setVisible(!this.manager.getMode().equals(TableViewerMode.VIEW_ONLY));
		//
		this.getTableViewContainer().getChildren().add(this.manager.getTableViewDelegate().getTableView());
		
		SizePropertyUtils.childNodeResizeWithParentNode(this.manager.getTableViewDelegate().getTableView(), this.getTableViewContainer());
		
		//when the window is closed, need to clear the selected row in the VisframeUDTTypeTableViewDelegate
//		this.getStage().setOnCloseRequest(e->{
//			this.manager.getTableViewDelegate().clearSelectedRow();
//		});
	}
	
	Pane getTableViewContainer() {
		return tableViewContainerHBox;
	}
	
	private Stage getStage() {
		return (Stage) this.getTableViewContainer().getScene().getWindow();
	}
	
	//////////////////////////////////////////////////
	@FXML
	public void initialize() {

	}
	/**
	 * the root container for the window; 
	 */
	@FXML
	private VBox tableViwerContainerVBox;
	@FXML
	private HBox tableViewContainerHBox;
	@FXML
	private Button viewSelectedItemDetailButton;
	@FXML
	private HBox selectionRelatedButtonHBox;
	@FXML
	private Button finishButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button clearButton;
	
	// Event Listener on Button[#finishButton].onAction
	@FXML
	public void finishButtonOnAction(ActionEvent event) {
		try {
			if(this.manager.getTableViewDelegate().getCurrentlySelectedRowSet().isEmpty()) {
				AlertUtils.popAlert("Warning", "No item is selected!");
			}else {
//				if(!this.manager.getMode().equals(TableViewerMode.VIEW_ONLY)) {
//					PrimaryKeyID<?> id = this.manager.getTableViewDelegate().getCurrentlySelectedRow().getTableRow().getEntity().getID();
//					
//					for(SimpleName name:this.manager.getInvokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap().keySet()) {
//						this.manager.getInvokerSelectedVisframeUDTEntityPrimaryKeyIDAttributeNameDisplayTextFieldMap().get(name).setText(id.getPrimaryKeyAttributeNameStringValueMap().get(name));
//					}
//					
//					//perform any 
//					if(this.manager.getOperationAfterSelectionIsDone()!=null){
//						this.manager.getOperationAfterSelectionIsDone().run();
//					}
					
//				}
				this.manager.closeWindow();
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".finishButtonOnAction"), e, this.getStage());
		}
	}

	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancelButtonOnAction(ActionEvent event) {
		try {
//			if(this.manager.getTableViewDelegate().getCurrentlySelectedRow()!=null) {
//				this.manager.getTableViewDelegate().getCurrentlySelectedRow().setSelected(false);
//			}
			
			this.clearButtonOnAction(event);
			
			this.manager.closeWindow();
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".cancelButtonOnAction"), e, this.getStage());
		}
	}
	// Event Listener on Button[#clearButton].onAction
	/**
	 * clear any selected row (checkbox)
	 * @param event
	 */
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		try {
			this.manager.getTableViewDelegate().getCurrentlySelectedRowSet().forEach(row->{
				row.setSelected(false);
			});
			
			this.manager.getTableViewDelegate().getCurrentlySelectedRowSet().clear();
			
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".clearButtonOnAction"), e, this.getStage());
		}
	}
	
	// Event Listener on Button[#viewSelectedItemDetailButton].onAction
	/**
	 * this method is hard coded, need to be improved later;
	 * @param event
	 */
	@FXML
	public void viewSelectedItemDetailButtonOnAction(ActionEvent event) {
		try {
			if(this.manager.getTableViewDelegate().getCurrentlySelectedRowSet().size()!=1) {
				AlertUtils.popAlert("Warning", "Can only view whenn there is one single item selected!");
			}else {
				
				T entity = (T)this.manager.getTableViewDelegate().getCurrentlySelectedRowSet().iterator().next().getTableRow().getEntity();
				
				NodeBuilder<? extends T,?> builder = this.manager.getNodeBuilderFactory().build(entity);
				
				//set builder modifiable 
				builder.setModifiable(false);
				
				Scene scene;
				
				if(builder instanceof NonLeafNodeBuilder<?>) {
					NonLeafNodeBuilder<?> nnb = (NonLeafNodeBuilder<?>)builder;
//					nnb.getIntegrativeUIController().showAndWait(null, title);
					scene = new Scene(nnb.getIntegrativeUIController().getRootNode());
				}else {//LeafNodeBuilder, the shown window contains the embedded content UI
					LeafNodeBuilder<?,?> lnb = (LeafNodeBuilder<?,?>)builder;
					scene = new Scene(lnb.getEmbeddedUIRootContainerNodeController().getRootContainerPane());
				}
				
				
				
				//////////////
				Stage stage = new Stage();
				stage.setScene(scene);
				
				stage.setWidth(1200);
				stage.setHeight(800);
				stage.initModality(Modality.NONE);
				String title = entity.getID().toString();
				stage.setTitle(title);
				stage.showAndWait();
			}
			
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".finishButtonOnAction"), e, this.getStage());
		}
	}
	
	
}
