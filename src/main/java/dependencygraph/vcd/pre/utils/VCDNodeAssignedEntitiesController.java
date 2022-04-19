package dependencygraph.vcd.pre.utils;

import function.composition.CompositionFunctionID;
import function.group.CompositionFunctionGroupID;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import metadata.MetadataID;
import operation.OperationID;

public class VCDNodeAssignedEntitiesController {
	public static final String FXML_FILE_DIR_STRING = "/dependencygraph/vcd/utils/VCDNodeAssignedEntities.fxml";
	
	private VCDNodeAssignedEntitiesManager manager;
	
	void setManager(VCDNodeAssignedEntitiesManager manager) {
		this.manager = manager;
		
		
		this.precedenceIndexTextField.setText(Integer.toString(this.getManager().getNode().getPrecedenceIndex()));
		this.totalMetadataNumTextField.setText(Integer.toString(this.getManager().getNode().getAssignedMetadataIDSet().size()));
		this.totalOperationNumTextField.setText(Integer.toString(this.getManager().getNode().getAssignedOperationIDSet().size()));
		this.totalCFGNumTextField.setText(Integer.toString(this.getManager().getNode().getAssignedCFGIDSet().size()));
		this.totalCFNumTextField.setText(Integer.toString(this.getManager().getNode().getAssignedCFIDSet().size()));
		
		this.metadataListView.getItems().addAll(this.getManager().getNode().getAssignedMetadataIDSet());
		this.operationListView.getItems().addAll(this.getManager().getNode().getAssignedOperationIDSet());
		this.cfgListView.getItems().addAll(this.getManager().getNode().getAssignedCFGIDSet());
		this.cfListView.getItems().addAll(this.getManager().getNode().getAssignedCFIDSet());
	}
	
	VCDNodeAssignedEntitiesManager getManager() {
		return this.manager;
	}
	
	
	Parent getRootNodePane() {
		return this.rootContainerScrollPane;
	}
	
	
	////////////////////////////////////////
	@FXML
	public void initialize() {
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
		
		this.cfgListView.setCellFactory(
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
		this.cfListView.setCellFactory(
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
	private ScrollPane rootContainerScrollPane;
	@FXML
	private TextField precedenceIndexTextField;
	@FXML
	private TextField totalMetadataNumTextField;
	@FXML
	private ListView<MetadataID> metadataListView;
	@FXML
	private TextField totalOperationNumTextField;
	@FXML
	private ListView<OperationID> operationListView;
	@FXML
	private TextField totalCFGNumTextField;
	@FXML
	private ListView<CompositionFunctionGroupID> cfgListView;
	@FXML
	private TextField totalCFNumTextField;
	@FXML
	private ListView<CompositionFunctionID> cfListView;
}
