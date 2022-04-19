package viewer.visframe.operation.vftree.trim;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import operation.vftree.trim.RerootTreeOperation;
import rdb.table.data.DataTableColumnName;
import viewer.visframe.generic.tree.trim.helper.PositionOnTreeViewer;
import viewer.visframe.metadata.MetadataIDViewer;
import viewer.visframe.operation.vftree.VfTreeTrimmingOperationBaseViewerController;

/**
 * 
 * @author tanxu
 *
 */
public class RerootTreeOperationViewerController extends VfTreeTrimmingOperationBaseViewerController<RerootTreeOperation>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/operation/vftree/trim/RerootTreeOperationViewer.fxml";
	
	//////////////////////////////////////
	@Override
	protected void setup() {
		this.operationTypeTextField.setText(this.getViewer().getValue().getOperationTypeName().getStringValue());
		this.operationInstanceNameTextField.setText(this.getViewer().getValue().getName().getStringValue());
		this.notesTextArea.setText(this.getViewer().getValue().getNotes().getNotesString());
		
		MetadataIDViewer inputVfTreeMetadataIDViewer = new MetadataIDViewer(this.getViewer().getValue().getInputVfTreeMetadataID(), this.getViewer().getHostVisframeContext());
		this.inputVfTreeMetadataIDViewerHBox.getChildren().add(inputVfTreeMetadataIDViewer.getController().getRootContainerPane());
		
		///
		int i = 0;
		for(DataTableColumnName name:this.getViewer().getValue().getInputNodeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData().getSet()) {
			TextField tf = new TextField();
			tf.setText(name.getStringValue());
			tf.setEditable(false);
			this.nodeNonMandatoryAdditionalFeatureToBeKeptGridPane.add(tf, 0, i+1);
			i++;
		}
		
		int j = 0;
		for(DataTableColumnName name:this.getViewer().getValue().getInputEdgeRecordNonMandatoryAdditionalFeatureColumnSetToKeepInOutputVfTreeData().getSet()) {
			TextField tf = new TextField();
			tf.setText(name.getStringValue());
			tf.setEditable(false);
			this.edgeNonMandatoryAdditionalFeatureToBeKeptGridPane.add(tf, 0, j+1);
			j++;
		}
		
		//
		PositionOnTreeViewer newRootPositionOnTreeViewer = new PositionOnTreeViewer(this.getViewer().getValue().getNewRootPositionOnTree());
		this.newRootPositionOnTreeViewerHBox.getChildren().add(newRootPositionOnTreeViewer.getController().getRootContainerPane());
	}
	

	@Override
	public RerootTreeOperationViewer getViewer() {
		return (RerootTreeOperationViewer)this.viewer;
	}
	
	@Override
	public Parent getRootContainerPane() {
		return rootContainerVBox;
	}
	
	
	///////////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	
	@FXML
	private VBox rootContainerVBox;
	@FXML
	private TextField operationTypeTextField;
	@FXML
	private TextField operationInstanceNameTextField;
	@FXML
	private TextArea notesTextArea;
	@FXML
	private HBox inputVfTreeMetadataIDViewerHBox;
	@FXML
	private GridPane nodeNonMandatoryAdditionalFeatureToBeKeptGridPane;
	@FXML
	private GridPane edgeNonMandatoryAdditionalFeatureToBeKeptGridPane;
	@FXML
	private HBox newRootPositionOnTreeViewerHBox;

}
