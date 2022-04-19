package viewer.visframe.metadata.graph.type;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import metadata.graph.type.GraphMetadataType;
import viewer.AbstractViewerController;

public class GraphMetadataTypeViewerController extends AbstractViewerController<GraphMetadataType>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/type/GraphMetadataTypeViewer.fxml";

	@Override
	public GraphMetadataTypeViewer getViewer() {
		return (GraphMetadataTypeViewer)this.viewer;
	}
	
	@Override
	protected void setup() {
		this.containingOnlyDirectedEdgesCheckBox.setSelected(this.getViewer().getValue().isContainingDirectedEdgeOnly());
		this.containingOnlyUnDirectedEdgesCheckBox.setSelected(this.getViewer().getValue().isContainingUndirectedEdgeOnly());
		this.containingSelfLoopsCheckBox.setSelected(this.getViewer().getValue().isContainingSelfLoop());
		this.containingParallelEdgesCheckBox.setSelected(this.getViewer().getValue().isContainingParallelEdges());
		
		if(this.getViewer().getValue().isContainingCycle()==null)
			this.containingCyclesCheckBox.setIndeterminate(true);
		else {
			this.containingCyclesCheckBox.setSelected(this.getViewer().getValue().isContainingCycle());
		}
		
		if(this.getViewer().getValue().isNotConnected()==null)
			this.notConnectedCheckBox.setIndeterminate(true);
		else
			this.notConnectedCheckBox.setSelected(this.getViewer().getValue().isNotConnected());
		
	}

	
	/////////////////////////
	@Override
	public Pane getRootContainerPane() {
		return this.rootContainerGridPane;
	}

	
	//////////////////////////////
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@FXML
	private GridPane rootContainerGridPane;
	@FXML
	private CheckBox containingOnlyDirectedEdgesCheckBox;
	@FXML
	private CheckBox containingOnlyUnDirectedEdgesCheckBox;
	@FXML
	private CheckBox containingSelfLoopsCheckBox;
	@FXML
	private CheckBox containingParallelEdgesCheckBox;
	@FXML
	private CheckBox containingCyclesCheckBox;
	@FXML
	private CheckBox notConnectedCheckBox;
	
}
