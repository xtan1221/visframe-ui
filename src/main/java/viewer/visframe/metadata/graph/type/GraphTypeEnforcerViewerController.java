package viewer.visframe.metadata.graph.type;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import metadata.graph.type.GraphTypeEnforcer;
import viewer.AbstractViewerController;

public class GraphTypeEnforcerViewerController extends AbstractViewerController<GraphTypeEnforcer>{
	public static final String FXML_FILE_DIR_STRING = "/viewer/visframe/metadata/graph/type/GraphTypeEnforcerViewer.fxml";

	@Override
	public GraphTypeEnforcerViewer getViewer() {
		return (GraphTypeEnforcerViewer)this.viewer;
	}
	
	@Override
	protected void setup() {
		this.toForceDirectedCheckBox.setSelected(this.getViewer().getValue().isToForceDirected());
		this.directedForcingModeTextField.setText(this.getViewer().getValue().getDirectedForcingMode().toString());
		
		this.toForceUndirectedCheckBox.setSelected(this.getViewer().getValue().isToForceUndirected());
		
		this.toForceNoParallelEdgesCheckBox.setSelected(this.getViewer().getValue().isToForceNoParallelEdges());
		
		this.toForceNoSelfLoopsCheckBox.setSelected(this.getViewer().getValue().isToForceNoSelfLoops());
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
	private CheckBox toForceDirectedCheckBox;
	@FXML
	private CheckBox toForceUndirectedCheckBox;
	@FXML
	private CheckBox toForceNoParallelEdgesCheckBox;
	@FXML
	private CheckBox toForceNoSelfLoopsCheckBox;
	@FXML
	private TextField directedForcingModeTextField;
	
}
