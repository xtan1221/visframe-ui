package viewer.visframe.metadata.graph.vftree.feature;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import metadata.graph.vftree.VfTreeMandatoryEdgeDataTableSchemaUtils;
import metadata.graph.vftree.feature.VfTreeEdgeFeature;
import rdb.table.data.DataTableColumnName;
import viewer.AbstractViewerController;

public class VfTreeEdgeFeatureViewerController extends AbstractViewerController<VfTreeEdgeFeature>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/vftree/feature/VfTreeEdgeFeatureViewer.fxml";

	@Override
	public VfTreeEdgeFeatureViewer getViewer() {
		return (VfTreeEdgeFeatureViewer)this.viewer;
	}

	@Override
	protected void setup() {
		//
		this.getViewer().getValue().getIDColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.edgeIDFeatureNameVBox.getChildren().add(tf);
		});
		
		//
		VfTreeMandatoryEdgeDataTableSchemaUtils.getMandatoryAdditionalFeatureColumnNameList().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.mandatoryAdditionalFeatureVBox.getChildren().add(tf);
		});
		this.getViewer().getValue().getNonMandatoryAdditionalColumnNameSet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			this.nonMandatoryAdditionalFeatureVBox.getChildren().add(tf);
		});
		
		//
		this.getViewer().getValue().getNodeIDColumnNameEdgeSinkNodeIDColumnNameMap().keySet().forEach(n->{
			TextField tf = new TextField(n.getStringValue());
			tf.setEditable(false);
			
		});
	
		////////////
		this.edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsCheckBox.setSelected(this.getViewer().getValue().isEdgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSets());
		
		/////////////
		int i = 1; //first row index is 1, since the row of index 0 is already used by title TextFields
		for(DataTableColumnName nodeFeatureIDColName:this.getViewer().getValue().getNodeIDColumnNameEdgeSourceNodeIDColumnNameMap().keySet()) {
			//
			TextField tf = new TextField(nodeFeatureIDColName.getStringValue());
			tf.setEditable(false);
			this.nodeIDColumnEdgeSourceSinkIDColumnNameMapGridPane.add(tf, 0, i); //col, row
			//
			TextField tf2 = new TextField(this.getViewer().getValue().getNodeIDColumnNameEdgeSourceNodeIDColumnNameMap().get(nodeFeatureIDColName).getStringValue());
			tf2.setEditable(false);
			this.nodeIDColumnEdgeSourceSinkIDColumnNameMapGridPane.add(tf2, 1, i); //col, row
			
			TextField tf3 = new TextField(this.getViewer().getValue().getNodeIDColumnNameEdgeSinkNodeIDColumnNameMap().get(nodeFeatureIDColName).getStringValue());
			tf3.setEditable(false);
			this.nodeIDColumnEdgeSourceSinkIDColumnNameMapGridPane.add(tf3, 2, i); //col, row
			
			i++;
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
	private VBox edgeIDFeatureNameVBox;
	@FXML
	private VBox mandatoryAdditionalFeatureVBox;
	@FXML
	private VBox nonMandatoryAdditionalFeatureVBox;
	@FXML
	private CheckBox edgeIDColumnSetDisjointWithSourceAndSinkNodeIDColumnSetsCheckBox;
	@FXML
	private GridPane nodeIDColumnEdgeSourceSinkIDColumnNameMapGridPane;
	
}
