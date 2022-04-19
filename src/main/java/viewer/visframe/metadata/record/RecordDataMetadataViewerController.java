package viewer.visframe.metadata.record;

import java.io.IOException;
import java.sql.SQLException;

import context.project.VisProjectDBContext;
import core.table.hasIDTypeRelationalTableSchema.HasIDTypeRelationalTableContentViewer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import metadata.SourceType;
import metadata.record.RecordDataMetadata;
import utils.FXUtils;
import viewer.visframe.metadata.MetadataViewerControllerBase;
import viewer.visframe.rdb.table.AbstractRelationalTableColumnViewer;

public class RecordDataMetadataViewerController extends MetadataViewerControllerBase<RecordDataMetadata>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/record/RecordDataMetadataViewer.fxml";
	
	@Override
	public RecordDataMetadataViewer getViewer() {
		return (RecordDataMetadataViewer) this.viewer;
	}
	
	@Override
	protected void setup() {
		this.dataNameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		
		this.setSourceTypeRelatedNodes();
		
		this.setColumnVBox();
		
		//
		if(this.getViewer().getHostVisframeContext() instanceof VisProjectDBContext) {
			FXUtils.set2Disable(this.viewDataTableContentButton, false);
			visSchemeBasedInforLabel.setVisible(false);
		}else {
			FXUtils.set2Disable(this.viewDataTableContentButton, true);
			visSchemeBasedInforLabel.setVisible(true);
		}
	}

	/**
	 * 
	 */
	private void setSourceTypeRelatedNodes() {
		this.setImportedSourceTypeDisabled(!this.getViewer().getValue().getSourceType().equals(SourceType.IMPORTED));
		this.setResultedFromOperationSourceTypeDisabled(!this.getViewer().getValue().getSourceType().equals(SourceType.RESULT_FROM_OPERATION));
		this.setStructuralComponentSourceTypeDisabled(!this.getViewer().getValue().getSourceType().equals(SourceType.STRUCTURAL_COMPONENT));
	}
	
	private void setImportedSourceTypeDisabled(boolean disabled) {
		this.importedRadioButton.setSelected(!disabled);
		FXUtils.set2Disable(this.importedRadioButton,disabled);
		FXUtils.set2Disable(this.viewDataImporterDetailButton,disabled);
	}
	
	private void setResultedFromOperationSourceTypeDisabled(boolean disabled) {
		this.resultedFromOperationRadioButton.setSelected(!disabled);
		if(!disabled) {
			this.operationNameTextField.setText(this.getViewer().getValue().getSourceOperationID().getInstanceName().getStringValue());
		}
		FXUtils.set2Disable(this.resultedFromOperationRadioButton,disabled);
		FXUtils.set2Disable(this.viewOperationDetailButton,disabled);
		FXUtils.set2Disable(this.operationNameHBox,disabled);
	}
	
	private void setStructuralComponentSourceTypeDisabled(boolean disabled) {
		this.componentOfGraphDataRadioButton.setSelected(!disabled);
		if(!disabled) {
			this.graphDataNameTextField.setText(this.getViewer().getValue().getSourceCompositeDataMetadataID().getName().getStringValue());
			
			this.isGraphNodeRadioButton.setSelected(this.getViewer().getValue().isOfGenericGraphNode());
			this.isGraphEdgeRadioButton.setSelected(!this.getViewer().getValue().isOfGenericGraphNode());
		}
		FXUtils.set2Disable(this.componentOfGraphDataRadioButton,disabled);
		FXUtils.set2Disable(this.viewGraphDataDetailButton,disabled);
		FXUtils.set2Disable(this.graphDataNameHBox,disabled);
	}
	
	/**
	 * add viewer UI of each column of data table schema to the columnViewerVBox
	 */
	private void setColumnVBox() {
		for(int i=0;i<this.getViewer().getValue().getDataTableSchema().getOrderedListOfNonRUIDColumn().size();i++) {
			AbstractRelationalTableColumnViewer colViewer = new AbstractRelationalTableColumnViewer(this.getViewer().getValue().getDataTableSchema().getOrderedListOfNonRUIDColumn().get(i),i);
			this.columnViewerVBox.getChildren().add(colViewer.getController().getRootContainerPane());
		}
	}
	
	////////////////////////////////
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}

	
	/////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField dataNameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private RadioButton resultedFromOperationRadioButton;
	@FXML
	private RadioButton componentOfGraphDataRadioButton;
	@FXML
	private RadioButton importedRadioButton;
	@FXML
	private HBox operationNameHBox;
	@FXML
	private TextField operationNameTextField;
	@FXML
	private HBox graphDataNameHBox;
	@FXML
	private TextField graphDataNameTextField;
	@FXML
	private RadioButton isGraphNodeRadioButton;
	@FXML
	private RadioButton isGraphEdgeRadioButton;
	@FXML
	private Button viewOperationDetailButton;
	@FXML
	private Button viewGraphDataDetailButton;
	@FXML
	private Button viewDataImporterDetailButton;
	@FXML
	private VBox columnViewerVBox;
	@FXML
	private Button viewDataTableContentButton;
	@FXML
	private Label visSchemeBasedInforLabel;
	
	// Event Listener on Button[#viewOperationDetailButton].onAction
	@FXML
	public void viewOperationDetailButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#viewGraphDataDetailButton].onAction
	@FXML
	public void viewGraphDataDetailButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#viewDataImporterDetailButton].onAction
	@FXML
	public void viewDataImporterDetailButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
	
	///////////////
	private HasIDTypeRelationalTableContentViewer dataTableViewerManager;
	@FXML
	public void viewDataTableContentButtonOnAction(ActionEvent event) throws IOException, SQLException {
		if(this.dataTableViewerManager==null)
			this.dataTableViewerManager = 
					new HasIDTypeRelationalTableContentViewer(
							(VisProjectDBContext) this.getViewer().getHostVisframeContext(),
							this.getViewer().getValue(),
							this.getViewer().getValue().getDataTableSchema());
		
		this.dataTableViewerManager.showWindow((Stage)this.getRootContainerPane().getScene().getWindow());
	}
}
