package viewer.visframe.metadata.graph.vftree;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import metadata.SourceType;
import metadata.graph.vftree.VfTreeDataMetadata;
import metadata.record.RecordDataMetadata;
import utils.FXUtils;
import viewer.visframe.metadata.MetadataViewerControllerBase;
import viewer.visframe.metadata.graph.type.GraphMetadataTypeViewer;
import viewer.visframe.metadata.graph.vftree.feature.VfTreeEdgeFeatureViewer;
import viewer.visframe.metadata.graph.vftree.feature.VfTreeNodeFeatureViewer;
import viewer.visframe.metadata.record.RecordDataMetadataViewer;

/**
 * 
 * @author tanxu
 *
 */
public class VfTreeDataMetadataViewerController extends MetadataViewerControllerBase<VfTreeDataMetadata>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/vftree/VfTreeDataMetadataViewer.fxml";
	
	@Override
	public VfTreeDataMetadataViewer getViewer() {
		return (VfTreeDataMetadataViewer)this.viewer;
	}
	
	@Override
	protected void setup() {
		this.nameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		//
		this.setImportedSourceType();
		this.setResultedFromOperationSourceType();
		//
		this.vertexRecordDataNameTextField.setText(this.getViewer().getValue().getNodeRecordDataName().getStringValue());
		VfTreeNodeFeatureViewer vftreeVertexFeatureViewer = new VfTreeNodeFeatureViewer(this.getViewer().getValue().getGraphVertexFeature());
		this.vertexFeatureVBox.getChildren().add(vftreeVertexFeatureViewer.getController().getRootContainerPane());
		//
		this.edgeRecordDataNameTextField.setText(this.getViewer().getValue().getEdgeRecordDataName().getStringValue());
		VfTreeEdgeFeatureViewer vftreeEdgeFeatureViewer = new VfTreeEdgeFeatureViewer(this.getViewer().getValue().getGraphEdgeFeature());
		this.edgeFeatureVBox.getChildren().add(vftreeEdgeFeatureViewer.getController().getRootContainerPane());
		
		//
		if(this.getViewer().getValue().getBootstrapIteration()!=null) {
			this.bootstrappedCheckBox.setSelected(true);
			this.bootstrapIterationTextField.setText(Integer.toString(this.getViewer().getValue().getBootstrapIteration()));
		}else {
			this.bootstrappedCheckBox.setSelected(true);
			FXUtils.set2Disable(this.bootstrapIterationHBox, true);
		}
		
		//
		GraphMetadataTypeViewer GraphMetadataTypeViewer = new GraphMetadataTypeViewer(this.getViewer().getValue().getObservedGraphType());
		this.observedTypeVBox.getChildren().add(GraphMetadataTypeViewer.getController().getRootContainerPane());
	}

	private void setImportedSourceType() {
		this.importedRadioButton.setSelected(this.getViewer().getValue().getSourceType().equals(SourceType.IMPORTED));
		FXUtils.set2Disable(this.viewDataImporterDetailButton, !this.getViewer().getValue().getSourceType().equals(SourceType.IMPORTED));
	}
	
	private void setResultedFromOperationSourceType() {
		this.resultedFromOperationRadioButton.setSelected(this.getViewer().getValue().getSourceType().equals(SourceType.RESULT_FROM_OPERATION));
		
		FXUtils.set2Disable(this.operationNameHBox, !this.getViewer().getValue().getSourceType().equals(SourceType.RESULT_FROM_OPERATION));
		FXUtils.set2Disable(this.viewOperationDetailButton, !this.getViewer().getValue().getSourceType().equals(SourceType.RESULT_FROM_OPERATION));
		
		if(this.getViewer().getValue().getSourceType().equals(SourceType.RESULT_FROM_OPERATION)) {
			this.operationNameTextField.setText(this.getViewer().getValue().getSourceOperationID().getInstanceName().getStringValue());
		}
		
	}
	/////////////////////////
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerVBox;
	}

	
	//////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField nameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private RadioButton resultedFromOperationRadioButton;
	@FXML
	private RadioButton importedRadioButton;
	@FXML
	private HBox operationNameHBox;
	@FXML
	private TextField operationNameTextField;
	@FXML
	private Button viewOperationDetailButton;
	@FXML
	private Button viewDataImporterDetailButton;
	@FXML
	private TextField vertexRecordDataNameTextField;
	@FXML
	private Button viewVertexRecordDataMetadataDetailButton;
	@FXML
	private VBox vertexFeatureVBox;
	@FXML
	private TextField edgeRecordDataNameTextField;
	@FXML
	private Button viewEdgeRecordDataMetadataDetailsButton;
	@FXML
	private VBox edgeFeatureVBox;
	@FXML
	private CheckBox bootstrappedCheckBox;
	@FXML
	private HBox bootstrapIterationHBox;
	@FXML
	private TextField bootstrapIterationTextField;
	@FXML
	private VBox observedTypeVBox;

	// Event Listener on Button[#viewOperationDetailButton].onAction
	@FXML
	public void viewOperationDetailButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#viewDataImporterDetailButton].onAction
	@FXML
	public void viewDataImporterDetailButtonOnAction(ActionEvent event) {
		// TODO Autogenerated
	}
	
	//////////////////////////////////
	private RecordDataMetadataViewer vertexRecordDataMetadataViewer;
	private Stage vertexRecordDataMetadataViewerStage;
	private Scene vertexRecordDataMetadataViewerScene;
	@FXML
	public void viewVertexRecordDataMetadataDetailButtonOnAction(ActionEvent event) throws SQLException {
		if(this.vertexRecordDataMetadataViewer == null) {
			RecordDataMetadata vertexRecordDataMetadata = 
					(RecordDataMetadata) this.getViewer().getHostVisframeContext().getMetadataLookup().lookup(this.getViewer().getValue().getNodeRecordMetadataID());
			this.vertexRecordDataMetadataViewer = new RecordDataMetadataViewer(vertexRecordDataMetadata, this.getViewer().getHostVisframeContext());
		
			this.vertexRecordDataMetadataViewerScene = new Scene(this.vertexRecordDataMetadataViewer.getController().getRootContainerPane());
			
			this.vertexRecordDataMetadataViewerStage = new Stage();
			this.vertexRecordDataMetadataViewerStage.setScene(this.vertexRecordDataMetadataViewerScene);
			this.vertexRecordDataMetadataViewerStage.initModality(Modality.WINDOW_MODAL);
			this.vertexRecordDataMetadataViewerStage.initOwner(this.getRootContainerPane().getScene().getWindow());
		}
		
		this.vertexRecordDataMetadataViewerStage.show();
	}

	
	
	/////////////////////////
	private RecordDataMetadataViewer edgeRecordDataMetadataViewer;
	private Stage edgeRecordDataMetadataViewerStage;
	private Scene edgeRecordDataMetadataViewerScene;
	@FXML
	public void viewEdgeRecordDataMetadataDetailsButtonOnAction(ActionEvent event) throws SQLException {
		if(this.edgeRecordDataMetadataViewer == null) {
			RecordDataMetadata edgeRecordDataMetadata = 
					(RecordDataMetadata) this.getViewer().getHostVisframeContext().getMetadataLookup().lookup(this.getViewer().getValue().getEdgeRecordMetadataID());
			this.edgeRecordDataMetadataViewer = new RecordDataMetadataViewer(edgeRecordDataMetadata, this.getViewer().getHostVisframeContext());
		
			this.edgeRecordDataMetadataViewerScene = new Scene(this.edgeRecordDataMetadataViewer.getController().getRootContainerPane());
			
			this.edgeRecordDataMetadataViewerStage = new Stage();
			this.edgeRecordDataMetadataViewerStage.setScene(this.edgeRecordDataMetadataViewerScene);
			this.edgeRecordDataMetadataViewerStage.initModality(Modality.WINDOW_MODAL);
			this.edgeRecordDataMetadataViewerStage.initOwner(this.getRootContainerPane().getScene().getWindow());
		}
		
		this.edgeRecordDataMetadataViewerStage.show();
	}
	
}
