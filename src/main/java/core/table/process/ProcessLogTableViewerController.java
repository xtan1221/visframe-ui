package core.table.process;

import java.util.Optional;

import context.project.process.logtable.ProcessLogTableRow;
import context.scheme.appliedarchive.reproducedandinsertedinstance.VisSchemeAppliedArchiveReproducedAndInsertedInstanceID;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.AlertUtils;
import utils.FXUtils;
import utils.SizePropertyUtils;
import utils.exceptionhandler.ExceptionHandlerUtils;

public class ProcessLogTableViewerController {
	public static final String FXML_FILE_DIR_STRING = "/core/table/process/ProcessLogTableViewer.fxml";
	
	private ProcessLogTableViewerManager manager;
	
	/**
	 * set manager;
	 * 
	 * also set the TableView
	 * @param manager
	 */
	void setManager(ProcessLogTableViewerManager manager) {
		this.manager = manager;
		
		
		
		//
		this.getTableViewContainer().getChildren().add(this.manager.getTableViewDelegate().getTableView());
		
		SizePropertyUtils.childNodeResizeWithParentNode(this.manager.getTableViewDelegate().getTableView(), this.getTableViewContainer());
		
		
		////
		this.rowSelectableCheckBox.setSelected(true);
		this.rowSelectableCheckBox.selectedProperty().addListener(e->{
			if(this.rowSelectableCheckBox.isSelected()) {
				this.manager.getTableViewDelegate().setSelectionColumnHidden(false);
				
				FXUtils.set2Disable(this.selectionRelatedButtonHBox, false);
			}else {
				this.manager.getTableViewDelegate().setSelectionColumnHidden(true);
				
				FXUtils.set2Disable(this.selectionRelatedButtonHBox, true);
			}
		});
		this.rowSelectableCheckBox.setSelected(false);
	}
	
	public Pane getRootContainerNode() {
		return this.rootContainerVBox;
	}
	
	Pane getTableViewContainer() {
		return tableViewContainerHBox;
	}
	
	private Stage getStage() {
		return (Stage) this.getTableViewContainer().getScene().getWindow();
	}
	
	
	////////////////////////
	@FXML
	public void initialize() {
		//
	}
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private HBox tableViewContainerHBox;
	@FXML
	private CheckBox rowSelectableCheckBox;
	@FXML
	private HBox selectionRelatedButtonHBox;
	@FXML
	private Button rollbackSelectedProcessButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button refreshButton;
	@FXML
	private Button closeWindowButton;
	
	
	// Event Listener on Button[#finishButton].onAction
//	@FXML
//	public void finishButtonOnAction(ActionEvent event) {
//		try {
//			if(this.manager.getTableViewDelegate().getCurrentlySelectedRow()==null) {
//				AlertUtils.popAlert("Warning", "No item is selected!");
//			}else {
////				if(this.manager.getInvokerSelectedFileFormatDisplayTextField()!=null) {
////					this.manager.getInvokerSelectedFileFormatDisplayTextField().setText(this.manager.getTableViewDelegate().getCurrentlySelectedRow().getTableRow().getEntity().getID().toString());
////				}
//				//TODO
//				this.manager.closeWindow();
//			}
//		}catch(Exception e) {
//			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".finishButtonOnAction"), e, this.getStage());
//		}
//	}
	
	@FXML
	public void rollbackSelectedProcessButtonOnAction(ActionEvent event) {
		try {
			if(this.manager.getTableViewDelegate().getCurrentlySelectedRow()==null) {//no selected process
				AlertUtils.popAlert("Warning", "No selected process to rollback!");
				return;
			}else {//
				ProcessLogTableRow processRow = this.manager.getTableViewDelegate().getCurrentlySelectedRow().getProcessLogTableRow();
				
				String title = "Warning!";
				String header = "Confirmation needed!";
				String context = null;
				
				if(processRow.getProcessID() instanceof VisSchemeAppliedArchiveReproducedAndInsertedInstanceID) {//VisSchemeAppliedArchiveReproducedAndInsertedInstance
					StringBuilder sb = new StringBuilder();
					sb.append("Rollback this process will also delete all inserted process type entities reproduced by this process:");
					sb.append(processRow.getInsertedProcessIDSet().toString());
					context = sb.toString();
				}else if(processRow.getReproduced()!=null && processRow.getReproduced()){//ReproduceableProcessType and reproduced
					AlertUtils.popAlert("Warning", "Cannot directly initialize roll back of a reproduced process!");
					return;
				}else {//
					//set up confirmation dialog infor
					StringBuilder sb = new StringBuilder();
					sb.append("Rollback this process will also delete all inserted non-process type entities by this process:");
					sb.append(processRow.getInsertedNonProcessIDSet().toString());
					sb.append("\n");
					sb.append("also rollback all dependent processes on this process:");
					sb.append("TBD");
					context = sb.toString();
				}

				Optional<ButtonType> result = 
						AlertUtils.popConfirmationDialog(
								title, 
								header, 
								context);
				if (result.get() == ButtonType.OK){
					System.out.println(this.getClass().getSimpleName()+": rollback");
					this.manager.getProcessLogTableAndProcessPerformerManager().getHostVisProjectDBContext().rollbackFinishedProcess(processRow.getUID());
				}
			}
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".closeWindowButtonOnAction"), e, this.getStage());
		}
	}
	
	
	// Event Listener on Button[#refreshButton].onAction
	@FXML
	public void refreshButtonOnAction(ActionEvent event) {
		try {
			this.manager.getTableViewDelegate().refresh();
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".refreshButtonOnAction"), e, this.getStage());
		}
	}
	// Event Listener on Button[#closeWindowButton].onAction
	@FXML
	public void closeWindowButtonOnAction(ActionEvent event) {
		try {
			this.clearButtonOnAction(event);
			
			this.manager.closeWindow();
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".closeWindowButtonOnAction"), e, this.getStage());
		}
	}
	
//	// Event Listener on Button[#cancelButton].onAction
//	@FXML
//	public void cancelButtonOnAction(ActionEvent event) {
//		try {
//			this.clearButtonOnAction(event);
//			
//			this.manager.closeWindow();
//		}catch(Exception e) {
//			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".cancelButtonOnAction"), e, this.getStage());
//		}
//	}
	// Event Listener on Button[#clearButton].onAction
	/**
	 * clear any selected row (checkbox)
	 * @param event
	 */
	@FXML
	public void clearButtonOnAction(ActionEvent event) {
		try {
			if(this.manager.getTableViewDelegate().getCurrentlySelectedRow()!=null) {
				this.manager.getTableViewDelegate().getCurrentlySelectedRow().setSelected(false);
			}
//			if(this.manager.getInvokerSelectedFileFormatDisplayTextField()!=null) {
//				this.manager.getInvokerSelectedFileFormatDisplayTextField().setText("");
//			}
			//TODO
		}catch(Exception e) {
			ExceptionHandlerUtils.show(this.getClass().getSimpleName().concat(".clearButtonOnAction"), e, this.getStage());
		}
	}
}
