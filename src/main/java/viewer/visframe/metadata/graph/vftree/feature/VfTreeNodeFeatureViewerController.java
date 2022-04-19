package viewer.visframe.metadata.graph.vftree.feature;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.graph.vftree.VfTreeMandatoryNodeDataTableSchemaUtils;
import metadata.graph.vftree.feature.VfTreeNodeFeature;
import viewer.AbstractViewerController;

public class VfTreeNodeFeatureViewerController extends AbstractViewerController<VfTreeNodeFeature>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/vftree/feature/VfTreeNodeFeatureViewer.fxml";

	@Override
	public VfTreeNodeFeatureViewer getViewer() {
		return (VfTreeNodeFeatureViewer)this.viewer;
	}

	@Override
	protected void setup() {
		//
		this.getViewer().getValue().getIDColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.vertexIDFeatureVBox.getChildren().add(tf);
		});
		
		//
		VfTreeMandatoryNodeDataTableSchemaUtils.getMandatoryAdditionalFeatureColumnNameList().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.mandatoryAdditionalFeatureVBox.getChildren().add(tf);
		});
		this.getViewer().getValue().getNonMandatoryAdditionalColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.nonMandatoryAdditionalFeatureVBox.getChildren().add(tf);
		});
		
	}

	
	/////////////////////////
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerHBox;
	}

	
	//////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	
	@FXML
	private HBox rootContainerHBox;
	@FXML
	private VBox vertexIDFeatureVBox;
	@FXML
	private VBox mandatoryAdditionalFeatureVBox;
	@FXML
	private VBox nonMandatoryAdditionalFeatureVBox;
}
